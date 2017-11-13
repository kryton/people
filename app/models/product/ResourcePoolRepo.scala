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
import javax.inject.Inject

import models.people.{MatrixTeamMemberRepo, OfficeRepo, PositionTypeRepo, TeamDescriptionRepo}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import projectdb.Tables
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ResourcePoolRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id: Int): Future[Option[ResourcepoolRow]] = db.run(Resourcepool.filter(_.id === id).result.headOption)

  def all: Future[Seq[ResourcepoolRow]] = db.run(Resourcepool.sortBy(_.ordering).result)
  def allPoolsTeams: Future[Set[Either[ResourceteamRow,ResourcepoolRow]]] = {
    db.run(Resourceteam.joinLeft(Resourcepool).on(_.resourcepoolid === _.id).result).map{ seq =>
      seq.map{ rtrp =>
        rtrp._2 match {
          case Some(rp) => Right(rp)
          case None => Left(rtrp._1)
        }
      }.toSet
    }

  }

  def search(term: String):Future[Seq[ResourcepoolRow]] = db.run(Resourcepool.filter(_.name startsWith term).sortBy(_.name).result)

  def findTeams(id: Int): Future[Seq[Tables.ResourceteamRow]] = db.run(Resourceteam.filter(_.resourcepoolid === id).result)

  def insert(pt: ResourcepoolRow): Future[_root_.projectdb.Tables.ResourcepoolRow] = db
    .run(Resourcepool returning Resourcepool.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Int, pt: ResourcepoolRow): Future[Boolean] = {
    db.run(Resourcepool.filter(_.id === id).update(pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int): Future[Boolean] =
    db.run(Resourcepool.filter(_.id === id).delete) map { _ > 0 }

  def getTeamSummaryByVendorCountry(id: Int
                                   )(implicit teamDescriptionRepo: TeamDescriptionRepo,
                                     officeRepo: OfficeRepo, matrixTeamMemberRepo:MatrixTeamMemberRepo,
                                     positionTypeRepo: PositionTypeRepo): Future[Seq[TeamSummary]] = {
    import models.people.EmpRelationsRowUtils._
    (for{
      m <- matrixTeamMemberRepo.getPETeamMembers.map{x=> x.map(_._2.login.toLowerCase).toSet}
      f <- findTeams(id)
    } yield(f,m)).map{ x =>
      val rtS = x._1
      val peTeam = x._2
      val teamResult: Future[Seq[Seq[TeamSummary]]] = Future.sequence(rtS.map { (rt: ResourceteamRow) =>
        val xyz: Future[Seq[TeamSummary]] = rt.pplteamname match {
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
                  //  TODO fix PE
                  TeamSummary(emp.agency, isContractor = emp.isContractor, officeCountry, pt, isPE = peTeam.contains(emp.login.toLowerCase), tally = 1)
                }
              })
              val tally: Future[Seq[TeamSummary]] = line.map { (seq: Seq[TeamSummary]) =>
                seq.groupBy(p => p).map { sumLine =>
                  val newTally: Int = sumLine._2.map{ x => x.tally}.sum
                  sumLine._1.copy(tally = newTally)
                 // (sumLine._1._1, sumLine._1._2, sumLine._1._3, sumLine._1._4, sumLine._2.size)
                }.toSeq
              }

              tally
            }.flatMap(identity)
            summary
        }
        xyz
      })
      val summary2: Future[Seq[TeamSummary]] = teamResult.map { (sseq: Seq[Seq[TeamSummary]]) =>
        sseq.flatten.groupBy(p => (p.agency, p.isContractor, p.country, p.positionType, p.isPE))
          .map { p =>
            TeamSummary(agency = p._1._1,
              isContractor =  p._1._2,
              country = p._1._3,
              positionType =  p._1._4,
              isPE = p._1._5,
              tally = p._2.map (     _.tally ) .sum)
        }.toSeq
      }
      summary2
    }.flatMap(identity)
  }

  def breakDownPool(poolId:Int): Future[(Iterable[(ProductfeatureRow, Int, Map[Date, Double])],
                                         Iterable[(ProjectRow, Int, Map[Date, Double])])] = {

    (for {
    // p <-  this.findByFeatureEx(featureId )
      p2 <- db.run(
        Project
          .join(Resourceteamproject).on(_.id === _.projectid)
          .join(Resourceteam).on(_._2.resourceteamid === _.id)
          .filter(_._2.resourcepoolid === poolId)
          .join(Productfeature).on(_._1._1.productfeatureid === _.id)
          .result
      ).map { res =>
        res.map { line =>
          val x: (ProjectRow, ResourceteamprojectRow, ResourceteamRow, ProductfeatureRow)= ( line._1._1._1, line._1._1._2, line._1._2, line._2)
          x
        }.groupBy(_._1)
      }
    } yield p2)
      .map { x =>
        val projectsEx: Map[ProjectRow, Seq[(ProjectRow, ResourceteamprojectRow, ResourceteamRow, ProductfeatureRow )]] = x.filter(_._1.isactive)
        val byMonth: Iterable[(ProjectRow, Seq[(ResourceteamprojectRow, ResourceteamRow, ProductfeatureRow)], (Int, Seq[(Date, Int)]))] = projectsEx.map { p =>
          val reduced: Seq[(ResourceteamprojectRow, ResourceteamRow, ProductfeatureRow)] = p._2.map { l =>
            (l._2, l._3, l._4)
          }

          if (p._1.started.nonEmpty && p._1.finished.nonEmpty) {
            (p._1, reduced, utl.Conversions.explodeDateRange(p._1.started.get, p._1.finished.get))
          } else {
            (p._1, reduced, (0, Seq.empty))
          }
        }

        val byResources: Iterable[(ProjectRow, Seq[(ProductfeatureRow, (Int, Seq[(Date, Double)]))], (Int, Seq[(Date, Int)]))] = byMonth.map { p =>
          val featureByMonth: Seq[(ProductfeatureRow, (Int, Seq[(Date, Double)]))] = p._2.map { res =>
            val featureSize = res._1.featuresize
            val totalDays: Double = if (p._3._1 < 1) 0.00001 else p._3._1
            val daysBreakout: Seq[(Date, Int)] = p._3._2
            val ratio: Double = 1.0 * featureSize / totalDays

            (res._3, (featureSize, daysBreakout.map { d => (d._1, d._2 * ratio) }))
          }

          (p._1, featureByMonth, p._3)

        }
        val byTeam: Iterable[(ProductfeatureRow, Int, Map[Date, Double])] = byResources.flatMap { proj =>

          proj._2.map { res =>
            res
          }
        }.groupBy(_._1).map { teamDetail =>
          val team = teamDetail._1
          val p2: Iterable[(Int, Seq[(Date, Double)])] = teamDetail._2.map { y => y._2 }
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
        val byProject: Iterable[(ProjectRow, Int, Map[Date, Double])] = byResources.flatMap { proj =>
          proj._2.map { res =>
            (proj._1, res._2)
          }
        }.groupBy(_._1).map { projDetail =>
          val project = projDetail._1
          val p2: Iterable[(Int, Seq[(Date, Double)])] = projDetail._2.map { y => y._2 }
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
  def quarterSlack: Future[Seq[RoadmapslackRow]] = db.run( Roadmapslack.sortBy(_.ordering).result)
  def getDevStats(id:Int)(implicit resourceTeamRepo: ResourceTeamRepo,
                          teamDescriptionRepo: TeamDescriptionRepo,
                          matrixTeamMemberRepo: models.people.MatrixTeamMemberRepo,
                          officeRepo:models.people.OfficeRepo,
                          positionTypeRepo:models.people.PositionTypeRepo):Future[Seq[(ResourceteamRow,
                                                          Seq[(offline.Tables.EmprelationsRow, Int, EfficencyMonth)],
                                                          Seq[TeamSummary])]
                                                        ] = {
    resourceTeamRepo.findInPool( id).map{
      (teamS: Seq[ResourceteamRow]) => Future.sequence( teamS.map { team:ResourceteamRow =>
        (for{
          dsX <- resourceTeamRepo.getDevStatsForTeam(team.id)
          ctX <- resourceTeamRepo.getTeamSummaryByVendorCountry(team.id)
        } yield (dsX,ctX)).map{ x=>
          (team,x._1, x._2)
        }
      })
    }.flatMap(identity)
  }
  def roadMap(id:Int): Future[Seq[(ResourceteamRow, ResourceteamprojectRow, ProjectRow, ProductfeatureRow)]] = {
    val qry = Resourceteam.filter(_.resourcepoolid === id)
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
}
