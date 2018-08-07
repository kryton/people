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

import java.sql.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import models.people.{MatrixTeamMemberRepo, OfficeRepo, PositionTypeRepo, TeamDescriptionRepo}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import offline.Tables
import offline.Tables.EmprelationsRow
import play.api.Logger
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ResourceTeamRepo @Inject()( /* @NamedDatabase("offline") */  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

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


  def getTeamSummaryByVendorCountry(id:Int
                                   )(implicit teamDescriptionRepo: TeamDescriptionRepo,
                                            officeRepo:OfficeRepo, matrixTeamMemberRepo:MatrixTeamMemberRepo,
                                            positionTypeRepo: PositionTypeRepo): Future[ Seq[TeamSummary]] = {
    import models.people.EmpRelationsRowUtils._

    (for{
      rt <- find(id)
      mt <- matrixTeamMemberRepo.getPETeamMembers.map{ x => x.map{ _._2.login.toLowerCase}.toSet }
    } yield (rt,mt)).map { x=>
      x._1 match {
        case None => Future.successful(Seq.empty)

        case Some(rt) =>
          val peTeam = x._2
          rt.pplteamname match {
            case None => Future.successful(Seq.empty)
            case Some(teamName) =>
              val summary: Future[Seq[TeamSummary]] = teamDescriptionRepo.findTeamMembers(teamName).map { emps =>
                val line: Future[Seq[TeamSummary]] = Future.sequence(emps.toSeq.map { emp =>
                  (for {
                    o <- officeRepo.find(emp.officeid.getOrElse(0))
                    p <- positionTypeRepo.find(emp.position)
                  } yield (o, p)).map { x =>
                    val officeCountry = x._1 match {
                      case Some(oc) => oc.country
                      case None => None
                    }
                    val pt = x._2 match {
                      case Some(p) => p.positiontype
                      case None => "UNKNOWN"
                    }
                    TeamSummary(emp.agency, isContractor = emp.isContractor,
                      country = officeCountry,
                      positionType = pt,
                      isPE = peTeam.contains(emp.login.toLowerCase),
                      tally = 1)
                  }
                })
                val tally: Future[Seq[TeamSummary]] = line.map { (seq: Seq[TeamSummary]) =>
                  seq.groupBy(p => p).map { sumLine =>
                    val newTally = sumLine._2.map(_.tally).sum
                    sumLine._1.copy(tally = newTally)
                  }.toSeq
                }

                tally
              }.flatMap(identity)
              summary
          }
      }
    }.flatMap(identity)

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

  def breakDownTeam(teamId:Int): Future[(Iterable[(ProductfeatureRow, Long, Map[Date, Double])], Iterable[(ProjectRow, Long, Map[Date, Double])])] = {

    (for {
    // p <-  this.findByFeatureEx(featureId )
      p2 <- db.run(
        Project
          .join(Resourceteamproject).on(_.id === _.projectid)
          .filter( _._2.resourceteamid === teamId )
          .join(Productfeature).on(_._1.productfeatureid === _.id)
          .result
      ).map { res =>
        res.map { line =>
          val x: (ProjectRow, ResourceteamprojectRow, ProductfeatureRow)= (line._1._1, line._1._2, line._2)
          x
        }.groupBy(_._1)
      }
    } yield p2)
      .map { x =>
        val projectsEx: Map[ProjectRow, Seq[(ProjectRow, ResourceteamprojectRow, ProductfeatureRow )]] = x.filter(_._1.isactive)
        val byMonth: Iterable[(ProjectRow, Seq[(ResourceteamprojectRow, ProductfeatureRow)], (Int, Seq[(Date, Int)]))] = projectsEx.map { p =>

          val reduced: Seq[(ResourceteamprojectRow, ProductfeatureRow)] = p._2.map { l =>
            (l._2, l._3)
          }
          if (p._1.started.nonEmpty && p._1.finished.nonEmpty) {
            (p._1, reduced, utl.Conversions.explodeDateRange(p._1.started.get, p._1.finished.get))
          } else {
            (p._1, reduced, (0, Seq.empty))
          }
        }

        val byResources: Iterable[(ProjectRow, Seq[(ProductfeatureRow, (Long, Seq[(Date, Double)]))], (Int, Seq[(Date, Int)]))] = byMonth.map { p =>
          val featureByMonth: Seq[(ProductfeatureRow, (Long, Seq[(Date, Double)]))] = p._2.map { res =>
            val featureSize = res._1.featuresize
            val totalDays: Double = if (p._3._1 < 1) 0.00001 else p._3._1
            val daysBreakout: Seq[(Date, Int)] = p._3._2
            val ratio: Double = 1.0 * featureSize / totalDays
            (res._2, (featureSize, daysBreakout.map { d => (d._1, d._2 * ratio) }))
          }
          (p._1, featureByMonth, p._3)
        }
        val byTeam: Iterable[(ProductfeatureRow, Long, Map[Date, Double])] = byResources.flatMap { proj =>

          proj._2.map { res =>
            res
          }
        }.groupBy(_._1).map { teamDetail =>
          val team = teamDetail._1
          val p2: Iterable[(Long, Seq[(Date, Double)])] = teamDetail._2.map { y => y._2 }
          val p2_total = p2.map(_._1).sum
          val p2_agg: Map[Date, Double] = p2.flatMap { p3 =>
            p3._2
          }.groupBy(_._1).map { p4 =>
            (p4._1, p4._2.map {
              _._2
            }.sum)
          }
          (team, p2_total, p2_agg)
        }
        val byProject: Iterable[(ProjectRow, Long, Map[Date, Double])] = byResources.flatMap { proj =>
          proj._2.map { res =>
            (proj._1, res._2)
          }
        }.groupBy(_._1).map { projDetail =>
          val project = projDetail._1
          val p2: Iterable[(Long, Seq[(Date, Double)])] = projDetail._2.map { y => y._2 }
          val p2_total = p2.map(_._1).sum
          val p2_agg: Map[Date, Double] = p2.flatMap { p3 =>
            p3._2
          }.groupBy(_._1).map { p4 =>
            (p4._1, p4._2.map {
              _._2
            }.sum)
          }
          (project, p2_total, p2_agg)
        }
        (byTeam, byProject)
      }
  }

  def roadMap(id:Int): Future[Seq[(ResourceteamRow, ResourceteamprojectRow, ProjectRow, ProductfeatureRow)]] = {
    val qry = Resourceteam.filter(_.id === id)
        .join(Resourceteamproject).on(_.id === _.resourceteamid)
        .join( Project).on(_._2.projectid === _.id).filter(_._2.isactive)
        .join( Productfeature).on( _._2.productfeatureid === _.id )
          .filter( _._2.isactive)
    db.run(qry.result).map { x =>
      x.map { row =>
        (row._1._1._1, row._1._1._2, row._1._2, row._2)
      }
    }
  }

  def insert(rt: ResourceteamRow): Future[ResourceteamRow] = db
    .run(Resourceteam returning Resourceteam.map(_.id) += rt)
    .map(id => rt.copy(id = id))


  def update(id: Int, rt:ResourceteamRow): Future[Boolean] = {
    db.run(Resourceteam.filter(_.id === id).update( rt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int): Future[Boolean] =
    db.run(Resourceteam.filter(_.id === id).delete) map { _ > 0 }
}

case class EfficencyMonth( t0:BigDecimal, t3:BigDecimal, t6:BigDecimal, t9:BigDecimal, t12:BigDecimal ) {
  def add(  e:EfficencyMonth):EfficencyMonth = {
    EfficencyMonth( t0 + e.t0, t3 + e.t3, t6 + e.t6, t9 + e.t9, t12 + e.t12 )
  }

}
case class TeamSummary(agency:String, isContractor:Boolean, country:Option[String], positionType:String, isPE:Boolean=false, tally:Int)
