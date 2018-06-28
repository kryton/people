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
//import slick.lifted.Tag
//import slick.model.Table


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class MatrixTeamRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[MatrixteamRow]] = db.run(Matrixteam.filter(_.id === id).result.headOption)
  def find(name:String):Future[Option[MatrixteamRow]] = db.run(Matrixteam.filter(_.name === name).result.headOption)

  def search(searchString:String): Future[Seq[MatrixteamRow]] = db.run{
    Matrixteam.filter { m =>
      m.name startsWith searchString
    }.sortBy(_.name).result
  }


  def all: Future[Seq[MatrixteamRow]] = db.run{
    Matrixteam.sortBy(_.name.toLowerCase).result
  }

  def insert(empTag: MatrixteamRow): Future[Tables.MatrixteamRow] =
    db.run(Matrixteam returning Matrixteam.map(_.id) += empTag )
    .map(id => empTag.copy(id = id ))


  def update(id: Long, empTag:MatrixteamRow) = {
    db.run(Matrixteam.filter(_.id === id).update( empTag.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Matrixteam.filter(_.id === id).delete) map { _ > 0 }

  def bulkInsertUpdate(rows:Iterable[String])(implicit matrixTeamMemberRepo: MatrixTeamMemberRepo): Future[Iterable[MatrixteamRow]] = {
    Future.sequence(rows.map{ row =>
      find(row).map{
        case None => insert(MatrixteamRow(id = 0, name = row, owner = None))
        case Some(pc) => matrixTeamMemberRepo.deleteByTeam(pc.id).map{ _ => pc }
      }.flatMap(identity)
    })
  }

}
