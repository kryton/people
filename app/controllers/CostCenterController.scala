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

import java.nio.file.Path
import javax.inject._

import models.people._
import offline.Tables.{CostcenterRow, FunctionalareaRow, ProfitcenterRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import utl.importFile.CostCenterImport
import utl.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class CostCenterController @Inject()
(
  protected val dbConfigProvider: DatabaseConfigProvider,
  employeeRepo: EmployeeRepo,
  costCenterRepo: CostCenterRepo,
  functionalCenterRepo: FunctionalCenterRepo,
  profitCenterRepo: ProfitCenterRepo,
  user: User
)(implicit
  ec: ExecutionContext,
  webJarsUtil: org.webjars.play.WebJarsUtil,
  //override val messagesApi: MessagesApi,
  cc: ControllerComponents,
  webJarAssets: org.webjars.play.WebJarAssets,
  assets: AssetsFinder,
  ldap: LDAP
) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {


  def search(page: Int, search: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    search match {
      case None => costCenterRepo.all.map { pt => Ok(views.html.person.costcenter.search(Page(pt, page, pageSize = 60), search)) }
      case Some(searchString) => costCenterRepo.search(searchString).map { costc =>
        if (costc.size == 1) {
          Redirect(routes.CostCenterController.id(costc.head.costcenter))
        } else {
          Ok(views.html.person.costcenter.search(Page(costc, page, pageSize = 60), search))
        }
      }
    }
  }

  def id(costcenter: Long, page: Int): Action[AnyContent] = Action.async { implicit request =>

    costCenterRepo.find(costcenter).map {
      case Some(costc) =>
        (for {
          emps <- employeeRepo.findByCC(costc.costcenter)
          functional <- functionalCenterRepo.find(costc.functionalareaid.getOrElse(0L))
          profitC <- profitCenterRepo.find(costc.profitcenterid.getOrElse(0L))
        } yield (emps, functional, profitC)).map { x =>
          Ok(views.html.person.costcenter.id(costcenter, costc, x._2, x._3, Page(x._1, page, pageSize = 60)))
        }
      case None => Future.successful(NotFound(views.html.page_404("Costcenter not found")))
    }.flatMap(identity)
  }

  def profit(profitCenterID: Long, page: Int): Action[AnyContent] = Action.async { implicit request =>
    profitCenterRepo.find(profitCenterID).map {
      case Some(profitCenter) =>
        (for {
          cc <- costCenterRepo.findByProfitCenter(profitCenterID)
          func <- costCenterRepo.findByProfitCenter(profitCenterID).map{ res =>
            val funcs = res.groupBy(_.functionalareaid).flatMap(_._1)
            functionalCenterRepo.find( funcs.toSet )
          }.flatMap(identity)

        } yield (cc, func)).map { x =>
          val f2 = x._2
          Ok(views.html.person.costcenter.profit(profitCenterID, profitCenter, Page(x._1, page, pageSize = 60), f2))
        }
      case None => Future.successful(NotFound(views.html.page_404("Profit center not found")))
    }.flatMap(identity)
  }

  def function(functionAreaID: Long, page: Int): Action[AnyContent] = Action.async { implicit request =>
    functionalCenterRepo.find(functionAreaID).map {
      case Some(functionA) =>
        (for {
          cc <- costCenterRepo.findByFunctionalCenter(functionAreaID)
          profit <- costCenterRepo.findByFunctionalCenter(functionAreaID).map{ res =>
            val profits = res.groupBy(_.profitcenterid).flatMap(_._1)
            profitCenterRepo.find( profits.toSet )
          }.flatMap(identity)
        } yield (cc, profit)).map { x =>
          Ok(views.html.person.costcenter.function(functionAreaID, functionA, Page(x._1, page, pageSize = 60), x._2))
        }
      case None => Future.successful(NotFound(views.html.page_404("Profit center not found")))
    }.flatMap(identity)
  }

  def importFile = LDAPAuthAction {
    Action.async { implicit request =>
      user.isAdmin(LDAPAuth.getUser()).map {
        case true =>
          Ok(views.html.person.costcenter.importFile())
        case false =>
          Unauthorized(views.html.page_403("User not authorized"))
      }
    }
  }

  def doImport = Action.async(parse.multipartFormData) { implicit request =>
    user.isAdmin(LDAPAuth.getUser()).map {
      case true =>
        request.body.file("importFile").map { picture =>
          val filename = picture.filename
          val path: Path = picture.ref.path
          CostCenterImport.importFile(path).map {
            case Left(errorMsg) => Future.successful(Ok(errorMsg))
            case Right(seq) =>
              val functions = seq.groupBy(p => (p.functionalArea, p.functionalDept, p.functionalDeptShortName, p.pAndlCategory))
                .map(x => FunctionalareaRow(x._1._1, Some(x._1._2), Some(x._1._3), Some(x._1._4)))
              val profits = seq.groupBy(p => (p.profitCenterNumber, p.profitCenterDesc))
                .map(x => ProfitcenterRow(x._1._1, Some(x._1._2)))

              //  val sizes = functions.foldLeft( (0,0))( (a,b)=> ( Math.max( a._1, b.shortname.getOrElse("").size  ), Math.max(a._2, b.company.getOrElse("").size )    ))
              val ccs = seq.map { p =>
                CostcenterRow(p.costCenter, Some(p.costCenterShort),
                  functionalareaid = Some(p.functionalArea),
                  profitcenterid = Some(p.profitCenterNumber),
                  company = Some(p.companyShort))
              }
              (for {
                funcImport <- functionalCenterRepo.bulkInsertUpdate(functions)
                profitImport <- profitCenterRepo.bulkInsertUpdate(profits)
                ccImport <- costCenterRepo.bulkInsertUpdate(ccs).map { x => costCenterRepo.cleanup }
              } yield (funcImport, profitImport, ccImport)).map { x =>
                (for {
                  funcClean <- functionalCenterRepo.cleanup
                  profitClean <- profitCenterRepo.cleanup

                } yield (funcClean, profitClean)).map { done =>
                  Ok(views.html.person.costcenter.importFileResult())
                }
              }.flatMap(identity)
          }.flatMap(identity)
        }.getOrElse {
          Future.successful(Redirect(routes.ProductFeatureController.importFile()).flashing(
            "error" -> "Missing file"))
        }

      case false => Future.successful(Unauthorized("You don't have access to import Cost Center files"))

    }.flatMap(identity)
  }

}

