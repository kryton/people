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

import models.people._
import offline.Tables
import offline.Tables.{CostcenterRow, FunctionalareaRow, ProfitcenterRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import slick.jdbc.JdbcProfile
import utl.importFile.CostCenterImport
import utl.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class OfficeController @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider,
  employeeRepo: EmployeeRepo,
  costCenterRepo: CostCenterRepo,
  officeRepo: OfficeRepo,
  user: User
)(implicit
  ec: ExecutionContext,
  //override val messagesApi: MessagesApi,
  cc: ControllerComponents,
  webJarAssets: org.webjars.play.WebJarAssets,
  webJarsUtil: org.webjars.play.WebJarsUtil,
  assets: AssetsFinder,
  ldap: LDAP
) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {


  def search(page: Int, search: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    officeRepo.active.map { pt =>
      val countries: Map[Option[String], Seq[(Tables.OfficeRow,Int)]] = pt.groupBy(_._1.country)
      Ok(views.html.person.office.search(countries))
    }

  }
  def byCountry(country:String, page:Int) = Action.async{ implicit request =>
    employeeRepo.findByCountry(country).map{ seq =>
      Ok(views.html.person.office.byCountry( country, Page(seq, page, pageSize = 60)))
    }
   // Future.successful(Ok(""))
  }
  def id(officeId: Long, page: Int): Action[AnyContent] = Action.async { implicit request =>

    officeRepo.find(officeId).map {
      case Some(office) =>
        (for {
          emps <- employeeRepo.findByOffice(office.id)
        } yield (emps,emps)).map { x =>
          Ok(views.html.person.office.id(officeId, office,  Page(x._1, page, pageSize = 60)))
        }
      case None => Future.successful(NotFound(views.html.page_404("Costcenter not found")))
    }.flatMap(identity)
  }


}

