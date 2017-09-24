package forms

import java.sql.Date

/**
  * Created by iholsman on 9/23/2017.
  * All Rights reserved
  */

case class KudosExt(
                     fromLogin: String,
                     toLogin: String,
                     fromPerson: Option[String],
                     toPerson: Option[String],
                     dateAdded: Date,
                     feedback: String,
                     headShotFrom: String,
                     headShotTo: String
                   )
