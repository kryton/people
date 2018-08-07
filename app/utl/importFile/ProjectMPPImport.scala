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

package utl.importFile

import java.nio.charset.CodingErrorAction
import java.nio.file.Path
import java.util
import java.util.Date

import models.product.ResourceTeamRepo
import net.sf.mpxj.mpp.MPPReader
import net.sf.mpxj.{ProjectFile, Relation, ResourceAssignment, Task}
import play.api.Logger
import offline.Tables

import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Codec}
/**
  * Created by iholsman on 4/29/2017.
  * All Rights reserved
  */
object ProjectMPPImport {
  val fieldNames = Set("Program", "CID", "CID Inventory File Name", "Dev Estimate", "Anchor", "Buffered Estimate")

  //importFileMPP("/tmp/roadmap-2017-04-29.mpp")

  def importFileMPP(filename: String)(implicit
                                      executionContext: ExecutionContext,
                                      resourceTeamRepo: ResourceTeamRepo):Future[Seq[FeatureImport]]  = {
    val reader: MPPReader = new MPPReader()
    val inFile: ProjectFile = reader.read(filename)
    importFileMPP(inFile)
  }

  def importFile(path: Path)(implicit
                             executionContext: ExecutionContext,
                             resourceTeamRepo: ResourceTeamRepo):Future[ Either[String,List[FeatureImport]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    val bufferedSource: BufferedSource = scala.io.Source.fromFile(path.toFile)
    importFile(path.toFile)
  }

  def importFile(file: java.io.File )(implicit
                                      executionContext: ExecutionContext,
                                      resourceTeamRepo: ResourceTeamRepo):Future[ Either[String,List[FeatureImport]]]  = {
    val reader: MPPReader = new MPPReader()
    val inFile: ProjectFile = reader.read(file)
    importFileMPP(inFile).map{ x =>
      Right(x.toList )
    }

  }

  def importFileMPP(inFile:ProjectFile)(implicit
                                        executionContext: ExecutionContext,
                                        resourceTeamRepo: ResourceTeamRepo): Future[Seq[FeatureImport]] ={
    resourceTeamRepo.allEx.map { rts =>
      rts.map { rt =>
        rt._1.msprojectname.trim.toLowerCase -> rt
      }.toMap

    }.map { resourceMap: Map[String, (Tables.ResourceteamRow, Option[Tables.ResourcepoolRow])] =>
      val tasks = inFile.getTasks
      val resources = inFile.getResources
      val resourceAssignment = inFile.getResourceAssignments
      val size = tasks.size
      val rootTask = tasks.get(0)
      val childFeatures = rootTask.getChildTasks
      val featureList: Seq[FeatureImport] = (0 until childFeatures.size()).flatMap { id =>
        val feature = childFeatures.get(id)
        dumpFeature(feature, resourceMap)
      }
      featureList
    }
  }

  def dumpFeature(task: Task, resourceMap:Map[String, (Tables.ResourceteamRow, Option[Tables.ResourcepoolRow])]): Option[FeatureImport] = {
    val projects = task.getChildTasks
    //val resources = task.getResourceAssignments
    val eTasks = getETasks(task)
  //  val resourceNames: Seq[(Option[String], Option[Double])] = getResources(task.getResourceAssignments)

    val childrenS: Seq[ProjectImport] = (0 until projects.size()).flatMap { id =>
      val childT = projects.get(id)
      dumpProject(childT, task.getName, resourceMap)
    }
    val cidValue: String = eTasks.get("CID").flatten.getOrElse("N")
    val anchorValue = eTasks.get("Anchor").flatten.getOrElse("N")

    Some(FeatureImport(task.getName,
      program = eTasks.get("Program").flatten,
      isCID= cidValue.equals("Y"),
      isAnchor = anchorValue.equals("Y"),
      priority = task.getPriority.getValue,
      start =  new java.sql.Date(task.getStart.getTime),
      finish = new java.sql.Date(task.getFinish.getTime),
      projects = childrenS.toList
    ))
  }

  protected def getResources(resources: util.List[ResourceAssignment]): Seq[(Option[String], Option[Double])] = {
    (0 until resources.size()).flatMap { id =>
      val res = resources.get(id)
      val units = res.getUnits


      if (res.getResource == null) {
        Some(None, None)
      } else {
        Some(Some(res.getResource.getName), Some(units.doubleValue() / 100))
      }
    }
  }

  protected def getETasks(task:Task) : Map[String, Option[String]]= {
    fieldNames.map { row =>
      val n = task.getFieldByAlias(row)
      if (n == null) {
        row -> None
      } else {
        row -> Some(n.toString)
      }
    }.toMap
  }

  def dumpProject(task:Task, featureName:String,resourceMap:Map[String, (Tables.ResourceteamRow, Option[Tables.ResourcepoolRow])]):Option[ProjectImport] = {
    val projects = task.getChildTasks
    if (projects.size()>0) {
      Logger.error(s"Invalid - we only do 2 level projects. ${task.getName}")
    }
 //   val resources: util.List[ResourceAssignment] = task.getResourceAssignments
    val predecessors: util.List[Relation] = task.getPredecessors
    val pred: Seq[(Int,String)] = ( 0 until predecessors.size()).map{ id =>
      val relation = predecessors.get(id)
      if ( relation != null) {
        val source: Task = relation.getSourceTask
        val target: Task = relation.getTargetTask
          // FS - Finish Start (default)
          // FF - Finish Finish
          // SS - Start Start
          // SF - Start Finish
        if (target != null) {
          (Some(target.getID.toInt),relation.getType.toString)
        } else {
          (None,"")
        }
      } else {
        (None,"")
      }
    }.filter(p => p._1.isDefined).map{ p => (p._1.get,p._2)}.filterNot( p=> p._1 == task.getID)
    val constraintType = task.getConstraintType.getValue match {
      case 0 => "ASAP"
      case 1 => "ALAP"
      case 2 => "MSO"
      case 3 => "MFO"
      case 4 => "SNET"
      case 5 => "SNLT"
      case 6 => "FNET"
      case 7 => "FNLT"
      case _ => "???"
    }
    /*
     AS_SOON_AS_POSSIBLE(0),
   AS_LATE_AS_POSSIBLE(1),
   MUST_START_ON(2),
   MUST_FINISH_ON(3),
   START_NO_EARLIER_THAN(4),
   START_NO_LATER_THAN(5),
   FINISH_NO_EARLIER_THAN(6),
   FINISH_NO_LATER_THAN(7);
     */
    val constraintDate: Option[java.sql.Date] = Option(task.getConstraintDate) match {
      case Some(d) =>Some( new java.sql.Date(d.getTime))
      case None => None
    }
    if ( !constraintType.equalsIgnoreCase("ASAP")) {
      Logger.error(s"Task #${task.getID} - ${task.getName} has a constraint $constraintType - $constraintDate")
    }


    val eTasks = getETasks( task )

    val resourceNames = getResources(task.getResourceAssignments)
    if ( resourceNames.size>1) {
      Logger.error(s"Task #${task.getID} - ${task.getName} has more than 1 resource Assigned. that isn't supported. ")
    }
    val devE = eTasks.get("Dev Estimate").flatten.getOrElse("0.0").toDouble
    val bufE = eTasks.get("Buffered Estimate").flatten.getOrElse("0.0").toDouble
    val resName = resourceNames.headOption.getOrElse( (None,None))
    val cidValue = eTasks.getOrElse("CID","N")
    val anchorValue = eTasks.getOrElse("Anchor","N")

    Some( ProjectImport(task.getID,
      predecessor = pred,
      disabled = !task.getActive,
      program = eTasks.get("Program").flatten,
      isCID= cidValue.equals("Y"),
      isAnchor = anchorValue.equals("Y"),
      priority = task.getPriority.getValue,
      feature=featureName,
      task = task.getName,
      resource =resName._1.getOrElse(""),
      resourceNumber = resName._2.getOrElse(0.0).toInt,
      devEstimate = devE,
      bufferedEstimate = bufE,
      work = task.getWork.getDuration,
      duration = task.getDuration.getDuration,
      start =  new java.sql.Date(task.getStart.getTime),
      finish = new java.sql.Date(task.getFinish.getTime),
      percentComplete = task.getPercentageComplete.doubleValue(),
      resourceTeam = resourceMap.get(resName._1.getOrElse("").toLowerCase.trim),
      constraint = constraintType,
      constraintDate = constraintDate
    ))
  }
}
