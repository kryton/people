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

import java.io.{File, FileInputStream}
import java.net.URI
import java.nio.charset.CodingErrorAction
import java.nio.file.Path

import models.people.{CostCenterRepo, FunctionalCenterRepo, ProfitCenterRepo}
import models.product.ResourceTeamRepo
import offline.Tables.CostcenterRow
import org.apache.poi.ss.usermodel._
import play.api.Logger
import projectdb.Tables
import projectdb.Tables.ResourcepoolRow
import utl.Conversions

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Codec}

/**
  * Created by iholsman on 6/28/2018.
  * Import a custom file to update matrix team names & members
  *   *
  * All Rights reserved
  */
object ResourceFileImport {
  def importFile(path: Path)(implicit executionContext: ExecutionContext
                             ):Future[ Either[String,Seq[ResourceFileRow]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    val bufferedSource: BufferedSource = scala.io.Source.fromFile(path.toFile)
    Future(importFile(path.toFile))
  }

  def importFile(file: URI)(implicit   executionContext: ExecutionContext
                           ):Future[ Either[String,Seq[ResourceFileRow]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    Future(importFile(new File(file)))
  }

  def importFile(fileRef: File)(implicit executionContext: ExecutionContext): Either[String,Seq[ResourceFileRow]] = {

    val wb: Workbook = WorkbookFactory.create(new FileInputStream(fileRef))
    val sheet: Sheet = wb.getSheet("ALL")
    val rows: Int = sheet.getPhysicalNumberOfRows
    val headerRow: Row = sheet.getRow(3)
    val headerCells: Map[String, Int] = ((0 to 21 /* 'U' */) map { c: Int =>
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

    val fieldnames = Vector("Service Team", "Sub Team Name","Portfolio Group", "Employee ID")

    val numColumns = fieldnames.size

    val headerCheck = headerCells.keySet.intersect(fieldnames.toSet)
    if (headerCheck.size != fieldnames.size) {
      Left(s"Missing Header Field(s): ${fieldnames.toSet.diff(headerCells.keySet).mkString(",")}")
    } else {
      val cellHeaders: Map[Int, String] = headerCells.map { x => x._2 -> x._1 }
      val resourceFileRows: Seq[ResourceFileRow] = (1 until rows).flatMap { r =>
        val row: Row = sheet.getRow(r)
        if (row != null) {
          val cellValues: Map[String, String] = (0 until numColumns).map { column =>
            val posn = fieldnames(column)
            val cellNumber = headerCells(posn)
            val cell: Cell = row.getCell(cellNumber)
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
            posn -> value.trim
          }.toMap

          val serviceArea = cellValues("Service Team")
          val portfolioGroup = cellValues("Portfolio Group")
          val teamName = cellValues("Sub Team Name")
          val employee = cellValues("Employee ID").replaceAll("\\.[0-9]+$","")



          val resourceFileRow = ResourceFileRow(r,
            portfolioGroup = portfolioGroup,
            serviceArea = serviceArea,
            teamName= teamName,
            employee = employee
          )

          Some(resourceFileRow)
        }
        else {
          Logger.error(s"Resource File Import - Row $row is null ?")
          None
        }
      }
      wb.close()

      if (resourceFileRows.size < 10) {
        Left("Resource File Center file has less than 10 rows. not re-populating")
      } else {
        Right(resourceFileRows)
      }
    }
  }
}
case class ResourceFileRow(row:Int,  portfolioGroup:String, serviceArea:String, teamName:String, employee:String)
