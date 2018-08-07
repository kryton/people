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

import play.api.data.Forms.{mapping, optional, text}
import play.api.data.Form
import play.api.data.Forms._

/**
  * Created by iholsman on 9/24/2017.
  * All Rights reserved
  */
object AuthorizationTypeForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "description" -> optional(text)
    )(Data.apply)(Data.unapply)
  )
  case class Data(name:String, description:Option[String])
}

object ReleaseTypeForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "emailalias" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
  case class Data(name:String, emailAlias:String)
}

object ReleaseAuthPeopleForm {
  val form = Form(
    mapping(
      "releasetypeid" -> number(min=1),
      "releaseauthoritytypeid" -> number(min=1),
      "login" -> nonEmptyText,
      "isprimary" -> boolean
    )(Data.apply)(Data.unapply)
  )
  case class Data(releasetypeid:Int, releaseauthoritytypeid:Int, login:String, isprimary:Boolean )
}
