package de.biba.wrapper.simpleXMLWrapper;

import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;

/**[XML Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
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

public class ObjectPropertyLinkingObject {

	private Individual indi = null;
	private String specificDomain = null; // This is the domain of the concept
	private String objectPropertyName = null;
	private ObjectProperty ob = null;
	private String TargetIndividualID = null;
	public Individual getIndi() {
		return indi;
	}
	
	
	public ObjectProperty getOb() {
		return ob;
	}


	public void setOb(ObjectProperty ob) {
		this.ob = ob;
	}


	


	public void setIndi(Individual indi) {
		this.indi = indi;
	}
	public String getObjectPropertyName() {
		return objectPropertyName;
	}
	public void setObjectPropertyName(String objectPropertyName) {
		this.objectPropertyName = objectPropertyName;
	}
	public String getTargetIndividualID() {
		return TargetIndividualID;
	}
	public void setTargetIndividualID(String targetIndividualID) {
		TargetIndividualID = targetIndividualID;
	}
	public ObjectPropertyLinkingObject(Individual indi,
			String objectPropertyName, String targetIndividualID, String domain) {
		super();
		this.indi = indi;
		this.specificDomain = domain;
		this.objectPropertyName = objectPropertyName;
		TargetIndividualID = targetIndividualID;
	}


	public String getSpecificDomain() {
		return specificDomain;
	}


	public void setSpecificDomain(String specificDomain) {
		this.specificDomain = specificDomain;
	}
	
	
}
