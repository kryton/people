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
import java.util.concurrent.TimeUnit
import javax.inject._

import models.people._
import models.product.{ResourcePoolRepo => _, _}
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
    val pageSize =20
      search match {
        case None => resourceTeamRepo.allEx.map{ pt => Ok(views.html.product.team.search( Page(pt,page,pageSize),search)) }
        case Some(searchString) => resourceTeamRepo.searchEx(searchString).map{ emps =>
          if (emps.size == 1 ) {
            Redirect(routes.ResourceTeamController.id( emps.head._1.id))
          } else {
            Ok(views.html.product.team.search(Page(emps,page,pageSize), search))
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
          isAdmin <- user.isAdmin( LDAPAuth.getUser())
        } yield (f,t,ts,td,isAdmin )).map { x =>
          val tMembers: Set[(offline.Tables.EmprelationsRow, Boolean)] = x._2
         /*
          x._3.foreach{ e =>
            Logger.info( e._1.login )
          }
          */
          val prodTeamDet: (Int,EfficencyMonth) = x._3
            .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 +1,z._2.add( i._3)) )

          Ok(views.html.product.team.id( rtId, rtRP, Page(x._1,page, pageSize = 20), tMembers, prodTeamDet, x._4, x._5 ) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Team not found")))
    }.flatMap(identity)
  }

}

