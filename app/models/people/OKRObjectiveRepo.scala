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

import java.sql.Date
import javax.inject.Inject

import offline.Tables
import com.github.nscala_time.time.Imports._
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}
import util.Conversions

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class OKRObjectiveRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(login:String, id:Long):Future[Option[OkrobjectiveRow]] = db.run(Okrobjective.filter(_.id === id).filter(_.login.toLowerCase === login.toLowerCase).result.headOption)

  def find( login:String, quarterDate:java.sql.Date): Future[Seq[Tables.OkrobjectiveRow]] = db.run{
    Okrobjective.filter(_.login.toLowerCase === login.toLowerCase).filter(_.quarterdate === quarterDate).result
  }
  def findEx( login:String, quarterDate:java.sql.Date): Future[Seq[(Tables.OkrobjectiveRow, Seq[Tables.OkrkeyresultRow])]] = {
    val res: Future[Seq[(Tables.OkrobjectiveRow, Option[Tables.OkrkeyresultRow])]] = db.run{
      Okrobjective
        .filter(_.login.toLowerCase === login.toLowerCase)
        .filter(_.quarterdate === quarterDate)
        .joinLeft( Okrkeyresult).on( _.id === _.objectiveid)
        .result
    }
    res.map{ x:Seq[(Tables.OkrobjectiveRow, Option[Tables.OkrkeyresultRow])] =>
        x.groupBy( _._1.id).map{ z =>
          ( z._2.head._1, z._2.flatMap( y => y._2 ))
        }.toSeq
      }
  }


   def findEx(login:String, id:Long): Future[Option[(Tables.OkrobjectiveRow, Seq[Tables.OkrkeyresultRow])]] = {
    val res: Future[Seq[(Tables.OkrobjectiveRow, Option[Tables.OkrkeyresultRow])]] = db.run{
      Okrobjective
        .filter(_.id === id )
        .filter(_.login.toLowerCase === login.toLowerCase)
        .joinLeft( Okrkeyresult).on( _.id === _.objectiveid)
        .result
    }
    res.map{ x:Seq[(Tables.OkrobjectiveRow, Option[Tables.OkrkeyresultRow])] =>
        x.groupBy( _._1.id).map{ z =>
          val krs = z._2.flatMap(_._2)
          ( z._2.head._1, krs )
        }.headOption
      }
  }

   def findKR(login:String, okr:Long, kr:Long): Future[Option[(Tables.OkrobjectiveRow, Tables.OkrkeyresultRow)]] = {
    db.run{
      Okrobjective
        .filter(_.id === okr )
        .filter(_.login.toLowerCase === login.toLowerCase)
        .join( Okrkeyresult).on( _.id === _.objectiveid)
        .filter(_._2.id === kr )
        .result.headOption
    }
  }


  def find( logins:Set[String], quarterDate:java.sql.Date): Future[Seq[Tables.OkrobjectiveRow]] = db.run{
    Okrobjective.filter(_.login.toLowerCase inSet logins.map( x =>x.toLowerCase)).filter(_.quarterdate === quarterDate).result
  }

  def search(searchString:String): Future[Seq[OkrobjectiveRow]] = db.run{
    Okrobjective.filter { m =>
      m.objective like s"%$searchString%"
    }.sortBy(_.quarterdate).result
  }

  def all: Future[Seq[OkrobjectiveRow]] = db.run{
    Okrobjective.sortBy(_.quarterdate desc ).result
  }

  def latest( size:Int): Future[Seq[(OkrobjectiveRow, EmprelationsRow)]] = {
    val qry = Okrobjective
      .join(Emprelations).on( _.login === _.login)
      .sortBy( p=> (p._1.quarterdate desc, p._1.dateadded desc ))
    db.run( qry.result ).map( x=> x.slice(0, size)).map( seq => seq.map( x=> ( x._1,x._2)))
  }


  def getQuartersAsSelect(numBehind: Int = 2, numAhead: Int = 4): Future[Seq[(Date, String)]] = {

    // TODO numBehind - use it
    db.run( Okrobjective.map(_.quarterdate).distinct.result ).map { dates =>
     val beforeSet: Set[Date] =  dates.flatMap{ d =>
        Conversions.quarterD(Some( Conversions.quarterS( d)) )
      }.toSet
      val jt: DateTime = DateTime.now

      val after: Set[Date] = (0 to numAhead).toList.map(x => x * 3)
        .map(x => jt + x.months)
        .flatMap ( x =>  Conversions.quarterD(Some(Conversions.quarterS(Some(new Date(x.getMillis))) ))).toSet

      (beforeSet ++ after).toSeq.sortBy(_.getTime).map { x: Date =>
        ( x, Conversions.quarterS(Some(x)))
      }
    }
  }


  def insert(empTag: OkrobjectiveRow): Future[Tables.OkrobjectiveRow] =
    db.run(Okrobjective returning Okrobjective.map(_.id) += empTag )
    .map(id => empTag.copy(id = id ))

def insertKR(kr: OkrkeyresultRow): Future[Tables.OkrkeyresultRow] =
    db.run(Okrkeyresult returning Okrkeyresult.map(_.id) += kr )
    .map(id => kr.copy(id = id ))

  def update(id: Long, empTag:OkrobjectiveRow): Future[Boolean] = {
    db.run(Okrobjective.filter(_.id === id).update( empTag.copy(id = id))) map { _ > 0 }
  }

  def updateKR(id: Long, kr:OkrkeyresultRow): Future[Boolean] = {
    db.run(Okrkeyresult.filter(_.id === id).update( kr.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Okrobjective.filter(_.id === id).delete) map { _ > 0 }

  def deleteKR(id: Long) =
    db.run(Okrkeyresult.filter(_.id === id).delete) map { _ > 0 }

}
