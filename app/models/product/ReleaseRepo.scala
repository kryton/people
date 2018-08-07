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
import offline.Tables
import offline.Tables.Projectrelease
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ReleaseRepo @Inject()(/*@NamedDatabase("offline") */ protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Int):Future[Option[ProjectreleaseRow]] = db.run(Projectrelease.filter(_.id === id).result.headOption)
  def find(name:String):Future[Option[ProjectreleaseRow]] = db.run(Projectrelease.filter(_.name === name ).result.headOption)
  def findOrCreate(name:String, releaseType:Int):Future[ProjectreleaseRow] = {
    find(name).map {
      case Some(release) => Future.successful(release)
      case None => insert(ProjectreleaseRow(id = 0, name = name, releasetypeid = releaseType))
    }
  }.flatMap(identity)

  def findByReleaseType( releaseTypeId: Int): Future[Seq[ProjectreleaseRow]] = {
    db.run(Projectrelease.filter( _.releasetypeid === releaseTypeId).result )
  }

  def all = db.run(Projectrelease.sortBy( _.name).result)
  def search(term:String) = db.run(Projectrelease.filter(_.name startsWith term).sortBy( _.name).result)

  def insert(pt: ProjectreleaseRow): Future[ProjectreleaseRow] = db
    .run(Projectrelease returning Projectrelease.map(_.id) += pt)
    .map(id => pt.copy(id = id))

  def update(id: Int, pt:ProjectreleaseRow) = {
    db.run(Projectrelease.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Projectrelease.filter(_.id === id).delete) map { _ > 0 }
}
