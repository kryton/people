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

package models.people

import java.net.URLDecoder
import javax.inject.Inject

import offline.Tables.{Empbio, TeamdescriptionRow}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class EmpBioRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def findByLogin(login:String):Future[Option[EmpbioRow]] = db.run(Empbio.filter(_.login === login.toLowerCase).result.headOption)

  def insert(er: EmpbioRow) ={
    val erRec = er.copy( login = er.login.toLowerCase )
    db.run(Empbio returning Empbio.map(_.login) += erRec)
    .map(login => erRec )
  }

  def update(login:String, er:EmpbioRow) = {
    db.run(Empbio.filter(_.login === login.toLowerCase).update( er.copy(login = login.toLowerCase))) map { _ > 0 }
  }

  def delete(login:String) =
    db.run(Empbio.filter(_.login === login.toLowerCase).delete) map { _ > 0 }
}
