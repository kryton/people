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
import offline.Tables
import offline.Tables.{KudostoRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.people.EmpRelationsRowUtils._
import org.abstractj.kalium.crypto.Random
import play.api.data.Form
import play.api.{Configuration, Environment, Logger, Mode}
import play.api.i18n.I18nSupport
import play.api.libs.Crypto
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.libs.mailer.{Email, MailerClient}
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
   configuration: Configuration,
   mailerClient: MailerClient
   // resourcePoolRepository: ResourcePoolRepository

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  protected val returnURL: String = ConfigFactory.load().getString("kudos.returnURL")
  protected val mailReceipient: Boolean = ConfigFactory.load().getBoolean("kudos.emailRecipient")
  protected val emailDomain: String = ConfigFactory.load().getString("kudos.emailDomain")
  protected val offlineHostname: String = ConfigFactory.load().getString("kudos.hostname")

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
      }.flatMap(identity)
  }

  def id( ID:Long): Action[AnyContent] = Action.async { implicit request =>
    user.isKudosAdmin(LDAPAuth.getUser()).map { isAdmin =>
      kudosToRepo.find(ID, isAdmin ).map {
        case Some(kudos: KudostoRow) =>
          val fromF = employeeRepo.findByLogin(kudos.fromperson)
          val toF = employeeRepo.findByLogin(kudos.toperson)
          (for {
            from <- fromF
            to <- toF
          } yield (from, to))
            .map { x =>
              Ok(views.html.shoutout.id(ID, kudos, x._1, x._2, isAdmin ))
            }
        case None => Future.successful(NotFound(views.html.page_404("Shoutout ID not found")))
      }.flatMap(identity)
    }.flatMap(identity)
  }

  def testAuth() = Action.async { implicit request =>
    Future.successful(Ok(views.html.shoutout.testAuth(forms.KudosForm.form)))
  }

  def genKudosEmail() = Action.async { implicit request =>
    forms.KudosForm.form.bindFromRequest.fold (
      form => {
        Future.successful(BadRequest("I don't understand that request"))
      },
      data => {
        if ( data.from.contentEquals(data.to)) {
          Future.successful(BadRequest("Can't compliment yourself"))
        } else {
          (for {
            f <- employeeRepo.findByLogin(data.from)
            t <- employeeRepo.findByLogin(data.to)
            mgr <- employeeRepo.manager(data.to)
            admins <- employeeRepo.findByLogin(user.kudosAdmins)
          } yield( f,t, admins,mgr)).map{ x =>
            x._1 match {
              case Some(from) => x._2 match {
                case Some(to) =>
                  val now: java.sql.Date = new java.sql.Date(System.currentTimeMillis())
                  val kudos = KudostoRow(id=0,
                    fromperson=from.login.toLowerCase,
                    toperson=to.login.toLowerCase,
                    dateadded = now,
                    feedback = data.message,
                    rejected = true,
                    rejectedby = Some("SYSTEM"),
                    rejectedon = Some(now),
                    rejectedreason = Some("AwaitingAuth"))
                  kudosToRepo.insert(kudos).map { newKudos =>
                    val cc: Seq[String] = x._4 match {
                      case None => Seq.empty[String]
                      case Some(mgr) => Seq(s"${mgr.login}@$emailDomain")
                    }
                    val crypt = encryptit(newKudos.id.toString)
                    val url = routes.KudosController.authKudos(crypt.nonce,crypt.cipher).url
                    val bodyText = views.html.shoutout.authEmailText(newKudos,from,to,offlineHostname, url, x._3, emailDomain )
                    val bodyHTML = views.html.shoutout.authEmail(newKudos,from,to, offlineHostname,url, x._3, emailDomain )
                    val email: Email = Email(
                      subject=s"Authorization Required: for ${to.fullName}",
                      from= s"Shoutout Admin <Shoutout-noreply@$emailDomain>",
                      to = Seq(s"${from.login}@$emailDomain"),
                      cc = cc,
                      //cc = x._3.map( f => s"${f.login}@$emailDomain" ),
                      bodyText = Some(bodyText.body),
                      bodyHtml = Some(bodyHTML.body)
                    )
                    mailerClient.send(email)
                    Redirect(returnURL)
                  }

                case None => Future.successful(BadRequest("can't find person you are sending it to"))
              }
              case None => Future.successful(BadRequest("Can't find person you are sending it from"))
            }
          }.flatMap(identity)
        }
      }
    )

  //  Future.successful(Ok("TBD"))
  }

  case class Encrypted( nonce:String, cipher:String)

  protected def encryptit(toEncrypt:String) = {

    val nonce = Nonce.createNonce()
    val rawData = ("42|" + toEncrypt).getBytes(StandardCharsets.UTF_8)
    val cipherText = box.encrypt(nonce.raw, rawData)

    val nonceHex = encoder.encode(nonce.raw)
    val cipherHex = encoder.encode(cipherText)
    Encrypted(nonceHex,cipherHex)
  }

  def genKudos(toEncyrpt:String) = Action.async { implicit request =>
    Logger.error("Warning. genKudos should not be run in production. please leave this route commented")
    if ( environment.mode == Mode.Prod  ) {
      Future.successful(InternalServerError("This should not be run in production"))
    } else {
      val res= encryptit(toEncyrpt)
      Future.successful(Ok(routes.KudosController.authKudos(res.nonce, res.cipher).toString))
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

    if (parts.length != 2 ) {
      Future.successful(BadRequest(s"Invalid message. I was expecting 2 parts\n$decrypted"))
    } else {
      if (parts(0) != "42") {
        Future.successful(BadRequest("Invalid message. Bad cookie"))
      } else {

        kudosToRepo.find(parts(1).toLong,isAdmin = true).map{
          case Some(k) =>
            if (k.rejected) {
              (for{
                kO <- kudosToRepo.update(k.id,
                  k.copy( rejected = false,rejectedby = None, rejectedreason = None, rejectedon = None)
                )
                f <- employeeRepo.findByLogin(k.fromperson)
                t <- employeeRepo.findByLogin(k.toperson)
                admins <- employeeRepo.findByLogin(user.kudosAdmins)

              } yield(f,t,kO,admins)).map{ xx  =>
                // TODO get these from LDAP
                val fromEmail = xx._1 match {
                  case Some(x) => s"${x.fullName} <${x.login}@$emailDomain>"
                  case None =>s"${k.fromperson} <${k.fromperson}@$emailDomain>"
                }
                val toEmail = xx._2 match {
                  case Some(x) => s"${x.fullName} <${x.login}@$emailDomain>"
                  case None =>s"${k.toperson} <${k.toperson}@$emailDomain>"
                }
                val subject = xx._2 match {
                  case Some(x) => s"New Shoutout received for ${x.fullName}"
                  case None => s"New Shoutout received for ${k.toperson }"
                }

                val bodyText = views.html.shoutout.emailText(k,xx._1,xx._2,offlineHostname,  xx._4, emailDomain)
                val bodyHTML = views.html.shoutout.email(k,xx._1,xx._2, offlineHostname, xx._4, emailDomain )
                val email: Email = Email(
                  subject= subject,
                  from= s"Shoutout Admin <Shoutout-noreply@$emailDomain>",
                  to = Seq(toEmail,fromEmail),
                  cc = xx._4.map( f => s"${f.login}@$emailDomain" ),
                  bodyText = Some(bodyText.body),
                  bodyHtml = Some(bodyHTML.body)
                )
                mailerClient.send(email)

                Redirect(routes.KudosController.id(k.id))
              }
            } else {
              Future.successful(Redirect(routes.KudosController.id(k.id)))
            }

          case None => Future.successful( NotFound("Couldn't find the original message"))
        }.flatMap(identity)
       // Ok(parts(1)
      }
    }
  }

  def topX( size:Int, format:Option[String]) = Action.async { implicit request =>
    kudosToRepo.latest(size).map { seq =>
      seq.map { line =>
        val theKudos = line._1
        val fromEmp = line._3
        val toEmp = line._2
        KudosExt(fromLogin = theKudos.fromperson,
          fromPerson = fromEmp match {
            case Some(from) =>Some(from.fullName)
            case None => None
          },
          headShotFrom = routes.HeadshotController.headShot(theKudos.fromperson).url,
          toLogin = line._1.toperson,

          toPerson = Some(toEmp.fullName),
          dateAdded = theKudos.dateadded,
          feedback = theKudos.feedback,

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
  }

  def reject(id:Long, reject:Boolean) = Action.async { implicit request =>
    val userid = LDAPAuth.getUser()
    kudosToRepo.find(id,isAdmin = true).map {
        case Some(k:KudostoRow) => user.isOwnerManagerOrKudosAdmin(k.toperson, userid).map {
          case true =>
            if (reject) {
              kudosToRepo.update(id,
                k.copy(
                  rejected = true,
                  rejectedon = Some(new java.sql.Date(System.currentTimeMillis())),
                  rejectedby = userid,
                  rejectedreason = Some("Website")
                )
              ).map { result =>
                Redirect(routes.KudosController.id(id))
              }
            } else {
              kudosToRepo.update(id,
                k.copy(
                  rejected = false,
                  rejectedby = userid
                )
              ).map { result =>
                Redirect(routes.KudosController.id(id))
              }
            }
          case false =>Future.successful(Unauthorized(views.html.page_403("Not Authorized")))
        }.flatMap(identity)
        case None => Future.successful(NotFound(views.html.page_404("Shoutout not found")))
    }.flatMap(identity)
  //  Future(Ok(""))
  }

  implicit val DateWrites = new Writes[java.sql.Date] {
    def writes( d: java.sql.Date) = Json.toJson(
      d.toString
    )
  }

  // XXX TEMPORARY FIX
  // TODO REVERT THIS BACK
  implicit val KudosExtWrites = new Writes[KudosExt] {
    def writes( k : KudosExt ): JsObject = Json.obj(
      "fromLogin" -> k.fromLogin,
      "fromPerson" -> k.fromPerson,
      "headshotFrom" -> k.headShotFrom,

      "toLogin" -> k.toLogin,
      "toPerson" -> k.toPerson,
      "headshotTo" -> k.headShotTo,

      "dateAdded"-> k.dateAdded,
      "feedback" -> k.feedback
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
