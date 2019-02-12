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

import java.sql.Date
import java.util.Calendar
import javax.inject._

import models.people._
import models.people.EmpRelationsRowUtils._
import offline.Tables
import offline.Tables.{EmprelationsRow, OkrkeyresultRow, OkrobjectiveRow}
import org.joda.time.DateTime
import play.api.Logger
import play.api.data.Form
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsObject, JsValue, Json, Writes}
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import utl.{Conversions, LDAP, Page, User}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class OKRController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   employeeRepo: EmployeeRepo,
   empBioRepo: EmpBioRepo,
   teamDescriptionRepo: TeamDescriptionRepo,
   empTagRepo: EmpTagRepo,
   okrObjectiveRepo: OKRObjectiveRepo,
   user: User

  )(implicit    costCenterRepo: CostCenterRepo,
    officeRepo: OfficeRepo,
    empHistoryRepo: EmpHistoryRepo,
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

  implicit val okrObjectiveExtWrites = new Writes[OKRObjectiveExt] {
    def writes( k : OKRObjectiveExt ): JsObject = Json.obj(
      "login" -> k.login,
      "person" -> k.person,
      "dateAdded"-> k.dateAdded,
      "objective" -> k.objective,
      "score" -> k.score,
      "quarter" -> Conversions.quarterS(k.quarter),
      "completed" -> k.completed,
      "retired" -> k.retired
    )
  }

  def login( login:String, quarter:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val quarterDate = Conversions.quarterD(quarter)
    if (quarterDate.isDefined) {
      employeeRepo.findByLogin(login).map {
        case Some(emp) =>
          val peerF: Future[Set[Tables.EmprelationsRow]] = emp.managerid match {
            case Some(m) => employeeRepo.managedBy(m)
            case None => Future.successful(Set.empty)
          }
          val mgrF = emp.managerid match {
            case Some(m) => employeeRepo.findByLogin(m)
            case None => Future.successful(None)
          }

          val ppl: Future[
            (Option[Tables.EmprelationsRow],
              Set[Tables.EmprelationsRow],
              Set[Tables.EmprelationsRow],
              Seq[(Tables.OkrobjectiveRow, Seq[Tables.OkrkeyresultRow])],
              Seq[(Date, String)],
              Boolean)
            ]
          = for {
            objKR <- okrObjectiveRepo.findEx(login, quarterDate.get)
            peers <- peerF
            directs <- employeeRepo.managedBy(emp.login)
            mgr <- mgrF
            qtrDate <- okrObjectiveRepo.getQuartersAsSelect()
            userPerms <- user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser())
          } yield (mgr, peers, directs, objKR, qtrDate, userPerms)
          ppl.map { x =>
            val mgrs: Set[String] = x._1.toSet.map { xx: Tables.EmprelationsRow => xx.login.toLowerCase }
            val peers = x._2.filterNot(p => p.login == emp.login)
            val directs: Set[String] = x._3.map { xx => xx.login.toLowerCase }
            val empMap: Map[String, Tables.EmprelationsRow] = (x._1.toSet ++ x._2 ++ x._3).map(e => e.login.toLowerCase -> e).toMap
            val logins: Set[String] = mgrs ++ directs ++ Set(login.toLowerCase)
            val eO: Seq[(Tables.OkrobjectiveRow, Seq[Tables.OkrkeyresultRow])] = x._4
            val qtrDate = x._5

            okrObjectiveRepo.find(logins, quarterDate.get).map(seq =>
              seq.groupBy(_.login)).map { loginO =>
              //  val eO = loginO.getOrElse( emp.login.toLowerCase, Seq.empty )
              val mO = loginO.filter(p => mgrs.contains(p._1)).map(rec => (rec._1, empMap(rec._1), rec._2))
              val pO = peers.toSeq.sortBy(_.lastname)
              val dO = x._3.map { d =>
                (d.login, d, loginO.getOrElse(d.login.toLowerCase, Seq.empty))
              }

              Ok(views.html.okr.search(login, emp, Conversions.quarterS(quarterDate), eO, x._1, mO, pO, dO, qtrDate, x._6, forms.ObjectiveForm.form))
            }
          }.flatMap(identity)

        case None => Future.successful(NotFound(views.html.page_404("Can't locate employee")))
      }.flatMap(identity)
    } else {
      Future.successful(NotAcceptable("Quarter must be in the format Qnn/YY"))
    }

  }


  def topX( size:Int, format:Option[String]) = Action.async { implicit request =>
    okrObjectiveRepo.latest(size).map { seq =>
      val toFormat = format match {
        case Some(x) => x.toLowerCase
        case None => "html"
      }
      toFormat match {
        case "json" =>
          val jsonSeq = seq.map { line =>
            OKRObjectiveExt(login = line._1.login,
              person = Some(line._2.fullName),
              dateAdded = line._1.dateadded,
              objective = line._1.objective,
              quarter = line._1.quarterdate,
              score = line._1.score match {
                case Some(x) => Some(x.toDouble)
                case None => None
              },
              completed = line._1.completed,
              retired = line._1.retired
            )
          }
          val json = Json.toJson( jsonSeq )
          Ok( json).as("application/json;charset=utf-8").withHeaders(("Access-Control-Allow-Origin","*"))
        case _ =>
          val orderedSeq= seq.groupBy(p=> ( p._1.quarterdate, p._2) ).toSeq
          Ok(views.html.okr.lastX(orderedSeq))
      }
    }
  }

  def byObjective(login:String, id:Long) = Action.async { implicit request =>
    (for {
      e<-  employeeRepo.findByLogin(login)
      o <-  okrObjectiveRepo.findEx(login, id)
      u <- user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser())
    } yield( e,o,u )).map{ x =>
      x._1 match {
        case Some(emp) => x._2 match {
          case Some(okrKR) =>
            import com.github.nscala_time.time.Imports._
            val quarter= okrKR._1.quarterdate.getOrElse(new java.sql.Date( System.currentTimeMillis()))
            val jt:DateTime = new DateTime( quarter.getTime)
            val pastQ = new java.sql.Date( (jt - 3.months).getMillis )
            val postQ = new java.sql.Date( (jt + 3.months).getMillis )
            (for{
              objs <- okrObjectiveRepo.find(login,quarter)
              future <- okrObjectiveRepo.find(login,postQ)
              past <- okrObjectiveRepo.find(login,pastQ)
            }yield(past, objs, future )).map { pastPresentFuture =>
              Ok(views.html.okr.objective(login,
                emp,
                Conversions.quarterS(okrKR._1.quarterdate),
                okrKR._1,
                okrKR._2,
                pastPresentFuture._2, pastPresentFuture._1, pastPresentFuture._3,
                canEdit = x._3,
                form = forms.KRForm.form
              ))
            }
          case None =>  Future.successful(NotFound(views.html.page_404("Objective not found")))
        }
        case None => Future.successful(NotFound(views.html.page_404("Person not found")))
      }
    }.flatMap(identity)

  }


  def byObjectiveUPD(login:String, id:Long) = Action.async { implicit request =>
    request.body.asFormUrlEncoded match {
      case None =>Future.successful( NotFound("No Fields"))
      case Some((keys: Map[String, Seq[String]])) =>
        user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser()).map { canEdit =>
          if (canEdit) {
            okrObjectiveRepo.find(login, id).map {
              case Some(objective) =>
                val fieldName: String = keys.getOrElse("name", Seq("x")).head.toLowerCase
                val valueSeq: Seq[String] = keys.getOrElse("value", Seq.empty)
                val res: Future[Result] = valueSeq.headOption match {
                  case None => Future.successful(NotAcceptable("Missing value"))
                  case Some(value) =>
                    val upd: Future[Boolean] = if (fieldName.equals("objective")) {
                      okrObjectiveRepo.update(id, objective.copy(objective = value))
                    } else if (fieldName.equals("score")) {
                      okrObjectiveRepo.update(id, objective.copy(score = Some(value.toInt)))
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
              case None => Future.successful(NotFound(views.html.page_404("Objective not found")))
            }.flatMap(identity)
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }
        }.flatMap(identity)
    }
  }


  def completeObjective(login:String, id:Long) = Action.async { implicit request =>
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        okrObjectiveRepo.findEx(login, id).map {
          case Some(objective) =>
            val score = objective._1.score match {
              case None =>
                val completed = objective._2.filter(p => p.completed).flatMap( _.score)
                completed match {
                  case Nil => None
                  case x:Seq[Int] => Some( completed.sum / completed.size )
                }
              case Some(x) => Some(x)
            }

            okrObjectiveRepo.update(id, objective._1.copy(score = score, completed = true )).map{ x=>
              Redirect(routes.OKRController.byObjective(login,id))
            }
          case None => Future.successful(NotFound(views.html.page_404("objective Not found")))
        }.flatMap(identity)
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def unCompleteObjective(login:String, id:Long) = Action.async { implicit request =>
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        okrObjectiveRepo.findEx(login, id).map {
          case Some(objective) =>
            okrObjectiveRepo.update(id, objective._1.copy(completed = false )).map{ x=>
              Redirect(routes.OKRController.byObjective(login,id))
            }
          case None => Future.successful(NotFound(views.html.page_404("objective Not found")))
        }.flatMap(identity)
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }



  def byObjectiveKRUPD(login:String, okr:Long, kr:Long) = Action.async { implicit request =>
    request.body.asFormUrlEncoded match {
      case None =>Future.successful( NotFound("No Fields"))
      case Some((keys: Map[String, Seq[String]])) =>
        // TODO Fix this so it's a single for{ .. } yield instead of future[future]
        user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser()).map { canEdit =>
          if (canEdit) {
            okrObjectiveRepo.findKR(login, okr, kr ).map {
              case Some(objectiveKR) =>
                val fieldName: String = keys.getOrElse("name", Seq("x")).head.toLowerCase
                val valueSeq: Seq[String] = keys.getOrElse("value", Seq.empty)
                val res: Future[Result] = valueSeq.headOption match {
                  case None => Future.successful(NotAcceptable("Missing value"))
                  case Some(value) =>
                    val updRec = fieldName match {
                      case "objectivekr" => objectiveKR._2.copy( objective = value)
                      case "scorekr" => objectiveKR._2.copy( score = Some(value.toInt))
                      case "descriptionkr" =>
                        if (value.isEmpty || value.trim == "") {
                          objectiveKR._2.copy( description = None)
                        } else {
                          objectiveKR._2.copy( description = Some(value))
                        }

                      case _ => objectiveKR._2
                    }
                    okrObjectiveRepo.updateKR(objectiveKR._2.id, updRec)
                      .map { r =>
                        if (r) {
                          Ok("Updated")
                        } else {
                          NotFound("Invalid Value/Not Updated")
                        }
                      }
                }
                res
              case None => Future.successful(NotFound(views.html.page_404("Objective not found")))
            }.flatMap(identity)
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }
        }.flatMap(identity)
    }
  }



  def completeKR(login:String, okr:Long, kr:Long) = Action.async { implicit request =>
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        okrObjectiveRepo.findKR(login, okr, kr).map {
          case Some(objectiveKR) =>
            okrObjectiveRepo.updateKR( objectiveKR._2.id , objectiveKR._2.copy(completed = true )).map{ x=>
              Redirect(routes.OKRController.byObjective(login,okr))
            }
          case None => Future.successful(NotFound(views.html.page_404("KR Not found")))
        }.flatMap(identity)
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def unCompleteKR(login:String, okr:Long, kr:Long) = Action.async { implicit request =>
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        okrObjectiveRepo.findKR(login, okr,kr).map {
          case Some(objectiveKR) =>
            okrObjectiveRepo.updateKR(kr, objectiveKR._2.copy(completed = false )).map{ x=>
              Redirect(routes.OKRController.byObjective(login,okr))
            }
          case None => Future.successful(NotFound(views.html.page_404("KR Not found")))
        }.flatMap(identity)
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def newKR(login:String, okr:Long) = Action.async { implicit request =>

    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        forms.KRForm.form.bindFromRequest.fold(
          form => {
            Future.successful(Redirect(routes.OKRController.byObjective(login, okr)))
          },
          data => {
            okrObjectiveRepo.find(login, okr).map {
              case Some(obj) =>
                val nowD = new java.sql.Date( System.currentTimeMillis())
                val newKR = OkrkeyresultRow(id=0,objectiveid = obj.id,
                  description = data.description,
                  objective = data.keyResult,
                  score=None,
                  completed=false,
                  dateadded = nowD)

                okrObjectiveRepo.insertKR( newKR ).map{ ignore =>
                Redirect(routes.OKRController.byObjective(login, okr))
              }
              case None => Future.successful( NotFound(views.html.page_404("Objective not found")))
            }.flatMap(identity)
          }
        )
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def newObjective(login:String, quarter:Option[String]) = Action.async { implicit request =>
    val quarterDate = Conversions.quarterD(quarter)
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        forms.ObjectiveForm.form.bindFromRequest.fold(
          form => {
            Future.successful(Redirect(routes.OKRController.login(login, quarter)))
          },
          data => {
            employeeRepo.findByLogin(login).map {
              case Some(obj) =>
                val nowD = new java.sql.Date( System.currentTimeMillis())
                val newObj = OkrobjectiveRow(id=0, login = obj.login,
                  objective = data.objective,
                  quarterdate = quarterDate,
                  score=None,
                  completed=false,
                  dateadded = nowD)

                okrObjectiveRepo.insert( newObj ).map{ o =>
                Redirect(routes.OKRController.byObjective(login, o.id))
              }
              case None => Future.successful( NotFound(views.html.page_404("Login not found")))
            }.flatMap(identity)
          }
        )
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def deleteKR( login:String, okr:Long, kr:Long ) = Action.async { implicit request =>
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        okrObjectiveRepo.findKR(login,okr,kr).map{
          case None => Future.successful(NotFound(views.html.page_404("Can't find that Key Result")))
          case Some(obj) => okrObjectiveRepo.deleteKR(obj._2.id).map{ x =>
            Redirect(routes.OKRController.byObjective(login,okr))
          }
        }.flatMap(identity)
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }

  def delete( login:String, okr:Long) = Action.async { implicit request =>
    user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map { canEdit =>
      if (canEdit) {
        okrObjectiveRepo.find(login,okr).map{
          case None => Future.successful(NotFound(views.html.page_404("Can't find that objective")))
          case Some(obj) => okrObjectiveRepo.delete(obj.id).map{ x =>
            Redirect(routes.OKRController.login(login,Some(Conversions.quarterS(obj.quarterdate))))
          }
        }.flatMap(identity)
      } else {
        Future.successful(Unauthorized(views.html.page_403("No Permission")))
      }
    }.flatMap(identity)
  }


  implicit val DateWrites = new Writes[java.sql.Date] {
    def writes( d: java.sql.Date): JsValue = Json.toJson(
      d.toString
    )
  }


}


case class OKRObjectiveExt(
                     login: String,
                     person: Option[String],
                     dateAdded: Date,
                     objective: String,
                     quarter: Option[Date],
                     score: Option[Double],
                     completed:Boolean,
                     retired: Boolean
                    )
