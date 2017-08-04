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

import models.product._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import projectdb.Tables.ReleaseauthorizationtypeRow
import slick.jdbc.JdbcProfile
import util.{Conversions, LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ReleaseController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   releaseAuthorizationTypeRepo: ReleaseAuthorizationTypeRepo,
   releaseAuthorizationRepo: ReleaseAuthorizationRepo,
   releaseTypeAuthorizationPeopleRepo: ReleaseTypeAuthorizationPeopleRepo,
   user: User

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  //implicit val ldap: LDAP = new LDAP
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */


  def releaseAuthorizationTypeSearch(): Action[AnyContent] = Action.async { implicit request =>
    releaseAuthorizationTypeRepo.all.map { pt => Ok(views.html.release.auth.search(Page(pt), None, forms.AuthorizationTypeForm.form)) }
  }

  def releaseAuthorizationType(id: Int, page: Int, page2: Int): Action[AnyContent] = Action.async { implicit request =>

    (for {
      rat <- releaseAuthorizationTypeRepo.find(id)
      relPPl <- releaseAuthorizationRepo.findByReleaseAuthorityType(id)
      relTypePPl <- releaseTypeAuthorizationPeopleRepo.findByReleaseAuthorizationType(id)
    } yield (rat, relTypePPl, relPPl)).map { x =>
      x._1 match {
        case Some(rat) => Ok(views.html.release.auth.id(id, rat, Page(x._2, page), Page(x._3, page2)))
        case None => NotFound(views.html.page_404("Authorization not found"))
      }
    }
  }

  def releaseAuthorizationTypeNew() = LDAPAuthPermission("ReleaseAuthType") {
    Action.async { implicit request =>
      forms.AuthorizationTypeForm.form.bindFromRequest.fold(
        form => {
          Future.successful(Redirect(routes.ReleaseController.releaseAuthorizationTypeSearch()))
        },
        data => {
          releaseAuthorizationTypeRepo.insert(ReleaseauthorizationtypeRow(id = 0, name = data.name, description = data.description)).map { result =>
            Redirect(routes.ReleaseController.releaseAuthorizationType(result.id))
          }
        }
      )
    }
  }
  def releaseAuthorizationTypeUPD(id:Int) = LDAPAuthPermission("ReleaseAuthType") {
    Action.async { implicit request =>
      forms.AuthorizationTypeForm.form.bindFromRequest.fold(
        form => {
          Future.successful(Redirect(routes.ReleaseController.releaseAuthorizationType(id)))
        },
        data => {
          releaseAuthorizationTypeRepo.find(id).map {
            case Some(rat) => releaseAuthorizationTypeRepo.update(id, rat.copy( name=data.name, description = data.description)).map { result =>
              Redirect(routes.ReleaseController.releaseAuthorizationType(id))
            }
            case None => Future.successful(Redirect(routes.ReleaseController.releaseAuthorizationType(id)))
          }.flatMap(identity)
        }
      )
    }
  }
}

