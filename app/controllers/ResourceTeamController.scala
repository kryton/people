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

import java.sql.Date
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject._

import models.people._
import models.product.{ResourcePoolRepo, _}
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import projectdb.Tables
import projectdb.Tables._
import slick.jdbc.JdbcProfile
import util.{LDAP, Page, User}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ResourceTeamController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
   productFeatureRepo: ProductFeatureRepo,
   resourcePoolRepo: ResourcePoolRepo,
   resourceTeamRepo: ResourceTeamRepo,
   empEfficiencyRepo: EmpEfficiencyRepo,
   user: User

  )(implicit
    teamDescriptionRepo: TeamDescriptionRepo,
    empHistoryRepo: EmpHistoryRepo,
    positionTypeRepo: PositionTypeRepo,
    matrixTeamRepo: MatrixTeamRepo,
    matrixTeamMemberRepo: MatrixTeamMemberRepo,
    officeRepo:OfficeRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

 // implicit val ldap: LDAP = new LDAP
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */



  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val pageSize =40
    val isAdminF = user.isAdmin(LDAPAuth.getUser())
      search match {
        case None =>
          (for {
            r <-  resourceTeamRepo.allEx
            u <- isAdminF
            p <- resourcePoolRepo.all
          } yield (r,u))
            .map{ pt => Ok(views.html.product.team.search( Page(pt._1,page,pageSize),search,pt._2)) }
        case Some(searchString) =>
          (for {
            r <- resourceTeamRepo.searchEx(searchString)
            u <- isAdminF
          } yield (r,u))
         .map{ emps =>
          if (emps._1.size == 1 ) {
            Redirect(routes.ResourceTeamController.id( emps._1.head._1.id))
          } else {
            Ok(views.html.product.team.search(Page(emps._1,page,pageSize), search, emps._2))
          }
        }
      }
  }


  def id(rtId:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    resourceTeamRepo.findEx(rtId).map {
      case Some(rtRP) =>
        val features: Future[Seq[(ProductfeatureRow, ResourceteamproductfeatureRow, Seq[(ProducttrackRow, ProducttrackfeatureRow)])]] =
          resourceTeamRepo.findFeatures(rtId).map { rfS =>
            Future.sequence(rfS.map { rtP =>
              productFeatureRepo.findTracksByFeature(rtP._2.id).map { f => (rtP._2, rtP._1, f)
              }
            }).map( unsorted => unsorted.sortBy(_._1.ordering))
          }.flatMap(identity)

        val pplTeamName = rtRP._1.pplteamname.getOrElse("")
        val teamMembers: Future[Set[(offline.Tables.EmprelationsRow, Boolean)]] = teamDescriptionRepo.findTeamMembers(pplTeamName).map { teamM =>
          Future.sequence( teamM.map{ emp =>
            matrixTeamMemberRepo.isPE(emp.login).map( mtm => ( emp,mtm))
          })
        }.flatMap(identity)
        val teamDetails: Future[Seq[(offline.Tables.EmprelationsRow, String, Option[Date])]] = teamDescriptionRepo.getPositionHireForTeam(pplTeamName)
        val teamDevStats: Future[Seq[(offline.Tables.EmprelationsRow, Int, EfficencyMonth)]] = resourceTeamRepo.getDevStatsForTeam( rtRP._1.id)

        (for {
          f <- features
          t <- teamMembers
          ts <- teamDevStats
          td <- teamDetails
          teamSummary <- resourceTeamRepo.getTeamSummaryByVendorCountry( rtId)
          isAdmin <- user.isAdmin( LDAPAuth.getUser())
          rp <- resourcePoolRepo.all.map{ x => x.sortBy(_.ordering).map{ rpool => rpool.id -> rpool.name}}
        } yield (f,t,ts,td,isAdmin, teamSummary, rp )).map { x =>
          val tMembers: Set[(offline.Tables.EmprelationsRow, Boolean)] = x._2
          val rpoolLookup = x._7
          val prodTeamDet: (Int,EfficencyMonth) = x._3
            .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 +1,z._2.add( i._3)) )

          Ok(views.html.product.team.id( rtId, rtRP, Page(x._1,page, pageSize = 20), tMembers, prodTeamDet, x._4, x._5, x._6, Seq(0 -> "None") ++ rpoolLookup ) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Team not found")))
    }.flatMap(identity)
  }


  def updateRT(id:Int) = LDAPAuthAction {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          (for {
            u <- user.isAdmin(LDAPAuth.getUser())
            rec <- resourceTeamRepo.find(id)
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
                        case "teamsize" => rec.copy(teamsize = value.toInt)
                        case "msprojectname" => rec.copy(msprojectname = value)
                        case "pplteamname" => rec.copy(pplteamname = Option(value))
                        case _ => rec
                      }
                      resourceTeamRepo.update(id, updRec)
                        .map { r =>
                          if (r) {
                            Ok("Updated")
                          } else {
                            NotFound("Invalid Value/Not Updated")
                          }
                        }
                  }
                  res
                case None => Future.successful(NotFound(views.html.page_404("Resource Team not found")))
              }
            } else {
              Future.successful(Unauthorized(views.html.page_403("No Permission")))
            }
          }.flatMap(identity)
      }
    }
  }


  def updateRTPool(id:Int) = LDAPAuthAction {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          (for {
            u <- user.isAdmin(LDAPAuth.getUser())
            rec <- resourceTeamRepo.find(id)
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

                      val updRec: Future[ResourceteamRow] = fieldName match {
                        case "pool" =>
                          if ( value.toInt == 0) {
                            Future.successful(rec.copy(resourcepoolid = None))
                          } else {
                            resourcePoolRepo.find(value.toInt).map {
                                case Some(rp) => rec.copy( resourcepoolid = Some(rp.id))
                                case None => rec
                            }
                          }
                        case _ =>  Future.successful(rec)
                      }
                      updRec.map { upd =>
                        resourceTeamRepo.update(id, upd).map { r =>
                          if (r) {
                            Ok("Updated")
                          } else {
                            NotFound("Invalid Value/Not Updated")
                          }
                        }
                      }.flatMap(identity)
                  }
                  res
                case None => Future.successful(NotFound(views.html.page_404("Resource Team not found")))
              }
            } else {
              Future.successful(Unauthorized(views.html.page_403("No Permission")))
            }
          }.flatMap(identity)
      }
    }
  }

  def doBreakdown(id:Int) = Action.async { implicit request =>
    resourceTeamRepo.find(id).map {
      case Some(pf) =>
        (for {
          p <- resourceTeamRepo.breakDownTeam(id)
        } yield p ).map { x =>

          val dates = x._1.flatMap{ p=> p._3.keys }

          val monthRange: Seq[Date] = if ( dates.isEmpty) {
            Seq.empty
          } else {
            val minX: Date = dates.minBy(_.getTime)
            val maxX: Date = dates.maxBy(_.getTime)
            util.Conversions.monthRange(minX, maxX)
          }
          val monthsLimit: Seq[Date] = monthRange.sortBy(_.getTime).slice(0,15) //16 dates = 5Q
          //=  util.Conversions.monthRange(minX, maxX)

          val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-YYYY")

          Ok(views.html.product.team.breakout(id, pf, x._1, monthsLimit, dateFormat))
        }

      case None => Future.successful(NotFound(views.html.page_404("Feature not found")))
    }.flatMap(identity)
  }
}

