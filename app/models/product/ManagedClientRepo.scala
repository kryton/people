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

package models.product

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import offline.Tables
import offline.Tables.Managedclient
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ManagedClientRepo @Inject()( /*@NamedDatabase("offline") */ protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Int):Future[Option[ManagedclientRow]] = db.run(Managedclient.filter(_.id === id).result.headOption)
  def find(name:String):Future[Option[ManagedclientRow]] = db.run(Managedclient.filter(_.name.toLowerCase === name.toLowerCase).result.headOption)
  def findMSProject(name:Option[String]):Future[Option[ManagedclientRow]] = {
    name match {
      case Some(n) => db.run(Managedclient.filter(_.msprojectname.toLowerCase === n.toLowerCase).result.headOption)
      case None => Future.successful(None)
    }
  }

  def all = db.run(Managedclient.sortBy( _.name).result)
  def search(term:String) = db.run(Managedclient.filter(_.name startsWith term).sortBy( _.name).result)

  def insert(mc: ManagedclientRow) = db
    .run(Managedclient returning Managedclient.map(_.id) += mc)
    .map(id => mc.copy(id = id))

  def findFeatures( manageClientId: Int): Future[Seq[(Tables.ProductfeatureRow, Tables.ManagedclientproductfeatureRow)]] = {
    db.run(Productfeature.join(Managedclientproductfeature).on(_.id === _.productfeatureid)
      .filter(_._2.managedclientid === manageClientId)
      .sortBy(_._1.name).result)
  }

  def update(id: Int, mc:ManagedclientRow) = {
    db.run(Managedclient.filter(_.id === id).update( mc.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Managedclient.filter(_.id === id).delete) map { _ > 0 }
}
