package de.biba.wrapper.dummies;

import java.util.Properties;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.constraints.ClassConstraint;
import de.biba.ontology.ABox;
import de.biba.ontology.ComplexOntClass;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.DataSource;
import de.biba.wrapper.ValidationReport;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/

public class DummyWrapper3 extends DataSource {

	OntClass mann;
	OntClass frau;
	ComplexOntClass zwitter;
	DatatypeProperty name;
	
	public DummyWrapper3(){
		String namespace = "http://www.samsys-lufo.de";
		mann = baseModel.createOntClass(namespace, "Mann", "Mensch");
		frau = baseModel.createOntClass(namespace, "Frau", "Mensch");
		name = baseModel.createDatatypeProperty(namespace, "name");
		zwitter = baseModel.createComplexOntClass(namespace, "Zwitter", new ClassConstraint(null, mann.getUri()), new ClassConstraint(null, frau.getUri()));
	}
	
	
	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		baseModel.abox = new ABox();
		baseModel.resetIndividualCounter();
		for(int i=0; i<DummyWrapper1.INDIVIDUALS;i++){
			if(i%10!=5){
				Individual ind = baseModel.createIndividual(mann);
				baseModel.addProperty(ind, name, new StringDatatype("Mann"+i));
				ind = baseModel.createIndividual(frau);
				baseModel.addProperty(ind, name, new StringDatatype("Frau"+i));
			}
			else{
				Individual ind = baseModel.createIndividual(mann);
				baseModel.addProperty(ind, name, new StringDatatype("Zwitter"+i));
				baseModel.addAdditionalType(ind, frau);
			}
		}
		return baseModel;
	}


	@Override
	public void initialize(String pPropertyFile) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getPathOfConfigurationFile() {
		// TODO Auto-generated method stub
		return null;
	}


	

}
