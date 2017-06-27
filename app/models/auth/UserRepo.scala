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
import offline.Tables.{Authrole, AuthroleRow, Authuser, AuthuserRow, Authuserpreference, Emprelations}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class UserRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import projectdb.Tables.profile.api._

  def find(id:Long):Future[Option[AuthuserRow]] = db.run(Authuser.filter(_.id === id).result.headOption)

  def find(name:String):Future[Seq[(AuthuserRow, AuthroleRow)]] =
    db.run(Authuser.filter(_.username === name )
      .join(Authrole).on(_.roleid === _.id)  .result)

  def findByRole(roleId:Long): Future[Seq[(Tables.AuthuserRow, Tables.EmprelationsRow)]] =
    db.run(Authuser.filter(_.roleid === roleId)
    .join(Emprelations).on(_.username === _.login).result)

  def find(username:String, roleId:Long):Future[Option[AuthuserRow]] =
    db.run(Authuser.filter(_.roleid === roleId ).filter(_.username === username ).result.headOption)

  def findOrCreate(username:String, roleId:Long):Future[AuthuserRow] = {
    find(username, roleId).map {
      case Some(stage) => Future.successful(stage)
      case None => insert(AuthuserRow(id = 0, roleid = roleId, username= username.toLowerCase))
    }
  }.flatMap(identity)

  def all = db.run(Authuser.sortBy( _.username).result)
  def search(term:String) = db.run(Authuser.filter(_.username startsWith term).sortBy( _.username).result)

  def insert(pt: AuthuserRow): Future[AuthuserRow] = db
    .run(Authuser returning Authuser.map(_.id) += pt)
    .map(id => pt.copy(id = id))

  def update(id: Long, pt:AuthuserRow) = {
    db.run(Authuser.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Authuser.filter(_.id === id).delete) map { _ > 0 }


  def enableRole(login:String,id:Long): Future[Int] = {
    disableRole(login, id).map { x =>
      db.run(Authuser += AuthuserRow(id=0,roleid = id, username = login))
    }.flatMap(identity)
  }

  def disableRole(login:String,id:Long): Future[Int] =
    db.run( Authuser.filter(_.username === login).filter(_.roleid === id).delete)

}
