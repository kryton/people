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

import models.people.{CostCenterRepo, FunctionalCenterRepo, ProfitCenterRepo}
import models.product.ResourceTeamRepo
import offline.Tables.CostcenterRow
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
object CostCenterImport {
  def importFile(path: Path)(implicit executionContext: ExecutionContext
                             ):Future[ Either[String,Seq[CostCenterImportRow]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    val bufferedSource: BufferedSource = scala.io.Source.fromFile(path.toFile)
    Future(importFile(path.toFile))
  }

  def importFile(file: URI)(implicit   executionContext: ExecutionContext
                           ):Future[ Either[String,Seq[CostCenterImportRow]]] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    Future(importFile(new File(file)))
  }

  def importFile(fileRef: File)(implicit executionContext: ExecutionContext): Either[String,Seq[CostCenterImportRow]] = {

    val wb: Workbook = WorkbookFactory.create(new FileInputStream(fileRef))
    val sheet: Sheet = wb.getSheet("Master Cost Centers")
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

    val fieldnames = Vector("Cost\nCenter", "Cost Center Short Desc. (20 char)",
      "Functional Area", "P&L Category",
      "Functional Department", "Funct. Dept. Short Name",
      "Profit Center Number",
      "Profit Center Description",
      "Company Description")

    val numColumns = fieldnames.size

    val headerCheck = headerCells.keySet.intersect(fieldnames.toSet)
    if (headerCheck.size != fieldnames.size) {
      Left(s"Missing Header Field(s): ${fieldnames.toSet.diff(headerCells.keySet).mkString(",")}")
    } else {
      val cellHeaders: Map[Int, String] = headerCells.map { x => x._2 -> x._1 }
      val costCenterImportRows: Seq[CostCenterImportRow] = (1 until rows).flatMap { r =>
        val row: Row = sheet.getRow(r)
        if (row != null) {
          val cellValues: Map[String, String] = (0 until numColumns).map { column =>
            val posn = fieldnames(column)
            val cellNumber = headerCells(posn )
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
          val functionalAreaID =  Conversions.toLong(cellValues.getOrElse("Functional Area", "0"), 0)
          if (functionalAreaID == 0 ) {
            Logger.error("ERROR FunctionalID = 0")
            val longV = Conversions.toLong(cellValues.getOrElse("Functional Area", "0"), 0)
          }

          val profitCenterID =  Conversions.toLong(cellValues.getOrElse("Profit Center Number", "0"), 0)
          if (profitCenterID == 0 ) {
            Logger.error("ERROR profitCenterID = 0")
            val longV = Conversions.toLong(cellValues.getOrElse("Profit Center Number", "0"), 0)
          }

          val costCenterImportRow = CostCenterImportRow(r,
            costCenter = Conversions.toLong(cellValues.getOrElse("Cost\nCenter", "0"), 0),
            costCenterShort = cellValues("Cost Center Short Desc. (20 char)"),
            functionalArea =functionalAreaID,
            pAndlCategory = cellValues("P&L Category"),
            functionalDept = cellValues("Functional Department"),
            functionalDeptShortName = cellValues("Funct. Dept. Short Name"),
            profitCenterNumber = profitCenterID,
            profitCenterDesc = cellValues("Profit Center Description"),
            companyShort = cellValues("Company Description")
          )

          Some(costCenterImportRow)
        }
        else {
          Logger.error(s"CostCenter Import - Row $row is null ?")
          None
        }
      }
      wb.close()

      if (costCenterImportRows.size < 10) {
        Left("Cost Center file has less than 10 cost centers. not re-populating")
      } else {
        Right(costCenterImportRows)
      }
    }
  }
}
case class CostCenterImportRow(row:Int, costCenter:Long, costCenterShort:String,
                               functionalArea:Long, pAndlCategory:String, functionalDept:String,
                               functionalDeptShortName:String,
                               profitCenterNumber:Long, profitCenterDesc:String, companyShort:String)
