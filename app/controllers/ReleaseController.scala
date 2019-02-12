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

import javax.inject._
import models.product._
import play.api.{Logger, Logging}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import offline.Tables
import offline.Tables.{ReleaseauthorizationtypeRow, ReleasetypeRow, ReleasetypeauthorizationpeopleRow}
import slick.jdbc.JdbcProfile
import utl.{Conversions, LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ReleaseController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
//   @NamedDatabase("offline") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   releaseAuthorizationTypeRepo: ReleaseAuthorizationTypeRepo,
   releaseAuthorizationRepo: ReleaseAuthorizationRepo,
   releaseTypeAuthorizationPeopleRepo: ReleaseTypeAuthorizationPeopleRepo,
   releaseTypeRepo: ReleaseTypeRepo,
   releaseRepo: ReleaseRepo,
   user: User

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport with Logging {


  def release(id:Int) = Action.async{ implicit request =>
    (for {
      rel <- releaseRepo.find(id)
      ra <- releaseAuthorizationRepo.findByRelease(id)
      rat <- releaseAuthorizationTypeRepo.all.map{ seq => seq.map{ row => (row.id.toString, row.name)}}
    } yield (rel,ra,rat)).map { x =>
      x._1 match {
        case Some(rel) =>
          (for {
            rtO <- releaseTypeRepo.find(rel.releasetypeid)
            rtP <- releaseTypeAuthorizationPeopleRepo.findByReleaseType(rel.releasetypeid)
          } yield(rtO, rtP)).map { xx =>
            val rtO = xx._1
            val authTypesRequired = xx._2
            val ra = x._2
            val rat = x._3
            val authsRecvd = ra.map{ row => row._1.releaseauthorityid}.toSet
            val missingAuth: Map[ReleaseauthorizationtypeRow, Seq[(ReleasetypeauthorizationpeopleRow, ReleaseauthorizationtypeRow)]] = authTypesRequired.groupBy( _._2).filterNot { p =>
              authsRecvd.contains(p._1.id)
            }
            Ok(views.html.release.rel.id(id, rel, rtO, ra, forms.ReleaseAuthPeopleForm.form, rat, missingAuth ))
          }

        case None =>Future.successful(NotFound(views.html.page_404("Release not found")))
      }
    }.flatMap(identity)
  }

  def login(login:String) = Action.async{ implicit request =>
    Future.successful(Ok("TBD - By Login"))
  }

  def pplNew() = LDAPAuthPermission("ReleaseAuthType") {
    Action.async { implicit request =>
      forms.ReleaseAuthPeopleForm.form.bindFromRequest.fold(
        form => {
          logger.info(form.errors.map{ f => f.toString}.mkString(","))
          Future.successful(Redirect(routes.ReleaseController.releaseTypeSearch()))
        },
        data => {
          releaseTypeAuthorizationPeopleRepo.insert(ReleasetypeauthorizationpeopleRow(id = 0,
                isprimary = data.isprimary,
                login = data.login,
                releasetypeid = data.releasetypeid,
            releaseauthorityid = data.releaseauthoritytypeid)).map { result =>
            Redirect(routes.ReleaseController.releaseType( data.releasetypeid ))
          }
        }
      )
    }
  }


  def releaseTypeSearch(): Action[AnyContent] = Action.async { implicit request =>
    releaseTypeRepo.all.map { pt => Ok(views.html.release.typ.search(Page(pt), None, forms.ReleaseTypeForm.form)) }
  }

  def releaseType(id: Int, page: Int, page2: Int): Action[AnyContent] = Action.async { implicit request =>

    (for {
      rt <- releaseTypeRepo.find(id)
      relPPl <- releaseRepo.findByReleaseType(id)
      relTypePPl <- releaseTypeAuthorizationPeopleRepo.findByReleaseType(id)
      rat <- releaseAuthorizationTypeRepo.all.map { x => x.map{ row => (row.id.toString,row.name)}}
    } yield (rt, relTypePPl, relPPl, rat)).map { x =>
      x._1 match {
        case Some(rt) => Ok(views.html.release.typ.id(id, rt, Page(x._2, page), Page(x._3, page2), forms.ReleaseAuthPeopleForm.form, x._4))
        case None => NotFound(views.html.page_404("Release Type not found"))
      }
    }
  }

  def releaseTypeNew() = LDAPAuthPermission("ReleaseType") {
    Action.async { implicit request =>
      forms.ReleaseTypeForm.form.bindFromRequest.fold(
        form => {
          Future.successful(Redirect(routes.ReleaseController.releaseTypeSearch()))
        },
        data => {
          releaseTypeRepo.insert(ReleasetypeRow(id = 0, name = data.name, emailalias = data.emailAlias)).map { result =>
            Redirect(routes.ReleaseController.releaseType(result.id))
          }
        }
      )
    }
  }

  def releaseTypeUPD(id: Int) = LDAPAuthPermission("ReleaseType") {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          releaseTypeRepo.find(id).map {
            case Some(rt) =>
              val fieldName: String = keys.getOrElse("name", Seq("x")).head.toLowerCase
              val valueSeq: Seq[String] = keys.getOrElse("value", Seq.empty)
              val res: Future[Result] = valueSeq.headOption match {
                case None => Future.successful(NotAcceptable("Missing value"))
                case Some(value) =>
                  val upd: Future[Boolean] =
                    if (fieldName.equals("name")) {
                      releaseTypeRepo.update(id, rt.copy(name = value))
                    } else {
                    if ( fieldName.equals("emailalias")) {
                        releaseTypeRepo.update(id, rt.copy(emailalias = value))
                    }else {
                      Future.successful(false)
                    }
                  }
                  upd.map { r =>
                    if (r) {
                      Ok("Updated")
                    } else {
                      NotFound("Invalid Value/Not Updated")
                    }
                  }
              }
              res
            case None => Future.successful(NotFound(views.html.page_404("RT not found")))
          }.flatMap(identity)
      }
    }
  }

  def releaseTypeDel(id: Int) = LDAPAuthPermission("ReleaseType") {
    Action.async { implicit request =>

      releaseTypeRepo.find(id).map {
        case Some(rt) =>
          releaseTypeRepo.delete(id).map { x =>
            Redirect(routes.ReleaseController.releaseTypeSearch())
          }
        case None => Future.successful(NotFound(views.html.page_404("RT not found")))
      }.flatMap(identity)
    }
  }


  def releaseAuthorizationTypeSearch(): Action[AnyContent] = Action.async { implicit request =>
    releaseAuthorizationTypeRepo.all.map { pt => Ok(views.html.release.auth.search(Page(pt), None, forms.AuthorizationTypeForm.form)) }
  }

  def releaseAuthorizationType(id: Int, page: Int, page2: Int): Action[AnyContent] = Action.async { implicit request =>

    (for {
      rat <- releaseAuthorizationTypeRepo.find(id)
      relPPl <- releaseAuthorizationRepo.findByReleaseAuthorityType(id)
      relTypePPl <- releaseTypeAuthorizationPeopleRepo.findByReleaseAuthorizationType(id)
      rt <- releaseTypeRepo.all.map { x => x.map{ row => (row.id.toString,row.name)}}
    } yield (rat, relTypePPl, relPPl,rt)).map { x =>
      x._1 match {
        case Some(rat) => Ok(views.html.release.auth.id(id, rat, Page(x._2, page), Page(x._3, page2), forms.ReleaseAuthPeopleForm.form, x._4))
        case None => NotFound(views.html.page_404("RAT not found"))
      }
    }
  }

  def releaseAuthorizationTypeNew() = LDAPAuthPermission("ReleaseAuthType") {
    Action.async { implicit request =>
      forms.AuthorizationTypeForm.form.bindFromRequest.fold(
        form => {
          Future.successful(Redirect(routes.ReleaseController.releaseAuthorizationTypeSearch()))
        },
        data => {
          releaseAuthorizationTypeRepo.insert(ReleaseauthorizationtypeRow(id = 0, name = data.name, description = data.description)).map { result =>
            Redirect(routes.ReleaseController.releaseAuthorizationType(result.id))
          }
        }
      )
    }
  }

  def releaseAuthorizationTypeUPD(id: Int) = LDAPAuthPermission("ReleaseAuthType") {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          releaseAuthorizationTypeRepo.find(id).map {
            case Some(rat) =>
              val fieldName: String = keys.getOrElse("name", Seq("x")).head.toLowerCase
              val valueSeq: Seq[String] = keys.getOrElse("value", Seq.empty)
              val res: Future[Result] = valueSeq.headOption match {
                case None => Future.successful(NotAcceptable("Missing value"))
                case Some(value) =>
                  val upd: Future[Boolean] = if (fieldName.equals("name")) {
                    releaseAuthorizationTypeRepo.update(id, rat.copy(name = value))
                  } else {
                    Future.successful(false)
                  }
                  upd.map { r =>
                    if (r) {
                      Ok("Updated")
                    } else {
                      NotFound("Invalid Value/Not Updated")
                    }
                  }
              }
              res
            case None => Future.successful(NotFound(views.html.page_404("RAT not found")))
          }.flatMap(identity)
      }
    }
  }

  def releaseAuthorizationTypeDel(id: Int) = LDAPAuthPermission("ReleaseAuthType") {
    Action.async { implicit request =>

      releaseAuthorizationTypeRepo.find(id).map {
        case Some(rat) =>
          releaseAuthorizationTypeRepo.delete(id).map { x =>
            Redirect(routes.ReleaseController.releaseAuthorizationTypeSearch())
          }
        case None => Future.successful(NotFound(views.html.page_404("RAT not found")))
      }.flatMap(identity)
    }
  }

}


