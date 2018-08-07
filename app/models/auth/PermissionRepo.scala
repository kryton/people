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

import offline.Tables
import offline.Tables.{Authpermission, AuthpermissionRow, Authrolepermission, Authuser, AuthuserRow}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class PermissionRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[AuthpermissionRow]] =
    db.run(Authpermission.filter(_.id === id).result.headOption)
  def find(name:String):Future[Option[AuthpermissionRow]] =
    db.run(Authpermission.filter(_.permission === name ).result.headOption)

  def findByRole(roleId:Long): Future[Seq[(Tables.AuthrolepermissionRow, Tables.AuthpermissionRow)]] =
    db.run(Authrolepermission.filter(_.roleid === roleId)
    .join(Authpermission).on(_.permissionid === _.id).result  )

  def findOrCreate(permission:String, desc:Option[String] = None):Future[AuthpermissionRow] = {
    find(permission).map {
      case Some(stage) => Future.successful(stage)
      case None => insert(AuthpermissionRow(id = 0,permission= permission, description = desc))
    }
  }.flatMap(identity)

  def permissionsForUser(user:String) :Future[Seq[AuthpermissionRow]] = {
    db.run(
      Authuser.filter(_.username.toLowerCase === user.toLowerCase)
        .join(Authrolepermission).on(_.roleid === _.roleid)
        .join(Authpermission).on(_._2.permissionid === _.id)
        .result
    ).map { resultS => resultS.map(_._2) }
  }

  def all = db.run(Authpermission.sortBy( _.permission).result)
  def search(term:String) = db.run(Authpermission.filter(_.permission startsWith term).sortBy( _.permission).result)

  def insert(pt: AuthpermissionRow): Future[AuthpermissionRow] = db
    .run(Authpermission returning Authpermission.map(_.id) += pt)
    .map(id => pt.copy(id = id))

  def update(id: Long, pt:AuthpermissionRow) = {
    db.run(Authpermission.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Authpermission.filter(_.id === id).delete) map { _ > 0 }
}
