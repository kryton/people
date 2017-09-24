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

package utl

import play.api.Logger

/**
*  Created by iholsman on 11/08/2014.
*  All Rights reserved
*/

case class Page[A](itemsIn: Seq[A], page: Int = 0,  pageSize:Int = 10) {
  val offset: Int = page * pageSize
  lazy val items: Seq[A] = itemsIn.slice(offset, offset + pageSize)
 // Logger.info(s"PAGE - $page items.size = ${items.size} itemsIn.size = ${itemsIn.size}")
  lazy val prev: Option[Int] = Option(page - 1).filter(_ >= 0)
  val total: Int = itemsIn.size
  lazy val next: Option[Int] = Option(page + 1).filter(_ => (offset + items.size) < total)
}
