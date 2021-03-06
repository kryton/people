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
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ReleaseAuthorizationRepo @Inject()( /*@NamedDatabase("offline") */
                                          protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Int):Future[Option[ReleaseauthorizationRow]] = db.run(Releaseauthorization.filter(_.id === id).result.headOption)
  def findByLogin(login:String):Future[Seq[ReleaseauthorizationRow]] = db.run(Releaseauthorization.filter(_.login === login ).result)
  def findByRelease(release:Int):Future[Seq[(ReleaseauthorizationRow,ReleaseauthorizationtypeRow)]] =
    db.run(Releaseauthorization.filter(_.releaseid === release )
        .join(Releaseauthorizationtype).on(_.releaseauthorityid === _.id )
      .result)
  def findByReleaseAuthorityType(auth:Int):Future[Seq[(ReleaseauthorizationRow, ProjectreleaseRow) ]] =
    db.run(Releaseauthorization.filter(_.releaseauthorityid === auth )
              .join(Projectrelease).on( _.releaseid === _.id )
      .result)
  def find(release:Int, login:String):Future[Option[ReleaseauthorizationRow]] = db.run(Releaseauthorization.filter(_.login === login ).filter(_.releaseid === release).result.headOption)
  def findOrCreate(release:Int, login:String, authority:Int):Future[ReleaseauthorizationRow] = {
    find(release,login).map {
      case Some(releaseRecord) => Future.successful(releaseRecord)
      case None => insert(ReleaseauthorizationRow(id = 0, releaseid= release, login = login, releaseauthorityid = authority))
    }
  }.flatMap(identity)

  def all = db.run(Releaseauthorization.sortBy( _.login).result)

  def insert(pt: ReleaseauthorizationRow): Future[ReleaseauthorizationRow] = db
    .run(Releaseauthorization returning Releaseauthorization.map(_.id) += pt)
    .map(id => pt.copy(id = id))

  def update(id: Int, pt:ReleaseauthorizationRow) = {
    db.run(Releaseauthorization.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Releaseauthorization.filter(_.id === id).delete) map { _ > 0 }
}
