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

package util.importFile

import java.io.{File, FileInputStream}
import java.net.URI
import java.nio.charset.CodingErrorAction
import java.nio.file.Path

import models.people.{CostCenterRepo, EmpHistoryRepo, OfficeRepo, PositionTypeRepo}
import models.product.ResourceTeamRepo
import org.apache.poi.ss.usermodel.CellType._
import org.apache.poi.ss.usermodel._
import play.api.Logger
import projectdb.Tables
import projectdb.Tables.ResourcepoolRow
import util.Conversions

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Codec}

/**
  * Created by iholsman on 4/15/2017.
  * All Rights reserved
  */
object ProjectXLSImport {
  def importFile(path: Path)(implicit
                             executionContext: ExecutionContext,
                             resourceTeamRepo: ResourceTeamRepo):Future[ Either[String,List[FeatureImport]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    val bufferedSource: BufferedSource = scala.io.Source.fromFile(path.toFile)
    importFile(path.toFile)
  }

  def importFile(file: URI)(implicit
                            executionContext: ExecutionContext,
                            resourceTeamRepo: ResourceTeamRepo):Future[ Either[String,List[FeatureImport]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    importFile(new File(file))
  }

  def importFile(fileRef: File)(implicit executionContext: ExecutionContext,
                                resourceTeamRepo: ResourceTeamRepo) :Future[ Either[String,List[FeatureImport]]] = {
    resourceTeamRepo.allEx.map { rts =>
      rts.map { rt =>
        rt._1.msprojectname.trim.toLowerCase -> rt
      }.toMap

    }.map { (resourceMap: Map[String, (Tables.ResourceteamRow,Option[ResourcepoolRow])]) =>
      val wb: Workbook = WorkbookFactory.create(new FileInputStream(fileRef))
      val sheet: Sheet = wb.getSheetAt(0)
      val rows: Int = sheet.getPhysicalNumberOfRows
      val headerRow: Row = sheet.getRow(0)
      val headerCells: Map[String, Int] = ((0 to 100) map { c: Int =>
        val cell = headerRow.getCell(c)
        if (cell != null) {
          val str = cell.getStringCellValue
          if (!str.isEmpty) {
            str -> c
          } else {
            " " -> c
          }
        } else {
          " " -> c
        }
      }).toMap

      val fieldnames = Set("Predecessors", "Program", "CID", "Anchor", "Priority",
        "CID Inventory File Name",
        "Task Name",
        "Resource Names", "Dev Estimate", "Buffered Estimate",
        "Work", "Duration",
        "Start", "Finish", "% Complete")
      val numColumns = fieldnames.size

      val headerCheck = headerCells.keySet.intersect(fieldnames)
      if (headerCheck.size != fieldnames.size) {
        Left(s"Missing Header Field(s): ${fieldnames.diff(headerCells.keySet).mkString(",")}")
      } else {
        val cellHeaders: Map[Int, String] = headerCells.map { x => x._2 -> x._1 }
        val projects: Seq[ProjectImport] = (1 until rows).flatMap { r =>
          val row: Row = sheet.getRow(r)
          if (row != null) {
            val featureCol = headerCells("Task Name")
            val cellValues: Map[String, String] = (0 to numColumns).map { column =>
              val cell: Cell = row.getCell(column)
              val value = if (cell == null) {
                ""
              } else {
                cell.getCellTypeEnum match {
                  case CellType.NUMERIC => cell.getNumericCellValue.toString
                  case CellType.BOOLEAN => if (cell.getBooleanCellValue) {
                    "Y"
                  } else {
                    "N"
                  }
                  case CellType.BLANK => ""
                  case CellType.STRING => cell.getStringCellValue
                  case _ => ""
                }
              }
              if ( column == featureCol) {
                // for Tasks, we need to keep the leading space if there is one so we can tell if this is a feature or task
                cellHeaders.getOrElse(column, "") -> value
              } else {
                cellHeaders.getOrElse(column, "") -> value.trim
              }
            }.toMap
            val priCell = row.getCell(headerCells("Priority"))
            val isDisabled = wb.getFontAt(priCell.getCellStyle.getFontIndex).getStrikeout
            val predecessorsValue: Seq[Int] = cellValues("Predecessors").split(",").flatMap(Conversions.toIntO)
            val priorityV = Conversions.toInt(cellValues("Priority"), 0)

            val isAnchorV = Conversions.toBool(cellValues("Anchor"))
            val isCIDV = Conversions.toBool(cellValues("CID"))
            val programO = Conversions.toStringO(cellValues("Program"))
            val resName = cellValues("Resource Names")
            val (teamName, numberDevs) = if (resName.contains("[")) {
              // Logger.info(s"Resname = $resName")
              val r = "^(.*)\\[([0-9]+)%\\]".r
              resName match {
                case r(name, percentage) => (name, Conversions.toInt(percentage, 100) / 100)
                case _ => (resName, 1)
              }
            } else {
              if (resName.trim.equals("")) {
                ("", 0)
              } else {
                (resName.trim, 1)
              }
            }
            val resourceTeamV = resourceMap.get(teamName.toLowerCase.trim)
            val workInDays: Double = Conversions.parseDays(cellValues("Work"), 0.0)
            val durationsInDays: Double = Conversions.parseDays(cellValues("Duration"), 0.0)
            val defEffortV = Conversions.toDouble(cellValues("Dev Estimate"), 0.0)
            val bufferedEffortV = Conversions.toDouble(cellValues("Buffered Estimate"), 0.0)
            val startD = Conversions.toDate(cellValues("Start"), new java.sql.Date(System.currentTimeMillis()))
            val finishD = Conversions.toDate(cellValues("Finish"), new java.sql.Date(System.currentTimeMillis()))
            val projectImportRecord = ProjectImport(r, predecessorsValue,
              isDisabled, programO,
              isCIDV, isAnchorV,
              priorityV, cellValues("CID Inventory File Name"), cellValues("Task Name"),
              teamName, numberDevs, defEffortV, bufferedEffortV,
              workInDays, durationsInDays,
              startD, finishD,
              Conversions.parsePercent(cellValues("% Complete"), 0.0) /*,notesS,parsePercent(cells(percentWorkComplete, 0.0 ) */ ,
              resourceTeam = resourceTeamV
            )
            Some(projectImportRecord)
          }
          else {
            Logger.error(s"ProjectXLSImport - Row $row is null ?")
            None
          }
        }
        wb.close()
        val features = convertToFeatures( projects.toList)

        if ( features.size< 10 ) {
          Left("Project file has less than 10 features. not re-populating")
        } else {
          Right(features)
        }

      }
      //Left("TBD")
    }
  }

  def convertToFeatures(projects:List[ProjectImport]): List[FeatureImport] = {
    @tailrec
    def convertToFeatures(features: List[FeatureImport], currentFeature: Option[FeatureImport], currentProjects: List[ProjectImport]): List[FeatureImport] = {

      currentProjects match {
        case Nil => currentFeature match {
          case None => features
          case Some(f: FeatureImport) => features ++ List(f)
        }
        case h :: t =>
          if (h.task.startsWith(" ")) {
            val cf = currentFeature match {
              case None => FeatureImport(h.task.trim, h.program, h.isCID, h.isAnchor, h.priority, h.start, h.finish, List.empty)
              case Some(c: FeatureImport) => c
            }
            convertToFeatures(features, Some(cf.copy(projects =  cf.projects ++ List(h.copy(task=h.task.trim)),
              start = Conversions.dateMin(cf.start,h.start),
              finish = Conversions.dateMax(cf.finish,h.finish))), t)
          } else {
            val newFeature = FeatureImport(h.task.trim, h.program, h.isCID, h.isAnchor, h.priority, h.start, h.finish, List.empty)
            if (currentFeature.isDefined) {
              convertToFeatures(features ++ List(currentFeature.get), Some(newFeature), t)
            } else {
              convertToFeatures(features, Some(newFeature), t)
            }
          }
      }
    }

    convertToFeatures(List.empty, None, projects)
  }

}
