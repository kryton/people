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
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class EmpEfficiencyRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Long):Future[Option[EmpefficiencyRow]] = db.run(Empefficiency.filter(_.id === id).result.headOption)
  def all = db.run(Empefficiency.sortBy( _.month).result)

  def insert(ee: EmpefficiencyRow) = db
    .run(Empefficiency returning Empefficiency.map(_.id) += ee)
    .map(id => ee.copy(id = id))


  def update(id: Long, ee:EmpefficiencyRow) = {
    db.run(Empefficiency.filter(_.id === id).update( ee.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Empefficiency.filter(_.id === id).delete) map { _ > 0 }
}
