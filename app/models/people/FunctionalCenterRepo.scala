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

class FunctionalCenterRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(functionalarea:Long):Future[Option[FunctionalareaRow]] =
    db.run(Functionalarea.filter(_.functionalarea === functionalarea).result.headOption)

  def find(functionalareaS:Set[Long]):Future[Seq[FunctionalareaRow]] =
    db.run(Functionalarea.filter(_.functionalarea inSet functionalareaS).result)

  def findOrCreate( functionalarea:Long, function:FunctionalareaRow):Future[FunctionalareaRow] = {
    find(functionalarea).map{
      case Some( cc) => Future.successful(cc)
      case None => insert( function.copy(functionalarea = functionalarea))
    }.flatMap(identity)
  }
  def all = db.run(Functionalarea.sortBy( _.functionalarea).result)
  def search(term:String) = db.run(Functionalarea.filter(_.department startsWith term).sortBy( _.functionalarea).result)

  def insert(cc: FunctionalareaRow): Future[Tables.FunctionalareaRow] =
    db.run( Functionalarea.insertOrUpdate(cc) ).map ( x => cc )

  def update(functionalarea:Long, er:FunctionalareaRow) = {
    db.run(Functionalarea.filter(_.functionalarea === functionalarea).update( er.copy(functionalarea = functionalarea))) map { _ > 0 }
  }

  def delete(functionalarea:Long) =
    db.run(Functionalarea.filter(_.functionalarea === functionalarea).delete) map { _ > 0 }

  def findCostCenters(functionalcenter: Long) =
    db.run(Costcenter.filter(_.functionalareaid === functionalcenter).sortBy(_.costcenter).result )

  def bulkInsertUpdate(rows:Iterable[FunctionalareaRow]): Future[Iterable[FunctionalareaRow]] = {
    Future.sequence(rows.map{ row =>
      find( row.functionalarea).map{
        case None => insert(row)
        case Some(pc) => update(row.functionalarea, row).map{ ignore => row }
      }.flatMap(identity)
    })
  }
  def cleanup: Future[Int] = db.run(
    Functionalarea.joinLeft(Costcenter).on( _.functionalarea === _.functionalareaid).filter(_._2.isEmpty).map(_._1).result
  ).map { x =>
    val ids =  x.map( pc => pc.functionalarea)
    db.run(Functionalarea.filter( _.functionalarea inSet ids ).delete )
  }.flatMap(identity )
}
