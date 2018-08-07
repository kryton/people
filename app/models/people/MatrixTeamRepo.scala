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

package models.people

import javax.inject.Inject

import offline.Tables
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
//import slick.lifted.Tag
//import slick.model.Table


/**
  * Created by iholsman on 3/26/2017.
  * All Rights reserved
  */

case class MatrixTeamTreeNode( node: Tables.MatrixteamRow, children: Set[MatrixTeamTreeNode], size:Int) {
  def toJson(level:Int = 0): String = {
    if (children.isEmpty) {
      "{ \"name\": \"" + node.name + "\", \"level\":"+level + ", \"size\":" + size +"}"
    }  else {
      "{ \"name\": \"" + node.name + "\", \"level\":"+level + ",\n \"children\": [\n" + children.map{ x => x.toJson(level+1) }.mkString(",") + "\n] }"
    }
  }
}

class MatrixTeamRepo @Inject()(@NamedDatabase("default")  protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import offline.Tables._
  import offline.Tables.profile.api._

  def find(id:Long):Future[Option[MatrixteamRow]] = db.run(Matrixteam.filter(_.id === id).result.headOption)
  def find(ids:Seq[Long]):Future[Seq[MatrixteamRow]] = db.run(Matrixteam.filter(_.id inSet ids).result)
  def findSize(id:Long):Future[Option[(MatrixteamRow,Int)]] = {
    val q = Matrixteam.filter(_.id === id).join(Matrixteammember).on(_.id === _.matrixteammemberid)
    db.run( q.result).map { res =>
      res.groupBy(_._1).map{ l => (l._1, l._2.size)}.headOption
    }
  }
  def find(name:String):Future[Option[MatrixteamRow]] = db.run(Matrixteam.filter(_.name === name).result.headOption)

  def search(searchString:String): Future[Seq[MatrixteamRow]] = db.run{
    Matrixteam.filter { m =>
      m.name startsWith searchString
    }.sortBy(_.name).result
  }

  def findParent(id:Long): Future[Option[MatrixteamRow]] = {

    db.run(
      a = (for {
        (c, p) <- Matrixteam join Matrixteam on (_.parent === _.id) filter (_._1.id === id)
      } yield p).result.headOption
    )
  }

  def all: Future[Seq[MatrixteamRow]] = db.run{
    Matrixteam.sortBy(_.name.toLowerCase).result
  }

  def insert(empTag: MatrixteamRow): Future[Tables.MatrixteamRow] =
    db.run(Matrixteam returning Matrixteam.map(_.id) += empTag )
    .map(id => empTag.copy(id = id ))


  def update(id: Long, empTag:MatrixteamRow) = {
    db.run(Matrixteam.filter(_.id === id).update( empTag.copy(id = id))) map { _ > 0 }
  }

  def delete(id: Long) =
    db.run(Matrixteam.filter(_.id === id).delete) map { _ > 0 }

  def bulkInsertUpdate(rows:Iterable[(String,Option[String])]) (implicit matrixTeamMemberRepo: MatrixTeamMemberRepo): Future[Iterable[MatrixteamRow]] = {
    Future.sequence(rows.map { row =>
      (for {
        team <- find(row._1)
        parent <- row._2 match {
          case Some(p: String) => find(p).map {
            case Some(rec) => Some(rec.id)
            case None => None
          }
          case None => Future.successful(None)
        }
      } yield (team, parent)
        ).map { result =>
        result._1 match {
          case None => Future.successful(insert(MatrixteamRow(id = 0, name = row._1, owner = None, parent = result._2)))
          case Some(pc) =>
            // Update parent, and delete all the team members (if any)
            val newRec = pc.copy(parent = result._2 )
            update(pc.id,newRec).map { ignore =>
              matrixTeamMemberRepo.deleteByTeam(pc.id).map { _ => newRec }
            }
        }
      }.flatMap(identity).flatMap(identity)
    })
  }
  /* for top of the tree(s) */
  def findChildren( ): Future[Seq[MatrixteamRow]] = {
    db.run(
      Matrixteam.filter(_.parent.isEmpty).join(Matrixteam).on( _.id === _.parent).map(_._2).result
    )
  }
  def findChildren(id:Long): Future[Seq[MatrixteamRow]] = db.run(Matrixteam.filter(_.parent === id).result)


  def findChildren(ids:Set[Long]) : Future[Seq[MatrixteamRow]] = {
    db.run( Matrixteam.filter( _.parent inSet ids).result)
  }
  def managementTreeDownAsTree():Future[Seq[MatrixTeamTreeNode]] = {
    findChildren().map{ rootNodes =>
      Future.sequence(rootNodes.map{ rootNode =>
        managementTreeDownAsTree(rootNode.id)
      })
    }.flatMap(identity).map  { seqO:Seq[Option[MatrixTeamTreeNode]] =>
      seqO.flatten
    }
  }

  def managementTreeDownAsTree(id:Long):Future[Option[MatrixTeamTreeNode]] = {

    findSize(id).map{
      case Some(rec) => findChildren(rec._1.id).map { children =>
        Future.sequence(children.map { child =>
          managementTreeDownAsTree(child.id)
        })
      }.flatMap(identity).map { childSet =>
          val children = childSet.flatten
          Some(MatrixTeamTreeNode(rec._1, children.toSet, rec._2))
        }
      case None => Future.successful(None)
    }
  }.flatMap(identity)
}
