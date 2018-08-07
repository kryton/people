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

import java.sql.Date
import javax.inject.Inject

import offline.Tables
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
//import slick.lifted.Tag
//import slick.model.Table


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

class TeamDescriptionRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[TeamdescriptionRow]] =
    db.run(Teamdescription.filter(_.id === id).result.headOption)
  def findTeamForLoginEx(login:String)(implicit employeeRepo: EmployeeRepo):Future[Option[TeamdescriptionRow]] = {
    db.run( Teamdescription.filter(_.login === login ).result.headOption)
  }

  def findTeamForLoginEx(logins:Set[String])(implicit employeeRepo: EmployeeRepo):Future[Option[TeamdescriptionRow]] = {
    db.run( Teamdescription.filter(_.login inSet logins ).result.headOption)
  }
  // TODO use Set function in here
  def findTeamForLogin(login:String)(implicit employeeRepo: EmployeeRepo):Future[Option[TeamdescriptionRow]] = {
    findTeamForLoginEx(login).map{
      case Some(td)=> Future(Some(td))
      case None =>
        val managerTeams:Future[Seq[Option[TeamdescriptionRow]]] = employeeRepo.managementTreeUp(login).map{ seq:Seq[EmprelationsRow] =>
          val x : Seq[Future[Option[TeamdescriptionRow]]] =  seq.map( emp => findTeamForLoginEx(emp.login))
          Future.sequence(x)
        }.flatMap(identity)
        managerTeams.map { seq =>  seq.flatten.headOption }
    }.flatMap(identity)
  }

  def findTeam( team:String ) :Future[Seq[TeamdescriptionRow]] = {
    db.run( Teamdescription.filter(_.tagtext.toLowerCase === team.toLowerCase)
      .join(Emprelations).on( _.login.toLowerCase === _.login.toLowerCase).map(_._1)  .result)
  }

  def findTeamMembers( team:String):Future[Set[EmprelationsRow]] = {
    val qry = Teamdescription.filter( _.tagtext.toLowerCase === team.toLowerCase)
      .join(Emprelations).on(_.login.toLowerCase === _.login.toLowerCase).map(_._2)

    db.run( qry.result ).map { emps =>
      val logins = emps.map(_.login.toLowerCase).toSet
      findTeamMembers_ex( logins,  Future.successful(emps.toSet ))
    }.flatMap(identity)
  }

  def findTeamMembers_ex( managers:Set[String], acc:Future[Set[EmprelationsRow]]):Future[Set[EmprelationsRow]] = {
    managers.toList match {
      case Nil => acc
      case people =>
        //managers.foreach(m => Logger.info(s"findTeamMembers_ex1: $m"))
        val qry = Emprelations.filter(_.managerid inSet people.toSet)
          .joinLeft(Teamdescription) .on(_.login === _.login).filter(_._2.isEmpty ).map(_._1)

        db.run( qry.result ).map { ppl =>
          val mgr:Set[String] = ppl.map( _.login ).toSet
         // mgr.foreach(m => Logger.info(s"findTeamMembers_ex2: $m"))
          findTeamMembers_ex( mgr, acc.map{ list1 => list1 ++ ppl.toSet })
        }.flatMap(identity)
    }

  }

  def search(searchString:String): Future[Seq[TeamdescriptionRow]] = db.run {
    Teamdescription.filter(_.tagtext startsWith searchString)
      .join(Emprelations).on(_.login.toLowerCase === _.login.toLowerCase)
      .map(_._1).sortBy(_.tagtext).result
  }

  /**
    * Get position type, and hire date of each member of the team
    * @param team the name of the team
    * @return a list of Login, Position (DEV/QA/SLT/MGR etc etc ) and date of hire
    */
  def getPositionHireForTeam(team:String): Future[Seq[(EmprelationsRow, String, Option[Date])]] = {
    findTeamMembers(team).map { emps =>
        val empPosition: Seq[(String, List[String])] = emps.map(emp => ( emp.login, emp.position) )
                                                                  .toList
                                                                  .groupBy(_._2).map{ x =>(x._1, x._2.map( _._1)) }.toSeq

        val qryGetPositionAndStart = Emprelations.filter( _.login inSet emps.map(_.login) )
          .joinLeft(Positiontype).on(_.position === _.position)
          .joinLeft(Emphistory).on(_._1.login === _.login )
          .map( x=> ( x._1._1, x._1._2, x._2 ) )

        db.run( qryGetPositionAndStart.result ).map{ rS =>
          rS.map { r =>
            (r._1, r._2 match {
              case Some(pt) => pt.positiontype
              case None => "UNKNOWN"
            }, r._3 match {
              case Some(eH) => eH.hirerehiredate
              case None => None
            })
          }
        }
    }.flatMap(identity)
  }


  def all: Future[Seq[TeamdescriptionRow]] = db.run{
    Teamdescription.join(Emprelations).on(_.login.toLowerCase === _.login.toLowerCase).map(_._1).sortBy(_.tagtext).result
  }

  def upsert( login:String, team:String): Future[Any] =  {
    db.run ( Teamdescription.filter(_.login.toLowerCase === login.toLowerCase).result.headOption).map {
      case Some(td) => update( td.id, td.copy(tagtext = team ))
      case None =>
        val now = new java.sql.Date(System.currentTimeMillis())
        insert( TeamdescriptionRow(id=0,login=login, dateadded = now, tagtext = team ))
    }.flatMap(identity)
  }

  def insert(td: TeamdescriptionRow): Future[TeamdescriptionRow] = db
    .run(Teamdescription returning Teamdescription.map(_.id) += td)
    .map(id => td.copy(id = id))


  def update(id: Long, td:TeamdescriptionRow): Future[Boolean] = {
    db.run(Teamdescription.filter(_.id === id).update( td.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long): Future[Boolean] =
    db.run(Teamdescription.filter(_.id === id).delete) map { _ > 0 }

  def delete(login: String): Future[Boolean] =
    db.run(Teamdescription.filter(_.login === login).delete) map { _ > 0 }
}
