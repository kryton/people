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

import models.people._
import models.product.{ProductFeatureRepo, ProductTrackRepo, StageRepo}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import util.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class StageController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
   productFeatureRepo: ProductFeatureRepo,
   resourcePoolRepo: ResourcePoolRepo,
   stageRepo: StageRepo,
   user: User

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  //implicit val ldap: LDAP = new LDAP
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */



  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
      search match {
        case None => stageRepo.all.map{ pt => Ok(views.html.product.stage.search( Page(pt,page),search)) }
        case Some(searchString) => stageRepo.search(searchString).map{ emps =>
          if (emps.size == 1 ) {
            Redirect(routes.StageController.id( emps.head.id))
          } else {
            Ok(views.html.product.stage.search(Page(emps,page), search))
          }
        }
      }
  }

  def id(stageId:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    stageRepo.find(stageId).map {
      case Some(stage) =>
        val features = productFeatureRepo.findFeatureByStage(stageId).map{ pfs =>
          Future.sequence(pfs.map{ pf =>
            productFeatureRepo.findTracksByFeature(pf.id).map { f => ( pf, f )}
          })
        }.flatMap(identity)

        (for {
          f <- features
        } yield(f))
        .map { x =>
          Ok(views.html.product.stage.id( stageId, stage, Page(x,page) ) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Stage not found")))
    }.flatMap(identity)
  }

}

