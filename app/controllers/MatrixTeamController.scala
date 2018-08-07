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
import models.people.EmpRelationsRowUtils._
import models.people._
import models.product.ProductTrackRepo
import offline.Tables
import offline.Tables.{EmprelationsRow, MatrixteamRow, MatrixteammemberRow}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.libs.Files
import play.api.mvc._
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import utl.importFile.{CostCenterImport, ResourceFileImport}
import utl.{LDAP, Page, User}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class MatrixTeamController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   productTrackRepo: ProductTrackRepo,
   teamDescriptionRepo: TeamDescriptionRepo,
   matrixTeamRepo: MatrixTeamRepo,
   user: User
  )(implicit
    employeeRepo: EmployeeRepo,
    empHistoryRepo: EmpHistoryRepo,
    matrixTeamMemberRepo: MatrixTeamMemberRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap: LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{


  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
      search match {
        case None => matrixTeamRepo.all.map{ emps =>
          Ok(views.html.matrix.search(Page(emps,page),search))
        }
        case Some(searchString) => matrixTeamRepo.search(searchString).map{ emps:Seq[MatrixteamRow] =>
          if (emps.size == 1 ) {
            Redirect(routes.MatrixTeamController.id( emps.head.id))
          } else {
            Ok(views.html.matrix.search(Page(emps,page),search))
          }
        }
      }
  }

  def id( ID:Long,page:Int): Action[AnyContent] = Action.async{ implicit request =>
    matrixTeamRepo.find(ID).map {
      case Some(mt: MatrixteamRow) =>
        val empsF = matrixTeamMemberRepo.findLoginByMatrixTeam(mt.id)
        (for {
          emps <- empsF
          teams <- empsF.map{ empS => Future.sequence( empS.map{ emp => teamDescriptionRepo.findTeamForLogin( emp.login).map{ x => emp.login -> x}}) }.flatMap(identity)
          parent <- matrixTeamRepo.findParent(ID)
          children <- matrixTeamRepo.findChildren(ID)
        } yield (emps,teams, parent, children))
        .map { x =>
          val emps = x._1
          val teams: Map[String, Option[Tables.TeamdescriptionRow]] = x._2.toMap
          Ok(views.html.matrix.id( ID, mt, Page(emps,page, pageSize = 9), teams, x._3, x._4 ))
        }
      case None => Future.successful(NotFound(views.html.page_404("Team ID not found")))
    }.flatMap(identity)
  }


  def importFile = LDAPAuthAction {
    Action.async { implicit request =>
      user.isAdmin(LDAPAuth.getUser()).map {
        case true =>
          Ok(views.html.matrix.importFile())
        case false =>
          Unauthorized(views.html.page_403("User not authorized"))
      }
    }
  }
  case class MatrixTeamMemberImportRow( teamName: String, login:String, parent:Option[String])

  def doImport(): Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { implicit request =>
    user.isAdmin(LDAPAuth.getUser()).map {
      case true =>
        request.body.file("importFile").map { picture =>
          val filename = picture.filename
          val path: Path = picture.ref.path
          ResourceFileImport.importFile(path).map {
            case Left(errorMsg) => Future.successful(Ok(errorMsg))
            case Right(seq) =>
              val portfolio = seq.groupBy(p => p.portfolioGroup).flatMap( x => x._2.map{ y =>
                MatrixTeamMemberImportRow(x._1,  y.employee, None)})
              val serviceTeam = seq.groupBy(p => p.serviceArea).flatMap(x => x._2.map { y =>
                MatrixTeamMemberImportRow(x._1, y.employee, Some(y.portfolioGroup))
              })
              val teams = seq.groupBy(p => p.teamName).flatMap( x => x._2.map{ y =>
                MatrixTeamMemberImportRow(x._1,  y.employee, Some(y.serviceArea))
              })

              val combinedTeams: Map[(String, Option[String]), immutable.Iterable[MatrixTeamMemberImportRow]] = {
                (portfolio ++ serviceTeam ++ teams).groupBy(x => (x.teamName, x.parent)).filterNot(_._1._1.trim.isEmpty)
              }
              // ordering is important here.. we want the top of the heirarchy updated/created first
              // so children find their parents.

              val insertedTeams: Future[Iterable[MatrixteamRow]] =  matrixTeamRepo.bulkInsertUpdate(
                portfolio.groupBy(x => (x.teamName, x.parent)).filterNot(_._1._1.trim.isEmpty).keys
              ).map { portFolioRecs =>
                val st = matrixTeamRepo.bulkInsertUpdate(
                  serviceTeam.groupBy(x => (x.teamName, x.parent)).filterNot(_._1._1.trim.isEmpty).keys
                ).map { serviceTeamRecs =>
                  matrixTeamRepo.bulkInsertUpdate(
                    teams.groupBy(x => (x.teamName, x.parent)).filterNot(_._1._1.trim.isEmpty).keys
                  ).map { t =>
                    serviceTeamRecs ++ t
                  }
                }
                st.flatMap(identity).map{ p =>
                  portFolioRecs ++ p
                }
              }.flatMap(identity)

              insertedTeams.map{ records =>
                 val mapTeams = records.map{ x => x.name.toLowerCase -> x}.toMap
                 combinedTeams.flatMap{ x =>
                   x._2.map { mtm =>
                     MatrixteammemberRow(matrixteammemberid = mapTeams(x._1._1.toLowerCase).id, login = mtm.login, id = 0L )
                   }
                 }
               }.map{ matrixTeamMemberList=>
                  matrixTeamMemberRepo.bulkInsertUpdate(matrixTeamMemberList)
                }.map { _ =>
                  Ok(views.html.matrix.importFileResult())
                }
          }.flatMap(identity)
        }.getOrElse {
          Future.successful(Redirect(routes.MatrixTeamController.importFile()).flashing(
            "error" -> "Missing file"))
        }

      case false => Future.successful(Unauthorized("You don't have access to import Resource File files"))

    }.flatMap(identity)
  }

  def matrixHierarchy(team:Option[Long]): Action[AnyContent] = Action.async { implicit request =>
    val fTree: Future[Option[MatrixTeamTreeNode]] = team match {
      case Some(teamid) => matrixTeamRepo.managementTreeDownAsTree(teamid)
      case None => matrixTeamRepo.managementTreeDownAsTree().map {
        nodes =>
         Some(MatrixTeamTreeNode(MatrixteamRow(0,"ALL",ispe = false,None,None),nodes.toSet,0))
      }
    }

    fTree.map{
      case Some(node) => Ok(node.toJson()).as("application/json; charset=utf-8").withHeaders(("Access-Control-Allow-Origin", "*"))
      case None => NotFound("I Can't find that team")

    }
  }
}

