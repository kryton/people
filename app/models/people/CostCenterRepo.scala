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
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class CostCenterRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(costcenter:Long):Future[Option[CostcenterRow]] = db.run(Costcenter.filter(_.costcenter === costcenter).result.headOption)

  def findOrCreate( costCenter:Long, text:String):Future[CostcenterRow] = {
    find(costCenter).map{
      case Some( cc) => Future.successful(cc)
      case None => insert( CostcenterRow(costcenter = costCenter, costcentertext = Some(text)))
    }.flatMap(identity)
  }

  def insert(cc: CostcenterRow): Future[Tables.CostcenterRow] =
    db.run( Costcenter.insertOrUpdate(cc) ).map ( x => cc )
    /*
    db.run(Costcenter returning Costcenter.map(_.costcenter) += cc)
    .map(costcenter => cc)
*/

  def update(costcenter:Long, er:CostcenterRow) = {
    db.run(Costcenter.filter(_.costcenter === costcenter).update( er.copy(costcenter = costcenter))) map { _ > 0 }
  }

  def delete(costcenter:Long) =
    db.run(Costcenter.filter(_.costcenter === costcenter).delete) map { _ > 0 }
}
