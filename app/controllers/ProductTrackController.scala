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

import java.nio.file.Path
import javax.inject._

import models.people.EmpRelationsRowUtils._
import models.people._
import models.product.{ProductFeatureRepo, ProductTrackRepo}
import offline.Tables.EmprelationsRow
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import utl.importFile.SAPImport
import utl.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProductTrackController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
   productFeatureRepo: ProductFeatureRepo,
   user: User

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */



  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
      search match {
        case None => productTrackRepo.all.map{ pt => Ok(views.html.product.productTrack.search( Page(pt,page),search)) }
        case Some(searchString) => productTrackRepo.search(searchString).map{ emps =>
          if (emps.size == 1 ) {
            Redirect(routes.ProductTrackController.id( emps.head.id))
          } else {
            Ok(views.html.product.productTrack.search(Page(emps,page), search))
          }
        }
      }
  }

  def id( id:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    productTrackRepo.find(id).map {
      case Some(emp) =>
        val features = productFeatureRepo.findByProductTrack(id)

        (for {
          f <- features
        } yield(f))
        .map { x =>
          Ok(views.html.product.productTrack.id( id, emp, Page(x,page, pageSize = 20) ) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Login not found")))
    }.flatMap(identity)
  }

}

