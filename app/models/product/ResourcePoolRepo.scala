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

import models.people.{MatrixTeamMemberRepo, OfficeRepo, PositionTypeRepo, TeamDescriptionRepo}
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import projectdb.Tables
import slick.jdbc.JdbcProfile

import scala.collection.immutable
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

  def all = db.run(Resourcepool.sortBy(_.ordering).result)

  def search(term: String) = db.run(Resourcepool.filter(_.name startsWith term).sortBy(_.name).result)

  def findTeams(id: Int): Future[Seq[Tables.ResourceteamRow]] = db.run(Resourceteam.filter(_.resourcepoolid === id).result)

  def insert(pt: ResourcepoolRow) = db
    .run(Resourcepool returning Resourcepool.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Int, pt: ResourcepoolRow) = {
    db.run(Resourcepool.filter(_.id === id).update(pt.copy(id = id))) map {
      _ > 0
    }
  }

  def delete(id: Int) =
    db.run(Resourcepool.filter(_.id === id).delete) map {
      _ > 0
    }

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
}
