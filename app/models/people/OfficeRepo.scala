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

import models.Db
import offline.Tables
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
//import slick.lifted.Tag
//import slick.model.Table


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class OfficeRepo @Inject()( @NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[OfficeRow]] = db.run(Office.filter(_.id === id).result.headOption)

  def findOrCreate( city:Option[String], street:Option[String], POBox:Option[String],
                    region:Option[String], zipcode:Option[String], country:Option[String]):Future[OfficeRow] = {
    val qry = Office.filter(_.city === city || city.isEmpty)
      .filter(_.country === country || country.isEmpty)
      .filter(_.street === street || street.isEmpty)
      .filter(_.pobox === POBox || POBox.isEmpty)
      .filter(_.zipcode === zipcode || zipcode.isEmpty)

    db.run(qry.result.headOption).map{
      case Some( office) => Future.successful(office)
      case None => insert( OfficeRow(id =0, city, street, POBox, region, zipcode, country  ))
    }.flatMap(identity)
  }
  def insert(office: OfficeRow): Future[Tables.OfficeRow] =
    db.run(Office returning Office.map(_.id) += office)
    .map(id => office.copy(id = id))


  def update(id: Long, office:OfficeRow) = {
    db.run(Office.filter(_.id === id).update( office.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Office.filter(_.id === id).delete) map { _ > 0 }
}
