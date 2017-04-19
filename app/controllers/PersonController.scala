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

import models.people.EmpRelationsRowUtils._
import models.people._
import models.product.ProductTrackRepo
import offline.Tables
import offline.Tables.EmprelationsRow
import play.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import util.{LDAP, User}

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.JdbcProfile
import util.importFile.SAPImport

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
   resourcePoolRepo: ResourcePoolRepo,
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
    webJarAssets: WebJarAssets,
    assets: AssetsFinder
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  implicit val ldap: LDAP = new LDAP
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */



  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
      search match {
        case None => Future( Redirect(routes.HomeController.index()) )
        case Some(searchString) => employeeRepo.search(searchString).map{ emps:Seq[EmprelationsRow] =>
          if (emps.size == 1 ) {
            Redirect(routes.PersonController.id( emps.head.login))
          } else {
            Ok(emps.map(x => x.fullName).mkString("\n"))
          }
        }
      }
  }

  def id( login:String): Action[AnyContent] = Action.async{ implicit request =>

    val canEdit = user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser())
    employeeRepo.findByLogin(login).map {
      case Some(emp:EmprelationsRow) =>
        val manager = employeeRepo.manager(login)
        val matrix = matrixTeamMemberRepo.findMatrixTeamByLogin(login)
        val directs = employeeRepo.managedBy(login).map { empsd =>
          empsd.map { empd => matrixTeamMemberRepo.findMatrixTeamByLogin(empd.login).map(mtseq => ( empd, mtseq )) }
        }.map { x => Future.sequence(x)}.flatMap(identity)


        val team = teamDescriptionRepo.findTeamForLogin(login)(employeeRepo)
        val bio = empBioRepo.findByLogin(login)
        val kTo = kudosToRepo.findTo(login).map( x=>  Future.sequence(x.map( k =>  employeeRepo.findByLogin(k.fromperson).map( e =>  (k,e))))).flatMap(identity)
        val kFrom = kudosToRepo.findFrom(login).map( x=> Future.sequence( x.map( k => employeeRepo.findByLogin(k.toperson).map( e=> (k,e))))).flatMap(identity)
        val tags = empTagRepo.findByLogin(login)
        val office = emp.officeid match {
          case Some(officeid) =>officeRepo.find(officeid)
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
        } yield( m,d,t,b,kt, kf, tag, o,matrixT,c ))
        .map { x =>
          Ok(views.html.person.id( login.toLowerCase, emp, x._1 , x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, x._10) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Login not found")))
    }.flatMap(identity)
  }

  def personNumber( id:Long) = Action.async{
    employeeRepo.findByPersonNumber(id).map {
      case Some(emp:EmprelationsRow) => Redirect(routes.PersonController.id(emp.login))
      case None => NotFound(views.html.page_404(  "No matching person number"))
    }
  }

  def importFile = Action.async{ implicit request =>
    user.isAdmin(LDAPAuth.getUser()).map {
      case true =>Ok(views.html.person.importFile())
      case false => Unauthorized(views.html.page_403(  "You don't have access to import SAP files"))
    }
  }
  def doImport = Action.async(parse.multipartFormData) { implicit request =>
    user.isAdmin(LDAPAuth.getUser()).map {
      case false => Future.successful(Unauthorized(views.html.page_403("You don't have access to import SAP files")) )
      case true =>
        request.body.file("importFile").map { picture =>
          val filename = picture.filename
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
    }.flatMap(identity)
  }

}

