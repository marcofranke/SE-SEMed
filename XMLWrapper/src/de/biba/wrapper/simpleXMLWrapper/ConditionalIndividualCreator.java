package de.biba.wrapper.simpleXMLWrapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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

public class ConditionalIndividualCreator extends IndidividualCreator{
	String column;
	Comparator<Datatype> comparator;
	Datatype value;
	
	

	@Override
	public Individual createIndividual(OntModel pModel, IReader pReader, ArrayList<ObjectPropertyLinkingObject> list,HashMap<String, Individual>allSortedIndis) {
		
		
		if ((datatypeProperties.size()== 0) && (objectProperties.size()==0)){
			try {
				Datatype value = pReader.getValue("",column);
				if (value == null){
					return null;
				}
				if (value.toString().length()==0){
					return null;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		
		for(DatatypePropertyCreator dpc:datatypeProperties)	{
			if (dpc.isPropertyInverseFunctional()){
				Datatype value = dpc.getValueOfProperty(pReader);
				if (value== null){
					return null;
				}
				if (value.toString().length()==0){
					return null;
				}
			}
		}
		
		
		try {
			Datatype dt = pReader.getValue("",column);
			if(comparator.compare(value, dt)!=0)
				return null;
		} catch (ParseException e) {
			return null;
		}
		Individual ind = pModel.createIndividual(oc);
		String id = "-1";
		for(DatatypePropertyCreator dpc:datatypeProperties)	{		
			dpc.addProperties(pModel, pReader, ind);
			if (dpc.isPropertyInverseFunctional()){
				id = dpc.getResultedValue().toString();
				if (allSortedIndis.containsKey(dpc.getResultedValue().toString())==false){
					allSortedIndis.put(dpc.getResultedValue().toString(), ind);
					}
			}
		}
		for(ObjectPropertyCreator opc: objectProperties)
			try{
				String idOfUnknownObjectProperty = id+";"+opc.op.getUri();	
				ObjectPropertyLinkingObject linking = new ObjectPropertyLinkingObject(ind, opc.op.toString(), idOfUnknownObjectProperty,oc.getUri());
				linking.setOb(opc.op);
				linking.setOb(opc.op);
				list.add(linking);
				}
				catch(Exception e){
					System.out.println("Error: " + e.getLocalizedMessage());
				}
		return ind;
	}
}
