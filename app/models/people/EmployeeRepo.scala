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

package models.people

import java.net.URLDecoder
import javax.inject.Inject

import com.unboundid.ldap.sdk.SearchResultEntry
import offline.Tables
import offline.Tables.OfficeRow
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.dbio.{DBIOAction, Effect}
import slick.jdbc.JdbcProfile
import slick.sql.FixedSqlAction

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class EmployeeRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def findByLogin(login:String):Future[Option[EmprelationsRow]] = db.run(Emprelations.filter(_.login === login.toLowerCase).result.headOption)
  def findByLogin(logins:Set[String]):Future[Seq[EmprelationsRow]] =
    db.run(Emprelations.filter(_.login inSet logins.map(_.toLowerCase)).result)
  def findByCC(costcenter:Long):Future[Seq[EmprelationsRow]] =
    db.run(Emprelations.filter(_.costcenter === costcenter).sortBy(_.lastname).result)

  def findByPersonNumber(ID:Long):Future[Option[EmprelationsRow]] = db.run(Emprelations.filter(_.personnumber === ID).result.headOption)

  def search(searchString:String): Future[Seq[EmprelationsRow]] = db.run{
    if ( searchString.trim.contains(" ")) {
      val parts = searchString.trim.split(" ",2)
      Emprelations.filter { m =>
        ( (  m.lastname startsWith parts(1) ) && ( m.firstname startsWith parts(0) )   )   ||
          ((  m.lastname startsWith parts(1) ) && ( m.nickname startsWith parts(0) ))
      }.sortBy(_.lastname).result
    } else {
      Emprelations.filter { m =>
        (  m.lastname startsWith searchString ) || ( m.firstname startsWith searchString )      ||
          ( m.login startsWith searchString ) || (m.nickname startsWith searchString)
      }.sortBy(_.lastname).result
    }

  }

  def manager(login:String): Future[Option[EmprelationsRow]] = {
    val qry = (for {
      emp <- Emprelations if emp.login === login.toLowerCase
      mgr <- Emprelations if mgr.login === emp.managerid
    } yield mgr).result.headOption
    db.run(qry)
  }

  def managedBy( login:String): Future[Set[EmprelationsRow]] = {
    db.run( Emprelations.filter(_.managerid === login.toLowerCase ).result ).map( _.toSet)
  }

  def managedBy( logins:Set[String]) : Future[Seq[EmprelationsRow]] = {
    db.run( Emprelations.filter( _.managerid inSet logins.map{ _.toLowerCase }).result)
  }

  def managementTreeUp(login:String):Future[Seq[EmprelationsRow]] = {

    def managementTreeUpI(login: String, acc: Future[Seq[EmprelationsRow]]): Future[Seq[EmprelationsRow]] = {
      manager(login.toLowerCase).map {
        case Some(m) => managementTreeUpI(m.login, acc.map { setMgr => setMgr ++ Seq(m) })
        case None => acc
      }.flatMap(identity)
    }
    managementTreeUpI(login, Future(Seq.empty))
  }

  def managementTreeDown(login:String):Future[Set[EmprelationsRow]] = {
    def managementTreeDownI( logins:Future[Set[String]], acc:Future[Set[EmprelationsRow]] ):Future[Set[EmprelationsRow]] = {
      logins.map { l =>
        if (l.isEmpty) {
          acc
        } else {
          val res = managedBy(l).map(_.toSet)
          val loginsNew = res.map(x => x.map(_.login))
          val newAcc = util.Helpers.mergeFutureSets(acc, res)
          managementTreeDownI(loginsNew, newAcc)
        }
      }.flatMap(identity)
    }
    managementTreeDownI(Future(Set(login.toLowerCase)), Future(Set.empty))
  }

  def repopulate(updatedEmployeeList:Seq[EmprelationsRow]): Future[(Set[EmprelationsRow],Int, Set[String])] = {
    val newLogins: Set[String] = updatedEmployeeList.map(_.login.toLowerCase).toSet
    val oldList: Future[Set[String]] = db.run(Emprelations.result).map { emp =>
      emp.map(_.login.toLowerCase).toSet
    }
    val loginData: Map[String, EmprelationsRow] = updatedEmployeeList.map(emp => emp.login.toLowerCase -> emp.copy(login = emp.login.toLowerCase)).toMap

    val terminations: Future[Set[String]] = oldList.map { oldLogins => oldLogins.diff(newLogins) }
    val newHires: Future[Set[EmprelationsRow]] = oldList.map { oldLogins => newLogins.diff(oldLogins).flatMap(login => loginData.get(login)) }
    val updates: Future[Set[EmprelationsRow]] = oldList.map { oldLogins => newLogins.union(oldLogins).flatMap { login => loginData.get(login)} }

    val tF: Future[Iterator[Int]] = terminations.map{set =>
    //  Logger.info(s"Terms ${set.size}")
     // set.foreach(x =>  Logger.info(s"Term $x"))
      delete( set)}.map(x => Future.sequence(x)).flatMap(identity)
    val uF: Future[Seq[Int]] = updates.map{set =>
      //Logger.info(s"Matches ${set.size}")
      insertOrUpdate(set)
    }.flatMap(identity)
    val iF: Future[Seq[Int]] =  newHires.map{set =>
      //Logger.info(s"New Hires ${set.size}")
     // set.foreach(x =>  Logger.info(s"New ${x.login}"))
      insertOrUpdate(set)
    }.flatMap(identity)
    val resultF = for {
      tx <- tF
      u <- uF
      ix <- iF
      t <- terminations
      i <- newHires
    } yield (i, u, t, ix, tx )

    resultF.map { x =>
      Logger.info(s"Insert F ${x._1.size}")
      Logger.info(s"Delete F ${x._3.size}")
      (x._1, x._2.sum, x._3)
    }


  }


  def insert(er: EmprelationsRow) ={
    val erRec = er.copy( login = er.login.toLowerCase )
    db.run(Emprelations += erRec)
    .map(login => erRec )
  }

  def insert(ers: Set[EmprelationsRow], batchSize:Int = 100): Iterator[Future[Option[Int]]] ={
    val erRec = ers.map( emp => emp.copy( login = emp.login.toLowerCase ))
    ers.grouped(batchSize).map( group => Emprelations ++= group ).map( group=> db.run(group) )
    //db.run(Emprelations ++= erRec)
    //.map(login => erRec )
  }

  def insertOrUpdate( ers:Set[EmprelationsRow], batchSize:Int = 100 ): Future[Vector[Int]] = {
    val erRec = ers.map( emp => emp.copy( login = emp.login.toLowerCase )).toSeq

    val actions:Vector[DBIO[Int]] = erRec.map { x =>
       Emprelations.insertOrUpdate(x)
    }.toVector
    db.run( DBIO.sequence(actions ))
  }

  def update(login:String, er:EmprelationsRow): Future[Boolean] = {
    db.run(Emprelations.filter(_.login.toLowerCase === login.toLowerCase).update( er.copy(login = login.toLowerCase))) map { _ > 0 }
  }


  def delete(login:String): Future[Boolean] =
    db.run(Emprelations.filter(_.login.toLowerCase === login.toLowerCase).delete) map { _ > 0 }

  def delete(logins:Set[String], batchSize:Int =100 ): Iterator[Future[Int]] = {
     logins.grouped(batchSize).map( group => db.run ( Emprelations.filter(_.login inSet group).delete ) )
  }
}

object EmpRelationsRowUtils {

  import offline.Tables.EmprelationsRow

  implicit class EmpRelationsRowAddIn(val emp: EmprelationsRow) {
    def fullName: String = emp.nickname match {
      case Some(nick) => s"$nick ${emp.lastname}"
      case None => s"${emp.firstname} ${emp.lastname}"
    }
    def isContractor: Boolean = {
      emp.employeegroup.equalsIgnoreCase("External employee")
    }
    def interOfficePhone( LDAPPerson:Option[SearchResultEntry]=None)(implicit ldap:util.LDAP):Option[String] = {
      val LDAPPerson2: Option[SearchResultEntry] = LDAPPerson //ldap.getPersonByAccount(URLDecoder.decode(emp.login, "UTF-8")).headOption
      LDAPPerson2 match {
        case Some(ldapPerson) => Option( ldapPerson.getAttributeValue("otherTelephone"))
        case None => None
      }
    }
    def deskPhone( LDAPPerson:Option[SearchResultEntry]=None)(implicit ldap:util.LDAP):Option[String] = {
      val LDAPPerson2: Option[SearchResultEntry] = LDAPPerson //ldap.getPersonByAccount(URLDecoder.decode(emp.login, "UTF-8")).headOption
      LDAPPerson2 match {
        case Some(ldapPerson) => Option( ldapPerson.getAttributeValue("telephoneNumber"))
        case None => None
      }
    }
    def eMail( LDAPPerson:Option[SearchResultEntry]=None)(implicit ldap:util.LDAP):Option[String] = {
      val LDAPPerson2: Option[SearchResultEntry] = LDAPPerson //ldap.getPersonByAccount(URLDecoder.decode(emp.login, "UTF-8")).headOption
      LDAPPerson2 match {
        case Some(ldapPerson) => Option( ldapPerson.getAttributeValue("mail"))
        case None => None
      }
    }
    def cellPhone( LDAPPerson:Option[SearchResultEntry]=None)(implicit ldap:util.LDAP):Option[String] = {
      val LDAPPerson2 = LDAPPerson // ldap.getPersonByAccount(URLDecoder.decode(emp.login, "UTF-8")).headOption
      LDAPPerson2 match {
        case Some(ldapPerson) => Option( ldapPerson.getAttributeValue("mobile"))
        case None => None
      }
    }

    def getLDAPPerson()(implicit ldap:util.LDAP): Option[SearchResultEntry] = {
      ldap.getPersonByAccount(URLDecoder.decode(emp.login, "UTF-8")).headOption
    }

  }

}
