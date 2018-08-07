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

package models.jira

import play.api.libs.ws.{WSAuthScheme, WSClient, WSRequest}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

/**
  * Created by iholsman on 4/27/2017.
  * All Rights reserved
  */
class Jira(hostname:String, user:String, password:String)(implicit ws:WSClient, executionContext: ExecutionContext ) {

  val jiraApiPref="/rest/api/2/"
  val defaultTimeout: FiniteDuration = 5000.millis

  protected def buildRequest(api:String, query:Option[String]=None): WSRequest = {
    val url = hostname + jiraApiPref + api
    ws.url(url).withRequestTimeout(defaultTimeout)
      .withAuth(user, password, WSAuthScheme.BASIC)
      .withFollowRedirects(true)
      .withHttpHeaders("Accept" -> "application/json")
  }
  def projects = {
    buildRequest("project").get.map{ response =>
      response.status match {
        case 200 =>
      }
    }


  }
}
