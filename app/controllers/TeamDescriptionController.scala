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
import javax.inject._

import models.people._
import offline.Tables.{EmprelationsRow, EmptagRow, TeamdescriptionRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.people.EmpRelationsRowUtils._
import org.joda.time.DateTime
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
class TeamDescriptionController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   employeeRepo: EmployeeRepo,
   teamDescriptionRepo: TeamDescriptionRepo,
   user: User

  )(implicit
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
    val pageSize = 60
      search match {
        case None => teamDescriptionRepo.all.map{ emps =>
          val uniqueTags:List[(String,Int)] = emps.groupBy(_.tagtext).map{ x => (x._1, x._2.size)} .toList.sortBy( x => x._1.toLowerCase)

          Ok(views.html.team.search(Page(uniqueTags,page, pageSize = pageSize),search)) }
        case Some(searchString) => teamDescriptionRepo.search(searchString).map{ emps =>
          val uniqueTags:List[(String,Int)] = emps.groupBy(_.tagtext).map{ x => (x._1, x._2.size)} .toList.sortBy( x => x._1.toLowerCase)

          if (uniqueTags.size == 1 ) {
            Redirect(routes.TagController.id( uniqueTags.head._1))
          } else {

            Ok(views.html.team.search(Page(uniqueTags,page,pageSize = pageSize),search))
          }
        }
      }
  }

  def id( tag:String,page:Int): Action[AnyContent] = Action.async { implicit request =>
    teamDescriptionRepo.findTeamMembers(tag).map { emps:Set[EmprelationsRow] =>
      emps.toList match {
        case Nil => NotFound(views.html.page_404("Team not found"))
        case empList =>
          Ok(views.html.team.id(tag, Page(empList.sortBy( x=> (x.lastname,x.firstname )), page, pageSize = 9)))
      }
    }
  }
  def setTeam( login:String): Action[AnyContent] = Action.async { implicit request =>
    request.body.asFormUrlEncoded match {
      case None =>Future.successful( NotFound("No Team"))
      case Some((keys: Map[String, Seq[String]])) =>
        if ( keys.getOrElse("name",Seq("x")).head.equalsIgnoreCase("team") ) {
          keys.get("value") match {
            case None => Future.successful( NotFound("No Value"))
            case Some(valueS) => valueS.headOption match {
              case None => Future.successful(NotFound("missing value for Value"))
              case Some(value: String) =>
                teamDescriptionRepo.upsert( login, value).map { x =>
                  Ok("value updated")
                }
              //  Future.successful()
            }
          }
        } else {
          Future.successful(NotFound("Missing team field"))
        }
    }
   // Future.successful(Ok(""))
  }
}

