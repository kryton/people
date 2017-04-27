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

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import models.people.{MatrixTeamMemberRepo, TeamDescriptionRepo}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import projectdb.Tables
import offline.Tables.EmprelationsRow
import play.api.Logger
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ResourceTeamRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Int):Future[Option[ResourceteamRow]] = db.run(Resourceteam.filter(_.id === id).result.headOption)
  def findEx(id:Int):Future[Option[(ResourceteamRow,Option[ResourcepoolRow])]] = db.run{
    Resourceteam.filter(_.id === id).joinLeft(Resourcepool).on(_.resourcepoolid === _.id) .result.headOption
  }

  def findOrCreate( msProjectName:String):Future[ResourceteamRow] = {
    db.run( Resourceteam.filter(_.msprojectname === msProjectName).result.headOption).map{
      case Some(rt) => Future.successful(rt)
      case None =>
        Logger.info(s"Inserting Resource Team - $msProjectName")
        insert( ResourceteamRow(id=0, msprojectname = msProjectName, name = msProjectName))
    }.flatMap(identity)
  }

  def findFeatures(resourceteamid:Int): Future[Seq[( Tables.ResourceteamproductfeatureRow, Tables.ProductfeatureRow)]] = {
    db.run{
      Resourceteamproductfeature.filter(_.resourceteamid === resourceteamid)
        .join(Productfeature).on(_.productfeatureid === _.id)
        .result
    }
  }
  def findProjects(resourceteamid:Int): Future[Seq[( Tables.ResourceteamprojectRow, Tables.ProjectRow)]] = {
    db.run{
      Resourceteamproject.filter(_.resourceteamid === resourceteamid)
        .join(Project).on(_.projectid === _.id)
        .result
    }
  }

  def findInPool(poolId:Int): Future[Seq[ResourceteamRow]] = db.run( Resourceteam.filter(_.resourcepoolid === poolId).sortBy(_.name).result)

  def all: Future[Seq[Tables.ResourceteamRow]] = db.run(Resourceteam.sortBy( x=>(x.resourcepoolid.getOrElse(0),x.name)).result)
  def allEx: Future[Seq[(Tables.ResourceteamRow, Option[ResourcepoolRow])]] = {
    db.run(Resourceteam.joinLeft(Resourcepool).on(_.resourcepoolid === _.id).sortBy( x=>(x._2.map( y => y.name), x._1.name)).result)
  }

  def search(term:String): Future[Seq[Tables.ResourceteamRow]] = db.run(Resourceteam.filter(_.name startsWith term).sortBy( _.ordering).result)
  def searchEx(term:String) :Future[Seq[(Tables.ResourceteamRow, Option[ResourcepoolRow])]]= {
    db.run(Resourceteam.filter(_.name startsWith term)
      .joinLeft(Resourcepool).on(_.resourcepoolid === _.id).sortBy( x=>(x._2.map( y => y.name), x._1.name)).result)
  }

  def getDevStatsForTeam(id:Int)(implicit teamDescriptionRepo: TeamDescriptionRepo,matrixTeamMemberRepo:MatrixTeamMemberRepo):Future[Seq[(EmprelationsRow, Int, EfficencyMonth)]]  = {
    val empEF= db.run{
        Empefficiency.result
      }.map { effT =>
      effT.map(e => e.month -> e.efficiency.getOrElse(BigDecimal(0))).toMap
    }

    find(id).map{
      case Some(rt) =>
        // team members who are not in PE.
        rt.pplteamname match {
          case Some(pplTeamName) =>
            val teamMembers = teamDescriptionRepo.findTeamMembers(pplTeamName)
              .map { teamM: Set[offline.Tables.EmprelationsRow] =>
                Future.sequence(teamM.map {
                  emp =>
                    matrixTeamMemberRepo.isPE(emp.login).map { isPE =>
                      if (isPE) {
                        None
                      } else {
                        Some(emp)
                      }
                    }
                }).map(s => s.flatten)
              }.flatMap(identity)
            val teamStats = teamDescriptionRepo.getPositionHireForTeam(pplTeamName)
            val now = System.currentTimeMillis()
            val nowD = new java.sql.Date( now )

            val xxxx: Future[Seq[(offline.Tables.EmprelationsRow, Int, EfficencyMonth)]] = (for {
              tM <- teamMembers
              tS <- teamStats
              ef <- empEF
            } yield( tM, tS, ef)).map { x =>
              val efficency = x._3
              val maxMonths = efficency.keys.max
              val prodLogins = x._1.map(_.login)

              x._2.filter(x => prodLogins.contains(x._1.login))
                .filter(x => x._2.equals("DEV"))
                .map { ePH =>
                  val hireDateTime: Long = ePH._3.getOrElse(nowD).getTime
                  val hireMonths: Int = Math.round(TimeUnit.MILLISECONDS.toDays(now - hireDateTime) / 365.0f * 12)
                  val ee = EfficencyMonth(
                    efficency.getOrElse(Math.min(hireMonths, maxMonths), BigDecimal(0)),
                    efficency.getOrElse(Math.min(hireMonths + 3, maxMonths), BigDecimal(0)),
                    efficency.getOrElse(Math.min(hireMonths + 6, maxMonths), BigDecimal(0)),
                    efficency.getOrElse(Math.min(hireMonths + 9, maxMonths), BigDecimal(0)),
                    efficency.getOrElse(Math.min(hireMonths + 12, maxMonths), BigDecimal(0)))
                  (ePH._1, hireMonths, ee)
                }
            }
            xxxx
          case None => Future(Seq.empty)
        }

      case None => Future(Seq.empty)
    }.flatMap(identity)
  }

  def insert(rt: ResourceteamRow) = db
    .run(Resourceteam returning Resourceteam.map(_.id) += rt)
    .map(id => rt.copy(id = id))


  def update(id: Int, rt:ResourceteamRow) = {
    db.run(Resourceteam.filter(_.id === id).update( rt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Resourceteam.filter(_.id === id).delete) map { _ > 0 }
}

case class EfficencyMonth( t0:BigDecimal, t3:BigDecimal, t6:BigDecimal, t9:BigDecimal, t12:BigDecimal ) {
  def add(  e:EfficencyMonth):EfficencyMonth = {
    EfficencyMonth( t0 + e.t0, t3+ e.t3, t6 + e.t6, t9 + e.t9, t12 + e.t12 )
  }

}
