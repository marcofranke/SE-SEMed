package de.biba.wrapper.dummies;

import java.util.Properties;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.constraints.CardinalityConstraint;
import de.biba.mediator.constraints.SomeConstraint;
import de.biba.ontology.ABox;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
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

public class DummyWrapper2 extends DataSource {
	OntClass mensch ;
	OntClass human ;
	OntClass tier;
	OntClass arbeitsloser;
	OntClass tierhalter;
	DatatypeProperty name;
	DatatypeProperty beruf;
	ObjectProperty hatTier;

	String namespace = "http://sfb637.uni-bremen.de";
	
	public DummyWrapper2(){
		super();
		
		mensch = baseModel.createOntClass(namespace,  "Mensch", "Lebewesen");
		human = baseModel.createOntClass(namespace,  "Human");
		tier = baseModel.createOntClass(namespace, "Tier", "Lebewesen");
		name = baseModel.createDatatypeProperty(namespace, "name");
		name.getDomain().add(namespace+"#Mensch");
		beruf = baseModel.createDatatypeProperty(namespace, "beruf");
		beruf.getDomain().add(namespace+"#Mensch");
		arbeitsloser = baseModel.createComplexOntClass(namespace,"Arbeitsloser",/**new ClassConstraint("",mensch.getUri()),*/ new CardinalityConstraint(beruf,0));
		hatTier = baseModel.createObjectProperty(namespace, "hatHaustier");
		baseModel.markEquivalentClasses(human, mensch);
		tierhalter = baseModel.createComplexOntClass(namespace, "Tierhalter", new SomeConstraint(hatTier, tier.getUri()));
	}
	
	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		boolean insertBeruf = pDsc.getPropertyNames().contains(namespace+"#beruf");
		boolean insertName = pDsc.getPropertyNames().contains(namespace+"#name");
		baseModel.setAbox(new ABox());
		baseModel.resetIndividualCounter();
		for(int i=0; i<DummyWrapper1.INDIVIDUALS;i++){
			Individual ind = baseModel.createIndividual(mensch);
			if(insertName)
				baseModel.addProperty(ind, name, new StringDatatype("Elter"+i));
			if(insertBeruf){
				if(i%10==0)
					baseModel.addProperty(ind,beruf, new StringDatatype("Informatiker"));
				else if(i%10==1)
					baseModel.addProperty(ind,beruf, new StringDatatype("Mathematiker"));
			}
			if(i%6==0){
				Individual t = baseModel.createIndividual(tier);
				
				baseModel.addProperty(t, name, new StringDatatype("Tier"+i));
				baseModel.addProperty(ind, hatTier, t);
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
