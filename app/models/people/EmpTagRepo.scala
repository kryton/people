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

class EmpTagRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[EmptagRow]] = db.run(Emptag.filter(_.id === id).result.headOption)
  def findByLogin(login:String):Future[Seq[EmptagRow]] = db.run(Emptag.filter(_.login.toLowerCase === login.toLowerCase).result)
  def findByTag(tag:String):Future[Seq[EmptagRow]] ={
    db.run(Emptag.filter(_.tagtext.toLowerCase === tag.toLowerCase)
      .join(Emprelations).on(_.login.toLowerCase=== _.login.toLowerCase)
      .map(_._1).result)
  }
  def findByTagEx(tag:String):Future[Seq[(EmptagRow,EmprelationsRow)]] ={
    db.run(Emptag.filter(_.tagtext.toLowerCase === tag.toLowerCase)
      .join(Emprelations).on(_.login.toLowerCase=== _.login.toLowerCase)
      .result)
  }

  def search(searchString:String): Future[Seq[EmptagRow]] = db.run{
    Emptag.filter { m =>
      m.tagtext startsWith searchString
    }.sortBy(_.tagtext).result
  }

  def all: Future[Seq[EmptagRow]] = db.run{
    Emptag.join(Emprelations).on(_.login.toLowerCase === _.login.toLowerCase).map(_._1).sortBy(_.tagtext).result
  }

  def insert(empTag: EmptagRow): Future[Tables.EmptagRow] =
    db.run(Emptag returning Emptag.map(_.id) += empTag.copy( login = empTag.login.toLowerCase))
    .map(id => empTag.copy(id = id, login = empTag.login.toLowerCase ))


  def update(id: Long, empTag:EmptagRow) = {
    db.run(Emptag.filter(_.id === id).update( empTag.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Emptag.filter(_.id === id).delete) map { _ > 0 }
}
