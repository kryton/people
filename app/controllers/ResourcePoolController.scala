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

import java.io.FileOutputStream
import java.sql.Date
import java.time.format.DateTimeFormatter
import javax.inject._

import com.typesafe.config.ConfigFactory
import models.people.TeamDescriptionRepo
import models.product._
import offline.Tables
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import projectdb.Tables
import projectdb.Tables._
import slick.jdbc.JdbcProfile
import util.{LDAP, Page, User}

import scala.concurrent.{ExecutionContext, Future}
import offline.Tables.EmprelationsRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.{Row, Sheet, Workbook}
import play.api.Logger

import scala.collection.immutable
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ResourcePoolController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
   resourcePoolRepo: ResourcePoolRepo,
   user: User
  )(implicit
    resourceTeamRepo: ResourceTeamRepo,
    teamDescriptionRepo: TeamDescriptionRepo,
    matrixTeamMemberRepo: models.people.MatrixTeamMemberRepo,
    officeRepo:models.people.OfficeRepo,
    positionTypeRepo:models.people.PositionTypeRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: WebJarAssets,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

//  implicit val ldap: LDAP = new LDAP
  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val canEditF = user.isAdmin(LDAPAuth.getUser())
    val pageSize =20
      search match {
        case None =>
          (for {
            isAdmin <- canEditF
            all <- resourcePoolRepo.all
          } yield (isAdmin,all) ).map{ x => Ok(views.html.product.pool.search( Page(x._2,page,pageSize),search, x._1, forms.ResourcePoolForm.form)) }
        case Some(searchString) =>
          (for {
            isAdmin <- canEditF
            all <- resourcePoolRepo.search(searchString)
          } yield (isAdmin,all) )
          .map{ x  =>
          if (x._2.size == 1 ) {
            Redirect(routes.ResourcePoolController.id( x._2.head.id))
          } else {
            Ok(views.html.product.pool.search(Page(x._2,page,pageSize), search, x._1, forms.ResourcePoolForm.form ))
          }
        }
      }
  }

  def id(rpId:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>

    resourcePoolRepo.find(rpId).map {
      case Some(rp) =>
        /*
        val teamsx: Future[Seq[(ResourceteamRow,
          Seq[(EmprelationsRow, Int, EfficencyMonth)],
          Seq[TeamSummary]
          )]] = resourceTeamRepo.findInPool( rp.id).map{
          (teamS: Seq[ResourceteamRow]) => Future.sequence( teamS.map { team:ResourceteamRow =>
            (for{
              dsX <- resourceTeamRepo.getDevStatsForTeam(team.id)//.map{ ds => (team, ds) }
              ctX <- resourceTeamRepo.getTeamSummaryByVendorCountry(team.id)

            } yield (dsX,ctX)).map{ x=>
              (team,x._1, x._2)
            }
          })
        }.flatMap(identity)
        */

        (for {
          t <- resourcePoolRepo.getDevStats(rpId)
          isAdmin <- user.isAdmin(LDAPAuth.getUser())
        } yield (t,isAdmin))
        .map { xx =>
          val isAdmin:Boolean  = xx._2
          val team = xx._1
          val byTeam: Seq[(ResourceteamRow, (Int, EfficencyMonth))] = team.map{ tE => ( tE._1, tE._2
            .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 +1,z._2.add( i._3))  ))
          }
          val byPool: (Int, EfficencyMonth) = byTeam.map( _._2)
                      .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 + i._1 ,z._2.add( i._2) ))

          val sum: Seq[TeamSummary] = team.flatMap{ line =>
            val baseX = line._3

            baseX.groupBy(p=> (p.agency, p.isContractor,p.country,p.positionType, p.isPE )).map{ xx =>
              (xx._1._1, xx._1._2,xx._1._3,xx._1._4, xx._1._5, xx._2.map(_.tally).sum )
            }.toSeq
          }.groupBy( p=> (p._1, p._2, p._3, p._4, p._5 )).map{ line2 =>
            TeamSummary(agency = line2._1._1, isContractor =line2._1._2, country = line2._1._3, positionType= line2._1._4,isPE = line2._1._5, tally = line2._2.map(_._6).sum )
          }.toSeq


          Ok(views.html.product.pool.id( rpId, rp, byPool, sum, Page( byTeam,page, pageSize = 20), isAdmin) )
        }

      case None => Future.successful(NotFound(views.html.page_404("Team not found")))
    }.flatMap(identity)
  }

  def doFullBreakdown(format:Option[String]=None, teamLevel:Option[String]) =LDAPAuthPermission("TeamPoolBreakdown") {
    Action.async { implicit request =>
      val x: Future[Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]] = resourceTeamRepo.allEx.map { rtOs =>
        val teamOrPool: Seq[Either[ResourceteamRow, ResourcepoolRow]] = rtOs.map { rtO =>
          if (teamLevel.getOrElse("N").equalsIgnoreCase("Y")) {
            Left(rtO._1)
          } else {
            rtO._2 match {
              case Some(rp) => Right(rp)
              case None => Left(rtO._1)
            }
          }
        }
        Future.sequence(teamOrPool.groupBy(p => p).keys.map {
          case x@Left(team) => resourceTeamRepo.getTeamSummaryByVendorCountry(team.id).map { res => (x, res) }
          case x@Right(pool) => resourcePoolRepo.getTeamSummaryByVendorCountry(pool.id).map { res => (x, res) }
        }.toSeq)
      }.flatMap(identity)

      x.map { (result: Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]) =>
        val formatS: String = format.getOrElse("html")

        if (formatS.equalsIgnoreCase("XLS")) {
          Ok.sendFile(doFullBreakdownXLS(result))
            .as("application/vnd.ms-excel")
            .withHeaders(("Access-Control-Allow-Origin", "*"),
              ("Content-Disposition", s"attachment; filename=ResourceList.xls"))
        } else {
          Ok(views.html.product.pool.fullBreakdown(result,teamLevel))
        }
      }
    }
  }

  def doFullBreakdownXLS(result:Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]):java.io.File = {
    val wb: Workbook = new HSSFWorkbook()
    val sheet: Sheet = wb.createSheet()
    val headerRow: Row = sheet.createRow(0)
    wb.setSheetName(0,"Resource Breakdown")
    headerRow.createCell(0).setCellValue("Pool/Team")
    headerRow.createCell(1).setCellValue("Employee/Vendor")
    headerRow.createCell(2).setCellValue("Country")
    headerRow.createCell(3).setCellValue("Position Type")
    headerRow.createCell(4).setCellValue("PE?")
    headerRow.createCell(5).setCellValue("#")

    val result2: Seq[(Either[ResourceteamRow, ResourcepoolRow], TeamSummary)] = result.flatMap { x =>
      x._2.map { line =>
        (x._1, line)
      }
    }

    for (rowNum <- result2.indices) {
      val r: Row = sheet.createRow(rowNum + 1)
      val ( teamPool:Either[ResourceteamRow,ResourcepoolRow],
            teamSum:TeamSummary) = result2(rowNum)
      val name = teamPool match {
        case Left(x) => x.name
        case Right(x) => x.name
      }
      r.createCell(0).setCellValue(name)
      if (teamSum.isContractor) {
        r.createCell(1).setCellValue(teamSum.agency)
      } else {
        r.createCell(1).setCellValue("Employee")
      }
      r.createCell(2).setCellValue(teamSum.country.getOrElse("-"))
      r.createCell(3).setCellValue(teamSum.positionType)
      r.createCell(4).setCellValue( teamSum.isPE)
      r.createCell(5).setCellValue(teamSum.tally)
    }
    val tmpDir = ConfigFactory.load.getString("scenario.tempdir")
    val f = java.io.File.createTempFile("resourceBreakdown-",".xls",new java.io.File(tmpDir))
    val os = new FileOutputStream(f)
    wb.write(os)
    wb.close()
    os.close()
    //val outFilename = s"resourceBreakdown".replaceAll(",|!","_")
    f.deleteOnExit()
    f
  }

  def newRP = LDAPAuthAction {
    Action.async { implicit request =>
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
      } yield (u, u)).map { resp =>
        val canEdit = resp._2
        if (canEdit) {
          forms.ResourcePoolForm.form.bindFromRequest.fold(
            form => {
              Future.successful(Redirect(routes.ResourcePoolController.search()))
            },
            data => {
              val newObj = ResourcepoolRow(id = 0, name = data.name, ordering = data.ordering, poolsize = data.poolsize)
              resourcePoolRepo.insert(newObj).map { o =>
                Redirect(routes.ResourcePoolController.id(o.id))
              }
            }
          )
        } else {
          Future.successful(Unauthorized(views.html.page_403("No Permission")))
        }
      }.flatMap(identity)
    }
  }


  def deleteRP(id:Int) = LDAPAuthAction{
    Action.async { implicit request =>
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
        mc <- resourcePoolRepo.find(id)
        teams <- resourcePoolRepo.findTeams(id)
      } yield (u, mc,teams)).map { resp =>
        val canEdit = resp._1
        if (canEdit) {
          resp._2 match {
            case Some(mc) =>
              if ( resp._3.nonEmpty) {
                Future.successful(Redirect(routes.ResourcePoolController.id(id)).flashing("message"->"Can't delete a pool with teams"))
              } else {
                resourcePoolRepo.delete(id).map { x =>
                  Redirect(routes.ResourcePoolController.search())
                }
              }
            case None => Future.successful(NotFound(views.html.page_404("Resource pool not found")))
          }
        } else {
          Future.successful(Unauthorized(views.html.page_403("No Permission")))
        }
      }.flatMap(identity)
    }
  }


  def updateRP(id:Int) = LDAPAuthAction {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          (for {
            u <- user.isAdmin(LDAPAuth.getUser())
            rec <- resourcePoolRepo.find(id)
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
                        case "ordering" => rec.copy(ordering = value.toInt)
                        case "poolsize" => rec.copy(poolsize = value.toInt)
                        case _ => rec
                      }
                      resourcePoolRepo.update(id, updRec)
                        .map { r =>
                          if (r) {
                            Ok("Updated")
                          } else {
                            NotFound("Invalid Value/Not Updated")
                          }
                        }
                  }
                  res
                case None => Future.successful(NotFound(views.html.page_404("Resource Pool not found")))
              }
            } else {
              Future.successful(Unauthorized(views.html.page_403("No Permission")))
            }
          }.flatMap(identity)
      }
    }
  }


  def doBreakdown(id:Int) = Action.async { implicit request =>
    resourcePoolRepo.find(id).map {
      case Some(pf) =>
        (for {
          p <- resourcePoolRepo.breakDownPool(id)
        } yield p ).map { x =>

          val dates = x._1.flatMap{ p=> p._3.keys }

          val monthRange: Seq[Date] = if ( dates.isEmpty) {
            Seq.empty
          } else {
            val minX: Date = dates.minBy(_.getTime)
            val maxX: Date = dates.maxBy(_.getTime)
            util.Conversions.monthRange(minX, maxX)
          }
          val monthsLimit: Seq[Date] = monthRange.sortBy(_.getTime).slice(0,15) //16 dates = 5Q
          //=  util.Conversions.monthRange(minX, maxX)

          val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-YYYY")

          Ok(views.html.product.pool.breakout(id, pf, x._1, monthsLimit, dateFormat))
        }

      case None => Future.successful(NotFound(views.html.page_404("Feature not found")))
    }.flatMap(identity)
  }

  def rpBreakdown(format:Option[String]) = Action.async { implicit request =>
    val resultSet: Future[(Set[(Either[ResourceteamRow, ResourcepoolRow], Iterable[(ProductfeatureRow, Int, Map[Date, Double])])], Seq[Date])] = resourcePoolRepo.allPoolsTeams.map { set =>
      Future.sequence( set.map {
        case rtrp@Left(rt) => resourceTeamRepo.breakDownTeam(rt.id).map { i => (rtrp, i) }
        case rtrp@Right(rp) => resourcePoolRepo.breakDownPool(rp.id).map { i => (rtrp, i) }
      })
    }.flatMap(identity).map { set =>
      val dates: Set[Date] = set.flatMap { d =>
        d._2._1.flatMap{ p =>
          p._3.keys
        }
      }
      val monthRange = if( dates.isEmpty) {
        Seq.empty[Date]
      } else {
        val minX = dates.minBy(_.getTime)
        val maxX = dates.maxBy(_.getTime)
        util.Conversions.monthRange(minX,maxX)
      }
      val monthsLimit = monthRange.sortBy(_.getTime).slice(0,15)
      val setReduced: Set[(Either[ResourceteamRow, ResourcepoolRow], Iterable[(ProductfeatureRow, Int, Map[Date, Double])])] = set.map{ s =>
        ( s._1, s._2._1 )
      }
      (setReduced,monthsLimit)
    }
    resultSet.map{ s =>

      Ok.sendFile( rpBreakdownXLS(s._1,s._2))
        .as("application/vnd.ms-excel")
        .withHeaders(("Access-Control-Allow-Origin", "*"),
          ("Content-Disposition", s"attachment; filename=ResourcePoolList.xls"))
    }
   // Future(Ok(""))
  }


  def rpBreakdownXLS(result:Set[(Either[ResourceteamRow, ResourcepoolRow], Iterable[(ProductfeatureRow, Int, Map[Date, Double])])],
                     monthLimit:Seq[Date]):java.io.File = {
    val wb: Workbook = new HSSFWorkbook()
    val sheet: Sheet = wb.createSheet()
    val headerRow: Row = sheet.createRow(0)
    wb.setSheetName(0,"Resource Breakdown")
    headerRow.createCell(0).setCellValue("Pool/Team")
    headerRow.createCell(1).setCellValue("Feature")
    headerRow.createCell(2).setCellValue("Month")
    headerRow.createCell(3).setCellValue("#")

    val monthSet = monthLimit.toSet
    val result2: Seq[(Either[ResourceteamRow, ResourcepoolRow],ProductfeatureRow, Date,Double)] = result.flatMap { x =>
      x._2.flatMap { line =>
        line._3.filter( p=> monthSet.contains( p._1)).map{ d =>
          (x._1, line._1, d._1,d._2)
        }.toSeq
      }.toSeq
    }.toSeq

    for (rowNum <- result2.indices) {
      val r: Row = sheet.createRow(rowNum + 1)
      val ( teamPool:Either[ResourceteamRow,ResourcepoolRow],
            feature:ProductfeatureRow,
            date:Date,
          devDays:Double) = result2(rowNum)
      val name = teamPool match {
        case Left(x) => x.name
        case Right(x) => x.name
      }
      r.createCell(0).setCellValue(name)
      r.createCell(1).setCellValue(feature.name)

      r.createCell(2).setCellValue(date)
      r.createCell(3).setCellValue(devDays)
    }
    val tmpDir = ConfigFactory.load.getString("scenario.tempdir")
    val f = java.io.File.createTempFile("resourcePoolBreakdown-",".xls",new java.io.File(tmpDir))
    val os = new FileOutputStream(f)
    wb.write(os)
    wb.close()
    os.close()
    f.deleteOnExit()
    f
  }

  def roadmap(rpId:Int) = Action.async { implicit request =>
    (for {
      rp <- resourcePoolRepo.find(rpId)
      r <-  resourcePoolRepo.roadMap(rpId)
      t <- resourcePoolRepo.getDevStats(rpId)
      slack <- resourcePoolRepo.quarterSlack
    } yield (rp,r,t,slack) ).map{ x =>
      x._1 match {
        case Some(rp) =>
          val devStats: Seq[(Int, EfficencyMonth)] = x._3.flatMap{ rtL =>
            rtL._2.map{ line => (line._2,line._3)}
          }
          val slack1: Map[Int, BigDecimal] = x._4.map{ line => line.ordering -> line.efficiency.getOrElse(BigDecimal(1.0))}.toMap
          val slack: Map[Int, BigDecimal] = (1 to 5).map{ l => l->slack1.getOrElse(l,BigDecimal(1.0))}.toMap

          val features: Seq[(Int, Some[ProductfeatureRow], BigDecimal, BigDecimal, Map[Int, BigDecimal])] = x._2.groupBy(_._4).map{ byFeature =>
            val teamEffortMap = byFeature._2.groupBy(_._1).map { byTeam =>
              val effort = byTeam._2.map{ projectL => projectL._2.featuresizeremaining.getOrElse(BigDecimal(projectL._2.featuresize))}.sum
              byTeam._1.id  -> effort
            }
            val remaining: BigDecimal = teamEffortMap.values.sum

            ( byFeature._1.ordering, Some(byFeature._1), remaining, BigDecimal(0),teamEffortMap )
          }.filter( p => p._3 >0).toSeq.sortBy(_._1)

          val teams: Seq[ResourceteamRow] =    x._2.groupBy(_._1).keys.toSeq.sortBy(_.ordering)


          val t: Option[ProductfeatureRow] = None
          val s:Map[Int, BigDecimal] = Map.empty

          val cumSize = features.scanLeft( (0, t, BigDecimal(0), BigDecimal(0), s)   )( (b,z) => ( z._1, z._2, z._3, b._4 + z._3, z._5)).drop(1)


         // val emps = x._3
          val prodTeamDet: (Int,EfficencyMonth) = devStats
            .foldLeft( (0,EfficencyMonth(0, 0, 0 ,0, 0))) ((z:(Int,EfficencyMonth), i) => (z._1 + 1, z._2.add( i._2)))
          // 3 months * 20 days/month * a bit of slack to allow unforseen
          val devDaysPerQ: (BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal) = (
            prodTeamDet._2.t0 * 3 * 20 * slack.getOrElse(1,BigDecimal(1.0)),
            prodTeamDet._2.t3 * 3 * 20 * slack.getOrElse(2,BigDecimal(1.0)),
            prodTeamDet._2.t6 * 3 * 20 * slack.getOrElse(3,BigDecimal(1.0)),
            prodTeamDet._2.t9 * 3 * 20 * slack.getOrElse(4,BigDecimal(1.0)),
            prodTeamDet._2.t12* 3 * 20 * slack.getOrElse(5,BigDecimal(1.0))
          )
          val cumDaysPerQ = (
            devDaysPerQ._1,
            devDaysPerQ._1 + devDaysPerQ._2,
            devDaysPerQ._1 + devDaysPerQ._2 + devDaysPerQ._3,
            devDaysPerQ._1 + devDaysPerQ._2 + devDaysPerQ._3 + devDaysPerQ._4,
            devDaysPerQ._1 + devDaysPerQ._2 + devDaysPerQ._3 + devDaysPerQ._4 + devDaysPerQ._5
          )

          val cumSize2:Seq[(Int, ProductfeatureRow, BigDecimal, BigDecimal,Map[Int, BigDecimal], String)] = cumSize.map{ line =>
            val s = line._2 match {
              case Some(pf) => pf.name
              case None => "-None-"
            }
            val q = if ( line._4 < cumDaysPerQ._1 ) {
              "Q-now"
            } else {
              if ( line._4 < cumDaysPerQ._2) {
                "Q+1"
              } else {
                if ( line._4 < cumDaysPerQ._3) {
                  "Q+2"
                } else {
                  if ( line._4 < cumDaysPerQ._4) {
                    "Q+3"
                  } else {
                    if ( line._4 < cumDaysPerQ._5 ) {
                      "Q+4"
                    } else {
                      "Q++"
                    }
                  }
                }
              }
            }
            (line._1, line._2.get, line._3, line._4,line._5, q)
          }

          Ok(views.html.product.pool.roadmap(rpId, rp, prodTeamDet,devDaysPerQ, teams, cumSize2, slack))
        case None => NotFound(views.html.page_404(  "Pool not found"))
      }
    }
  }
}

