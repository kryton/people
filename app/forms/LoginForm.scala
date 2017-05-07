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

package forms

import offline.Tables
import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by iholsman on 4/2/2017.
  * All Rights reserved
  */
object LoginForm {

  /**
    * A play framework form.
    */
  val form = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "rememberMe" -> boolean
    )(Data.apply)(Data.unapply)
  )

  /**
    * The form data.
    *
    * @param username The username of the user.
    * @param password The password of the user.
    * @param rememberMe Indicates if the user should stay logged in on the next visit.
    */
  case class Data(
                   username: String,
                   password: String,
                   rememberMe: Boolean)
}

object KRForm {
  val form = Form(
    mapping(
      "keyResult" -> text(maxLength = 100),
      "description" -> optional(text)
    )(Data.apply)(Data.unapply)
  )
  case class Data(keyResult:String, description:Option[String])
}

object ObjectiveForm {
  val form = Form(
    mapping(
      "objective" -> text(maxLength = 100)
    )(Data.apply)(Data.unapply)
  )
  case class Data(objective:String)
}

object KudosForm{

  val form = Form(
    mapping(
      "from" -> text,
      "to" -> text,
      "message" -> text
    )(Data.apply)(Data.unapply)
  )
  case class Data(from: String, to: String, message: String)

}

object ManagedClientProductFeatureForm {
  val form = Form(
    mapping(
      "featureId" -> number,
      "managedClientId" -> nonEmptyText,
      "allocation" -> bigDecimal
    )(Data.apply)(Data.unapply)
  )
  case class Data(featureId:Int, managedClientId:String, allocation:BigDecimal )
}

object ProductFeatureFlagForm {
  val form = Form(
    mapping(
      "featureId" -> number,
      "featureFlagId" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
  case class Data(featureId:Int, featureFlagId:String )
}
