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

package controllers

import javax.inject.Inject
import com.typesafe.config.ConfigFactory
import play.Logger
import play.api.Logging
import play.api.libs.typedmap.TypedKey
import play.api.mvc._
import play.shaded.ahc.org.asynchttpclient.netty.handler.intercept.Unauthorized401Interceptor
import utl.{LDAP, basicAuth}

import scala.concurrent.{ExecutionContext, Future}

object LDAPAuthAttrs {
  val UserName: TypedKey[String] = TypedKey("userName")
}

case class OLDLDAPAuthAction[A] (action: Action[A])  extends Action[A] with Logging {
  lazy val unauthResult: Result = Results.Unauthorized.withHeaders(("WWW-Authenticate", "Basic realm=\"use your Digital River Login please\""))

  def apply(request: Request[A]): Future[Result] = {
    request.headers.get("authorization").foreach { basicAuthHeader =>
      basicAuth.decodeBasicAuth(basicAuthHeader) match {
        case Some((user, pass)) =>
          val enableAuth = ConfigFactory.load().getBoolean("auth.enable")
          if (enableAuth) {
            val res: Boolean = new LDAP().authenticateAccount(user, pass)
            if (res) {
              return action( request.withAttrs( request.attrs.updated(LDAPAuthAttrs.UserName,user) ))
            } else {
              logger.error("XXX LDAP user/password fail")
            }
          } else {
            logger.error("XXXXX AUTH IS CURRENTLY DISABLED!")
            return action( request.withAttrs( request.attrs.updated(LDAPAuthAttrs.UserName,user) ))
          }

        case _ => ;
      }
    }
    Future.successful(unauthResult)
  }

  override  def parser: BodyParser[A] = action.parser
  override def executionContext: ExecutionContext = action.executionContext
}

case class LDAPAuthAction[A] (action: Action[A])  extends Action[A] {
  lazy val unauthResult: Result = Results.Unauthorized.withHeaders(("WWW-Authenticate", "Basic realm=\"use your Digital River Login please\""))

  def apply(request: Request[A]): Future[Result] = {
    request.session.get("userId") match {
      case Some(user) => action(request)
      case None => Future.successful( Results.Redirect( routes.HomeController.login()))
    }
  }

  override  def parser: BodyParser[A] = action.parser
  override def executionContext: ExecutionContext = action.executionContext
}

case class LDAPAuthPermission[A] (permission:String)(action: Action[A])  extends Action[A] with Logging {
  lazy val unauthResult: Result = Results.Unauthorized.withHeaders(("WWW-Authenticate", "Basic realm=\"use your Digital River Login please\""))

  def apply(request: Request[A]): Future[Result] = {
    request.session.get("userId") match {
      case Some(user) => if ( LDAPAuth.hasPermission(permission)(request)) {
        action(request)
      } else {
        logger.warn(s"User:$user - missing permission $permission ${request.uri}")
        Future.successful(Results.Unauthorized(views.html.page_403("You don't have permission for that")))
      }

      case None => Future.successful( Results.Redirect( routes.HomeController.login()))
    }
  }

  override  def parser: BodyParser[A] = action.parser
  override def executionContext: ExecutionContext = action.executionContext
}


object LDAPAuth {
  def getUser()(implicit request: Request[_]):Option[String] = {
   // request.attrs.get(LDAPAuthAttrs.UserName)
    request.session.get("userId")
  }
  def hasPermission(perm:String)(implicit request:Request[_]): Boolean = {
    val isAdmin = request.session.get("isAdmin") match {
      case Some(x) => x.equalsIgnoreCase("Y")
      case None => false
    }
    if (isAdmin) {
      true
    } else {
      request.session.get(s"perm$perm") match {
        case None => getUser match {
          case None => false
          // TODO if the permission isn't there..look it up. but it means making this a future.
          case Some(user) => false
        }
        case Some(s) => if (s.equalsIgnoreCase("Y")) {
          true
        } else {
          false
        }
      }
    }
  }
}
