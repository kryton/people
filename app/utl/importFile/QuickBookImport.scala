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

import java.sql.Date

/**
  * Created by iholsman on 4/5/2017.
  * All Rights reserved
  */

case class QuickBookImport(
                            personNumber: Long,
                            firstName: String,
                            nickName: Option[String],
                            lastName: String,
                            employeeStatus: String,
                            companyCode: Int,
                            companyCodeName: String,
                            costCenter: Long,
                            costCenterText: String,
                            personalArea: String,
                            personalSubArea: String,
                            employeeGroup: String,
                            position: String,
                            agency: String,
                            managerID: Option[Long],
                            managerName: Option[String],
                            managerLogin: Option[String],
                            executiveName: Option[String],
                            hireRehireDate: Option[Date],
                            terminationDate: Option[Date],
                            login: Option[String],
                            officeLocation: Option[String],
                            officeLocation2: Option[String],
                            employeeType:Option[String],
                            officeCity:Option[String],
                            officeStreet:Option[String],
                            officePOBox:Option[String],
                            officeRegion:Option[String],
                            officeZipcode:Option[String],
                            officeCountry:Option[String]
                          ) {
  def isFTE: Boolean = {
    employeeGroup.equalsIgnoreCase("EMPLOYEE_TYPE_PERMANENT")
  }

}
