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

import javax.inject._

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
    val pageSize =20
      search match {
        case None => resourcePoolRepo.all.map{ pt => Ok(views.html.product.pool.search( Page(pt,page,pageSize),search)) }
        case Some(searchString) => resourcePoolRepo.search(searchString).map{ emps =>
          if (emps.size == 1 ) {
            Redirect(routes.ResourcePoolController.id( emps.head.id))
          } else {
            Ok(views.html.product.pool.search(Page(emps,page,pageSize), search))
          }
        }
      }
  }

  def id(rpId:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    resourcePoolRepo.find(rpId).map {
      case Some(rp) =>
        val teams: Future[Seq[(ResourceteamRow, Seq[(offline.Tables.EmprelationsRow, Int, EfficencyMonth)])]] = resourceTeamRepo.findInPool( rp.id).map{
          teamS => Future.sequence( teamS.map { team =>
            resourceTeamRepo.getDevStatsForTeam(team.id).map{ ds =>
              (team, ds)
            }
          })
        }.flatMap(identity)

        (for {
          t <- teams
        } yield t)
        .map { x =>
          val byTeam: Seq[(ResourceteamRow, (Int, EfficencyMonth))] = x.map{ tE => ( tE._1, tE._2
            .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 +1,z._2.add( i._3))  ))
          }
          val byPool: (Int, EfficencyMonth) = byTeam.map( _._2)
                      .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 + i._1 ,z._2.add( i._2) ))

          Ok(views.html.product.pool.id( rpId, rp, byPool, Page( byTeam,page, pageSize = 20)) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Team not found")))
    }.flatMap(identity)
  }

}

