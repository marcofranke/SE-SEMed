package de.biba.wrapper.simpleXMLWrapper;

import java.text.ParseException;
import java.util.Comparator;

import de.biba.ontology.Individual;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;

/**[XML Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut f�r Produktion und Logistik GmbH)]

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

public class ConditionalDatatypePropertyCreator extends DatatypePropertyCreator{
	Comparator<Datatype> comparator;
	Datatype value;
	
	@Override
	public void addProperties(OntModel pModel, IReader pReader, Individual pInd) {
		Datatype value;
		try {
			value = pReader.getValue(p.getUri(), column);
			if(value==null || comparator.compare(value, this.value)!=0)
				return;
		} catch (ParseException e) {
			return;
		}
		pModel.addProperty(pInd, p, value);
	}
}
