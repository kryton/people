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
import offline.Tables
import offline.Tables.{EmptagRow, EmprelationsRow }
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
class TagController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
//   @NamedDatabase("offline") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   employeeRepo: EmployeeRepo,
   teamDescriptionRepo: TeamDescriptionRepo,
   empTagRepo: EmpTagRepo,
   kudosToRepo: KudosToRepo,
   user: User

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

 // implicit val ldap: LDAP =  LDAP
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
        case None => empTagRepo.all.map{ emps =>
          val uniqueTags:List[(String,Int)] = emps.groupBy(_.tagtext).map{ x => (x._1, x._2.size)} .toList.sortBy( x => x._1.toLowerCase)

          Ok(views.html.tag.search(Page(uniqueTags,page, pageSize = pageSize),search)) }
        case Some(searchString) => empTagRepo.search(searchString).map{ emps =>
          val uniqueTags:List[(String,Int)] = emps.groupBy(_.tagtext).map{ x => (x._1, x._2.size)} .toList.sortBy( x => x._1.toLowerCase)

          if (uniqueTags.size == 1 ) {
            Redirect(routes.TagController.id( uniqueTags.head._1))
          } else {

            Ok(views.html.tag.search(Page(uniqueTags,page,pageSize = pageSize),search))
          }
        }
      }
  }

  def id( tag:String,page:Int): Action[AnyContent] = Action.async{ implicit request =>
    empTagRepo.findByTag(tag).map {
      case Nil => Future.successful(NotFound(views.html.page_404("Tag not found")))
      case tagList =>
        val tagEmpF: Future[Seq[(EmptagRow, Option[EmprelationsRow])]] = Future.sequence(tagList.map { tagLine =>
           employeeRepo.findByLogin( tagLine.login).map{ x => ( tagLine, x)}
        })
        tagEmpF.map { tagEmp => Ok( views.html.tag.id( tag, Page(tagEmp,page) ))}

  //      Ok(views.html.tag.id( tag, Page(tagList,page) ))
        }.flatMap(identity)
  }
}

