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
import offline.Tables.Positiontype
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}
//import slick.lifted.Tag
//import slick.model.Table


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class PositionTypeRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[PositiontypeRow]] = db.run(Positiontype.filter(_.id === id).result.headOption)
  def find(position:String):Future[Option[PositiontypeRow]] = db.run(Positiontype.filter(_.position.toLowerCase === position.toLowerCase).result.headOption)

  def findOrCreate( position:String, positionType:Option[String]):Future[PositiontypeRow] = {
    val qry = Positiontype.filter(_.position.toLowerCase === position.toLowerCase)

    db.run(qry.result.headOption).map{
      case Some( pt) => Future.successful(pt)
      case None => insert( PositiontypeRow(id =0,position, positionType.getOrElse("UNKNOWN")))
    }.flatMap(identity)
  }

  def cleanup = {
   db.run {
     Positiontype.joinLeft(Emprelations).on( _.position === _.position).filter( _._2.isEmpty) .result
   }.map{ result =>
     val toClean = result.filter(_._1.positiontype.contentEquals("UNKNOWN"))
    // Logger.info(s"Position Type Cleanup: Potential = ${result.size} Clearing ${toClean.size}")
     toClean.map{ x => delete( x._1.id) }
   }

  }
  def insert(pt: PositiontypeRow): Future[Tables.PositiontypeRow] =
    db.run(Positiontype returning Positiontype.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Long, pt:PositiontypeRow) = {
    db.run(Positiontype.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Positiontype.filter(_.id === id).delete) map { _ > 0 }
}
