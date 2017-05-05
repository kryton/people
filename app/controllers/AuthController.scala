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

import forms.AuthPermissionForm
import models.auth._
import models.people._
import offline.Tables
import offline.Tables.{AuthpermissionRow, AuthroleRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import slick.jdbc.JdbcProfile
import util.{Conversions, LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class AuthController @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider,
  employeeRepo: EmployeeRepo,
  userPrefRepo: UserPrefRepo,
  userRepo:UserRepo,
  roleRepo:RoleRepo,
  permissionRepo:PermissionRepo,
  rolePermissionRepo:RolePermissionRepo,
  user: User
)(implicit
  ec: ExecutionContext,
  //override val messagesApi: MessagesApi,
  cc: ControllerComponents,
  webJarAssets: WebJarAssets,
  assets: AssetsFinder,
  ldap: LDAP
) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {


  def roleSearch(page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    user.isAdmin(LDAPAuth.getUser()).map { isAdmin =>
      search match {
        case None => roleRepo.all.map { pt => Ok(views.html.auth.search(Page(pt, page), search, canEdit = isAdmin, forms.AuthRoleForm.form)) }
        case Some(searchString) => roleRepo.search(searchString).map { roles =>
          if (roles.size == 1) {
            Redirect(routes.AuthController.role(roles.head.id))
          } else {
            Ok(views.html.auth.search(Page(roles, page), search, canEdit = isAdmin, forms.AuthRoleForm.form))
          }
        }
      }
    }.flatMap(identity)
  }

  def role(roleId:Long, page:Int): Action[AnyContent] = Action.async{ implicit request =>
    user.isAdmin( LDAPAuth.getUser()).map { canEdit =>
      roleRepo.find(roleId).map {
        case Some(roleRec) =>

          (for {
            f <- userRepo.findByRole(roleId)
            p <- permissionRepo.findByRole(roleId)
            pA <- permissionRepo.all
          } yield (f, p, pA))
            .map { x =>
              val hasPerms = x._2.map{ p => p._2.id }.toSet
              val remainingPerms = x._3.filterNot( p=> hasPerms.contains(p.id))
              Ok(views.html.auth.role(roleId, roleRec, Page(x._1, page), x._2,
                canEdit,
                forms.AuthRolePermissionForm.form,
                remainingPerms,
                forms.AuthUserRoleForm.form ))
            }

        case None => Future.successful(NotFound(views.html.page_404("Stage not found")))
      }.flatMap(identity)
    }.flatMap(identity)
  }

  def permission(permissionId:Long): Action[AnyContent] = Action.async{ implicit request =>

    permissionRepo.find(permissionId).map {
      case Some(permissionRec) =>
        (for {
          p <- roleRepo.findByPermission(permissionId )
        } yield(p))
          .map { x =>
            Ok(views.html.auth.permission( permissionId, permissionRec, x ) )
          }

      case None => Future.successful(NotFound(views.html.page_404("Stage not found")))
    }.flatMap(identity)
  }

  def userroles(userid: String ) = LDAPAuthAction {
    Action.async { implicit request =>
      LDAPAuth.getUser() match {
        case None => Future.successful(Unauthorized(views.html.page_403("You can't do this")))
        case Some(loggedInUser) =>
          val actualUser: String = userid
          user.isOwnerManagerOrAdmin(actualUser, Some(loggedInUser)).map {
            case true =>
              (for{
                e <- employeeRepo.findByLogin(actualUser)
                urolez <- userRepo.find(actualUser).map(list => list.map{ u => u._2.id -> u._2}.toMap)
                rolez <- roleRepo.all.map{ list => list.map { u => u.id -> u}.toMap }
              } yield(e, urolez, rolez)).map{ x=>
                x._1 match {
                  case Some(emp) => Ok(views.html.auth.updateUserRoles(actualUser, emp, x._2, x._3 ))
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
      user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map {
        case true =>
          //val enableIt = enable.getOrElse(false)
          userPrefRepo.findPref(pref).map{
            case Some(preference) =>
              if (enable) {
                userPrefRepo.enablePref(login, pref).map { result =>
                  Redirect(routes.UserController.prefs(login)).addingToSession(preference.code -> "Y")
                }
              } else {
                userPrefRepo.disablePref(login, pref).map { result =>
                  Redirect(routes.UserController.prefs(login)).removingFromSession(preference.code)
                }
              }
            case None => Future.successful(NotFound(views.html.page_404("Preference not found")))
          }.flatMap(identity)

        case false =>  Future.successful(Unauthorized(views.html.page_403("You can't do this")))
      }.flatMap(identity)
    }
  }

  def permissionSearch(page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    user.isAdmin(LDAPAuth.getUser()).map { isAdmin =>
      search match {
        case None => permissionRepo.all.map { pt =>
          Ok(views.html.auth.permissionSearch(Page(pt, page), search, canEdit = isAdmin, AuthPermissionForm.form))
        }
        case Some(searchString) => permissionRepo.search(searchString).map { permissions =>
          if (permissions.size == 1) {
            Redirect(routes.AuthController.role(permissions.head.id))
          } else {
            Ok(views.html.auth.permissionSearch(Page(permissions, page), search, canEdit = isAdmin, AuthPermissionForm.form))
          }
        }
      }
    }.flatMap(identity)
  }

  def newPermission = Action.async { implicit request =>
    user.isAdmin( LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        forms.AuthPermissionForm.form.bindFromRequest.fold(
          form => {
            Future.successful(Redirect(routes.AuthController.permissionSearch()))
          },
          data => {
            val newObj = AuthpermissionRow(id = 0, permission = data.permission, description = data.description)

            permissionRepo.insert(newObj).map { o =>
              Redirect(routes.AuthController.permissionSearch())
            }
          }
        )
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def newRole = Action.async { implicit request =>
    user.isAdmin( LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        forms.AuthRoleForm.form.bindFromRequest.fold(
          form => {
            Future.successful(Redirect(routes.AuthController.roleSearch()))
          },
          data => {
            val newObj = AuthroleRow(id = 0, role = data.role, description = data.description, isadmin = data.isAdmin)

            roleRepo.insert(newObj).map { o =>
              Redirect(routes.AuthController.roleSearch())
            }
          }
        )
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def newUserRole(roleId:Long ) = Action.async { implicit request =>
    (for{
      role <- roleRepo.find(roleId)
      u <- user.isAdmin(LDAPAuth.getUser())
    } yield ( role, u)).map { resp =>
      val canEdit = resp._2
      val roleO = resp._1
      roleO match {
        case Some(role) =>
          if (canEdit) {
            forms.AuthUserRoleForm.form.bindFromRequest.fold(
              form => {
                Future.successful(Redirect(routes.AuthController.role(roleId)))
              },
              data => {
                employeeRepo.findByLogin(data.login).map{
                  case Some(emp) =>
                    val newObj = Tables.AuthuserRow(id = 0, roleid = data.roleId, username = data.login)
                    userRepo.insert(newObj).map { o =>
                      Redirect(routes.AuthController.role(roleId))
                    }
                  case None => Future.successful( NotFound(views.html.page_404("login not found")))
                }.flatMap(identity)
              }
            )
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }
        case None =>  Future.successful(NotFound(views.html.page_404("Role not found")))
      }
    }.flatMap(identity)
  }
  def newRolePermission(roleId:Long ) = Action.async { implicit request =>
    (for{
      role <- roleRepo.find(roleId)
      u <- user.isAdmin(LDAPAuth.getUser())
    } yield ( role, u)).map { resp =>
      val canEdit = resp._2
      val roleO = resp._1
      roleO match {
        case Some(role) =>
          if (canEdit) {
            forms.AuthRolePermissionForm.form.bindFromRequest.fold(
              form => {
                Future.successful(Redirect(routes.AuthController.role(roleId)))
              },
              data => {
                val permId = data.permissionId.toLong
                permissionRepo.find(data.permissionId).map{
                  case Some(perm) =>
                    val newObj = Tables.AuthrolepermissionRow(id = 0, roleid = data.roleId, permissionid = permId)
                    rolePermissionRepo.insert(newObj).map { o =>
                      Redirect(routes.AuthController.role(roleId))
                    }
                  case None => Future.successful( NotFound(views.html.page_404("Permission not found")))
                }.flatMap(identity)
              }
            )
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }
        case None =>  Future.successful(NotFound(views.html.page_404("Role not found")))
      }
    }.flatMap(identity)
  }
}

