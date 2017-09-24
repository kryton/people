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

package utl.importFile

/**
  * Created by iholsman on 4/15/2017.
  * All Rights reserved
  */
case class FeatureImport(feature: String,
                         program: Option[String],
                         isCID: Boolean,
                         isAnchor: Boolean,
                         priority: Int,
                         start: java.sql.Date,
                         finish: java.sql.Date,
                         projects: List[ProjectImport])

