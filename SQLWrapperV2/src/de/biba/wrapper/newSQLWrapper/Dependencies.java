package de.biba.wrapper.newSQLWrapper;

import java.util.ArrayList;

import de.biba.wrapper.newSQLWrapper.Mapping.DatatypePropertyMapping;

public class Dependencies {

	/**[SQL Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
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
	
	private String propertyName ="";
	private ArrayList<DatatypePropertyMapping> allPrimaryProperties = new ArrayList<DatatypePropertyMapping>();
	private ArrayList<DatatypePropertyMapping> allNonPrimaryProperties = new ArrayList<DatatypePropertyMapping>();
	public ArrayList<DatatypePropertyMapping> getAllPrimaryProperties() {
		return allPrimaryProperties;
	}
	public void setAllPrimaryProperties(
			ArrayList<DatatypePropertyMapping> allPrimaryProperties) {
		this.allPrimaryProperties = allPrimaryProperties;
	}
	public ArrayList<DatatypePropertyMapping> getAllNonPrimaryProperties() {
		return allNonPrimaryProperties;
	}
	public void setAllNonPrimaryProperties(
			ArrayList<DatatypePropertyMapping> allNonPrimaryProperties) {
		this.allNonPrimaryProperties = allNonPrimaryProperties;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	
}
