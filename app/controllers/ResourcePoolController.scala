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

import java.io.FileOutputStream
import javax.inject._

import com.typesafe.config.ConfigFactory
import models.people.TeamDescriptionRepo
import models.product._
import offline.Tables
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import projectdb.Tables
import projectdb.Tables._
import slick.jdbc.JdbcProfile
import util.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}
import offline.Tables.EmprelationsRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.{Row, Sheet, Workbook}
import play.api.Logger

import scala.collection.immutable
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ResourcePoolController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   resourcePoolRepo: ResourcePoolRepo,
   resourceTeamRepo: ResourceTeamRepo,
   user: User
  )(implicit
    teamDescriptionRepo: TeamDescriptionRepo,
    matrixTeamMemberRepo: models.people.MatrixTeamMemberRepo,
    officeRepo:models.people.OfficeRepo,
    positionTypeRepo:models.people.PositionTypeRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

//  implicit val ldap: LDAP = new LDAP
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val canEditF = user.isAdmin(LDAPAuth.getUser())
    val pageSize =20
      search match {
        case None =>
          (for {
            isAdmin <- canEditF
            all <- resourcePoolRepo.all
          } yield (isAdmin,all) ).map{ x => Ok(views.html.product.pool.search( Page(x._2,page,pageSize),search, x._1, forms.ResourcePoolForm.form)) }
        case Some(searchString) =>
          (for {
            isAdmin <- canEditF
            all <- resourcePoolRepo.search(searchString)
          } yield (isAdmin,all) )
          .map{ x  =>
          if (x._2.size == 1 ) {
            Redirect(routes.ResourcePoolController.id( x._2.head.id))
          } else {
            Ok(views.html.product.pool.search(Page(x._2,page,pageSize), search, x._1, forms.ResourcePoolForm.form ))
          }
        }
      }
  }

  def id(rpId:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    resourcePoolRepo.find(rpId).map {
      case Some(rp) =>
        val teams: Future[Seq[(ResourceteamRow,
          Seq[(EmprelationsRow, Int, EfficencyMonth)],
          Seq[TeamSummary],
          Boolean)]] = resourceTeamRepo.findInPool( rp.id).map{
          (teamS: Seq[ResourceteamRow]) => Future.sequence( teamS.map { team:ResourceteamRow =>
            (for{
              dsX <- resourceTeamRepo.getDevStatsForTeam(team.id)//.map{ ds => (team, ds) }
              ctX <- resourceTeamRepo.getTeamSummaryByVendorCountry(team.id)
              isAdmin <- user.isAdmin( LDAPAuth.getUser())
            } yield (dsX,ctX, isAdmin)).map{ x=>
              (team,x._1, x._2, x._3)
            }
          })
        }.flatMap(identity)

        (for {
          t <- teams
        } yield t)
        .map { x:Seq[(ResourceteamRow, Seq[(EmprelationsRow, Int, EfficencyMonth)],
          Seq[TeamSummary],Boolean)] =>
          val isAdmin:Boolean  = x.headOption match {
            case None => false
            case Some(rec) => rec._4
          }
          val byTeam: Seq[(ResourceteamRow, (Int, EfficencyMonth))] = x.map{ tE => ( tE._1, tE._2
            .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 +1,z._2.add( i._3))  ))
          }
          val byPool: (Int, EfficencyMonth) = byTeam.map( _._2)
                      .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 + i._1 ,z._2.add( i._2) ))

          val sum: Seq[TeamSummary] = x.flatMap{ line =>
            val baseX = line._3

            baseX.groupBy(p=> (p.agency, p.isContractor,p.country,p.positionType, p.isPE )).map{ xx =>
              (xx._1._1, xx._1._2,xx._1._3,xx._1._4, xx._1._5, xx._2.map(_.tally).sum )
            }.toSeq
          }.groupBy( p=> (p._1, p._2, p._3, p._4, p._5 )).map{ line2 =>
            TeamSummary(agency = line2._1._1, isContractor =line2._1._2, country = line2._1._3, positionType= line2._1._4,isPE = line2._1._5, tally = line2._2.map(_._6).sum )
          }.toSeq


          Ok(views.html.product.pool.id( rpId, rp, byPool, sum, Page( byTeam,page, pageSize = 20), isAdmin) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Team not found")))
    }.flatMap(identity)
  }

  def doFullBreakdown(format:Option[String]=None) = Action.async { implicit request =>
    val x: Future[Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]] = resourceTeamRepo.allEx.map{ rtOs =>
      val teamOrPool: Seq[Either[ResourceteamRow, ResourcepoolRow]] = rtOs.map{ rtO =>
        rtO._2 match {
          case Some(rp) => Right(rp)
          case None => Left(rtO._1)
        }
      }
      Future.sequence(teamOrPool.groupBy(p => p).keys.map {
        case x@Left(team) => resourceTeamRepo.getTeamSummaryByVendorCountry(team.id).map{ res => (x,res)}
        case x@Right(pool) =>resourcePoolRepo.getTeamSummaryByVendorCountry(pool.id).map{ res => (x,res)}
      }.toSeq)
    }.flatMap(identity)

    x.map { (result: Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]) =>
      val formatS:String = format.getOrElse("html")

      if (formatS.equalsIgnoreCase("XLS")) {
        Ok.sendFile(doFullBreakdownXLS(result))
          .as("application/vnd.ms-excel")
          .withHeaders(("Access-Control-Allow-Origin", "*"),
            ("Content-Disposition", s"attachment; filename=ResourceList.xls"))
      } else {
        Ok(views.html.product.pool.fullBreakdown(result))
      }
    }
  }

  def doFullBreakdownXLS(result:Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]):java.io.File = {
    val wb: Workbook = new HSSFWorkbook()
    val sheet: Sheet = wb.createSheet()
    val headerRow: Row = sheet.createRow(0)
    wb.setSheetName(0,"Resource Breakdown")
    headerRow.createCell(0).setCellValue("Pool/Team")
    headerRow.createCell(1).setCellValue("Employee/Vendor")
    headerRow.createCell(2).setCellValue("Country")
    headerRow.createCell(3).setCellValue("Position Type")
    headerRow.createCell(4).setCellValue("PE?")
    headerRow.createCell(5).setCellValue("#")

    val result2: Seq[(Either[ResourceteamRow, ResourcepoolRow], TeamSummary)] = result.flatMap { x =>
      x._2.map { line =>
        (x._1, line)
      }
    }

    for (rowNum <- result2.indices) {
      val r: Row = sheet.createRow(rowNum + 1)
      val ( teamPool:Either[ResourceteamRow,ResourcepoolRow],
            teamSum:TeamSummary) = result2(rowNum)
      val name = teamPool match {
        case Left(x) => x.name
        case Right(x) => x.name
      }
      r.createCell(0).setCellValue(name)
      if (teamSum.isContractor) {
        r.createCell(1).setCellValue(teamSum.agency)
      } else {
        r.createCell(1).setCellValue("Employee")
      }
      r.createCell(2).setCellValue(teamSum.country.getOrElse("-"))
      r.createCell(3).setCellValue(teamSum.positionType)
      r.createCell(4).setCellValue( teamSum.isPE)
      r.createCell(5).setCellValue(teamSum.tally)
    }
    val tmpDir = ConfigFactory.load.getString("scenario.tempdir")
    val f = java.io.File.createTempFile("resourceBreakdown-",".xls",new java.io.File(tmpDir))
    val os = new FileOutputStream(f)
    wb.write(os)
    wb.close()
    os.close()
    //val outFilename = s"resourceBreakdown".replaceAll(",|!","_")
    f.deleteOnExit()
    f
  }

  def newRP = LDAPAuthAction {
    Action.async { implicit request =>
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
      } yield (u, u)).map { resp =>
        val canEdit = resp._2
        if (canEdit) {
          forms.ResourcePoolForm.form.bindFromRequest.fold(
            form => {
              Future.successful(Redirect(routes.ResourcePoolController.search()))
            },
            data => {
              val newObj = ResourcepoolRow(id = 0, name = data.name, ordering = data.ordering, poolsize = data.poolsize)
              resourcePoolRepo.insert(newObj).map { o =>
                Redirect(routes.ResourcePoolController.id(o.id))
              }
            }
          )
        } else {
          Future.successful(Unauthorized(views.html.page_403("No Permission")))
        }
      }.flatMap(identity)
    }
  }


  def deleteRP(id:Int) = LDAPAuthAction{
    Action.async { implicit request =>
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
        mc <- resourcePoolRepo.find(id)
        teams <- resourcePoolRepo.findTeams(id)
      } yield (u, mc,teams)).map { resp =>
        val canEdit = resp._1
        if (canEdit) {
          resp._2 match {
            case Some(mc) =>
              if ( resp._3.nonEmpty) {
                Future.successful(Redirect(routes.ResourcePoolController.id(id)).flashing("message"->"Can't delete a pool with teams"))
              } else {
                resourcePoolRepo.delete(id).map { x =>
                  Redirect(routes.ResourcePoolController.search())
                }
              }
            case None => Future.successful(NotFound(views.html.page_404("Resource pool not found")))
          }
        } else {
          Future.successful(Unauthorized(views.html.page_403("No Permission")))
        }
      }.flatMap(identity)
    }
  }


  def updateRP(id:Int) = LDAPAuthAction {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          (for {
            u <- user.isAdmin(LDAPAuth.getUser())
            rec <- resourcePoolRepo.find(id)
          } yield (u, rec)).map { result =>
            val canEdit = result._1
            val recO = result._2
            if (canEdit) {
              recO match {
                case Some(rec) =>
                  val fieldName: String = keys.getOrElse("name", Seq("x")).head.toLowerCase
                  val valueSeq: Seq[String] = keys.getOrElse("value", Seq.empty)
                  val res: Future[Result] = valueSeq.headOption match {
                    case None => Future.successful(NotAcceptable("Missing value"))
                    case Some(value) =>
                      val updRec = fieldName match {
                        case "name" => rec.copy(name = value)
                        case "ordering" => rec.copy(ordering = value.toInt)
                        case "poolsize" => rec.copy(poolsize = value.toInt)
                        case _ => rec
                      }
                      resourcePoolRepo.update(id, updRec)
                        .map { r =>
                          if (r) {
                            Ok("Updated")
                          } else {
                            NotFound("Invalid Value/Not Updated")
                          }
                        }
                  }
                  res
                case None => Future.successful(NotFound(views.html.page_404("Resource Pool not found")))
              }
            } else {
              Future.successful(Unauthorized(views.html.page_403("No Permission")))
            }
          }.flatMap(identity)
      }
    }
  }


}

