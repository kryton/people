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

import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class RoleRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[AuthroleRow]] =
    db.run(Authrole.filter(_.id === id).result.headOption)

  def find(name:String):Future[Option[AuthroleRow]] =
    db.run(Authrole.filter(_.role === name ).result.headOption)

  def findByPermission(permissionId:Long):Future[Seq[(AuthrolepermissionRow,AuthroleRow)]] =
    db.run(Authrolepermission.filter(_.permissionid === permissionId )
      .join(Authrole).on(_.roleid === _.id)  .result)

  def findOrCreate(role:String, desc:Option[String] = None):Future[AuthroleRow] = {
    find(role).map {
      case Some(roleRec) => Future.successful(roleRec)
      case None => insert(AuthroleRow(id = 0, role = role, isadmin=false, description = desc))
    }
  }.flatMap(identity)

  def all = db.run(Authrole.sortBy( _.role).result)
  def search(term:String) = db.run(Authrole.filter(_.role startsWith term).sortBy( _.role).result)

  def insert(pt: AuthroleRow): Future[AuthroleRow] = db
    .run(Authrole returning Authrole.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Long, pt:AuthroleRow) = {
    db.run(Authrole.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Authrole.filter(_.id === id).delete) map { _ > 0 }
}
