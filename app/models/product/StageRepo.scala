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
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class StageRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Int):Future[Option[StageRow]] = db.run(Stage.filter(_.id === id).result.headOption)
  def find(name:String):Future[Option[StageRow]] = db.run(Stage.filter(_.name === name ).result.headOption)
  def findOrCreate(name:String):Future[StageRow] = {
    find(name).map {
      case Some(stage) => Future.successful(stage)
      case None => insert(StageRow(id = 0, name = name))
    }
  }.flatMap(identity)

  def all = db.run(Stage.sortBy( _.ordering).result)
  def search(term:String) = db.run(Stage.filter(_.name startsWith term).sortBy( _.name).result)

  def insert(pt: StageRow): Future[Tables.StageRow] = db
    .run(Stage returning Stage.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Int, pt:StageRow) = {
    db.run(Stage.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Stage.filter(_.id === id).delete) map { _ > 0 }
}
