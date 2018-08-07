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
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class UserPrefRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def allPrefs: Future[Seq[AuthpreferenceRow]] = db.run(Authpreference.sortBy(_.name).result)
  def userPrefs(login:String): Future[Seq[(AuthuserpreferenceRow,AuthpreferenceRow)]] = db.run{
    Authuserpreference.filter(_.login.toLowerCase === login)
      .join(Authpreference).on( _.authpreferenceid === _.id).result
  }
  def findPref(id:Long): Future[Option[AuthpreferenceRow]] =
    db.run( Authpreference.filter(_.id === id).result.headOption)

  def enablePref(login:String,id:Long): Future[Int] = {
    disablePref(login, id).map { x =>
      db.run(Authuserpreference += AuthuserpreferenceRow(id, login))
    }.flatMap(identity)
  }

  def disablePref(login:String,id:Long): Future[Int] =
    db.run( Authuserpreference.filter(_.login === login).filter(_.authpreferenceid === id).delete)

  def deletePrefsForUser(login:String): Future[Int] = db.run{
    Authuserpreference.filter(_.login.toLowerCase === login).delete
  }
  def insertPrefForUser(rec: AuthuserpreferenceRow): Future[Int] =
    db.run(Authuserpreference  += rec)


}
