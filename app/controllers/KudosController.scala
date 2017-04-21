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

import java.nio.charset.StandardCharsets
import java.sql.Date
import java.util.Base64
import javax.inject._

import com.typesafe.config.ConfigFactory
import models.people._
import models.product.ProductTrackRepo
import offline.Tables
import offline.Tables.{KudostoRow, MatrixteamRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.people.EmpRelationsRowUtils._
import org.abstractj.kalium.crypto.Random
import play.api.{Configuration, Environment, Logger, Mode}
import play.api.i18n.I18nSupport
import play.api.libs.Crypto
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import util.{LDAP, Nonce, Page, User}
//import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class KudosController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   environment: Environment,
   employeeRepo: EmployeeRepo,
   kudosToRepo: KudosToRepo,
   user: User,
   configuration: Configuration
   // resourcePoolRepository: ResourcePoolRepository

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  private val encoder = org.abstractj.kalium.encoders.Encoder.HEX

  private val box = {
    /*
     * For every service you need, you should specify a specific crypto service with its own keys.   Keeping distinct
     * keys per service is known as the "key separation principle".
     *
     * More specifically, if you have a service which encrypts user information, and another service which encrypts
     * S3 credentials, they should not reuse the key.  If you use the same key for both, then an attacker can cross
     * reference between the encrypted values and reconstruct the key.  This rule applies even if you are sharing
     * the same key for hashing and encryption.
     *
     * Storing key information confidentially and doing key rotation properly is a specialized area. Check out Daniel Somerfield's talk: <a href="https://youtu.be/OUSvv2maMYI">Turtles All the Way Down: Storing Secrets in the Cloud and the Data Center</a> for the details.
     */
    val secretHexConfig: String = ConfigFactory.load().getString("kudos.secret")
    val secretHex  = Base64.getEncoder.encode( secretHexConfig.getBytes())

    new org.abstractj.kalium.crypto.SecretBox(secretHex)
  }
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */



  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
      val shoutoutsF = search match {
        case None => kudosToRepo.all //.map{ emps => Ok(views.html.shoutout.search(Page(emps,page),search)) }
        case Some(searchString) => kudosToRepo.all
        /*

        .map{ emps:Seq[KudostoRow] =>
        if (emps.size == 1 ) {
          Redirect(routes.KudosController.id( emps.head.id))
        } else {
          Ok(views.html.shoutout.search(Page(emps,page),search))
        }
        }
*/
      }
      shoutoutsF.map { shoutouts =>
        if ( shoutouts.size == 1) {
          Future.successful(Redirect( routes.KudosController.id(shoutouts.head.id)))
        } else {

           Future.sequence( shoutouts.map{ shoutout =>
            val from = employeeRepo.findByLogin(shoutout.fromperson)
            val to = employeeRepo.findByLogin(shoutout.toperson)
            (for {
              f <- from
              t <- to
            } yield(f,t)).map { x=> (shoutout, x._1, x._2 )}
          }).map{ s => Ok(views.html.shoutout.search( Page(s,page), search ))}
        }
      //  shoutouts

      }.flatMap(identity)
    //Future(Ok(""))
  }

  def id( ID:Long,page:Int): Action[AnyContent] = Action.async{ implicit request =>
    kudosToRepo.find(ID).map {
      case Some(kudos: KudostoRow) =>
        val fromF = employeeRepo.findByLogin( kudos.fromperson)
        val toF = employeeRepo.findByLogin( kudos.toperson)


        (for {
          from <- fromF
          to <- toF
        } yield (from,to))
        .map { x =>
          Ok(views.html.shoutout.id( ID, kudos, x._1,x._2 ))
        }
      case None => Future.successful(NotFound(views.html.page_404("Shoutout ID not found")))
    }.flatMap(identity)
  }
  def genKudosEmail() = Action.async { implicit request =>
    Future.successful(Ok("TBD"))
  }
  def genKudos(toEncyrpt:String) = Action.async { implicit request =>
    Logger.error("Warning. genKudos should not be run in production. please leave this route commented")
    if ( environment.mode == Mode.Prod  ) {
      Future.successful(InternalServerError("This should not be run in production"))
    } else {

      val nonce = Nonce.createNonce()
      val rawData = ("42|" + toEncyrpt).getBytes(StandardCharsets.UTF_8)
      val cipherText = box.encrypt(nonce.raw, rawData)

      val nonceHex = encoder.encode(nonce.raw)
      val cipherHex = encoder.encode(cipherText)

      Future.successful(Ok(routes.KudosController.authKudos(nonceHex, cipherHex).toString))

    }
  }
  def authKudos(nonceString:String,cryptString: String) = Action.async { implicit request =>
    val nonceHex =nonceString
    val nonce = Nonce.nonceFromBytes(encoder.decode(nonceHex))
    val cipherTextHex = cryptString
    val cipherText = encoder.decode(cipherTextHex)
    val rawData: Array[Byte] = box.decrypt(nonce.raw, cipherText)

    val decrypted: String = new String(  rawData, "UTF-8")
    val parts = decrypted.split("\\|")
    Future.successful(
    if (parts.length != 2 ) {
      BadRequest(s"Invalid message. I was expecting 2 parts\n$decrypted")
    } else {
      if (parts(0) != "42") {
        BadRequest("Invalid message. Bad cookie")
      } else {
        Ok(parts(1))
      }
    }
    )
  }

  def topX( size:Int, format:Option[String]) = Action.async { implicit request =>
    kudosToRepo.latest(size).map { seq =>
      seq.map { line =>
        KudosExt(fromLogin = line._1.fromperson,
          toLogin = line._1.toperson,
          fromPerson = Some(line._2.fullName),
          toPerson = line._3 match {
            case Some(to) => Some(to.fullName)
            case None => None
          },
          dateAdded = line._1.dateadded,
          feedback = line._1.feedback,
          headShotFrom = routes.HeadshotController.headShot(line._1.fromperson).url,
          headShotTo = routes.HeadshotController.headShot(line._1.toperson).url
        )
      }
    }.map { seq =>
      val toFormat = format match {
        case Some(x) => x.toLowerCase
        case None => "json"
      }
      val json = Json.toJson( seq )
      Ok( json).as("application/json;charset=utf-8").withHeaders(("Access-Control-Allow-Origin","*"))
    }
     // ???
  }
  implicit val DateWrites = new Writes[java.sql.Date] {
    def writes( d: java.sql.Date) = Json.toJson(
      d.toString
    )
  }
  implicit val KudosExtWrites = new Writes[KudosExt] {
    def writes( k : KudosExt ): JsObject = Json.obj(
      "fromLogin" -> k.fromLogin,
      "toLogin" -> k.toLogin,
      "fromPerson" -> k.fromPerson,
      "toPerson" -> k.toPerson,
      "dateAdded"-> k.dateAdded,
      "feedback" -> k.feedback,
      "headshotFrom" -> k.headShotFrom,
      "headshotTo" -> k.headShotTo
    )
  }

}

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
