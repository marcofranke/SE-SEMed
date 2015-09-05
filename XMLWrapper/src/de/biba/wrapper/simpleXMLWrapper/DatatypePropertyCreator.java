package de.biba.wrapper.simpleXMLWrapper;

import java.sql.ResultSet;
import java.text.ParseException;

import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;

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

public class DatatypePropertyCreator {

	DatatypeProperty p;
	String column;
	Datatype resultedValue = null;
	
	
	public Datatype getResultedValue() {
		return resultedValue;
	}

	public boolean isPropertyInverseFunctional(){
		return p.isInverseFunctional();
	}
	
	public Datatype getValueOfProperty(IReader pReader) {
		Datatype value;
		try {
			value = pReader.getValue(p.getUri(), column);
			return value;
		}
		catch (ParseException e) {
			return null;
		}
	}
	
	public void addProperties(OntModel pModel, IReader pReader, Individual pInd) {
		Datatype value;
		try {
			value = pReader.getValue(p.getUri(),column);
			
			if(value==null)
				return;
		} catch (ParseException e) {
			return;
		}
		resultedValue = value;
		boolean contain = false;
		String source ="";
		String target ="";
		if (pModel.getAbox().dtProperties.containsKey(p.getUri())){
		for (DatatypePropertyStatement st : pModel.getAbox().dtProperties.get(p.getUri())){
			 source = st.object.toString();
			 target = value.toString();
			if ((st.subject.equals(pInd)) && (source.equals(target))){
				contain= true;
			}
		}
		}
		if (contain==false) {
			pModel.addProperty(pInd, p, value);
		}
	}


	

}
