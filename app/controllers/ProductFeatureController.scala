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

import models.people.{EmployeeRepo, ResourcePoolRepo}
import models.product._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import projectdb.Tables._
import slick.jdbc.JdbcProfile
import util.importFile.{ProjectXLSImport, SAPImport}
import util.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProductFeatureController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,

   productFeatureRepo: ProductFeatureRepo,
   resourcePoolRepo: ResourcePoolRepo,
   user: User

  )(implicit
    resourceTeamRepo: ResourceTeamRepo,
    productTrackRepo: ProductTrackRepo,
    stageRepo: StageRepo,
    managedClientRepo: ManagedClientRepo,
    projectRepo: ProjectRepo,
    projectDependencyRepo: ProjectDependencyRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val pageSize = 20
      search match {
        case None => productFeatureRepo.all.map{ pt => Ok(views.html.product.productFeature.search( Page(pt,page, pageSize=pageSize),search)) }
        case Some(searchString) => productFeatureRepo.search(searchString).map{ emps =>
          if (emps.size == 1 ) {
            Redirect(routes.ProductFeatureController.id( emps.head.id))
          } else {
            Ok(views.html.product.productFeature.search(Page(emps,page,pageSize=pageSize), search))
          }
        }
      }
  }

  def id( id:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    productFeatureRepo.find(id).map {
      case Some(pf) =>
        val tracks = productFeatureRepo.findTracksByFeature(id)
        val flags = productFeatureRepo.findFeatureFlagsByFeature(id)
        val stage = stageRepo.find(pf.stageid)
        val rteams = productFeatureRepo.findResourceTeams(id)
        val managedClients: Future[Seq[(ManagedclientRow,ManagedclientproductfeatureRow)]] = productFeatureRepo.findByManagedClients(id)
        val projects = projectRepo.findByFeatureEx(id)

        (for {
          t <- tracks
          f <- flags
          s <- stage
          r <- rteams
          m <- managedClients
          p <- projects
        } yield(t,f,s,r,m,p))
        .map { x =>
          Ok(views.html.product.productFeature.id( id, pf, x._1, x._2, x._3, x._4, x._5, x._6 ) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Login not found")))
    }.flatMap(identity)
  }

  def importFile = LDAPAuthAction {
    Action.async { implicit request =>
      user.isAdmin(LDAPAuth.getUser()).map {
        case true =>Ok(views.html.product.productFeature.importFile() )
        case false => Unauthorized(views.html.page_403("User not authorized"))
      }
    }
  }
  def doImport = Action.async(parse.multipartFormData) { implicit request =>
      user.isAdmin(LDAPAuth.getUser()).map {
        case true =>
          request.body.file("importFile").map { picture =>
            val filename = picture.filename
            val path: Path = picture.ref.path
            ProjectXLSImport.importFile(path).map {
              case Left(errorMsg) => Future.successful(Ok(errorMsg))
              case Right(list) =>
                productFeatureRepo.repopulate( list ).map { x =>
                  Ok( views.html.product.productFeature.importFileResult(x))
                }
            }.flatMap(identity)
          }.getOrElse {
            Future.successful(Redirect(routes.ProductFeatureController.importFile()).flashing(
              "error" -> "Missing file"))
          }

        case false => Future.successful(Unauthorized("You don't have access to import SAP files"))

      }.flatMap(identity)
    }

}

