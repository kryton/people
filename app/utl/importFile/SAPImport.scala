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

import java.io.File
import java.net.URI
import java.nio.charset.CodingErrorAction
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.Calendar

import models.people.{CostCenterRepo, EmpHistoryRepo, OfficeRepo, PositionTypeRepo}
import offline.Tables.{EmphistoryRow, EmprelationsRow}
import play.api.Logging
import utl.Conversions

import scala.concurrent.{ExecutionContext, Future}
import scala.io.{BufferedSource, Codec, Source}


/**
 * Created by iholsman on 26/09/2014.
 * All Rights reserved
 */
object SAPImport extends Logging {


  private def toDateO(s: String): Option[java.sql.Date] = {
    val simpleDateFormat: SimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy")
    val result = s match {
      case "" => None
      case _ => try {
        Some(new java.sql.Date(simpleDateFormat.parse(s).getTime))

      } catch {
        case e: Exception => println(s"Date:$s - ${e.getMessage}"); None
      }
    }
    result
  }

  def validate(emps: Seq[EmprelationsRow]):Seq[String] = {
    val empMap = emps.map{ x => x.login.toLowerCase -> x}.toMap
    val emptyLogins = emps.filter( x => x.login.trim.contentEquals("")).map{ x=> x.personnumber.toString}
    val duplicateLogins = emps.groupBy(_.login).filter(x => x._2.size > 1).keys
    val numCEOs = emps.filter { x => x.managerid.isEmpty }.flatMap { x => x.login }
    val missingManager = emps.filter{ x => x.managerid.isDefined }
      .filter(x=> !empMap.contains(x.managerid.getOrElse("-")))
      .map{ x => x.login }


    val errors:Seq[String] = Seq(
      if (emptyLogins.isEmpty) { None } else {Some(s"Missing Logins for ${emptyLogins.mkString(",")}") },
      if (duplicateLogins.isEmpty) { None } else {Some( s"Multiple Logins for ${duplicateLogins.mkString(",")}") },
      if (numCEOs.size != 1) { None } else {Some( s"Missing Manager for more than 1 person (should only be CEO) ${numCEOs.mkString(",")}")},
      if (missingManager.isEmpty) { None } else {Some(s"Manager id for ${missingManager.mkString(",")} is missing from file")},
      if ( emps.size > 1000 ) { None} else {Some( s"File seems too small ${emps.size} should have at least 1,400 employees") }
    ).flatten

   errors
  }
  def importFile(path: Path)(implicit costCenterRepo: CostCenterRepo,
                             officeRepo: OfficeRepo,
                             empHistoryRepo: EmpHistoryRepo,
                             positionTypeRepo: PositionTypeRepo,
                             executionContext: ExecutionContext): Future[Seq[EmprelationsRow]] = {
    implicit val codec: Codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)


      val bufferedSource:BufferedSource =  scala.io.Source.fromFile(path.toFile)
      importFile(bufferedSource)
  }

  def importFile(file: URI)(implicit costCenterRepo: CostCenterRepo,
                            officeRepo: OfficeRepo,
                            empHistoryRepo: EmpHistoryRepo,
                            positionTypeRepo: PositionTypeRepo,
                            executionContext: ExecutionContext): Future[Seq[EmprelationsRow]] = {
    implicit val codec: Codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
      importFile(scala.io.Source.fromFile(file))
  }

  private def trimMatchS( in:String ):Option[String] =
    in.trim match {
      case "" => None
      case x:String => Some(x)
    }
  private def trimMatchL( in:String ):Option[Long] =
    in.trim match {
      case "" => None
      case x:String => Conversions.toLongO(x)
    }
  private def trimMatchD( in:String ):Option[java.sql.Date] =
    in.trim match {
      case "" => None
      case x:String => if ( x.equals("00/00/0000")) None else { this.toDateO(x)}
    }

  private def importFile( buffer: BufferedSource
              )( implicit costCenterRepo: CostCenterRepo,
                          officeRepo: OfficeRepo,
                          empHistoryRepo: EmpHistoryRepo,
                          positionTypeRepo: PositionTypeRepo,
                          executionContext: ExecutionContext): Future[Seq[EmprelationsRow]] = {

    val employees:List[QuickBookImport] = buffer.getLines().flatMap {
      line =>
        if (!line.startsWith("Pers No|")) {
          val bits: Array[String] = line.split("\\|", -1) //.drop(1)
          val nickName = trimMatchS(bits(2))
          val mgrNumber = trimMatchL(bits(15))
          val mgr = trimMatchS(bits(16))
          val mgrID = trimMatchS(bits(17)) match {
            case Some(x) => Some(x.toLowerCase)
            case None => None
          }
          val empID = trimMatchS(bits(21)) match {
            case Some(x) => Some(x.toLowerCase)
            case None => None
          }
          val exec = trimMatchS(bits(18))
          val termDate = trimMatchD(bits(20))
          val workstation = trimMatchS(bits(22))
          val workstation2 = trimMatchS(bits(23))
          val employeeType = trimMatchS(bits(24))
          val officeCity = trimMatchS(bits(25))
          val officeStreet = trimMatchS(bits(26))
          val officePOBox = trimMatchS(bits(27))
          val officeRegion = trimMatchS(bits(28))
          val officeZipCode = trimMatchS(bits(29))
          val officeCountry = trimMatchS(bits(30))

          val record = QuickBookImport(Conversions.toLong(bits(0), -1), bits(1).trim, nickName, bits(3).trim, bits(4),
            Conversions.toInt(bits(5), 0) /*CompanyCode */ ,
            bits(6), Conversions.toLong(bits(7), 0) /* costCenter */ , bits(8), bits(9), bits(10),
            bits(11), /*skip EmpSubgroup */ bits(13) /* position */ , bits(14).trim /* agency */ ,
            mgrNumber, mgrID, mgr, exec,
            this.toDateO(bits(19)), termDate /*TerminationDate*/ ,
            empID,
            workstation, workstation2,
            employeeType, officeCity, officeStreet, officePOBox, officeRegion, officeZipCode, officeCountry)
          Some(record)
        } else {
          None
        }
    }.toList

    val byId = employees.map(x => x.personNumber -> x).toMap

    val emptyLogins = employees.filter( x => x.login.isEmpty)
    if (emptyLogins.nonEmpty) {
      emptyLogins.foreach( x => println(s"Missing login for ${x.personNumber} - ${x.firstName} ${x.lastName} - ${x.managerLogin}"))
      throw new Exception("bad import file")
    }

    val v = employees.map(x =>MgrHierarchy (x.managerID, x.login.get, x.personNumber, 0,0,0, x.isFTE))

    val v2 = genTree(v)

    val today =  new java.sql.Date(Calendar.getInstance().getTime.getTime)

    val empRels:Future[Seq[Option[EmprelationsRow]]] = Future.sequence {
      v2.map {
        empWithCounts =>
          val empRecordO = byId.get(empWithCounts.personNumber)
          val mgrRecord = byId.get(empWithCounts.managerID.getOrElse(0L))

          val mgrLogin = mgrRecord match {
            case Some(x) => x.login
            case None => None
          }

          empRecordO match {
            case Some(empRecord) =>
              //    RegularTitles.findOrCreate(empRecord.position)
              val ccRowF = costCenterRepo.findOrCreate(empRecord.costCenter, empRecord.costCenterText)
              val officeF = officeRepo.findOrCreate(empRecord.officeCity, empRecord.officeStreet,
                empRecord.officePOBox, empRecord.officeRegion,
                empRecord.officeZipcode, empRecord.officeCountry)
              (for {
                c <- ccRowF
                o <- officeF
              } yield (c, o)).map { z =>
               
                val empRel: EmprelationsRow = EmprelationsRow(empRecord.personNumber,
                  empRecord.login.get.toLowerCase,
                  empRecord.firstName,
                  empRecord.nickName,
                  empRecord.lastName, mgrLogin,
                  directs = empWithCounts.directs,
                  reports = empWithCounts.reports /* reports */ ,
                  reportscontractor = empWithCounts.reportsContractors /* reportsContractors*/ ,
                  empRecord.companyCode, empRecord.companyCodeName,
                  costcenter = z._1.costcenter,
                  personalarea = empRecord.personalArea, empRecord.personalSubArea, empRecord.employeeGroup,
                  empRecord.position,
                  empRecord.agency,
                  empRecord.executiveName,
                  officelocation = empRecord.officeLocation,
                  officelocation2 = empRecord.officeLocation2,
                  officeid = Some(z._2.id),
                  employeetype = empRecord.employeeType)

                empHistoryRepo.findOrCreate(empRecord.login.get, EmphistoryRow(empRecord.personNumber, empRecord.login.getOrElse("?"),
                  empRecord.firstName, empRecord.nickName, empRecord.lastName, mgrLogin, empRecord.costCenter,
                  empRel.officeid.getOrElse(0), empRecord.employeeType,
                  Some(empRecord.hireRehireDate.getOrElse(today)), Some(today))).map { ignore =>
                  Some(empRel)
                }
              }.flatMap(identity)

            case None => Future.successful(None)
          }

      }
    }
    empRels.map{ empS => empS.flatten.groupBy(_.position).foreach( position => positionTypeRepo.findOrCreate(position._1,None))}
    positionTypeRepo.cleanup
    empRels.map{ x => x.flatten }
  }

  case class MgrHierarchy(managerID:Option[Long],login:String,personNumber:Long,directs:Int,reports:Int,reportsContractors:Int,isFTE:Boolean)

  def genTree(employees: List[MgrHierarchy], i: Int = 1): List[MgrHierarchy] = {
    val mgrs2 = employees.groupBy(_.managerID)
    val mgrs = mgrs2.map(x => x._1 -> (x._2.length + x._2.map(_.directs).sum))
    // Add Direct reports in here
    val noReports = employees.filter(emp => mgrs.get(Some(emp.personNumber)).isEmpty).map(
       x => MgrHierarchy(x.managerID, x.login, x.personNumber,
         x.directs + employees.count( y=> y.managerID.contains(x.personNumber)) +0,
         x.reports,
         x.reportsContractors,
         x.isFTE)
    )
    val reports = employees.filter(emp => mgrs.get(Some(emp.personNumber)).isDefined).map(
      emp2 => MgrHierarchy(emp2.managerID, emp2.login, emp2.personNumber,
        emp2.directs + noReports.count(x => x.managerID.contains(emp2.personNumber)), /* directs */
        emp2.reports + noReports.filter(x => x.managerID.contains(emp2.personNumber)).map(y => y.reports).sum +
                       noReports.count(x => x.managerID.contains(emp2.personNumber)), /*all*/
        emp2.reportsContractors + noReports.filter(x => x.managerID.contains(emp2.personNumber)).map(y => y.reportsContractors).sum +
                       noReports.count(x => x.managerID.contains(emp2.personNumber)&& !x.isFTE),
        emp2.isFTE
        )
    )

    reports.count(x => x.managerID.isDefined) match {
      case 0 => reports ::: noReports
      case _ => genTree(reports, i + 1) ::: noReports
    }
  }
}
