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
import java.nio.file.Path
import java.sql.Date
import java.time.format.DateTimeFormatter
import javax.inject._

import com.typesafe.config.ConfigFactory
import models.people.{MatrixTeamMemberRepo, OfficeRepo, PositionTypeRepo, TeamDescriptionRepo}
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.{Row, Sheet, Workbook}
import play.api.Logger

//import models.people.{EmployeeRepo}
import models.product._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.db.NamedDatabase
import play.utils.Conversions
import offline.Tables
import offline.Tables._
import slick.jdbc.JdbcProfile
import utl.importFile.{ProjectMPPImport, ProjectXLSImport, SAPImport}
import utl.{LDAP, Page, User}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProductFeatureController @Inject()
  (
   protected val dbConfigProvider: DatabaseConfigProvider,
   //@NamedDatabase("offline") protected val dbProjectConfigProvider: DatabaseConfigProvider,

   productFeatureRepo: ProductFeatureRepo,
   resourcePoolRepo: ResourcePoolRepo,
   featureFlagRepo: FeatureFlagRepo,
   user: User

  )(implicit
    resourceTeamRepo: ResourceTeamRepo,
    productTrackRepo: ProductTrackRepo,
    stageRepo: StageRepo,
    managedClientRepo: ManagedClientRepo,
    projectRepo: ProjectRepo,
    projectDependencyRepo: ProjectDependencyRepo,
    teamDescriptionRepo: TeamDescriptionRepo,
    officeRepo: OfficeRepo,
    postionTypeRepo: PositionTypeRepo,
    matrixTeamMemberRepo: MatrixTeamMemberRepo,
    ec: ExecutionContext,
    //override val messagesApi: MessagesApi,
    cc: ControllerComponents,
    webJarAssets: org.webjars.play.WebJarAssets,
    webJarsUtil: org.webjars.play.WebJarsUtil,
    assets: AssetsFinder,
    ldap:LDAP
  ) extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport{

  def search( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val pageSize = 20
      search match {
        case None => productFeatureRepo.all.map{ pt => Ok(views.html.product.productFeature.search( Page(pt,page, pageSize=pageSize),search)) }
        case Some(searchString) => productFeatureRepo.search(searchString).map{ emps =>
          if (emps.size == 1 ) {
            Redirect(routes.ProductFeatureController.id( emps.head.id))
          } else {
            Ok(views.html.product.productFeature.search(Page(emps,page,pageSize=pageSize), search))
          }
        }
      }
  }
  def searchCID( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val pageSize = 20
      search match {
        case None => productFeatureRepo.all.map{ pt =>
          val ptFilter = pt.filter(_.iscid.getOrElse(false))
          Ok(views.html.product.productFeature.search( Page(ptFilter,page, pageSize=pageSize),search,"CID"))
        }
        case Some(searchString) => productFeatureRepo.search(searchString).map{ emps =>
          val ptFilter = emps.filter(_.iscid.getOrElse(false))
          if (ptFilter.size == 1 ) {
            Redirect(routes.ProductFeatureController.id( ptFilter.head.id))
          } else {
            Ok(views.html.product.productFeature.search(Page(ptFilter,page,pageSize=pageSize), search,"CID"))
          }
        }
      }
  }
  def searchAnchor( page:Int, search:Option[String]): Action[AnyContent] = Action.async { implicit request =>
    val pageSize = 20
      search match {
        case None => productFeatureRepo.all.map{ pt =>
          val ptFilter = pt.filter(_.isanchor.getOrElse(false))
          Ok(views.html.product.productFeature.search( Page(ptFilter, page, pageSize=pageSize),search,"Anchor"))
        }
        case Some(searchString) => productFeatureRepo.search(searchString).map{ emps =>
          val ptFilter = emps.filter(_.isanchor.getOrElse(false))
          if (ptFilter.size == 1 ) {
            Redirect(routes.ProductFeatureController.id( ptFilter.head.id))
          } else {
            Ok(views.html.product.productFeature.search(Page(ptFilter, page, pageSize=pageSize), search,"Anchor"))
          }
        }
      }
  }


  def id( id:Int, page:Int): Action[AnyContent] = Action.async{ implicit request =>
    (for{
      u <- user.isAdmin(LDAPAuth.getUser())
      pf <- productFeatureRepo.find(id)
    } yield (u,pf)).map { xf =>
      xf._2 match {

        case Some(pf) =>
          val tracks = productFeatureRepo.findTracksByFeature(id)
          val flags = productFeatureRepo.findFeatureFlagsByFeature(id)
          val stage = stageRepo.find(pf.stageid)
          val rteams = productFeatureRepo.findResourceTeams(id)
          val managedClients: Future[Seq[(ManagedclientRow, ManagedclientproductfeatureRow)]] = productFeatureRepo.findManagedClientsForFeature(id)
          val projects = projectRepo.findByFeatureEx(id)

          (for {
            t <- tracks
            f <- flags
            s <- stage
            r <- rteams
            m <- managedClients
            p <- projects
            mc <- managedClientRepo.all
            ff <- featureFlagRepo.all
          } yield (t, f, s, r, m, p,mc,ff))
            .map { x =>
              val canEdit = xf._1
              val mcs: Seq[(String, String)] = x._7.map{ p => p.id.toString -> p.name}
              val ffs = x._8.map{ p=> p.id.toString -> p.name}

              Ok(views.html.product.productFeature.id(id, pf, x._1, x._2, x._3, x._4, x._5, x._6, canEdit,
                forms.ManagedClientProductFeatureForm.form,
              forms.ProductFeatureFlagForm.form,
                mcs,
                ffs))
            }

        case None => Future.successful(NotFound(views.html.page_404("Feature not found")))
      }
    }.flatMap(identity)
  }

  def importFile = LDAPAuthAction {
    Action.async { implicit request =>
      user.isAdmin(LDAPAuth.getUser()).map {
        case true =>Ok(views.html.product.productFeature.importFile() )
        case false => Unauthorized(views.html.page_403("User not authorized"))
      }
    }
  }
  def doImport = Action.async(parse.multipartFormData) { implicit request =>
      user.isAdmin(LDAPAuth.getUser()).map {
        case true =>
          request.body.file("importFile").map { picture =>
            val filename = picture.filename
            val path: Path = picture.ref.path
           // ProjectXLSImport.importFile(path).map {
            ProjectMPPImport.importFile(path).map {
              case Left(errorMsg) => Future.successful(Ok(errorMsg))
              case Right(list) =>
                productFeatureRepo.repopulate( list ).map { x =>
                  Ok( views.html.product.productFeature.importFileResult(x))
                }
            }.flatMap(identity)
          }.getOrElse {
            Future.successful(Redirect(routes.ProductFeatureController.importFile()).flashing(
              "error" -> "Missing file"))
          }

        case false => Future.successful(Unauthorized("You don't have access to import SAP files"))

      }.flatMap(identity)
    }

    def doBreakdown(id:Int) = Action.async { implicit request =>
      productFeatureRepo.find(id).map {
        case Some(pf) =>
          (for {
            p <- projectRepo.breakDownFeature(id)
          } yield p ).map { x =>

            val minX: Date = x._2.flatMap { p => p._3.keys }.minBy(_.getTime)
            val maxX: Date = x._2.flatMap { p => p._3.keys }.maxBy(_.getTime)
            val monthRange: Seq[Date] = utl.Conversions.monthRange(minX, maxX)

            val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-YYYY")

            Ok(views.html.product.productFeature.breakout(id, pf, x._1, x._2, monthRange, dateFormat))
          }

        case None => Future.successful(NotFound(views.html.page_404("Feature not found")))
      }.flatMap(identity)
    }

  def doFullBreakdown(format: Option[String] = None) = Action.async { implicit request =>
    productFeatureRepo.all.map{ (features: Seq[ProductfeatureRow]) =>
      val breakdown: Future[Seq[(String, Seq[(Boolean, String, Double)], String, Iterable[((ResourceteamRow, Option[ResourcepoolRow]), Long, Map[Date, Double])])]] = Future.sequence( features.filter(_.isactive.getOrElse(false)).map{ feature =>
        (for{
          mc <- productFeatureRepo.findManagedClientsForFeature(feature.id )
         // flags <- productFeatureRepo.findFeatureFlagsByFeature(feature.id)
          track <- productFeatureRepo.findTracksByFeature(feature.id)
          breakdown <- projectRepo.breakDownFeature(feature.id).map( _._1 )
        } yield (mc, /*flags, */ track, breakdown )).map { result: (
          Seq[(ManagedclientRow, ManagedclientproductfeatureRow)],
            Seq[(ProducttrackRow, ProducttrackfeatureRow)],
            Iterable[((ResourceteamRow, Option[ResourcepoolRow]), Long, Map[Date, Double])]) =>
          val mc: Seq[(Boolean, String, Double)] = result._1 match {
            case Nil => Seq((false, "NONE", 1.0))
            case mcs => mcs.map(p => (p._1.ismanaged.getOrElse(false), p._1.name, p._2.allocation.toDouble))
          }

          val pt: Seq[(Boolean, String, Double)] = result._2 match {
            case Nil => Seq((false, "None", 1.0))
            case pts => pts.map(p => (false, p._1.name, p._2.allocation.toDouble))
          }
          val line = if (feature.iscid.getOrElse(false)) {
            ("CID", mc, feature.name, result._3)
          } else {
            if (feature.isanchor.getOrElse(false)) {
              ("ANCHOR", pt, feature.name, result._3)
            } else {
              ("PRAGMATIC", pt, feature.name, result._3)
            }
          }
          line
        }
      })

      val featureResult: Future[(Map[(String, Boolean, String), Map[Either[ResourceteamRow, ResourcepoolRow], Map[Date, Double]]])]
         = breakdown.map { lines =>
        val dates: Seq[Date] = lines.flatMap(r => r._4.flatMap(_._3.keys))
        val minX: Date = dates.minBy(_.getTime)
        val maxX: Date = dates.maxBy(_.getTime)
        val monthRange: Seq[Date] = utl.Conversions.monthRange(minX, maxX)

        val allocatedTally: Seq[(String, Boolean, String, Double, String, Iterable[(Either[ResourceteamRow, ResourcepoolRow], Double, Seq[(Date, Double)])])] =
          lines.map{ line: (String, Seq[(Boolean, String, Double)], String, Iterable[((ResourceteamRow, Option[ResourcepoolRow]), Long, Map[Date, Double])]) =>
          val resDTFull: Iterable[(Either[ResourceteamRow, ResourcepoolRow], Long, Seq[(Date, Double)])] = line._4.map{ rtDates =>
            val adjDates: Seq[(Date, Double)] = monthRange.map{ dt =>
              rtDates._3.get(dt) match {
                case Some(al) => (dt,al)
                case None => (dt,0.0)
              }
            }
            val rRP:Either[ResourceteamRow,ResourcepoolRow] = rtDates._1._2 match {
              case None => Left(rtDates._1._1)
              case Some(rp) =>Right(rp)
            }
            (rRP,  rtDates._2.longValue(), adjDates )
          }
          (line._1,line._2, line._3, resDTFull)
        }.flatMap { fullLine: (String, Seq[(Boolean, String, Double)], String, Iterable[(Either[ResourceteamRow, ResourcepoolRow], Long, Seq[(Date, Double)])]) =>
          val total = Math.max(fullLine._2.map(_._3).sum,0.0000001)
          fullLine._2.map { allocLine =>
            val percentage = allocLine._3/total
            val numbersAlloc = fullLine._4.map{ rtB =>
              val adj = rtB._3.map{ dtD => (dtD._1, dtD._2*percentage)}
              (rtB._1, rtB._2*percentage, adj)
            }

            (fullLine._1,  allocLine._1, allocLine._2, allocLine._3, fullLine._3, numbersAlloc )
          }
        }
        // summary grouped by type(CID/Anchor/pragmattic), Managed(y/n), sub-type(eg. client, Software/Branded), team breakdown
        val summaryAllocation: Map[(String, Boolean, String), Map[Either[ResourceteamRow, ResourcepoolRow], Map[Date, Double]]] = allocatedTally.groupBy(p => (p._1,p._2,p._3) ).map{ l =>
          val total = l._2.flatMap(_._6).groupBy(_._1).map { rt =>
            val datesT: Map[Date, Double] = rt._2.flatMap{ _._3}.groupBy(_._1).map{ x => (x._1,x._2.map(_._2).sum) }
            (rt._1,datesT )
          }
          (l._1, total)
        }
        summaryAllocation
      }
      val resourcePoolBreakDown: Future[Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])]] = resourceTeamRepo.allEx.map{ rtOs =>
        val teamOrPool: Seq[Either[ResourceteamRow, ResourcepoolRow]] = rtOs.map{ rtO =>
          rtO._2 match {
            case Some(rp) => Right(rp)
            case None => Left(rtO._1)
          }
        }
        Future.sequence(teamOrPool.groupBy(p => p).keys.map {
          case x@Left(team) => resourceTeamRepo.getTeamSummaryByVendorCountry(team.id).map{ res => (x,res)}
          case x@Right(pool) =>resourcePoolRepo.getTeamSummaryByVendorCountry(pool.id).map{ res => (x,res)}
        }.toSeq)
      }.flatMap(identity)

      for {
        f<- featureResult
        r<- resourcePoolBreakDown
      } yield ( f,r)

      //featureResult

    }.flatMap(identity).map { tally =>

      val summary: Map[(String, Boolean, String), Map[ Either[ResourceteamRow, ResourcepoolRow], Map[Date, Double]]] = tally._1
      val resourcePool = tally._2
      val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-YYYY")
      val months: Seq[Date] = summary.flatMap{ x => x._2.flatMap{ y => y._2.keys}}.toSet.toSeq//
      val monthsLimit: Seq[Date] = months.sortBy(_.getTime).slice(0,15) //16 dates = 5Q
      if ( format.getOrElse("html").equalsIgnoreCase("XLS")) {
        Ok.sendFile(doFullBreakdownXLS(summary, resourcePool,monthsLimit, dateFormat))
          .as("application/vnd.ms-excel")
          .withHeaders(("Access-Control-Allow-Origin", "*"),
            ("Content-Disposition", s"attachment; filename=ProductResourceList.xls"))
      } else {
        Ok(views.html.product.fullBreakdown(monthsLimit, summary, dateFormat))
      }

    }
  }

  def doFullBreakdownXLS( featuresSummary: Map[(String, Boolean, String), Map[ Either[ResourceteamRow, ResourcepoolRow], Map[Date, Double]]],
    resources:Seq[(Either[ResourceteamRow, ResourcepoolRow], Seq[TeamSummary])],
    monthsLimit: Seq[Date],
    dateFormat: DateTimeFormatter):java.io.File = {
    val wb: Workbook = new HSSFWorkbook()

    val featureSheet: Sheet = wb.createSheet("Feature Analysis")
    val featureHeader:Row = featureSheet.createRow(0)
    val cellStyle = wb.createCellStyle
    val createHelper = wb.getCreationHelper
    cellStyle.setDataFormat( createHelper.createDataFormat().getFormat("mmm-yy"))

    featureHeader.createCell(0).setCellValue("Type")
    featureHeader.createCell(1).setCellValue("Sub-Type")
    featureHeader.createCell(2).setCellValue("Detail")
    featureHeader.createCell(3).setCellValue("Pool/Team")
    featureHeader.createCell(4).setCellValue("Month")
    featureHeader.createCell(5).setCellValue("DevDays")

    val allowableDates = monthsLimit.toSet

    val featureSum2 = featuresSummary.flatMap{ line =>
      line._2.flatMap{ line2 =>
        line2._2.filter( p => allowableDates.contains(p._1)) .map{ line3 =>
          ( line._1._1, line._1._2, line._1._3, line2._1, line3._1, line3._2 )
        }
      }
    }.toSeq
    for (rowNum <- featureSum2.indices) {
      val row = featureSum2(rowNum)
      val r = featureSheet.createRow(rowNum+1)
      val resourceName = row._4 match {
        case Left(x) => x.name
        case Right(x) => x.name
      }

      if (row._1.equalsIgnoreCase("CID")) {
        r.createCell(0).setCellValue(row._1)
        if (row._2) {
          r.createCell(1).setCellValue("Managed")
        } else {
          r.createCell(1).setCellValue("Other")
        }
      } else {
        r.createCell(0).setCellValue("ROADMAP")
        r.createCell(1).setCellValue(row._1)
      }

      r.createCell(2).setCellValue(row._3)
      r.createCell(3).setCellValue(resourceName)
      val dateCell = r.createCell(4)
      dateCell.setCellValue(row._5)
      dateCell.setCellStyle(cellStyle)

      r.createCell(5).setCellValue(row._6)
    }

    val resourceSheet: Sheet = wb.createSheet("Resource Breakdown")
    val resourceHeader: Row = resourceSheet.createRow(0)

    resourceHeader.createCell(0).setCellValue("Pool/Team")
    resourceHeader.createCell(1).setCellValue("Employee/Vendor")
    resourceHeader.createCell(2).setCellValue("Country")
    resourceHeader.createCell(3).setCellValue("Position Type")
    resourceHeader.createCell(4).setCellValue("isPE?")
    resourceHeader.createCell(5).setCellValue("#")

    val result2: Seq[(Either[ResourceteamRow, ResourcepoolRow], TeamSummary)] = resources.flatMap { x =>
      x._2.map { line =>
        (x._1, line)
      }
    }

    for (rowNum <- result2.indices) {
      val r: Row = resourceSheet.createRow(rowNum + 1)
      val ( teamPool:Either[ResourceteamRow,ResourcepoolRow],
      teamSummary:TeamSummary) = result2(rowNum)
      val name = teamPool match {
        case Left(x) => x.name
        case Right(x) => x.name
      }
      r.createCell(0).setCellValue(name)
      if (teamSummary.isContractor) {
        r.createCell(1).setCellValue(teamSummary.agency)
      } else {
        r.createCell(1).setCellValue("Employee")
      }
      r.createCell(2).setCellValue(teamSummary.country.getOrElse("-"))
      r.createCell(3).setCellValue(teamSummary.positionType)
      r.createCell(4).setCellValue(teamSummary.isPE)
      r.createCell(5).setCellValue(teamSummary.tally)
    }

    val tmpDir = ConfigFactory.load.getString("scenario.tempdir")
    val f = java.io.File.createTempFile("resourceFeatureBreakdown-",".xls",new java.io.File(tmpDir))
    val os = new FileOutputStream(f)
    wb.write(os)
    wb.close()
    os.close()
    //val outFilename = s"resourceBreakdown".replaceAll(",|!","_")
    f.deleteOnExit()
    f
  }

  def newMCProductFeature(featureId:Int ) = Action.async { implicit request =>
    (for{
      role <- productFeatureRepo.find(featureId)
      u <- user.isAdmin(LDAPAuth.getUser())
    } yield ( role, u)).map { resp =>
      val canEdit = resp._2
      val roleO = resp._1
      roleO match {
        case Some(role) =>
          if (canEdit) {
            forms.ManagedClientProductFeatureForm.form.bindFromRequest.fold(
              form => {
                Future.successful(Redirect(routes.ProductFeatureController.id(featureId)))
              },
              data => {
                val mcId = data.managedClientId.toInt
                managedClientRepo.find(mcId).map{
                  case Some(perm) =>
                    val newObj = Tables.ManagedclientproductfeatureRow(id = 0, productfeatureid = data.featureId, managedclientid = mcId, allocation = data.allocation)
                    productFeatureRepo.insert(newObj).map { o =>
                      Redirect(routes.ProductFeatureController.id(featureId))
                    }
                  case None => Future.successful( NotFound(views.html.page_404("Managed client not found")))
                }.flatMap(identity)
              }
            )
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }
        case None =>  Future.successful(NotFound(views.html.page_404("Managed Client not found")))
      }
    }.flatMap(identity)
  }

  def newProductFeatureFlag(featureId:Int ) = Action.async { implicit request =>
    (for{
      role <- productFeatureRepo.find(featureId)
      u <- user.isAdmin(LDAPAuth.getUser())
    } yield ( role, u)).map { resp =>
      val canEdit = resp._2
      val roleO = resp._1
      roleO match {
        case Some(role) =>
          if (canEdit) {
            forms.ProductFeatureFlagForm.form.bindFromRequest.fold(
              form => {
                Future.successful(Redirect(routes.ProductFeatureController.id(featureId)))
              },
              data => {
                val ffId = data.featureFlagId.toInt
                featureFlagRepo.find(ffId).map{
                  case Some(perm) =>
                    val newObj = Tables.ProductfeatureflagRow(id = 0, productfeatureid = data.featureId, featureflagid = ffId)
                    productFeatureRepo.insert(newObj).map { o =>
                      Redirect(routes.ProductFeatureController.id(featureId))
                    }
                  case None => Future.successful( NotFound(views.html.page_404("Feature Type not found")))
                }.flatMap(identity)
              }
            )
          } else {
            Future.successful(Unauthorized(views.html.page_403("No Permission")))
          }
        case None =>  Future.successful(NotFound(views.html.page_404("Feature Type not found")))
      }
    }.flatMap(identity)
  }

  def updCheck(featureId:Int, checkType:String, enable:String) = LDAPAuthAction {
    Action.async { implicit request =>
      val enableFlag: Boolean = enable.equalsIgnoreCase("Y")
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
        pf <- productFeatureRepo.find(featureId)
      } yield (u, pf)).map { x =>
        if (x._1) {
          x._2 match {
            case Some(pf) =>
              val upd: Future[Boolean] = checkType.toLowerCase match {
                case "cid" => productFeatureRepo.update(featureId, pf.copy(iscid = Some(enableFlag)))
                case "anchor" => productFeatureRepo.update(featureId, pf.copy(isanchor = Some(enableFlag)))
                case "active" => productFeatureRepo.update(featureId, pf.copy(isactive = Some(enableFlag)))
                case _ => Future.successful(false)
              }
              upd.map { res =>
                Redirect(routes.ProductFeatureController.id(featureId))
              }
            case None => Future.successful(NotFound("Feature Not Found"))
          }
        } else {
          Future.successful(Unauthorized(views.html.page_403("Unauthorized")))
        }

      }.flatMap(identity)
    }
  }



  def deleteMCPF(featureId:Int, id:Int) = LDAPAuthAction {
    Action.async{ implicit request =>
      (for {
        u <- user.isAdmin(LDAPAuth.getUser())
        rec <- productFeatureRepo.findMCPF(featureId, id)
      } yield (u, rec)).map { result =>

        if ( result._1) {
          result._2  match {
            case Some( rec ) => productFeatureRepo.deleteMCPF(featureId,id).map{ r => Redirect(routes.ProductFeatureController.id(featureId))}
            case None => Future.successful(NotFound("Managed client product feature record not found"))
          }
        } else {
          Future.successful(Unauthorized(views.html.page_403("Unauthorized")))
        }
      }.flatMap(identity )
    }
  }

  def updateMCPF(featureId:Int, id:Int) = LDAPAuthAction {
    Action.async { implicit request =>
      request.body.asFormUrlEncoded match {
        case None => Future.successful(NotFound("No Fields"))
        case Some((keys: Map[String, Seq[String]])) =>
          (for {
            u <- user.isAdmin(LDAPAuth.getUser())
            rec <- productFeatureRepo.findMCPF(featureId, id)
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
                      try {
                        val updRec = fieldName match {
                          case "allocation" => rec.copy(allocation = BigDecimal( value))
                          case _ => rec
                        }
                        productFeatureRepo.update(featureId, id, updRec)
                          .map { r =>
                            if (r) {
                              Ok("Updated")
                            } else {
                              NotFound("Invalid Value/Not Updated")
                            }
                          }
                      } catch {
                        case e:NumberFormatException => Future.successful(NotAcceptable("Invalid Number"))
                        case e:Exception => Logger.error(e.getLocalizedMessage)
                          Future.successful(NotAcceptable(e.getLocalizedMessage))
                      }
                  }
                  res
                case None => Future.successful(NotFound("Managed Client ProductFeature not found"))
              }
            } else {
              Future.successful(Unauthorized(views.html.page_403("No Permission")))
            }
          }.flatMap(identity)
      }
    }
  }
}

