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

import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import offline.Tables
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}
//import slick.lifted.Tag
//import slick.model.Table


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class EmpHistoryRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(login:String):Future[Option[EmphistoryRow]] = db.run(Emphistory.filter(_.login === login.toLowerCase).result.headOption)

  def findOrCreate(login:String, empHist:EmphistoryRow):Future[EmphistoryRow] = {

    find(login).map{
      case Some( ehist) => Future.successful(ehist )
      case None => insert( empHist )
    }.flatMap(identity)
  }

  def insert(empH: EmphistoryRow): Future[Tables.EmphistoryRow] = {
    val newEmpH = empH.copy( login = empH.login.toLowerCase)

    db.run( Emphistory.insertOrUpdate(newEmpH) ).map ( x => newEmpH )
  }

  def latest( size:Int): Future[Seq[(EmphistoryRow, EmprelationsRow)]] = {
    val qry = Emphistory
      .join(Emprelations).on( _.login === _.login)
      .sortBy(_._1.hirerehiredate desc)
    db.run( qry.result ).map( x=> x.slice(0, size))
  }

  def anniversariesThisWeek(weekNum:Int ): Future[Seq[(EmphistoryRow, EmprelationsRow, Long  )]] = {
    val week = SimpleFunction.unary[Option[java.sql.Date],Int]("week")
    val yearsOfService = SimpleFunction.unary[Option[java.sql.Date],Int]("year(now()) - year")
    val now = System.currentTimeMillis()

    db.run( Emphistory
      .filter(x => week( x.hirerehiredate) === weekNum )
      .filterNot( x=> yearsOfService( x.hirerehiredate) === 0 )
      .join(Emprelations).on( _.login === _.login)
      .result
    ).map { results: Seq[(EmphistoryRow, EmprelationsRow)] =>
      results.map { result =>
        val hire = result._1.hirerehiredate match {
          case Some(d) => d.getTime
          case None => now
        }
        val y =  TimeUnit.MILLISECONDS.toDays(now - hire)
        val years = Math.round( y/ 365.0 )
        ( result._1, result._2, years)
      }
    }
  }

  def update(login: String, eh:EmphistoryRow) = {
    db.run(Emphistory.filter(_.login === login.toLowerCase).update( eh.copy(login = login.toLowerCase))) map { _ > 0 }
  }

  def delete(login: String) =
    db.run(Emphistory.filter(_.login === login.toLowerCase).delete) map { _ > 0 }
}
