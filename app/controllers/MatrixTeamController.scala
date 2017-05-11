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

import models.people.EmpRelationsRowUtils._
import models.people._
import models.product.ProductTrackRepo
import offline.Tables.{EmprelationsRow, MatrixteamRow}
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
class MatrixTeamController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
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
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap: LDAP
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
        case None => matrixTeamRepo.all.map{ emps => Ok(views.html.matrix.search(Page(emps,page),search)) }
        case Some(searchString) => matrixTeamRepo.search(searchString).map{ emps:Seq[MatrixteamRow] =>
          if (emps.size == 1 ) {
            Redirect(routes.MatrixTeamController.id( emps.head.id))
          } else {
            Ok(views.html.matrix.search(Page(emps,page),search))
          }
        }
      }
  }

  def id( ID:Long,page:Int): Action[AnyContent] = Action.async{ implicit request =>
    matrixTeamRepo.find(ID).map {
      case Some(mt: MatrixteamRow) =>
        val empsF = matrixTeamMemberRepo.findLoginByMatrixTeam(mt.id)
        (for {
          emps <- empsF
        } yield emps)
        .map { x =>
          Ok(views.html.matrix.id( ID, mt, Page(x,page, pageSize = 9) ))
        }
      case None => Future.successful(NotFound(views.html.page_404("Team ID not found")))
    }.flatMap(identity)
  }
}

