package de.biba.report;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
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

import java.util.ArrayList;
import java.util.Set;

import de.biba.mediator.Datatypes;
import de.biba.ontology.DatatypeProperty;

public class DataProperyProperties {

	private DatatypeProperty property = null;
	private FailureTupel failure = null;
	private boolean isFunctionalYes = false;
	private boolean isFunctionalNo = false;
	private boolean isInverseFunctionalYes = false;
	private boolean isInverseFunctionalNo = false;
	ArrayList<String> datatypes = new ArrayList<String>();
	
	
	public void addInput( DatatypeProperty property){
		this.property = property ;
		Set<de.biba.mediator.Datatypes> allTypes = property.getRange();
		//Datentypen speichern
		for (Datatypes str : allTypes){
			String dType = str.toString();
			if (!(datatypes.contains(dType))){
				datatypes.add(dType);
			}
		}
		//Eigenschaften des Propertoes abfragen
		if (property.isFunctional()){
			isFunctionalYes = true;
			
		}
		else{
			isFunctionalNo = true;
		}
		
		
		if (property.isInverseFunctional()){
			isInverseFunctionalYes = true; 
		}
		else{
			isInverseFunctionalNo = true; 
		}
	
	}
	
	
	
	
public FailureTupel getFailure() {
		return failure;
	}




public boolean validateProperty(){
	failure = new FailureTupel();
	boolean result = true;
	failure.setProperty(property);
	
	if (isFunctionalNo && isFunctionalYes){
		failure.allDetectedInconsistencies.add(InconsistencyType.FUNCTIONAL_CONSTRAINT_VIOLETED);
		result = false;
	}
	if (isInverseFunctionalNo && isInverseFunctionalYes){
		failure.allDetectedInconsistencies.add(InconsistencyType.INVERSEFUNCTIONAL_CONSTRAINT_VIOLETED);
		result = false;
	}
	if (datatypes.size() > 1){
		failure.allDetectedInconsistencies.add(InconsistencyType.DATATYPE_CONSTRAINT_VIOLETED);
		result = false;
	}
	return result;
}




@Override
public String toString() {
	return "DataProperyProperties [property=" + property + ", failure="
			+ failure + ", isFunctionalYes=" + isFunctionalYes
			+ ", isFunctionalNo=" + isFunctionalNo
			+ ", isInverseFunctionalYes=" + isInverseFunctionalYes
			+ ", isInverseFunctionalNo=" + isInverseFunctionalNo
			+ ", datatypes=" + datatypes + "]";
}
	

}
