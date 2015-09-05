package de.biba.mediator.webservice;

import de.biba.wrapper.Consistency;

/**[QLMResponseSEMedService. This is a wrapper to tranform a xml based data base schema to an ontology]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    **/

public class LightDataSourceDescription {

	private String nameOfClass;
	private String pathOfPropertyFile;
	private String isConsistence = String.valueOf(Consistency.UNKNOWN);
	public String getNameOfClass() {
		return nameOfClass;
	}
	public void setNameOfClass(String nameOfClass) {
		this.nameOfClass = nameOfClass;
	}
	public String getPathOfPropertyFile() {
		return pathOfPropertyFile;
	}
	public void setPathOfPropertyFile(String pathOfPropertyFile) {
		this.pathOfPropertyFile = pathOfPropertyFile;
	}
	public String getIsConsistence() {
		return isConsistence;
	}
	public void setIsConsistence(String isConsistence) {
		this.isConsistence = isConsistence;
	}
	
	
	
}
