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
import play.api.libs.typedmap.TypedKey
import play.api.mvc._
import util.{LDAP, basicAuth}

import scala.concurrent.{ExecutionContext, Future}

object LDAPAuthAttrs {
  val UserName: TypedKey[String] = TypedKey("userName")
}

case class OLDLDAPAuthAction[A] (action: Action[A])  extends Action[A] {
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
              Logger.error("XXX LDAP user/password fail")
            }
          } else {
            Logger.error("XXXXX AUTH IS CURRENTLY DISABLED!")
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

//object LDAPAuthAction {
//  def apply(action:Action[AnyContent])(implicit executionContext: ExecutionContext) = new LDAPAuthAction(action)
//}

object LDAPAuth {
  def getUser()(implicit request: Request[_]):Option[String] = {
   // request.attrs.get(LDAPAuthAttrs.UserName)
    request.session.get("userId")
  }
}
