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

class ProfitCenterRepo @Inject()(@NamedDatabase("default") protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(profitcenter:Long):Future[Option[ProfitcenterRow]] = db.run(Profitcenter.filter(_.profitcenter === profitcenter).result.headOption)
  def find(profitcenters:Set[Long]):Future[Seq[ProfitcenterRow]] =
    db.run(Profitcenter.filter(_.profitcenter inSet profitcenters).result)

  def findOrCreate( profitcenter:Long, text:String):Future[ProfitcenterRow] = {
    find(profitcenter).map{
      case Some( cc) => Future.successful(cc)
      case None => insert( ProfitcenterRow(profitcenter = profitcenter, shortname = Some(text)))
    }.flatMap(identity)
  }
  def all = db.run(Costcenter.sortBy( _.costcenter).result)
  def search(term:String) = db.run(Profitcenter.filter(_.shortname startsWith term).sortBy( _.profitcenter).result)

  def insert(cc: ProfitcenterRow): Future[Tables.ProfitcenterRow] =
    db.run( Profitcenter.insertOrUpdate(cc) ).map ( x => cc )

  def update(profitcenter:Long, er:ProfitcenterRow): Future[Boolean] = {
    db.run(Profitcenter.filter(_.profitcenter === profitcenter).update( er.copy(profitcenter = profitcenter))) map { _ > 0 }
  }

  def delete(profitcenter:Long): Future[Boolean] =
    db.run(Profitcenter.filter(_.profitcenter === profitcenter).delete) map { _ > 0 }

  def findCostCenters(profitcenter: Long): Future[Seq[Tables.CostcenterRow]] =
    db.run(Costcenter.filter(_.profitcenterid === profitcenter).sortBy(_.costcenter).result)

  def bulkInsertUpdate(rows:Iterable[ProfitcenterRow]): Future[Iterable[ProfitcenterRow]] = {
    Future.sequence(rows.map{ row =>
      find( row.profitcenter).map{
        case None => insert(row)
        case Some(pc) => update(row.profitcenter, row).map{ ignore => row }
      }.flatMap(identity)
    })
  }
  def cleanup: Future[Int] = db.run(
    Profitcenter.joinLeft(Costcenter).on( _.profitcenter === _.profitcenterid).filter(_._2.isEmpty).map(_._1).result
  ).map { x =>
    val ids =  x.map( pc => pc.profitcenter)
    db.run(Profitcenter.filter( _.profitcenter inSet ids ).delete )
  }.flatMap(identity )
}
