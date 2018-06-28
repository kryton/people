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

class MatrixTeamMemberRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[MatrixteammemberRow]] = db.run(Matrixteammember.filter(_.id === id).result.headOption)


  def findMatrixTeamByLogin(login:String):Future[Seq[MatrixteamRow]]= {
    val qry = (Matrixteam join Matrixteammember on ( _.id === _.matrixteammemberid))
      .filter( _._2.login === login.toLowerCase)
      .map( f => f._1 )
    db.run( qry.result )
  }

  def isPE(login:String):Future[Boolean] =
    db.run( Matrixteam.filter(_.ispe)
                .join(Matrixteammember).on(_.id === _.matrixteammemberid)
                .filter(_._2.login.toLowerCase === login.toLowerCase).result
    ).map{ x => x.nonEmpty }

  def getPETeamMembers:Future[Seq[(MatrixteamRow,MatrixteammemberRow)]] =
    db.run( Matrixteam.filter(_.ispe)
        .join(Matrixteammember).on(_.id === _.matrixteammemberid).result)


 def findLoginByMatrixTeam(teamId:Long):Future[Seq[EmprelationsRow]]= {
    val qry = (Emprelations join Matrixteammember on ( _.login.toLowerCase === _.login.toLowerCase))
      .filter( _._2.matrixteammemberid === teamId)
      .map( f => f._1 )
      .sortBy(_.lastname)
    db.run( qry.result )
  }

  def findByLogin(login:String):Future[Seq[MatrixteammemberRow]]=
    db.run(Matrixteammember.filter(_.login === login.toLowerCase).result)

  def enablePref(login:String,id:Long): Future[Int] = {
    disablePref(login, id).map { x =>
      db.run(Matrixteammember += MatrixteammemberRow(id=0,matrixteammemberid = id, login= login))
    }.flatMap(identity)
  }

  def disablePref(login:String,id:Long): Future[Int] =
    db.run( Matrixteammember.filter(_.login === login).filter(_.matrixteammemberid === id).delete)

  def insert(mtm: MatrixteammemberRow): Future[MatrixteammemberRow] =
    db.run(Matrixteammember returning Matrixteammember.map(_.id) += mtm )
    .map(id => mtm.copy(id = id ))

  def update(id: Long, mtm:MatrixteammemberRow) = {
    db.run(Matrixteammember.filter(_.id === id).update( mtm.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Matrixteammember.filter(_.id === id).delete) map { _ > 0 }

  def deleteByTeam( teamid: Long) =
    db.run( Matrixteammember.filter(_.matrixteammemberid === teamid).delete) map { _ > 0 }

  def bulkInsertUpdate(rows:Iterable[MatrixteammemberRow]): Future[Iterable[MatrixteammemberRow]] = {
    Future.sequence(rows.map{ row =>
      find(row.id).map{
        case None => insert(row)
        case Some(pc) => Future.successful(pc)
      }.flatMap(identity)
    })
  }
}
