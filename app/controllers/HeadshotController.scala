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

import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO
import javax.inject.{Inject, Singleton}

import com.sksamuel.scrimage.{Image, WriteContext}
import com.sksamuel.scrimage.nio.JpegWriter
import com.typesafe.config.ConfigFactory
import forms.headShotUpload
import models.people.EmployeeRepo
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import utl.images.FaceDetect
import utl.User

import scala.concurrent.{ExecutionContext, Future}



@Singleton
class HeadshotController @Inject() (protected val dbConfigProvider: DatabaseConfigProvider,
                                    @NamedDatabase("projectdb") protected val dbProjectConfigProvider: DatabaseConfigProvider,
                                    employeeRepo: EmployeeRepo,
                                    user: User)
                                   (implicit ec: ExecutionContext,
                                    cc: ControllerComponents,
                                    webJarAssets: org.webjars.play.WebJarAssets,
                                    webJarsUtil: org.webjars.play.WebJarsUtil,
                                    assets: AssetsFinder)
  extends AbstractController(cc)  with HasDatabaseConfigProvider[JdbcProfile]{

  protected val imageDir: String = ConfigFactory.load().getString("image.directory")
  protected val cacheDir: String = ConfigFactory.load().getString("image.cache")
  protected val catPercentage: Double = ConfigFactory.load().getDouble("image.catPercentage")
  protected val userUploadDir: String = ConfigFactory.load().getString("image.user")
  protected val random = new scala.util.Random()

  protected def findPicInDirByLogin(directoryName: String, login:String): Option[String] = {
    def getMatchingPicsLogin(directoryName: String, login: String): List[String] = {
      def FNFilter(filename: String) = filename.toLowerCase.startsWith(s"${login.toLowerCase}-")

      val filteredList = new java.io.File(directoryName).list
      if (filteredList != null) {
        filteredList.filter(FNFilter).sortWith(_ > _).toList
      }
      else {
        List.empty
      }
    }
    getMatchingPicsLogin(directoryName, login).headOption match {
      case Some(x) => Some(directoryName +"/" + x)
      case None => None
    }
  }

 protected def findPicInDirByName(directoryName: String, firstName:String, lastName:String): Option[String] = {

   def getMatchingPicsByName(directoryName: String, firstName: String, lastName: String): List[String] = {
     def FNFilter(filename: String) = filename.toLowerCase.startsWith(firstName) && filename.toLowerCase.contains(lastName)

     val filteredList = new java.io.File(directoryName).list
     if (filteredList != null) {
       filteredList.filter(FNFilter).sortWith(_ > _).toList
     }
     else {
       List.empty
     }
   }

   getMatchingPicsByName(directoryName, firstName.toLowerCase, lastName.toLowerCase).headOption match {
     case Some(x) => Some(directoryName + "/" + x)
     case None => None
   }
 }

  protected def findPicByLogin(directories:Seq[String], login:String):Option[String] = {
    directories.flatMap( directory => findPicInDirByLogin(directory,login)).headOption
  }

  protected def findPicByName(directories:Seq[String], first:String,last:String):Option[String] = {
    directories.flatMap( directory => findPicInDirByName(directory,first,last)).headOption
  }

  protected def findAndCacheHeadShot(login:String,  currentOnly:Boolean ):Future[ Either[Option[String],Array[Byte]]]= {
    employeeRepo.findByLogin(login.toLowerCase).map {
      case Some(emp) =>
        findPicByLogin(Seq(userUploadDir,cacheDir), login.toLowerCase) match {
          case Some(res) => Left(Some(res))
          case None =>
            val f:String = emp.nickname match {
              case Some(x) => x
              case None => emp.firstname
            }
            findPicByName(Seq(cacheDir,imageDir), f, emp.lastname) match {
              case Some(res) =>
                val image =genImage(res).get
                Future {
                  Logger.debug(s"Genning Image  $login -> $res -- $cacheDir/${login}.jpg")
                  image.write(new File(s"$cacheDir/${login}-${System.currentTimeMillis()}.jpg"))
                }
                Right(image.bytes)
              case None => Left(None)
            }
        }
      case None =>
        if (currentOnly) {
          findPicByLogin(Seq(userUploadDir, cacheDir), login.toLowerCase) match {
            case Some(res) => Left(Some(res))
            case None => Left(None)
          }
        } else {
          Left(None)
        }
    }
  }
  protected def genImage(file:String): Some[WriteContext] = {
    val inImage: BufferedImage = ImageIO.read(new File(file))
    val theFace = FaceDetect.findFaces(inImage, 1, 40)
    Some(Image.fromAwt(theFace).scaleToWidth(300).fit(300,300).forWriter(JpegWriter()))
  }
  /* used as part of picture upload */
  protected def sizeImage(file:String):Some[WriteContext] = {
    val inImage: BufferedImage = ImageIO.read(new File( file))
    Some(Image.fromAwt(inImage).scaleToWidth(300).fit(300,300).forWriter(JpegWriter()))
  }

  /* make image a square */
  protected def makeSquare(file:String): Some[WriteContext] = {
    val inImage: BufferedImage = ImageIO.read(new File( file))
    Some(Image.fromAwt(inImage).scaleToWidth(300).fit(300,300).forWriter(JpegWriter()))
  }



  def headShot(login:String,notCurrent:Option[String]) = Action.async(bodyParser = Action.parser) { implicit request =>
    //val catsPrank: Boolean  = Option("Y") match {
    val catsPrank: Boolean  = request.session.get("cats") match {
      case Some(s) => if ( s.contentEquals("true")) {
        if ( catPercentage > random.nextDouble() ) {
          Logger.info("We have a Cat Lover!")
          true
        } else {
          false
        }
      } else {
        false
      }
      case None =>false
    }
    if (catsPrank) {
      val d = new File("app/assets/images/cat/")
      if ( d.exists && d.isDirectory ) {
       val cats: List[File] = d.listFiles().filter(_.isFile).filter(p => p.getName.endsWith(".jpg")).toList
        if ( cats.nonEmpty) {
          val index = random.nextInt(cats.size)
          //Logger.info(cats(index).getAbsolutePath)
          Future.successful(Ok(makeSquare(cats(index).getAbsolutePath).get.bytes).as("image/jpeg"))
        } else {
          Future.successful(Ok(makeSquare("app/assets/images/noFace.jpg").get.bytes).as("image/jpeg"))
        }
      } else {
        Logger.warn("No Cat Directory?")
        Future.successful(Ok(makeSquare("app/assets/images/noFace.jpg").get.bytes).as("image/jpeg"))
      }
    }
    else {
      val currentOnly: Boolean = if (notCurrent.getOrElse("N").equalsIgnoreCase("N")) {
        false
      } else {
        true
      }
      //Logger.info(s"HeadShot NotCurrent $notCurrent - currentOnly = $currentOnly ")
      findAndCacheHeadShot(login, currentOnly).map {
        case Left(oS) => oS match {
          case Some(path) =>
            Ok(makeSquare(path).get.bytes).as("image/jpeg")
          case None =>
            Ok(makeSquare("app/assets/images/noFace.jpg").get.bytes).as("image/jpeg")
            //TemporaryRedirect("/assets/images/noFace.jpg")
        }
        case Right(buffer) => Ok(buffer).as("image/jpeg")
      }
    }
  }


  val headShotUploadForm = Form(
    mapping(
      "login" -> nonEmptyText,
      "file" -> nonEmptyText
    )(headShotUpload.apply)(headShotUpload.unapply)
  )

  def headShotEdit(login: String ) = LDAPAuthAction {
    Action.async { implicit request =>
       user.isOwnerManagerOrAdmin(login, LDAPAuth.getUser()).map {
         case true =>
           Ok(views.html.image.uploadHeadShot(login, headShotUploadForm.fill(headShotUpload(login, ""))))
         case false =>
           NotFound("I'm sorry. you don't have access to edit that image")
       }
    }
  }
  def headShotUpdate(login:String ) = LDAPAuthAction  {
    Action.async(parse.multipartFormData) { implicit request =>
      user.isOwnerManagerOrAdmin(login,LDAPAuth.getUser()).map {
        case true =>
          request.body.file("file").map { picture =>
            val filename = picture.filename
            val contentType = picture.contentType match {
              case Some("image/png") => "png"
              case _ => "jpg"
            }
            val tmpDir = System.getProperty("java.io.tmpdir")
            val tempFile = s"$tmpDir/picutre-${login.toLowerCase}.$contentType"
            try {
              val f = new File(tempFile)
              f.delete()
            } catch {
              case _:Throwable =>
            }

            picture.ref.moveTo(new File(tempFile), replace = true)
            val sizedImage = sizeImage(tempFile).get
            sizedImage.write(s"$userUploadDir/${login.toLowerCase}-${System.currentTimeMillis()}.jpg")
            try {
              val f = new File(tempFile)
              f.delete()
            } catch {
              case _:Throwable =>
            }
            Redirect(routes.HeadshotController.headShotEdit(login))
          }.getOrElse {
            Redirect(routes.HeadshotController.headShotEdit(login)).flashing(
              "error" -> "Missing file")
          }
        case false =>
          NotFound("I'm sorry. you don't have access to edit that image")
      }
    }
  }
}
