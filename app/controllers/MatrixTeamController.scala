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
import offline.Tables
import offline.Tables.{EmprelationsRow, MatrixteamRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import utl.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class MatrixTeamController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   //@NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
   teamDescriptionRepo: TeamDescriptionRepo,
   matrixTeamRepo: MatrixTeamRepo,
   matrixTeamMemberRepo: MatrixTeamMemberRepo,
   user: User

  )(implicit
    employeeRepo: EmployeeRepo,
    empHistoryRepo: EmpHistoryRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{


  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
      search match {
        case None => matrixTeamRepo.all.map{ emps =>
          Ok(views.html.matrix.search(Page(emps,page),search))
        }
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
          teams <- empsF.map{ empS => Future.sequence( empS.map{ emp => teamDescriptionRepo.findTeamForLogin( emp.login).map{ x => emp.login -> x}}) }.flatMap(identity)
        } yield (emps,teams))
        .map { x =>
          val emps = x._1
          val teams: Map[String, Option[Tables.TeamdescriptionRow]] = x._2.toMap
          Ok(views.html.matrix.id( ID, mt, Page(emps,page, pageSize = 9), teams ))
        }
      case None => Future.successful(NotFound(views.html.page_404("Team ID not found")))
    }.flatMap(identity)
  }
}

