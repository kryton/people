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

import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import projectdb.Tables
import slick.jdbc.JdbcProfile
import util.importFile.ProjectImport

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ProjectRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Int):Future[Option[ProjectRow]] = db.run(Project.filter(_.id === id).result.headOption)
  def findEx(id:Int): Future[Option[(ProjectRow, Option[StatuscolorRow], Seq[(ResourceteamprojectRow,ResourceteamRow)], Seq[(ProjectdependencyRow, ProjectRow)])]] = {
    (for {
      p <- db.run( Project.filter(_.id === id).joinLeft(Statuscolor).on(_.currentstatusid === _.id).result.headOption )
      r <- db.run( Resourceteamproject.filter( _.projectid === id).join(Resourceteam).on(_.resourceteamid === _.id).result )
      d <- db.run( Projectdependency.filter(_.fromproject === id).join(Project).on(_.toproject === _.id).result )
    } yield( p,r,d)).map { result =>
      result._1 match {
        case Some(x) => Some( (x._1, x._2, result._2, result._3 ))
        case None => None
      }
    }
  }
  def all = db.run(Project.sortBy( _.name).result)
  def search(term:String) = db.run(Project.filter(_.name startsWith term).sortBy( _.name).result)


  def insert(pt: ProjectRow) = db
    .run(Project returning Project.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Int, pt:ProjectRow) = {
    db.run(Project.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int): Future[Boolean] =
    ( for{
      a <- db.run(Resourceteamproject.filter(_.projectid === id ).delete).map { _>0}

      c <- db.run(Projectdependency.filter(_.toproject === id).delete) map { _ > 0 }
      d <- db.run(Projectdependency.filter(_.fromproject === id).delete) map { _ > 0 }
    } yield( a,c,d) ).map{ x =>
      db.run(Project.filter(_.id === id).delete)
    }.flatMap(identity).map{ x =>
      x > 0
    }



  def findByFeature( featureId:Int) =
    db.run( Project.filter(_.productfeatureid === featureId).result)



  def findByFeatureEx( featureId:Int): Future[Seq[(ProjectRow, Option[StatuscolorRow], Seq[(ResourceteamprojectRow,ResourceteamRow)], Seq[(ProjectdependencyRow, ProjectRow)])]] =
    db.run( Project.filter(_.productfeatureid === featureId).result).map { projects =>
      Future.sequence( projects.map{ proj => findEx( proj.id)} )
    }.flatMap(identity).map{ seqO =>
      seqO.flatten
    }


  def findResourceTeams(projectId:Int): Future[Seq[( Tables.ResourceteamprojectRow, Tables.ResourceteamRow)]] = {
    db.run {
      Resourceteamproject.filter(_.projectid === projectId)
        .join(Resourceteam).on(_.resourceteamid === _.id)
        .result
    }
  }

  def repopulate( featureId:Int, projects:Seq[ProjectImport])(implicit projectDependencyRepo: ProjectDependencyRepo) :Future[(Iterable[Option[ResourceteamprojectRow]], Seq[Seq[Option[Tables.ProjectdependencyRow]]])] ={

    val interestingProjects: Seq[ProjectImport] = projects.filter( _.resource.nonEmpty)
    val fBf: Future[(Iterable[Option[ResourceteamprojectRow]], Seq[Seq[Option[Tables.ProjectdependencyRow]]])] = findByFeature(featureId).map{ existing =>
      if (featureId == 539) {
        Logger.info("DEBUG-Project-repopulate")
      }
      val existMap = existing.filter( _.msprojectname.nonEmpty ).map{ p =>  p.msprojectname.getOrElse("").toLowerCase -> p }.toMap
      val newMap: Map[String, ProjectImport] = interestingProjects.map{ p => p.task.toLowerCase+"/"+p.resource.toLowerCase -> p}.toMap
      val toDelS = existMap.keySet.diff( newMap.keySet )
      val toDel = existMap.filter( p => toDelS.contains(p._1))
      val toUpd = existMap.filterNot( p => toDelS.contains(p._1 ))
      val toUpdF = Future.sequence( toUpd.map{ (rec: (String, ProjectRow)) =>
        val imp = newMap(rec._1)
        update(rec._2.id, rec._2.copy( isactive = !imp.disabled, started = Some(imp.start), finished = Some(imp.finish)))
      })
      val delF: Future[Iterable [Boolean]] =Future.sequence( toDel.map{ (rec: (String,ProjectRow)) => delete( rec._2.id ) })
      val insF: Future[Set[ProjectRow]] = Future.sequence(newMap.keySet.diff(existMap.keySet).map{ msprojectname =>
        val imp = newMap(msprojectname)
        // TODO lookup status/put correct status in. (active/stopped from MS Project)
        insert(ProjectRow(id=0,name=s"${imp.task}/${imp.resource}",
          msprojectname = Some(s"${imp.task.toLowerCase}/${imp.resource.toLowerCase}"),
          execsummary = "Fill",
          currentstatusid = 6,
          started = Some(imp.start),
          finished = Some(imp.finish),
          isactive = !imp.disabled,
          productfeatureid = featureId
        ))
      })

      (for {
        del <- delF
        ins <- insF
        upd <- toUpdF
      } yield (del,ins,upd)).map{ x=>
        val insMap: Set[(String, ProjectRow)] = x._2.map{ prj => prj.msprojectname.getOrElse("") -> prj }
        val fullMap: Map[String, ProjectRow] = (insMap ++ toUpd).map{ x => x._1 -> x._2 }.toMap

        if (featureId == 539) {
        //  Logger.info("DEBUG-Project-repopulate2")
        } else {
       //   Logger.info(s"FeatureID ${featureId}")
        }

        // update resources for project
        val resourceF: Future[Iterable[Option[ResourceteamprojectRow]]] = Future.sequence(fullMap.map{ projT =>
          deleteResourcesForProject(projT._2.id).map{ ignore =>
            newMap.get(projT._1) match {
              case None =>
                Logger.error("Logic Error. Should match (ProjectRepo/Resource")
                Future.successful(None)
              case Some(imp) => imp.resourceTeam match {
                case Some(rtp) => val rt= ResourceteamprojectRow(id=0,
                  featuresize = imp.devEstimate.toInt,
                  maxdevs = imp.resourceNumber,
                  projectid = projT._2.id,
                  resourceteamid = rtp._1.id,
                  featuresizeremaining = Some( BigDecimal( imp.devEstimate*(100-imp.percentComplete)/100) )
                )
                   insert(rt) .map{ x => Some(x)}
                case None =>
                  Logger.error("Logic Error. Should have a resource team assigned at this stage??")
                  Future.successful(None)
              }
            }
          }.flatMap(identity)
        })

        val byProjRow = interestingProjects.map{ proj => proj.row -> s"${proj.task.toLowerCase}/${proj.resource.toLowerCase}" }.toMap

        // update dependencies for project
        val projectDependencyF = Future.sequence( interestingProjects.map{ proj =>
          val key = s"${proj.task.toLowerCase}/${proj.resource.toLowerCase}"
          val projID = fullMap.get(key) match {
            case None =>
              Logger.error(s"Logic Error. ProjectRepo - shouldn't have no match Key=$key")
              0
            case Some(pp) => pp.id
          }
          projectDependencyRepo.deleteFrom(projID).map{ aaaa =>
            Future.sequence(proj.predecessor.map{ predRow =>
              byProjRow.get( predRow ) match {
                case Some(task) => fullMap.get(task) match {
                  case Some(projRow) =>
                    projectDependencyRepo.insert(ProjectdependencyRow(0,projID, projRow.id))
                      .map( res => Some(res) )
                  case None =>
                    Logger.error(s"Logic Error.Project Repo Dependency not found Row=$predRow key=$key")
                    Future.successful(None)
                }
                case None =>
                  Logger.error(s"Logic Error.Project Repo Dependency not found Row=$predRow key=$key x2")
                  Future.successful(None)
              }
            })//.flatMap(identity)
          }.flatMap(identity)
        })
        (for{
          r <- resourceF
          p <- projectDependencyF
        } yield(r,p)).map { (ignore: (Iterable[Option[ResourceteamprojectRow]], Seq[Seq[Option[ProjectdependencyRow]]])) =>
          ignore
        }

      }
    }.flatMap(identity).flatMap(identity)
    fBf

  }

  protected def insert(rtpf: ResourceteamprojectRow): Future[ResourceteamprojectRow] = db
    .run(Resourceteamproject returning Resourceteamproject.map(_.id) += rtpf)
    .map(id => rtpf.copy(id = id))

  protected def deleteResourcesForProject(projectId: Int): Future[Boolean] =
    db.run(Resourceteamproject.filter(_.projectid === projectId).delete) map {  _ > 0  }


  def breakDownFeature(featureId:Int): Future[(Iterable[((ResourceteamRow,Option[ResourcepoolRow]), Int, Map[Date, Double])], Iterable[(ProjectRow, Int, Map[Date, Double])])] = {

    (for {
     // p <-  this.findByFeatureEx(featureId )
      p2 <- db.run(
        Project
          .filter(_.productfeatureid === featureId)
          .join(Resourceteamproject).on(_.id === _.projectid)
          .join(Resourceteam).on(_._2.resourceteamid === _.id)
          .joinLeft(Resourcepool).on(_._2.resourcepoolid === _.id)
        .result
      ).map{  res =>
        res.map{ line =>
          (line._1._1._1, line._1._1._2, (line._1._2, line._2))
        }.groupBy( _._1)
      }
    } yield  p2  )
      .map { x =>
        val projectsEx: Map[ProjectRow, Seq[(ProjectRow, ResourceteamprojectRow, (ResourceteamRow, Option[ResourcepoolRow]))]] = x.filter(_._1.isactive)
        val byMonth: Iterable[(ProjectRow, Seq[(ResourceteamprojectRow, (ResourceteamRow,Option[ResourcepoolRow]))], (Int, Seq[(Date, Int)]))] = projectsEx.map { p =>
          val reduced: Seq[(ResourceteamprojectRow, (ResourceteamRow,Option[ResourcepoolRow]))] =p._2.map{ l =>
            (l._2,l._3)
          }
          if (p._1.started.nonEmpty && p._1.finished.nonEmpty) {
            (p._1, reduced, util.Conversions.explodeDateRange(p._1.started.get, p._1.finished.get))
          } else {
            (p._1,reduced, (0, Seq.empty))
          }
        }

        val byResources: Iterable[(ProjectRow, Seq[( (ResourceteamRow,Option[ResourcepoolRow]), (Int, Seq[(Date, Double)]))], (Int, Seq[(Date, Int)]))] = byMonth.map { p =>
          val featureByMonth = p._2.map { res =>
            val featureSize = res._1.featuresize
            val totalDays: Double = if (p._3._1 < 1) 0.00001 else p._3._1
            val daysBreakout: Seq[(Date, Int)] = p._3._2
            val ratio: Double = 1.0 * featureSize / totalDays
            (res._2, (featureSize, daysBreakout.map { d => (d._1, d._2 * ratio) }))
          }
          (p._1, featureByMonth, p._3)
        }
        val byTeam: Iterable[((ResourceteamRow,Option[ResourcepoolRow]), Int, Map[Date, Double])] = byResources.flatMap { proj =>
          proj._2.map { res =>
            res
          }
        }.groupBy(_._1).map { teamDetail =>
          val team = teamDetail._1
          val p2: Iterable[(Int, Seq[(Date, Double)])] = teamDetail._2.map { y =>y._2 }
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
          val p2: Iterable[(Int, Seq[(Date, Double)])] = projDetail._2.map { y => y._2}
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
}
