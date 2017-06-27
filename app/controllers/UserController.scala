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

import models.auth.{UserPrefRepo, UserRepo}
import models.people._
import offline.Tables.{CostcenterRow, FunctionalareaRow, ProfitcenterRow}
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import slick.jdbc.JdbcProfile
import util.importFile.CostCenterImport
import util.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class UserController @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider,
  employeeRepo: EmployeeRepo,
  userPrefRepo: UserPrefRepo,
  user: User
)(implicit
  ec: ExecutionContext,
  //override val messagesApi: MessagesApi,
  cc: ControllerComponents,
  webJarAssets: WebJarAssets,
  assets: AssetsFinder,
  ldap: LDAP
) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  def prefs(userid: String ) = LDAPAuthAction {
    Action.async { implicit request =>
      LDAPAuth.getUser() match {
        case None => Future.successful(Unauthorized(views.html.page_403("You can't do this")))
        case Some(loggedInUser) =>

          val actualUser: String = userid
          user.isOwnerManagerOrAdmin(actualUser, Some(loggedInUser)).map {
            case true =>
              (for{
                e <- employeeRepo.findByLogin(actualUser)
                uprefz <- userPrefRepo.userPrefs(actualUser).map( list => list.map{ u => u._2.id -> u._2}.toMap)
                prefz <- userPrefRepo.allPrefs.map{ list => list.map { u => u.id -> u}.toMap }
              } yield(e,uprefz, prefz)).map{ x=>
                x._1 match {
                  case Some(emp) => Ok(views.html.prefs.updatePrefs(actualUser, emp, x._2, x._3 ))
                  case None => NotFound(views.html.page_404("Login not found"))
                }

              }

            case false => Future.successful(Unauthorized(views.html.page_403("You can't do this")))

          }.flatMap(identity)

      }
    }
  }

  def enabledisable(login:String, pref:Long, enable:Boolean) = LDAPAuthAction {

    Action.async { implicit request =>
      val loggedInUser = LDAPAuth.getUser()
      user.isOwnerManagerOrAdmin(login, loggedInUser ).map {
        case true =>
          userPrefRepo.findPref(pref).map{
            case Some(preference) =>
              ( if (enable) {
                userPrefRepo.enablePref(login, pref).map { result =>
                  user.getUserSession(login).map { session =>
                    if ( loggedInUser.getOrElse("-").equalsIgnoreCase(login)) {
                      Redirect(routes.UserController.prefs(login)).withSession(session:_*)//.addingToSession(session:_*)
                    } else {
                      Redirect(routes.UserController.prefs(login))
                    }
                  }
                }
              } else {
                userPrefRepo.disablePref(login, pref).map { result =>
                  user.getUserSession(login).map{ session =>
                    if ( loggedInUser.getOrElse("-").equalsIgnoreCase(login)) {

                      Redirect(routes.UserController.prefs(login)).withSession(session:_*)//.removingFromSession(preference.code).addingToSession(session:_*)
                    } else {
                      Redirect(routes.UserController.prefs(login))
                    }
                  }
                }
              }).flatMap(identity)
            case None => Future.successful(NotFound(views.html.page_404("Preference not found")))
          }.flatMap(identity)

        case false =>  Future.successful(Unauthorized(views.html.page_403("You can't do this")))
      }.flatMap(identity)
    }
  }
}

