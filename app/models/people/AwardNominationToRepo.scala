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
import offline.Tables.Awardnominationto
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class AwardNominationToRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._
   val REJECTED = 0
   val ACCEPTED = 1
   val UNACTIONED = 2


  def find(id:Long, isAdmin:Boolean = false):Future[Option[AwardnominationtoRow]] = db.run(
    Awardnominationto.filter( p => p.rejected===false || p.rejected === isAdmin )
      .filter(_.id === id).result.headOption
  )
  def findFrom(login:String, isAdmin:Boolean, isHR:Boolean, isAwardComittee:Boolean):Future[Seq[AwardnominationtoRow]] = {
    val isHRp = isAdmin || isHR
    val isAwardComitteep = isAdmin || isHR || isAwardComittee

    db.run(Awardnominationto.filter(_.fromperson === login.toLowerCase)
      .filter(p => p.rejected === false || p.rejected === isAdmin)
      .filter(p => p.hrapproved === ACCEPTED || isHRp == true)
      .filter(p=> p.awarded === ACCEPTED || isAwardComitteep == true).result)
  }

  def findTo(login:String, isAdmin:Boolean, isHR:Boolean, isAwardComittee:Boolean):Future[Seq[AwardnominationtoRow]] = {
    val isHRp = isAdmin || isHR
    val isAwardComitteep = isAdmin || isHR || isAwardComittee

    db.run(Awardnominationto.filter(_.toperson === login.toLowerCase)
      .filter(p => p.rejected === false || p.rejected === isAdmin)
      .filter(p => p.hrapproved === ACCEPTED || isHRp == true)
      .filter(p=> p.awarded === ACCEPTED || isAwardComitteep == true).result)
  }

  def all(isAdmin:Boolean, isHR:Boolean, isAwardComittee:Boolean) = {
    val isHRp = isAdmin || isHR
    val isAwardComitteep = isAdmin || isHR || isAwardComittee

    db.run(Awardnominationto
      .filter(p => p.rejected === false || p.rejected === isAdmin)
      .filter(p => p.hrapproved === ACCEPTED || isHRp == true)
      .filter(p=> p.awarded === ACCEPTED || isAwardComitteep == true)
      .sortBy(_.dateadded desc).result)
  }

  def awaitingHRApproval(): Future[Seq[(AwardnominationtoRow, EmprelationsRow, EmprelationsRow)]] = {
    db.run(Awardnominationto
      .filter(_.rejected === false)
      .filter(_.hrapproved === UNACTIONED)
      .join(Emprelations).on(_.fromperson === _.login)
      .join(Emprelations).on(_._1.toperson === _.login)
      .result).map { rows =>
      rows.map { line =>
        (line._1._1, line._1._2, line._2)
      }
    }
  }

  def hrRejected(): Future[Seq[(AwardnominationtoRow, EmprelationsRow, EmprelationsRow)]] = {
    db.run(Awardnominationto
      .filter(_.rejected === false)
      .filter(_.hrapproved === REJECTED)
      .join(Emprelations).on(_.fromperson === _.login)
      .join(Emprelations).on(_._1.toperson === _.login)
      .result).map { rows =>
      rows.map { line =>
        (line._1._1, line._1._2, line._2)
      }
    }
  }

  def awaitingAwardApproval(): Future[Seq[(AwardnominationtoRow, EmprelationsRow, EmprelationsRow)]] = {
    db.run(Awardnominationto
      .filter(_.rejected === false)
      .filter(_.hrapproved === ACCEPTED)
      .filter(_.awarded === UNACTIONED)
      .join(Emprelations).on(_.fromperson === _.login)
      .join(Emprelations).on(_._1.toperson === _.login)
      .result).map { rows =>
      rows.map { line =>
        (line._1._1, line._1._2, line._2)
      }
    }
  }
  def awardRejected(): Future[Seq[(AwardnominationtoRow, EmprelationsRow, EmprelationsRow)]] = {
    db.run(Awardnominationto
      .filter(_.rejected === false)
      .filter(_.hrapproved === ACCEPTED)
      .filter(_.awarded === REJECTED)
      .join(Emprelations).on(_.fromperson === _.login)
      .join(Emprelations).on(_._1.toperson === _.login)
      .result).map { rows =>
      rows.map { line =>
        (line._1._1, line._1._2, line._2)
      }
    }
  }


  def latest( size:Int,isAdmin:Boolean, isHR:Boolean, isAwardComittee:Boolean): Future[Seq[(AwardnominationtoRow, EmprelationsRow, Option[EmprelationsRow])]] = {
    val isHRp = isAdmin || isHR
    val isAwardComitteep = isAdmin || isHR || isAwardComittee

    val qry = Awardnominationto
      .filter(p => p.rejected === false || p.rejected === isAdmin)
      .filter(p => p.hrapproved === ACCEPTED || isHRp == true)
      .filter(p=> p.awarded === ACCEPTED ||isAwardComitteep == true)
      .join(Emprelations).on( _.toperson === _.login)
      .joinLeft(Emprelations).on(_._1.fromperson === _.login).sortBy(_._1._1.dateadded desc)
    db.run( qry.result ).map( x=> x.slice(0, size)).map( seq => seq.map( x=> ( x._1._1,x._1._2,x._2)))
  }

  def insert(kudos: AwardnominationtoRow) = db
    .run(Awardnominationto returning Awardnominationto.map(_.id) += kudos)
    .map(id => kudos.copy(id = id))

  def update(id:Long, kudos:AwardnominationtoRow) = {
    db.run(Awardnominationto.filter(_.id === id ).update( kudos.copy(id = id))) map { _ > 0 }
  }

  def delete(id:Long) =
    db.run(Awardnominationto.filter(_.id === id).delete) map { _ > 0 }
}
