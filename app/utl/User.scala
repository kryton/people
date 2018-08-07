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

package utl

import javax.inject.Inject

import com.typesafe.config.ConfigFactory
import com.unboundid.ldap.sdk.SearchResultEntry
import controllers.routes
import models.auth.{PermissionRepo, UserPrefRepo, UserRepo}
import models.people.EmployeeRepo
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/30/2017.
  * All Rights reserved
  */
class User @Inject()(implicit employeeRepo:EmployeeRepo,
                     userRepo:UserRepo,
                     permissionRepo: PermissionRepo,
                     userPrefRepo:UserPrefRepo,
                     executionContext: ExecutionContext ) {
  import scala.collection.JavaConversions._
  val specialLogo: Set[String] = ConfigFactory.load().getStringList("offline.speciallogo").toList.map(x => x.toLowerCase).toSet
  val catLover: Set[String] = ConfigFactory.load().getStringList("offline.cats").toList.map(x => x.toLowerCase).toSet
  val admins: Set[String] = ConfigFactory.load().getStringList("scenario.admins").toList.map(x => x.toLowerCase).toSet
  val adminsHierarchy: Set[String] = ConfigFactory.load().getStringList("scenario.admins_hierarchy").toList.map(x => x.toLowerCase).toSet
  val kudosAdmins: Set[String] = ConfigFactory.load().getStringList("kudos.admins").toList.map(x => x.toLowerCase).toSet
  val aceAwardAdmins: Set[String] = ConfigFactory.load().getStringList("aceaward.admins").toList.map(x => x.toLowerCase).toSet

  def isOwnerOrAdmin(owner: String, userO:Option[String]): Future[Boolean] = {
    userO match {
      case Some(user) =>
        if (user.toLowerCase == owner.toLowerCase) {
          Future.successful(true)
        } else {
          isAdmin(Some(user))
        }
      case None => Future.successful(false)
    }
  }
  def isSpecialLogo(userO:Option[String]):Future[Boolean] = {
   // Logger.info(s"LIGHT ${specialLogo.mkString(",")}"  )
    userO match {
      case None =>Future.successful(false)
      case Some(user) => Future.successful(specialLogo.contains(user.toLowerCase))
    }
  }
  def isACatLover(userO:Option[String]):Future[Boolean] = {
   // Logger.info(s"LIGHT ${specialLogo.mkString(",")}"  )
    userO match {
      case None =>Future.successful(false)
      case Some(user) => Future.successful(catLover.contains(user.toLowerCase))
    }
  }

  def isOwnerManagerOrKudosAdmin(owner: String, userO:Option[String]): Future[Boolean] = {
    userO match {
      case Some(user) =>
        if (user.toLowerCase == owner.toLowerCase) {
          Future.successful(true)
        } else {
          isKudosAdmin(Some(user)).map {
            case true =>  Future.successful(true)
            case false => inManagementTree(user,owner.toLowerCase)
          }.flatMap(identity)
        }
      case None => Future.successful(false)
    }
  }

  def isOwnerManagerOrAdmin(owner: String, userO:Option[String]): Future[Boolean] = {
    userO match {
      case Some(user) =>
        if (user.toLowerCase == owner.toLowerCase) {
          Future.successful(true)
        } else {
          isAdmin(Some(user)).map {
            case true =>  Future.successful(true)
            case false => inManagementTree(user,owner.toLowerCase)
          }.flatMap(identity)
        }
      case None => Future.successful(false)
    }
  }
   def isOwnerManagerOrACEAdmin(owner: String, userO:Option[String]): Future[Boolean] = {
    userO match {
      case Some(user) =>
        if (user.toLowerCase == owner.toLowerCase) {
          Future.successful(true)
        } else {
          isACEAwardAdmin(Some(user)).map {
            case true =>  Future.successful(true)
            case false => inManagementTree(user,owner.toLowerCase)
          }.flatMap(identity)
        }
      case None => Future.successful(false)
    }
  }

  /**
    * Is 'user' part of the management chain of 'owner' ?
    * @param user the user who is logged in
    * @param owner the owner of the record
    * @return true if it is
    */
  def inManagementTree(user:String,owner:String): Future[Boolean] = {
    userOrManager(owner).map{ users:Seq[String] => users.exists(_.equalsIgnoreCase(user)) }
  }

  /**
    * Is 'user' part of a distribution group?
    * @param user the user in question
    * @param group the group in question
    * @param ldap LDAP server to search
    * @return true if it is
    */
  // XXX still in development
  def inDistGroup(user:String, group:String)(implicit ldap:LDAP): Boolean = {

    Logger.warn("Warning. this feature is untested, and doesn't work for shared mailboxes")
    ldap.getPersonByAccount(user).headOption match {
      case Some(person) =>
        val groups: List[String] = person.getAttributeValues("memberof").toList
        groups.exists(p  => p.equalsIgnoreCase(s"CN=$group,"))

      case None=> false
    }
  }

  /**
    * return a set of users who are in management chain (including this user)
    * @param user the user login.
    * @return the Set of managers
    */

  def userOrManager(user: String): Future[Seq[String]] = {
    employeeRepo.managementTreeUp(user).map(sE => sE.map(_.login))
  }


  /**
    * Is the user an admin ?
    * @param loginO ths login.
    * @return true if they are, false if they are not
    *
    */
  def isAdmin(loginO:Option[String]): Future[Boolean] = {
    loginO match {
      case Some(login) =>
        //Logger.info(s"isAdmin - $login")
        if ( admins.contains(login.toLowerCase) ) {
       //   Logger.info(s"isAdmin - true - $login")
          Future.successful(true)
        } else {
          Logger.debug(s"isAdmin - false - $login")
          loginInAdminHierarchy(login.toLowerCase)
        }
      case None => Future.successful(false)
    }
  }

  /**
    * Is the user an admin for the Kudos Section?
    * @param loginO ths login.
    * @return true if they are, false if they are not
    *
    */
  def isKudosAdmin(loginO:Option[String]): Future[Boolean] = {
    loginO match {
      case None => Future.successful(false)
      case Some(login) =>Future.successful(kudosAdmins.contains(login))
    }
  }
  /**
    * Is the user an admin for the Kudos Section?
    * @param loginO ths login.
    * @return true if they are, false if they are not
    *
    */
  def isACEAwardAdmin(loginO:Option[String]): Future[Boolean] = {
    loginO match {
      case None => Future.successful(false)
      case Some(login) => Future.successful( aceAwardAdmins.contains(login))
    }
  }
  /**
    * Is the user login in the reporting hierarchy of 'admins groups'. Eg. does this person sit in the HR area?
    * @param login the login. usually the person attempting to login
    * @return true if they are, false if they are not
    */
  def loginInAdminHierarchy(login:String): Future[Boolean] = {
     Future.sequence(adminsHierarchy.map(manager => inManagementTree(login,manager))).map{ s:Set[Boolean] => s.contains(true)}
  }

  def getUserSession(login:String): Future[Seq[(String, String)]] = {
    import models.people.EmpRelationsRowUtils._
    (for{
      emp <- employeeRepo.findByLogin(login)
      isSpecial <- this.isSpecialLogo( Some( login))
      prefs <- userPrefRepo.userPrefs(login).map( s=> s.map(_._2.code))
      isCatLover <- this.isACatLover(Some(login))
      isAdmin <- this.isAdmin(Some(login))
      perms <- permissionRepo.permissionsForUser(login)

    } yield (emp, isSpecial,prefs,isCatLover,isAdmin,perms )).map { x =>
      //  Logger.info(s"CATS = ${x._4.toString}")
      val isAdmin = if ( x._5 ) { "Y" } else {"N"}
      val permKeys = x._6.map( p => s"perm${p.permission}" -> "Y")
      val session: Seq[(String, String)] = Seq("userId"->login, "speciallogo" -> x._2.toString,"cats" -> x._4.toString,"isAdmin" -> isAdmin) ++
        x._3.map(y=> y -> "Y") ++
        permKeys

      x._1 match {
        case Some(emp) => session ++ Seq( "name" -> emp.fullName)
        case None => session ++ Seq( "name" -> "?Not Found?")
      }
    }
  }
}
