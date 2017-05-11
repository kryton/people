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

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */
/*

class ResourcePoolTeamRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._


  def findEx(id: Long) = db.run(
    (for {
      resourcePoolTeam <- Resourcepoolteam if resourcePoolTeam.id === id
      resourcePool <- Resourcepool if resourcePool.id === resourcePoolTeam.poolid
    } yield (resourcePoolTeam, resourcePool)).result.headOption)

  def findByPool( poolId: Long): Future[Seq[ResourcepoolteamRow]] = db.run( Resourcepoolteam.filter(_.poolid === poolId).result)

  def find(id:Long ):Future[Option[ResourcepoolteamRow]] = db.run(Resourcepoolteam.filter(_.id === id).result.headOption)
  def search(searchString:String): Future[Seq[ResourcepoolteamRow]] = db.run{
    Resourcepoolteam.filter { m =>  m.teamdescription startsWith searchString
    }.sortBy(_.teamdescription).result
  }

  def insert(resourcePoolTeam: ResourcepoolteamRow) = db
    .run(Resourcepoolteam returning Resourcepoolteam.map(_.id) += resourcePoolTeam)
    .map(id => resourcePoolTeam.copy(id = id))


  def update(id: Long, rpt:ResourcepoolteamRow) = {
    db.run(Resourcepoolteam.filter(_.id === id).update( rpt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Resourcepoolteam.filter(_.id === id).delete) map { _ > 0 }
}
*/
