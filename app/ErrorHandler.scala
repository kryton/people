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

import javax.inject.{Inject, Singleton}

//import controllers.AssetsFinder
//import org.webjars.play.WebJarAssets
import play.api.{Environment, Logger}
import play.api.http.{DefaultHttpErrorHandler, HttpErrorHandler}
import play.api.mvc.{ControllerComponents, RequestHeader, Result}
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 4/4/2017.
  * All Rights reserved
  */

@Singleton
class ErrorHandler @Inject()( environment: Environment)(implicit executionContext: ExecutionContext) extends HttpErrorHandler {
  val default = DefaultHttpErrorHandler
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Logger.error(s"ErrorHandler ${environment.mode} $statusCode $message")
    default.onClientError(request, statusCode, message)

  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
      default.onServerError(request, exception)
  }
}
