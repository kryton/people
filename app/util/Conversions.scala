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

package util

import java.sql.Date
import java.util.Calendar

import org.joda.time.{DateTime, Days}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.Logger

import scala.annotation.tailrec

/**
  * Created by iholsman on 4/15/2017.
  * All Rights reserved
  */
object Conversions {

  val fmt: DateTimeFormatter = DateTimeFormat.forPattern("E MM/dd/yy")

  def dateMin( a:java.sql.Date, b:java.sql.Date) : java.sql.Date = {
    if ( a.getTime > b.getTime) {
      b
    }  else {
      a
    }
  }

  def dateMax( a:java.sql.Date, b:java.sql.Date) : java.sql.Date = {
    if ( a.getTime > b.getTime) {
      a
    }  else {
      b
    }
  }
  def toDate( value:String, default:java.sql.Date): java.sql.Date = {
    toDateO(value).getOrElse(default)
  }

  def toDateO( value:String): Option[java.sql.Date] = {
    if (value.trim.isEmpty || value.trim.equals("")) {
      None
    } else {
      try {
        val dt: DateTime = fmt.parseDateTime(value)
        Some(new java.sql.Date( dt.getMillis ))
      } catch {
        case e: Throwable =>
          Logger.error(s"toDate - $value $e")
          None
      }
    }
  }
  def toDouble(value:String, default:Double) : Double = {
    toDoubleO(value).getOrElse(default)
  }
  def toDoubleO( value:String): Option[Double] = {
    if (value.trim.isEmpty || value.trim.equals("")) {
      None
    } else {
      try {
       Some( value.replaceAll(",","").toDouble)
      } catch {
        case e: Throwable =>
          Logger.error(s"toDouble - $value $e")
          None
      }
    }
  }
  def toInt(value:String,default:Int ):Int = {
   toIntO(value).getOrElse(default)
  }

  def toIntO(value:String ):Option[Int] = {
    try {
      if (value.trim.isEmpty || value.trim.equals("")) {
        None
      } else {
        Some(value.replaceAll(",", "").toDouble.toInt)
      }
    } catch {
      case x:Throwable =>
        Logger.error(s"toInt($value) $x")
        None
    }
  }

  def toLongO(s: String): Option[Long] = {
    try {
      Some(s.trim.toLong)
    } catch {
      case e: Exception => println(s"TOLONG:$s - ${e.getMessage}"); None
    }
  }
  def toLong( s:String, default:Long):Long = {
    val regex = """\.\d+$"""
    val remDecimal = s.replaceAll(regex ,"")
    toLongO(remDecimal).getOrElse(default)
  }

  def toStringO( value:String):Option[String] = {
    if (value.isEmpty || value.trim.equals("")) {
      None
    } else {
      Some(value.trim)
    }
  }
  def toBool(value:String):Boolean = {
    if (value.trim.equalsIgnoreCase("Y")) {
      true
    } else {
      false
    }
  }
  def parseDays(inString:String, default:Double):Double = {
    if (inString.trim.isEmpty || inString.trim.equals("")) {
      default
    } else {
      val workInDays: Double = if (inString contains "day") {
        val r = "^(.*) days?\\??".r
        inString.trim.toLowerCase match {
          case r(days: String) =>
            toDouble(days, default)
          case _ =>
            Logger.info(s"parseDays Regex Failed $inString")
            default
        }
      } else {
        toDouble(inString,default)
      }

      workInDays
    }
  }

  def parsePercent(inString:String, default:Double):Double = {
    if (inString.trim.isEmpty || inString.trim.equals("")) {
      default
    } else {
      val workInDays: Double = if (inString contains "%") {
        val r = "^(.*)%".r
        inString.trim.toLowerCase match {
          case r(days: String) =>
            toDouble(days,default)
          case _ =>
            Logger.info(s"parsePercent -Regex Failed $inString")
            default
        }
      } else {
        toDouble(inString,default)
      }
      workInDays
    }
  }

  def quarterS(quarterDate: Option[Date]): String = {
    quarterDate match {
      case Some(date) =>
        val cal = Calendar.getInstance()
        cal.setTime(date)
        val q: Int = (cal.get(Calendar.MONTH) / 3) + 1
        val y: Int = cal.get(Calendar.YEAR) % 100

        s"Q$q/$y"
      case None => ""
    }
  }

  def quarterD(s: Option[String]): Option[Date] = {
    var cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.DAY_OF_MONTH, 1)

    s match {
      case None =>
        val q: Int = (cal.get(Calendar.MONTH) + 1) / 3
        //    println( s"q$q")
        cal.set(Calendar.MONTH, q * 3)
        Some(new java.sql.Date(cal.getTime.getTime))
      case Some(dQ) =>
        val parts: Array[String] = dQ.split("/")
        if (parts.length != 2) {
          None
        } else {
          try {
            val q = parts(0).substring(1).toInt
            val y = parts(1).toInt + 2000
            if (q < 1 && q > 4) {
              None
            } else {
              cal.set(Calendar.MONTH, (q - 1) * 3)
              cal.set(Calendar.YEAR, y)
              Some(new java.sql.Date(cal.getTime.getTime))
            }
          } catch {
            case e: Exception => None
          }

        }
    }
  }

  def monthRange(from:Date, to:Date): Seq[Date] = {
    @tailrec
    def monthRange(from: DateTime, to: DateTime, acc: Seq[DateTime]): Seq[DateTime] = {
      val fromEOM = from.dayOfMonth().withMaximumValue()
      val fromSOM = from.dayOfMonth().withMinimumValue()
      if (to.isAfter(from)) {
        monthRange(from.plusMonths(1).dayOfMonth().withMinimumValue(), to, acc ++ Seq(fromSOM))
      } else {
        acc
      }
    }

    val fromDT = new DateTime(from.getTime)
    val toDT = new DateTime(to.getTime)
    val res = monthRange(fromDT, toDT, Seq.empty).map {
      x => new java.sql.Date(x.getMillis)
    }
    res
  }
  def explodeDateRange( from:Date, to:Date ): (Int, Seq[(Date, Int)]) = {
    @tailrec
    def explodeDateRange(from: DateTime, to: DateTime, acc: Seq[(DateTime, Int)]): Seq[(DateTime, Int)] = {
      val fromEOM = from.dayOfMonth().withMaximumValue()
      val fromSOM = from.dayOfMonth().withMinimumValue()
      if (to.isAfter(from)) {
        val days = if (to.isAfter(fromEOM)) {
          fromEOM.dayOfMonth().get() - from.dayOfMonth().get()
        } else {
          to.dayOfMonth().get() - from.dayOfMonth().get()
        }
        explodeDateRange(from.plusMonths(1).dayOfMonth().withMinimumValue(), to, acc ++ Seq((fromSOM, days + 1)))
      } else {
        acc
      }
    }

    val fromDT = new DateTime(from.getTime)
    val toDT = new DateTime(to.getTime)
    val res = explodeDateRange(fromDT, toDT, Seq.empty).map {
      x => (new java.sql.Date(x._1.getMillis), x._2)
    }
    val diff = Days.daysBetween(fromDT, toDT).getDays
    (diff, res)

  }

}
