/*
 * Copyright (C) 2014  Ian Holsman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package controllers

import java.io.File
import java.nio.file.Path

import javax.inject._
import com.typesafe.config.ConfigFactory
import models.people.EmpRelationsRowUtils._
import models.people._
import models.product.ProductTrackRepo
import offline.Tables.{EmprelationsRow, OfficeRow}
import play.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.Files
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc._
import play.db.NamedDatabase
import utl.{LDAP, User}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import utl.importFile.{SAPImport, WDImport}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PersonController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
   //officeRepo: OfficeRepo,
   employeeRepo: EmployeeRepo,
   empBioRepo: EmpBioRepo,
   teamDescriptionRepo: TeamDescriptionRepo,
   empTagRepo: EmpTagRepo,
   kudosToRepo: KudosToRepo,
   matrixTeamRepo: MatrixTeamRepo,
   matrixTeamMemberRepo: MatrixTeamMemberRepo,
   user: User
  )(implicit    costCenterRepo: CostCenterRepo,
    officeRepo: OfficeRepo,
    empHistoryRepo: EmpHistoryRepo,
    positionTypeRepo: PositionTypeRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  // implicit val ldap: LDAP = new LDAP
  protected val sapImportDir: String = ConfigFactory.load().getString("offline.SAPImportDir")

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  case class person(id: String, name: String)

  case class autoCompleteResult(total_count: Int, incomplete_Results: Boolean, items: List[person])


  def personAutoComplete(q: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val result: Future[autoCompleteResult] = q match {
      case None => Future.successful(autoCompleteResult(0, incomplete_Results = false, List.empty))
      case Some(query) =>
        if (query.length < 2) {
          Future.successful(autoCompleteResult(total_count = 0, incomplete_Results = false, items = List.empty))
        } else {
          employeeRepo.search(query).map { seq =>
            autoCompleteResult(seq.size, incomplete_Results = false, items = seq.map { e => person(e.login, e.fullName) }.toList)
          }
        }
    }
    result.map { x => Ok(Json.toJson(x)).as("application/json; charset=utf-8").withHeaders(("Access-Control-Allow-Origin", "*")) }
    //Future.successful(Ok(""))
  }

  def search(page: Int, search: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    search match {
      case None => Future(Redirect(routes.HomeController.index()))
      case Some(searchString) => employeeRepo.search(searchString).map { emps: Seq[EmprelationsRow] =>
        if (emps.size == 1) {
          Redirect(routes.PersonController.id(emps.head.login))
        } else {
          Ok(views.html.person.search(search.getOrElse(""), utl.Page(emps, page = page)))
        }
      }
    }
  }

  def ceo: Action[AnyContent] = Action.async { implicit request =>
    employeeRepo.findCEO().map {
      case Some(emp) => Redirect(routes.PersonController.id(emp.login))
      case None => Redirect(routes.HomeController.index())
    }
  }

  def id(login: String): Action[AnyContent] = Action.async { implicit request =>

    val canEdit = user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser())
    employeeRepo.findByLogin(login).map {
      case Some(emp: EmprelationsRow) =>
        val manager = employeeRepo.manager(login)
        val matrix = matrixTeamMemberRepo.findMatrixTeamByLogin(login)
        val directs = employeeRepo.managedBy(login).map { empsd =>
          empsd.map { empd => matrixTeamMemberRepo.findMatrixTeamByLogin(empd.login).map(mtseq => (empd, mtseq)) }
        }.map { x => Future.sequence(x) }.flatMap(identity)

        val team = teamDescriptionRepo.findTeamForLogin(login)(employeeRepo)
        val bio = empBioRepo.findByLogin(login)
        val kTo = kudosToRepo.findTo(login).map(x => Future.sequence(x.map(k => employeeRepo.findByLogin(k.fromperson).map(e => (k, e))))).flatMap(identity)
        val kFrom = kudosToRepo.findFrom(login).map(x => Future.sequence(x.map(k => employeeRepo.findByLogin(k.toperson).map(e => (k, e))))).flatMap(identity)
        val tags = empTagRepo.findByLogin(login)
        val office = emp.officeid match {
          case Some(officeid) => officeRepo.find(officeid)
          case None => Future.successful(None)
        }

        (for {
          m <- manager
          d <- directs
          t <- team
          b <- bio
          kt <- kTo
          kf <- kFrom
          tag <- tags
          o <- office
          matrixT <- matrix
          c <- canEdit
        } yield (m, d, t, b, kt, kf, tag, o, matrixT, c))
          .map { x =>
            Ok(views.html.person.id(login.toLowerCase, emp, x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10))
          }

      case None => Future.successful(NotFound(views.html.page_404("Login not found")))
    }.flatMap(identity)
  }

  def personNumber(id: Long): Action[AnyContent] = Action.async {
    employeeRepo.findByPersonNumber(id).map {
      case Some(emp: EmprelationsRow) => Redirect(routes.PersonController.id(emp.login))
      case None => NotFound(views.html.page_404("No matching person number"))
    }
  }

  def importFile(): LDAPAuthPermission[AnyContent] = LDAPAuthPermission("ImportSAPFile") {
    Action.async { implicit request =>
      Future.successful(Ok(views.html.person.importFile()))
    }
  }

  def importWorkdayFile(): LDAPAuthPermission[AnyContent] = LDAPAuthPermission("ImportWorkdayFile") {
    Action.async { implicit request =>
      Future.successful(Ok(views.html.person.importWDFile()))
    }
  }


  def importFileDir(): LDAPAuthPermission[AnyContent] = LDAPAuthPermission("ImportSAPFile") {
    Action.async { implicit request =>
      val dir = new File(sapImportDir)
      val files2: List[String] = if (dir.exists() && dir.isDirectory) {
        dir.listFiles().filter(_.isFile).map(_.getName).filter(_.endsWith(".csv")).toList
      } else {
        List.empty
      }
      val files: List[String] = files2.sorted(Ordering[String].reverse).take(20)

      Future.successful(Ok(views.html.person.importFileInDir(files)))
    }
  }

  def doImport(): LDAPAuthPermission[MultipartFormData[Files.TemporaryFile]] = LDAPAuthPermission("ImportSAPFile") {
    Action.async(parse.multipartFormData) { implicit request =>
          request.body.file("importFile").map { picture =>
           // val filename = picture.filename
            val path: Path = picture.ref.path

            SAPImport.importFile(path).map {
              employees =>
                SAPImport.validate(employees) match {
                  case Nil => employeeRepo.repopulate(employees).map { x =>
                    Future.successful(Ok(views.html.person.importFileResult(x._1.toList.sortBy(_.login), x._2, x._3.toList)))
                  }
                  case x: Seq[String] => Future.successful(Future.successful(InternalServerError(s"Failed to Validate:\n ${x.mkString("\n")}")))

                }
            }.flatMap(identity).flatMap(identity)

          }.getOrElse {
            Future.successful(Redirect(routes.PersonController.importFile()).flashing(
              "error" -> "Missing file"))
          }
    }
  }

  def doImportWorkday(): LDAPAuthPermission[MultipartFormData[Files.TemporaryFile]] = LDAPAuthPermission("ImportWorkdayFile") {
    Action.async(parse.multipartFormData) { implicit request =>
          request.body.file("importFile").map { picture =>
           // val filename = picture.filename
            val path: Path = picture.ref.path

            WDImport.importFile(path).map {
              employees =>
                WDImport.validate(employees) match {
                  case Nil =>

                    employeeRepo.repopulate(employees).map { x =>
                      Future.successful(Ok(views.html.person.importFileResult(x._1.toList.sortBy(_.login), x._2, x._3.toList)))
                    }

                    // Future.successful(Future.successful(Ok(views.html.person.importFileResult(List.empty, 0, List.empty))))
                  case x: Seq[String] => Future.successful(Future.successful(InternalServerError(s"Failed to Validate:\n ${x.mkString("\n")}")))

                }
            }.flatMap(identity).flatMap(identity)

          }.getOrElse {
            Future.successful(Redirect(routes.PersonController.importWorkdayFile()).flashing(
              "error" -> "Missing file"))
          }
    }
  }


  // WARNING - this could possibly do more secure filename checking, but
  // a. it's in a Docker container
  // b. it's 'admin only'
  def doImportFile(filename: String): LDAPAuthPermission[AnyContent] = LDAPAuthPermission("ImportSAPFile") {
    Action.async { implicit request =>

          val file = new java.io.File(sapImportDir, filename)
          val canonical = file.getCanonicalPath
          /*
          if (!FileIO.isInSecureDir(file.toPath) && !System.getProperty("os.name").startsWith("Windows")) {
            Logger.error(s"Insecure directory $canonical")
            Future.successful(Redirect(routes.PersonController.importFileDir()).flashing( "error" -> "unable to read file"))
          } else {
          */
          if (canonical.startsWith(sapImportDir) && file.canRead && file.isFile && file.exists()) {

            //request.body.file("importFile").map { picture =>
            //  val filename = picture.filename
            val path: Path = file.toPath

            SAPImport.importFile(path).map {
              employees =>
                SAPImport.validate(employees) match {
                  case Nil => employeeRepo.repopulate(employees).map { x =>
                    Future.successful(Ok(views.html.person.importFileResult(x._1.toList.sortBy(_.login), x._2, x._3.toList)))
                  }
                  case x: Seq[String] => Future.successful(Future.successful(InternalServerError(s"Failed to Validate:\n ${x.mkString("\n")}")))

                }
            }.flatMap(identity).flatMap(identity)
          } else {
            Logger.error(s"Unable to import file $canonical $filename")
            Future.successful(Redirect(routes.PersonController.importFileDir()).flashing(
              "error" -> "unable to read file"))
          }
        //  }
      }
  }



  def personOrgChart(login: Option[String]): Action[AnyContent] = Action.async { implicit request =>

    val empF = login match {
      case None => employeeRepo.findCEO()
      case Some(x) => employeeRepo.findByLogin(x)
    }
    val mgrF = login match {
      case None => Future.successful(None)
      case Some(x) => employeeRepo.manager(x)

    }
    (for {
      e <- empF
      m <- mgrF
    } yield (e, m))
      .map { empMgrO =>
        empMgrO._1 match {
          case None => NotFound("I can't find that person/CEO not found")
          case Some(emp) => Ok(views.html.person.orgChart("Org Chart", emp, empMgrO._2))
        }
      }
  }

  protected def officeString(id: Option[Long], officeMap: Map[Long, OfficeRow]): String = {
    id match {
      case Some(officeID) => officeMap.get(officeID) match {
        case Some(office) => " (" + office.city.getOrElse("-") + "/" + office.country.getOrElse("-") + ")"
        case None => ""
      }
      case None => ""
    }
  }

  def agencyList(): LDAPAuthPermission[AnyContent] =LDAPAuthPermission("SeeAgency") {
    Action.async { implicit request =>
      employeeRepo.agencies().map { seq =>
        val sorted = seq.sortBy(_._1)
        Ok(views.html.person.agencies(sorted))
      }
    }
  }

  def byAgency(agency: String, page: Int): LDAPAuthPermission[AnyContent] = LDAPAuthPermission("SeeAgency") {
    Action.async { implicit request =>
      employeeRepo.findByAgency(agency).map { seq =>
        Ok(views.html.person.byAgency(agency, utl.Page(seq, page = page)))
      }
    }
  }


  def matrix(login:String): LDAPAuthAction[AnyContent] = LDAPAuthAction {
    Action.async { implicit request =>
      user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser()).map {
        case true => (for{
          e <- employeeRepo.findByLogin(login)
          m <- matrixTeamMemberRepo.findMatrixTeamByLogin(login).map( s => s.map( p=> p.id -> p).toMap )
          a <- matrixTeamRepo.all
        } yield (e,m,a)).map { x =>
          x._1 match {
            case Some(emp) =>  Ok(views.html.person.matrix(emp,x._2,x._3))
            case None => NotFound(views.html.page_404("Login not found"))
          }
        }
        case false => Future(Unauthorized(views.html.page_403("No Access")))
      }.flatMap(identity)
    }
  }

  def matrixEnabledisable(login:String, pref:Long, enable:Boolean) = LDAPAuthAction {

    Action.async { implicit request =>
      val loggedIn = LDAPAuth.getUser().get
      (for {
        u <- user.isOwnerManagerOrAdmin(login, Some(loggedIn))
        matrix <- matrixTeamRepo.find(pref)
      } yield (u,matrix)).map{ result =>
        result._2 match {
          case None => Future.successful(NotFound(views.html.page_404("Matrix team now found")))
          case Some(matrix) => if ( result._1 || ( matrix.owner.isDefined && matrix.owner.get.equalsIgnoreCase(loggedIn) )) {
            (if ( enable ) {
              matrixTeamMemberRepo.enablePref(login, pref)
            } else {
              matrixTeamMemberRepo.disablePref(login, pref)
            }).map{ _ => Redirect(routes.PersonController.matrix(login))}
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Access")))
          }
        }
      }.flatMap(identity)
    }
  }

  def personHierarchy(login:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val empF = login match {
      case None => employeeRepo.findCEO()
      case Some(x) => employeeRepo.findByLogin(x)
    }

    val directsF:Future[Set[EmprelationsRow]] = login match {
      case None => employeeRepo.managedBy()
      case Some(x) => employeeRepo.managedBy(x)

    }
    (for {
      e <- empF
    //  m <- mgrF
      d <- directsF
      dd <- directsF.map{ seq =>
        Future.sequence(seq.map{ di =>
         employeeRepo.managedBy(di.login)
        })
      }.flatMap(identity)
      o <- officeRepo.all().map{ seq => seq.map{ x => x.id -> x}.toMap }
    } yield (e, d, o, dd)) .map { empMgrO =>

      empMgrO._1 match {
        case None => NotFound("Login not found/CEO not found")
        case Some(emp) =>
        //  val mgr = empMgrO._2
          val directsEmp: Map[String, EmprelationsRow] = empMgrO._2.map{ p => p.login.toLowerCase -> p}.toMap
          val offices = empMgrO._3
          val directsDirectsEmpMgrs: Map[Option[String], Set[EmprelationsRow]] = empMgrO._4.flatten.groupBy(_.managerid)
          val directsNoMgrs:Map[Option[String], Set[EmprelationsRow]] = directsEmp.filterNot( p=> directsDirectsEmpMgrs.contains(  Some(p._1))).map{
             p => Some(p._1) -> Set.empty[EmprelationsRow]
          }
          val directsDirectsEmp: Map[Option[String], Set[EmprelationsRow]] = directsDirectsEmpMgrs ++ directsNoMgrs

           val children = directsDirectsEmp.map{ dDirects =>
            val direct:Option[EmprelationsRow] = directsEmp.get( dDirects._1.getOrElse("").toLowerCase)
            val ddJ = dDirects._2.map{  x:EmprelationsRow =>
              val o3 =officeString(x.officeid,offices)
              val directsA:String = if ( x.directs>0L) {
                s"(${x.directs})"
              } else {
                ""
              }
              JS_GG_ORGCHART(id = x.login,
                title = x.fullName +directsA,
                subtitle = x.position + o3, isFTE =  !emp.isContractor, children=List.empty)
            }
            direct match {
              case None =>  JS_GG_ORGCHART(id = "??", title = "???", subtitle = "???", isFTE =  false, children=List.empty)
              case Some(empD) =>
                val o3 =officeString(empD.officeid,offices)
                JS_GG_ORGCHART(id = empD.login, title = empD.fullName ,  subtitle = empD.position + o3, isFTE =  !empD.isContractor, children= ddJ.toList )
            }
          }
          val o = officeString(emp.officeid,offices)
          val empJ = JS_GG_ORGCHART( id = emp.login, title = emp.fullName ,  subtitle = emp.position + o, isFTE =  !emp.isContractor,
            children = children.toList  )

          Ok(JS_GG_ORGCHART_ROOT(id = emp.personnumber, title = s"${emp.fullName}'s org", root = empJ).toJson).as("application/json; charset=utf-8").withHeaders(("Access-Control-Allow-Origin", "*"))
      }
    }

  }
  case class JS_GG_ORGCHART_ROOT(id: Long, title:String, root:JS_GG_ORGCHART) {
    def toJson:String = {
      // title.replaceAll("'","''")
      // "{ \"id\":"+id+", \"title\": \""+title+"\",  \"root\":" +root.toJson+" }"
      "{ \"id\":"+1+", \"root\":" +root.toJson+" }"
    }
  }

  case class JS_GG_ORGCHART(id:String, title:String, subtitle:String,
                            isFTE:Boolean, children:List[JS_GG_ORGCHART] ) {
    def toJson:String ={
      val x = "{\"id\":\""+id+"\", \"title\":\""+title+"\", \"subtitle\":\""+subtitle+"\""
      if ( children.isEmpty ) {
        if(isFTE) {
          x+  ",\"type\":\"staff\" }"
        } else {
          x + ", \"type\":\"staff\", \"subtype\":\"dashed\" }"
        }
      } else {
        s"$x, "+"\"children\": [ "+ children.map( x=> x.toJson ).mkString(",") +" ] }\n"
      }

    }
  }
  implicit val personWrites: Writes[person] {
    def writes(k: person): JsObject
  } = new Writes[person] {
    def writes(k: person): JsObject = Json.obj(
      "id" -> k.id,
      "name" -> k.name
    )
  }

  implicit val autoCompleteResultWrites: Writes[autoCompleteResult] {
    def writes(k: autoCompleteResult): JsObject
  } = new Writes[autoCompleteResult] {
    def writes(k: autoCompleteResult): JsObject = Json.obj(
      "total_count" -> k.total_count,
      "incomplete_Results" -> k.incomplete_Results,
      "items" -> k.items
    )
  }
}

