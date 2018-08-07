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

class CostCenterRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(costcenter:Long):Future[Option[CostcenterRow]] = db.run(Costcenter.filter(_.costcenter === costcenter).result.headOption)

  def findOrCreate( costCenter:Long, text:String):Future[CostcenterRow] = {
    find(costCenter).map{
      case Some( cc) => Future.successful(cc)
      case None => insert( CostcenterRow(costcenter = costCenter, costcentertext = Some(text)))
    }.flatMap(identity)
  }


  def findByProfitCenter(profitcenter: Long) =
    db.run(Costcenter.filter(_.profitcenterid === profitcenter).sortBy(_.costcenter).result)
  def findByFunctionalCenter( functionalcenter:Long) =
    db.run(Costcenter.filter(_.functionalareaid === functionalcenter).sortBy(_.costcenter).result )

  def all = db.run(Costcenter.sortBy( _.costcenter).result)
  def search(term:String) = db.run(Costcenter.filter(_.costcentertext startsWith term).sortBy( _.costcenter).result)

  def insert(cc: CostcenterRow): Future[Tables.CostcenterRow] =
    db.run( Costcenter.insertOrUpdate(cc) ).map ( x => cc )

  def update(costcenter:Long, er:CostcenterRow) = {
    db.run(Costcenter.filter(_.costcenter === costcenter).update( er.copy(costcenter = costcenter))) map { _ > 0 }
  }
  private def updateFromImport(costcenter:Long, desc:Option[String],
                               functionalAreaId:Option[Long], profitCenterId:Option[Long],
                              company:Option[String]) = {
    db.run(Costcenter.filter(_.costcenter === costcenter)
      .map( x=> (x.costcenter, x.costcentertext, x.functionalareaid, x.profitcenterid, x.company))
      .update(costcenter, desc, functionalAreaId,profitCenterId, company )) map { _ > 0 }
  }

  def delete(costcenter:Long) =
    db.run(Costcenter.filter(_.costcenter === costcenter).delete) map { _ > 0 }

  def bulkInsertUpdate(rows:Iterable[CostcenterRow]): Future[Iterable[CostcenterRow]] = {
    Future.sequence(rows.map{ row =>
      find( row.costcenter).map{
        case None => insert(row)
        case Some(pc) => updateFromImport(row.costcenter, row.costcentertext, row.functionalareaid, row.profitcenterid, row.company)
          .map{ ignore => row }
      }.flatMap(identity)
    })
  }

  def cleanup: Future[Int] = db.run(
    Costcenter.joinLeft(Emprelations).on( _.costcenter === _.costcenter).filter(_._2.isEmpty).map(_._1).result
  ).map { x =>
    val ids =  x.map( pc => pc.costcenter)
    db.run(Costcenter.filter( _.costcenter inSet ids ).delete )
  }.flatMap(identity )
}
