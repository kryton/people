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

import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}

object AuthPermissionForm {
  val form = Form(
    mapping(
      "permission" -> text(maxLength = 20),
      "description" -> optional(text)
    )(Data.apply)(Data.unapply)
  )
  case class Data(permission:String, description:Option[String])
}

object AuthRoleForm {
  val form = Form(
    mapping(
      "role" -> text(maxLength = 20),
      "description" -> optional(text),
      "isAdmin" -> boolean
    )(Data.apply)(Data.unapply)
  )
  case class Data(role:String, description:Option[String], isAdmin:Boolean)
}

object AuthRolePermissionForm {
  val form = Form(
    mapping(
      "roleId" -> longNumber,
      "permissionId" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
  case class Data(roleId:Long, permissionId:String )
}

object AuthUserRoleForm {
  val form = Form(
    mapping(
      "roleId" -> longNumber,
      "login" -> text(maxLength=20)
    )(Data.apply)(Data.unapply)
  )
  case class Data(roleId:Long, login:String )
}
