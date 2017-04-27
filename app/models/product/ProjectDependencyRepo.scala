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

import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import util.importFile.ProjectImport

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ProjectDependencyRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Long ):Future[Option[ProjectdependencyRow]] = db.run(Projectdependency.filter(_.id === id).result.headOption)
  def findFrom(projectId:Int):Future[Seq[ProjectdependencyRow]] =
    db.run(Projectdependency.filter(_.fromproject === projectId).result)
  def findTo(projectId:Int):Future[Seq[ProjectdependencyRow]] =
    db.run(Projectdependency.filter(_.toproject === projectId).result)


  def insert(pt: ProjectdependencyRow) = db
    .run(Projectdependency returning Projectdependency.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Long, pt:ProjectdependencyRow) = {
    db.run(Projectdependency.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Projectdependency.filter(_.id === id).delete) map { _ > 0 }
  def deleteFrom(id: Int) =
    db.run(Projectdependency.filter(_.fromproject === id).delete) map { _ > 0 }
  def deleteTo(id: Int) =
    db.run(Projectdependency.filter(_.toproject === id).delete) map { _ > 0 }


}
