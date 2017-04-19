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

import javax.inject.Inject

import com.typesafe.config.ConfigFactory
import models.people.EmployeeRepo
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/30/2017.
  * All Rights reserved
  */
class User @Inject()(implicit employeeRepo:EmployeeRepo, executionContext: ExecutionContext ) {
  import scala.collection.JavaConversions._
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
  def isOwnerOrKudosAdmin(owner: String, userO:Option[String]): Future[Boolean] = {
    userO match {
      case Some(user) =>
        if (user.toLowerCase == owner.toLowerCase) {
          Future.successful(true)
        } else {
          isKudosAdmin(Some(user))
        }
      case None => Future.successful(false)
    }
  }

   def isOwnerOrACEAwardAdmin(owner: String, userO:Option[String]): Future[Boolean] = {
    userO match {
      case Some(user) =>
        if (user.toLowerCase == owner.toLowerCase) {
          Future.successful(true)
        } else {
          isACEAwardAdmin(Some(user))
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
          Logger.info(s"isAdmin - false - $login")
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

}
