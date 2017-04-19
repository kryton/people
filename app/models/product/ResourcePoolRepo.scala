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
import projectdb.Tables
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ResourcePoolRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Int):Future[Option[ResourcepoolRow]] = db.run(Resourcepool.filter(_.id === id).result.headOption)
  def all = db.run(Resourcepool.sortBy( _.ordering).result)
  def search(term:String) = db.run(Resourcepool.filter(_.name startsWith term).sortBy( _.name).result)

  def findTeams(id:Int): Future[Seq[Tables.ResourceteamRow]] = db.run( Resourceteam.filter(_.resourcepoolid === id).result)

  def insert(pt: ResourcepoolRow) = db
    .run(Resourcepool returning Resourcepool.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Int, pt:ResourcepoolRow) = {
    db.run(Resourcepool.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Resourcepool.filter(_.id === id).delete) map { _ > 0 }
}
