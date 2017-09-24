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
