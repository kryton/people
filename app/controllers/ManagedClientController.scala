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

import forms.ManagedClientForm
import models.people._
import models.product.{ManagedClientRepo, ProductFeatureRepo, ProductTrackRepo, StageRepo}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import offline.Tables
import offline.Tables.ManagedclientRow
import slick.jdbc.JdbcProfile
import utl.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ManagedClientController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
  /* @NamedDatabase("offline") protected val dbProjectConfigProvider: DatabaseConfigProvider,*/
   productTrackRepo: ProductTrackRepo,
   productFeatureRepo: ProductFeatureRepo,
   managedClientRepo: ManagedClientRepo,
   user: User

  )(implicit
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */



  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val canEditF = user.isAdmin(LDAPAuth.getUser())
      search match {
        case None => (for {
          isAdmin <- canEditF
          all <- managedClientRepo.all
        } yield (isAdmin,all) ).map{ pt =>
          Ok(views.html.product.managedClient.search( Page(pt._2,page, pageSize = 30),search, isAdmin = pt._1, ManagedClientForm.form))
        }
        case Some(searchString) => (for {
          isAdmin <- canEditF
          all <- managedClientRepo.search(searchString)
        } yield (isAdmin,all) )
          .map{x =>
          if (x._2.size == 1 ) {
            Redirect(routes.ManagedClientController.id( x._2.head.id))
          } else {
            Ok(views.html.product.managedClient.search(Page(x._2,page, pageSize=30), search, isAdmin = x._1, ManagedClientForm.form ))
          }
        }
      }
  }

  def id(mcId:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    (for {
      u <- user.isAdmin(LDAPAuth.getUser())
      mc <- managedClientRepo.find(mcId)
      f <- managedClientRepo.findFeatures(mcId )
    } yield (u,mc,f))
    .map { res =>
      val canEdit = res._1
      val features = res._3

      res._2 match {
        case Some(mc) =>
          val featuresPlusTrack: Future[Seq[((offline.Tables.ProductfeatureRow,
            offline.Tables.ManagedclientproductfeatureRow),
            Seq[(offline.Tables.ProducttrackRow, offline.Tables.ProducttrackfeatureRow)])
            ]] = Future.sequence(features.map {pf =>
              productFeatureRepo.findTracksByFeature(pf._2.productfeatureid).map { f => (pf, f) }
            })
          (for {
            f <- featuresPlusTrack
          } yield (f))
            .map { x =>
              Ok(views.html.product.managedClient.id(mcId, mc, Page(x, page), canEdit))
            }
        case None => Future.successful(NotFound(views.html.page_404("Managed Client not found")))
      }
    }.flatMap(identity)

  }

  def newMC = Action.async { implicit request =>
    (for{
      u <- user.isAdmin(LDAPAuth.getUser())
    } yield ( u, u)).map { resp =>
      val canEdit = resp._2

          if (canEdit) {
            forms.ManagedClientForm.form.bindFromRequest.fold(
              form => {
                Future.successful(Redirect(routes.ManagedClientController.search()))
              },
              data => {
                 (for{
                  x <- managedClientRepo.find(data.name)
                  y <- managedClientRepo.findMSProject(data.msprojectname)
                } yield (x,y)).map{ res:(Option[ManagedclientRow], Option[ManagedclientRow]) =>
                  if ( res._1.isEmpty && res._2.isEmpty) {
                    val newObj = Tables.ManagedclientRow(id = 0, name = data.name, msprojectname = data.msprojectname, ismanaged = Some( data.isManaged))
                    managedClientRepo.insert(newObj).map { o =>
                      Redirect(routes.ManagedClientController.id(o.id))
                    }
                  }
                  else {
                   Future.successful( Redirect(routes.ManagedClientController.search()).flashing("error"->"Duplicate Name") )
                  }
                }.flatMap(identity)
              }
            )
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }

    }.flatMap(identity)
  }


  def deleteMC(id:Int) = LDAPAuthAction{
    Action.async { implicit request =>
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
        mc <- managedClientRepo.find(id)
        features <- managedClientRepo.findFeatures(id)
      } yield (u, mc,features)).map { resp =>
        val canEdit = resp._1
        if (canEdit) {
         resp._2 match {
            case Some(mc) =>
              if ( resp._3.nonEmpty) {
                Future.successful(Redirect(routes.ManagedClientController.id(id)).flashing("message"->"Can't delete a client with projects"))
              } else {
                managedClientRepo.delete(id).map { x =>
                  Redirect(routes.ManagedClientController.search())
                }
              }
            case None => Future.successful(NotFound(views.html.page_404("Managed Client not found")))
          }
        } else {
          Future.successful(Unauthorized(views.html.page_403("No Permission")))
        }
      }.flatMap(identity)
    }
  }

  def updCheck(id:Int, checkType:String, enable:String) = LDAPAuthAction {
    Action.async { implicit request =>
      val enableFlag: Boolean = enable.equalsIgnoreCase("Y")
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
        mc <- managedClientRepo.find(id)
      } yield (u, mc)).map { x =>
        if (x._1) {
          x._2 match {
            case Some(pf) =>
              val upd: Future[Boolean] = checkType.toLowerCase match {
                case "managed" => managedClientRepo.update(id, pf.copy( ismanaged = Some(enableFlag)))
                case _ => Future.successful(false)
              }
              upd.map { res =>
                Redirect(routes.ManagedClientController.id(id))
              }
            case None => Future.successful(NotFound("Client Not Found"))
          }
        } else {
          Future.successful(Unauthorized(views.html.page_403("Unauthorized")))
        }

      }.flatMap(identity)
    }
  }


  def updateMC(id:Int) = LDAPAuthAction {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          (for {
            u <- user.isAdmin(LDAPAuth.getUser())
            rec <- managedClientRepo.find(id)
          } yield (u, rec)).map { result =>
            val canEdit = result._1
            val recO = result._2
            if (canEdit) {
              recO match {
                case Some(rec) =>
                  val fieldName: String = keys.getOrElse("name", Seq("x")).head.toLowerCase
                  val valueSeq: Seq[String] = keys.getOrElse("value", Seq.empty)
                  val res: Future[Result] = valueSeq.headOption match {
                    case None => Future.successful(NotAcceptable("Missing value"))
                    case Some(value) =>
                      val updRec = fieldName match {
                        case "name" => rec.copy(name = value)
                        case "msprojectname" => rec.copy(msprojectname = Some(value.toLowerCase))
                        case _ => rec
                      }
                      managedClientRepo.update(id, updRec)
                        .map { r =>
                          if (r) {
                            Ok("Updated")
                          } else {
                            NotFound("Invalid Value/Not Updated")
                          }
                        }
                  }
                  res
                case None => Future.successful(NotFound(views.html.page_404("Managed Client not found")))
              }
            } else {
              Future.successful(Unauthorized(views.html.page_403("No Permission")))
            }
          }.flatMap(identity)
      }
    }
  }


}

