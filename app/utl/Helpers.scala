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

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by iholsman on 3/31/2017.
  * All Rights reserved
  */
object Helpers {
  def mergeFutureSets[X](fl: Future[Set[X]]*)(implicit executionContext: ExecutionContext): Future[Set[X]] =
    Future.fold(fl)(Set.empty[X])(_ ++ _)
}
