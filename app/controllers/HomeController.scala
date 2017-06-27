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

import java.util.Calendar
import javax.inject._

import com.typesafe.config.ConfigFactory
import forms.LoginForm
import models.auth.{PermissionRepo, UserPrefRepo}
import models.people._
import models.product.{ProductFeatureRepo, ProductTrackRepo}
import offline.Tables.{EmphistoryRow, EmprelationsRow, KudostoRow}
import util.{LDAP, Page, User}
import play.api._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import play.db.NamedDatabase
import models.people.EmpRelationsRowUtils._
import offline.Tables
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.typedmap.TypedMap
import play.api.mvc.request.RequestAttrKey

import scala.concurrent.{ExecutionContext, Future}
//import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcProfile
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()
  (
    protected val dbConfigProvider: DatabaseConfigProvider,
    @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
    productTrackRepo: ProductTrackRepo,
    officeRepo: OfficeRepo,
    employeeRepo: EmployeeRepo,
    teamDescriptionRepo: TeamDescriptionRepo,
    productFeatureRepo: ProductFeatureRepo,
    kudosToRepo: KudosToRepo,
    empHistoryRepo: EmpHistoryRepo,
    userRepo: UserPrefRepo,
    permissionRepo: PermissionRepo,
    user: User
  )(implicit ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{



  def index = Action.async(bodyParser = Action.parser) {  implicit request =>
    val shoutouts: Future[Seq[(KudostoRow, EmprelationsRow, Option[EmprelationsRow])]] = kudosToRepo.latest(10)
    val newHires: Future[Seq[(EmphistoryRow,EmprelationsRow)]] = empHistoryRepo.latest(10)
    val now = new java.util.Date()
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(now)
    val week  = cal.get( Calendar.WEEK_OF_YEAR )
   // Logger.info(s"Week = $week")
    val empF = request.session.get("userId") match {
      case Some(userid) => employeeRepo.findByLogin(userid)
      case None => Future.successful(None)
    }
    (for {
      s <- shoutouts
      nh <- newHires
      anniversaries <- empHistoryRepo.anniversariesThisWeek( week )
      e <- empF
    } yield( nh,s, anniversaries,e   )).map{ x =>
      Ok(views.html.index(x._1,x._2, x._3.sortBy( y => -y._3), x._4))
    }
  }


  def login() = Action { implicit request =>
    Ok(views.html.login("Login", LoginForm.form)).withNewSession
  }
  def loginInSubmit: Action[AnyContent] = Action.async { implicit request =>
    Future {
     // Logger.info("loginSubmit1")
      LoginForm.form.bindFromRequest.fold(
        form => {
          Logger.info("loginSubmit2 - BadRequest")
          Future.successful(BadRequest(views.html.login("Login", form)))
        },
        data => {
       //   Logger.info("loginSubmit3")
        val enableAuth = ConfigFactory.load().getBoolean("auth.enable")
        val credentials = data.username
        if (enableAuth) {
          if (ldap.authenticateAccount(data.username, data.password)) {
            user.getUserSession(data.username).map {
              session =>
                Redirect(routes.HomeController.index()).withSession(session:_*)//.addingToSession(session:_*)
            }
          } else {
           // Logger.info(s"LoginSubmit ${data.username} Bad Password")
            Future.successful(Redirect(routes.HomeController.login()).flashing("error" -> "invalid.credentials"))
          }
        } else {
          Logger.info(s"LoginSubmit ${data.username} NoAuth")
          user.getUserSession(data.username).map {
            session =>
              Redirect(routes.HomeController.index()).withSession(session:_*)//.addingToSession(session:_*)
          }
        }
      }
      )
    }.flatMap(identity)
  }

  def logout = Action { implicit request =>
   // Ok( views.html.logout("Logged Out") ).withNewSession.addingToSession("userId" ->"u","name" ->"u")
    Redirect(routes.HomeController.logout2() ).withNewSession
  }
  def logout2: Action[AnyContent] = Action.async { implicit request =>
    // implicit val request = request2.withAttrs(TypedMap.empty  )
   // Ok( views.html.logout("Logged Out") ).withNewSession.addingToSession("userId" ->"u","name" ->"u")
    Future.successful(Ok( views.html.logout("Logged Out") ))//.withNewSession) //.addingToSession("userId" ->"u","name" ->"u"))
  }

  def search(  q:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    q match {
      case None => Future( Redirect(routes.HomeController.index()) )
      case Some(searchString) =>

        val employeesF = employeeRepo.search(searchString)
        val teamsF = teamDescriptionRepo.search(searchString)
        val featuresF = productFeatureRepo.search( searchString )
        val resultsF = for {
          employees <- employeesF
          teams <- teamsF
          features <- featuresF
        } yield (employees,teams, features)
        resultsF.map {  results =>
          val tdList:Seq[String] = results._2.groupBy(_.tagtext).keys.toSeq
          if ( results._1.size == 1 && tdList.isEmpty && results._3.isEmpty) {
            Redirect(routes.PersonController.id( results._1.head.login))
          } else {
            if ( tdList.size == 1 && results._1.isEmpty && results._3.isEmpty) {
              Redirect(routes.TeamDescriptionController.id( tdList.head))
            } else {
              if (results._3.size ==1 && results._1.isEmpty && tdList.isEmpty) {
                Redirect(routes.ProductFeatureController.id( results._3.head.id ))
              //  Ok("TBD go to Product Track Page")
              } else {
                Ok( views.html.universalSearch( searchString,
                  Page( results._1),
                  Page( tdList),
                  Page( results._3)) )
              }
            }

          }
        }

      }
  }

}

