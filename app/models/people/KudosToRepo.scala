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

import javax.inject.Inject

import offline.Tables
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class KudosToRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long, isAdmin:Boolean = false):Future[Option[KudostoRow]] = db.run(
    Kudosto.filter( p => p.rejected===false || p.rejected === isAdmin )
      .filter(_.id === id).result.headOption
  )
  def findFrom(login:String):Future[Seq[KudostoRow]] = db.run(Kudosto.filter(_.fromperson === login.toLowerCase).filter(_.rejected === false).result)
  def findTo(login:String):Future[Seq[KudostoRow]] = db.run(Kudosto.filter(_.toperson === login.toLowerCase).filter(_.rejected === false).result)

  def all = db.run(Kudosto.filter(_.rejected === false).sortBy( _.dateadded desc).result)
  def latest( size:Int): Future[Seq[(KudostoRow, EmprelationsRow, Option[EmprelationsRow])]] = {
    val qry = Kudosto.filter(_.rejected === false)
      .join(Emprelations).on( _.toperson === _.login)
      .joinLeft(Emprelations).on(_._1.fromperson === _.login).sortBy(_._1._1.dateadded desc)
    db.run( qry.result ).map( x=> x.slice(0, size)).map( seq => seq.map( x=> ( x._1._1,x._1._2,x._2)))
  }

  def insert(kudos: KudostoRow) = db
    .run(Kudosto returning Kudosto.map(_.id) += kudos)
    .map(id => kudos.copy(id = id))

  def update(id:Long, kudos:KudostoRow) = {
    db.run(Kudosto.filter(_.id === id ).update( kudos.copy(id = id))) map { _ > 0 }
  }

  def delete(id:Long) =
    db.run(Kudosto.filter(_.id === id).delete) map { _ > 0 }
}
