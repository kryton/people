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

package models.auth

import javax.inject.Inject

import offline.Tables.{Authpermission, AuthpermissionRow, Authrolepermission, AuthrolepermissionRow, Authrole, AuthroleRow}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class RolePermissionRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[AuthrolepermissionRow]] =
    db.run(Authrolepermission.filter(_.id === id).result.headOption)
  def find(permissionId:Long, roleId:Long):Future[Option[AuthrolepermissionRow]] =
    db.run(Authrolepermission.filter(_.roleid === roleId).filter(_.permissionid === permissionId).result.headOption)

  def findByRole(roleId:Long):Future[Seq[AuthrolepermissionRow]] =
    db.run(Authrolepermission.filter(_.roleid === roleId ).result)

  def findByPermission(permissionId:Long):Future[Seq[AuthrolepermissionRow]] =
    db.run(Authrolepermission.filter(_.permissionid === permissionId ).result)

  def findByRoleEx(roleId:Long):Future[Seq[(AuthrolepermissionRow,AuthpermissionRow)]] =
    db.run(Authrolepermission.filter(_.roleid === roleId )
      .join(Authpermission).on(_.permissionid === _.id) .result)

  def findByPermissionEx(permissionId:Long):Future[Seq[(AuthrolepermissionRow,AuthroleRow)]] =
    db.run(Authrolepermission.filter(_.permissionid === permissionId )
      .join(Authrole).on(_.roleid === _.id).result)

  def findOrCreate(permissionId:Long, roleId:Long):Future[AuthrolepermissionRow] = {
    find(permissionId, roleId).map {
      case Some(rolePermission) => Future.successful(rolePermission)
      case None => insert(AuthrolepermissionRow(id = 0,permissionid= permissionId, roleid = roleId))
    }
  }.flatMap(identity)

  def all = db.run(Authrolepermission.result)

  def insert(pt: AuthrolepermissionRow): Future[AuthrolepermissionRow] = db
    .run(Authrolepermission returning Authrolepermission.map(_.id) += pt)
    .map(id => pt.copy(id = id))

  def update(id: Long, pt:AuthrolepermissionRow) = {
    db.run(Authrolepermission.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Authrolepermission.filter(_.id === id).delete) map { _ > 0 }
}
