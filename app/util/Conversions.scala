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

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.Logger

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
    toLongO(s).getOrElse(default)
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
}
