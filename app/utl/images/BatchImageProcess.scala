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

package utl.images

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.nio.JpegWriter
import com.typesafe.config.ConfigFactory

import scala.reflect.io.{Directory, Path}

/**
*  Created by iholsman on 19/08/2014.
*  All Rights reserved
*/
object BatchImageProcess extends App {
  val srcDir = ConfigFactory.load().getString("image.directory")
  val srcContractorsDir = ConfigFactory.load().getString("image.directory") + "/Contractors"
  val destDir = ConfigFactory.load().getString("image.cache")

  System.out.println("Processing Contractors")
  for (fileName: Path <- Directory(srcContractorsDir).list.filter(
    p => {
    (p.extension.toLowerCase == "jpg" || p.extension.toLowerCase == "png") && !p.name.startsWith("._")
    }
  )
  ) {
      System.out.println(fileName.name)
      val inImage: BufferedImage = ImageIO.read(new java.io.File(fileName.toString()))
      val newName = fileName.name.split('.').init :+ "jpg" mkString "."
      try {
        val outImage = FaceDetect.findFaces(inImage, 1, 40)
        if (outImage.getWidth> outImage.getHeight) {
          Image.fromAwt(outImage).scaleToHeight(300).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
        } else {
          Image.fromAwt(outImage).scaleToWidth(300).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
        }

      } catch {
        case e: Exception =>
          System.err.println("Skipping " + fileName.name)
          e.printStackTrace()
          if (inImage.getWidth> inImage.getHeight) {
            Image.fromAwt(inImage).scaleToHeight(300).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
          } else {
            Image.fromAwt(inImage).scaleToWidth(300).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
          }

      }
   // ImageIO.write(outImage, "jpg", new java.io.File(destDir + "/" + newName))
  }
  System.out.println("Processing FTEs")
  for (fileName: Path <- Directory(srcDir).list.filter( p => {
    (p.extension.toLowerCase == "jpg" || p.extension.toLowerCase == "png") && !p.name.startsWith("._")
  })) {
    System.out.println(fileName.name)
    val inImage: BufferedImage = ImageIO.read(new java.io.File(fileName.toString()))
    val newName = fileName.name.split('.').init :+ "jpg" mkString "."
    try {
      val outImage = FaceDetect.findFaces(inImage, 1, 40)
      if (outImage.getWidth> outImage.getHeight) {
        Image.fromAwt(outImage).scaleToHeight(300).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
      } else {
        Image.fromAwt(outImage).scaleToWidth(300).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
      }
    } catch {
      case e: Exception =>
        System.err.println("Skipping " + fileName.name)
        e.printStackTrace()
        //Image.fromAwt(inImage).fit(300,300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
        if (inImage.getWidth > inImage.getHeight) {
          Image.fromAwt(inImage).scaleToHeight(300).fit(300, 300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
        } else {
          Image.fromAwt(inImage).scaleToWidth(300).fit(300, 300).forWriter(JpegWriter()).write(new File(destDir + "/" + newName))
        }
    }
  }
}
