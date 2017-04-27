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

package models.product

import javax.inject.Inject

import offline.Tables.Emprelations
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import projectdb.Tables
import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}
import util.importFile.FeatureImport

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/27/2017.
  * All Rights reserved
  */

class ProductFeatureRepo @Inject()(@NamedDatabase("projectdb")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  import projectdb.Tables._
  import projectdb.Tables.profile.api._

  def find(id:Int):Future[Option[ProductfeatureRow]] =
    db.run(Productfeature.filter(_.id === id).result.headOption)
  def findByMSProjectName( msProjectName :String): Future[Option[ProductfeatureRow]] =
    db.run(Productfeature.filter( _.msprojectname === msProjectName).result.headOption)
  def all: Future[Seq[ProductfeatureRow]] =
    db.run(Productfeature.sortBy( _.ordering).result)

  def search(searchString:String): Future[Seq[ProductfeatureRow]] = db.run{
    Productfeature.filter { m =>  m.name startsWith searchString
    }.sortBy(_.ordering).result
  }

  def findByProductTrack( productTrackId: Int): Future[Seq[(Tables.ProductfeatureRow, Tables.ProducttrackfeatureRow)]] = {
    db.run(Productfeature.join(Producttrackfeature).on(_.id === _.productfeatureid)
      .filter(_._2.producttrackid === productTrackId)
      .sortBy(_._2.priority).result)
  }

  def findByManagedClients( productFeatureId: Int): Future[Seq[(Tables.ManagedclientRow, Tables.ManagedclientproductfeatureRow)]] = {
    db.run(Managedclient.join(Managedclientproductfeature).on(_.id === _.managedclientid)
      .filter(_._2.productfeatureid === productFeatureId)
      .sortBy(_._1.name).result)
  }

  def findTracksByFeature(productFeatureId: Int): Future[Seq[(Tables.ProducttrackRow, Tables.ProducttrackfeatureRow)]] = {
    db.run(Producttrack.join(Producttrackfeature).on(_.id === _.producttrackid)
      .filter(_._2.productfeatureid === productFeatureId)
      .sortBy(_._2.priority).result)
  }

  def findByFeatureFlag( featureFlagId:Int): Future[Seq[(ProductfeatureRow, ProductfeatureflagRow)]] = {
    db.run(Productfeature.join(Productfeatureflag).on( _.id === _.productfeatureid )
      .filter(_._2.featureflagid === featureFlagId ).sortBy(_._1.ordering)  .result)
  }

  def findFeatureFlagsByFeature( productFeatureId:Int): Future[Seq[(FeatureflagRow, ProductfeatureflagRow)]] = {
    db.run(Featureflag.join(Productfeatureflag).on( _.id === _.featureflagid )
      .filter(_._2.productfeatureid === productFeatureId ).sortBy(_._1.ordering)  .result)
  }

  def findResourceTeams(productFeatureId:Int): Future[Seq[( Tables.ResourceteamproductfeatureRow, Tables.ResourceteamRow)]] = {
    db.run {
      Resourceteamproductfeature.filter(_.productfeatureid === productFeatureId)
        .join(Resourceteam).on(_.resourceteamid === _.id)
        .result
    }
  }

  def findFeatureByStage( stageId:Int): Future[Seq[ProductfeatureRow]] =
    db.run( Productfeature.filter(_.stageid === stageId ).sortBy(_.ordering).result)

  def insert(pt: ProductfeatureRow): Future[ProductfeatureRow] = db
    .run(Productfeature returning Productfeature.map(_.id) += pt)
    .map(id => pt.copy(id = id))


  def update(id: Int, pt:ProductfeatureRow): Future[Boolean] = {
    db.run(Productfeature.filter(_.id === id).update( pt.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Int) =
    db.run(Productfeature.filter(_.id === id).delete) map { _ > 0 }

  def repopulate( features:Seq[FeatureImport])(
            implicit
                  productTrackRepo: ProductTrackRepo,
                  resourceTeamRepo: ResourceTeamRepo,
                  stageRepo: StageRepo,
                  managedClientRepo: ManagedClientRepo,
                  projectRepo: ProjectRepo,
                  projectDependencyRepo: ProjectDependencyRepo
                ): Future[(Seq[ProductfeatureRow])] = {
    val maxPri = features.map( _.priority).max +1
    val oldMSFL = db.run( Productfeature.filterNot(_.msprojectname.isEmpty).result )

    val newFeatures = Future.sequence(features.map{ feature =>
      val isActive = !feature.projects.exists(_.disabled == false)
      val prog: String = feature.program.getOrElse("Internal")
    //  Logger.info(s"Inserting Feature:${feature.feature} (${feature.feature.size})")
      (for{
        pt <- productTrackRepo.findOrCreate(prog,prog )
        pf <- findByMSProjectName(feature.feature)
        s  <- stageRepo.findOrCreate("On Roadmap")
        m <- managedClientRepo.all
      } yield( pt, pf,s,m, oldMSFL )).map{ x =>
        val stage = x._3
        val track = x._1
        val mc = x._4.map{ x => x.msprojectname -> x}.toMap

        val foundPFF:Future[(ProductfeatureRow,
            Seq[(ProducttrackRow,ProducttrackfeatureRow)],
            Seq[(ResourceteamproductfeatureRow,ResourceteamRow)]
          )] = x._2 match {
          case Some(pf) =>
            val newPF =  pf.copy(iscid = Some( feature.isCID), isanchor = Some(feature.isAnchor),ordering = maxPri - feature.priority)
            val tracksFeature: Future[Seq[(ProducttrackRow, ProducttrackfeatureRow)]] = findTracksByFeature(pf.id)

            val insertIt = this.update(pf.id, newPF ).map( ignore => newPF )
            for {
              t <- tracksFeature
              i <- insertIt
              r <- findResourceTeams(pf.id)
            } yield (i, t, r)
          case None =>
          //  Logger.info(s"PFRepo - Repoulate - Insert PFR ${feature.feature}(${feature.feature.size})")
            this.insert(
              ProductfeatureRow(id=0,name=feature.feature,
                execsummary = "TBD",
                ordering = maxPri - feature.priority,
                stageid = stage.id,
                msprojectname = Some(feature.feature),
                mspriority = Some( feature.priority),
                isactive = Some(isActive), iscid = Some(feature.isCID), isanchor = Some(feature.isAnchor),
                start = Some(feature.start),
                finish = Some(feature.finish)
              )
            ).map{ newOne =>
             // Logger.info(s"PFRepo - Repoulate - Insert PFR3${feature.feature}(${feature.feature.size})")
              ( newOne, Seq.empty, Seq.empty) }
        }

        foundPFF.map { addition =>
          val currentPF: ProductfeatureRow = addition._1
          val pff = addition._2
          val resourceTeams = addition._3
          if ( pff.isEmpty) {
            val newProducttrackFeatureRow = ProducttrackfeatureRow(id=0,
              productfeatureid =  currentPF.id,
              producttrackid = track.id,
              priority = maxPri - feature.priority,
              allocation =  1)

            insert(newProducttrackFeatureRow).map{ newOne => Seq(newOne)}
          } else {
            // TODO
            Future.successful(pff)
          }
          //Logger.info(s"PFRepo - Repoulate - Insert PFR2 ${feature.feature}(${feature.feature.size})")

          if ( currentPF.iscid.getOrElse(false)) {
            deleteMClientForFeature(currentPF.id).map { ignore =>
              val searchStrings = mc.keys.flatten.map( _.toLowerCase )
              val fName = currentPF.msprojectname.getOrElse("").toLowerCase
              val m = searchStrings.filter( p =>  fName.contains(p))
              if ( m.nonEmpty) {
                val mc2 = mc.map{ x => x._1.getOrElse("").toLowerCase -> x._2   }
                val alloc = BigDecimal( 1.0  / m.size )
                m.foreach{ matchKey =>
                  val mcV = mc2(matchKey)
                  insert( ManagedclientproductfeatureRow(id=0,productfeatureid = currentPF.id,
                    managedclientid = mcV.id,
                    allocation = alloc ))

                }
              }
            }
          }

          val d: Future[(Iterable[Option[ResourceteamprojectRow]], Seq[Seq[Option[ProjectdependencyRow]]])] = deleteResourcesForFeature( currentPF.id).map { ignore =>
            val summary: Map[String, (Double, Int, Int)] = feature.projects
              .filter( p => p.resource.nonEmpty)
              .map{ p =>
                ( p.resource,  p.devEstimate * (1 - p.percentComplete), p.devEstimate.toInt, p.resourceNumber )
            }.groupBy( _._1).map { gp =>
              ( gp._1, gp._2.foldLeft( (0.0,0,0) ) ( (z,i) => (z._1 + i._2, z._2 + i._3 , Math.max(z._3 , i._4)  )) )
            }
            summary.map { proj =>
              resourceTeamRepo.findOrCreate(proj._1).map { rt =>
                val newRTPF = ResourceteamproductfeatureRow(id = 0,
                  featuresize = proj._2._2,
                  maxdevs = proj._2._3,
                  resourceteamid = rt.id,
                  productfeatureid = currentPF.id,
                  featuresizeremaining = Some(proj._2._1))
                insert(newRTPF)
              }
            }
           val xyz = projectRepo.repopulate( currentPF.id, feature.projects)
            xyz
          }.flatMap(identity)

          d.map{ ig =>
            currentPF
          }
          //currentPF
        }.flatMap(identity)
      }.flatMap(identity)
    })

    val deletes = (for{
      oldF <- oldMSFL
      newF <- newFeatures
    } yield (oldF,newF)).map { x =>
      val o: Map[String, ProductfeatureRow] = x._1.map { nn => nn.msprojectname.getOrElse("") -> nn}.toMap
      val n: Map[String, ProductfeatureRow] = x._2.map { nn => nn.msprojectname.getOrElse("") -> nn}.toMap

      val res = (o.keySet -- n.keySet).map { toD:String =>
        val deleteId = o(toD).id
        (for {
          r <- deleteResourcesForFeature(deleteId)
          t <- deleteTrackForFeature(deleteId)
          m <- deleteMClientForFeature(deleteId)
          f <- deleteFFlagForFeature(deleteId)
        } yield ( r, t, m,f)).map { y =>
          Logger.info( s"Deleting Feature ID $deleteId")
          delete(deleteId).map( x=> x)
        }
      }
    }
    newFeatures

  }


  protected def insert(ptf: ProducttrackfeatureRow): Future[ProducttrackfeatureRow] = db
    .run(Producttrackfeature returning Producttrackfeature.map(_.id) += ptf)
    .map(id => ptf.copy(id = id))

  protected def insert(rtpf: ResourceteamproductfeatureRow): Future[ResourceteamproductfeatureRow] = db
    .run(Resourceteamproductfeature returning Resourceteamproductfeature.map(_.id) += rtpf)
    .map(id => rtpf.copy(id = id))

  protected def insert(mcpf: ManagedclientproductfeatureRow): Future[ManagedclientproductfeatureRow] = db
    .run(Managedclientproductfeature returning Managedclientproductfeature.map(_.id) += mcpf)
    .map(id => mcpf.copy(id = id))

  protected def deleteResourcesForFeature(featureId: Int): Future[Boolean] =
    db.run(Resourceteamproductfeature.filter(_.productfeatureid === featureId).delete) map {  _ > 0  }

  protected def deleteTrackForFeature(featureId: Int): Future[Boolean] =
    db.run(Producttrackfeature.filter(_.productfeatureid === featureId).delete) map {  _ > 0  }

  protected def deleteMClientForFeature(featureId: Int): Future[Boolean] =
    db.run(Managedclientproductfeature.filter(_.productfeatureid === featureId).delete) map {  _ > 0  }

  protected def deleteFFlagForFeature(featureId: Int): Future[Boolean] =
    db.run(Productfeatureflag.filter(_.productfeatureid === featureId).delete) map {  _ > 0  }

}
