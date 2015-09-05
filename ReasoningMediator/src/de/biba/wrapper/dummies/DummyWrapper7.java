package de.biba.wrapper.dummies;

import java.util.Properties;

import de.biba.mediator.DataSourceQuery;
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

// Erstellen, um mit AbstractCsvWrapper zu kombinieren
public class DummyWrapper7 extends DataSource{
	
	public static int INDIVIDUALS = 50;
	
	String namespace = "http://fma.biba.uni-bremen.de.Personen.owl";
	
	 OntClass person;
	 OntClass eltern;
	 OntClass kinder;
	 OntClass grosseltern;
	 OntClass young;
	 OntClass adult;
	 OntClass valuepartition;
	 OntClass female;
	 OntClass male;
	 
	 ObjectProperty hasGender;
	 ObjectProperty isGrandparent;
	 ObjectProperty isKind;
	 ObjectProperty isParent;
	 DatatypeProperty name;
	
	 
	 public DummyWrapper7() {
			super();
			person = baseModel.createOntClass(namespace, "Person");
			young = baseModel.createOntClass(namespace,"Young", "Person");
			kinder = baseModel.createOntClass(namespace,"Kinder", "Young");
			
			adult = baseModel.createOntClass(namespace, "Adult", "Person");
			eltern = baseModel.createOntClass(namespace, "Eltern", "Adult");
			grosseltern = baseModel.createOntClass(namespace, "Grosseltern", "Adult");
			
			//value 
			valuepartition = baseModel.createOntClass(namespace, "ValuePartition");
			female = baseModel.createOntClass(namespace, "Female", "ValuePartition");
			male = baseModel.createOntClass(namespace,"Male", "ValuePartition");
			

         //Functional
			hasGender = baseModel.createObjectProperty(namespace, "hasGender");
			hasGender.setFunctional(true);
			hasGender.getDomain().add(namespace+"#Person");
			hasGender.getRange().add(namespace+"#ValuePartition");
			
			isGrandparent = baseModel. createObjectProperty(namespace, "isGrandparent");
			isGrandparent.getDomain().add(namespace+"#Person");
			isGrandparent.getRange().add(namespace+"#Person");
			
			isParent = baseModel.createObjectProperty(namespace, "isParent");
			isParent.getDomain().add(namespace+"#Person");
			isParent.getRange().add(namespace+"#Person");
			
			isKind = baseModel.createObjectProperty(namespace, "isKind");
			isKind.getDomain().add(namespace+"#Person");
			isKind.getRange().add(namespace+"#Person");
			
			//Inverse
			baseModel.markInverseProperties(isKind.getUri(), isParent.getUri());
			
			
			//Functional
			name = baseModel.createDatatypeProperty(namespace, "name");
			name.getDomain().add(namespace+"#Person");
			name.setInverseFunctional(true);
			
		 }

	
	 
	     @Override
	     public OntModel queryData(DataSourceQuery pDsc){
	    	baseModel.setAbox(new ABox());
	    	Individual grandparent = baseModel.createIndividual(grosseltern);
	    	Individual parent = baseModel.createIndividual(eltern);
	    	Individual sibling = baseModel.createIndividual(kinder);
	    	Individual child = baseModel.createIndividual(kinder);
	    	Individual fm = baseModel.createIndividual(female);
	    	Individual ml = baseModel.createIndividual(male);
	    	
	    	
	    	baseModel.addProperty(grandparent, name, new StringDatatype("Ludwig"));
	    	baseModel.addProperty(grandparent, hasGender, ml);
	    	baseModel.addProperty(parent, name, new StringDatatype("Konstantin"));
	    	baseModel.addProperty(parent, hasGender, ml);
	    	baseModel.addProperty(sibling, name, new StringDatatype("Martin"));
	    	baseModel.addProperty(sibling, hasGender, ml);
	    	baseModel.addProperty(child, name, new StringDatatype("Gerd"));
	    	baseModel.addProperty(child, hasGender, ml);
	    	
	    	baseModel.addProperty(grandparent, isParent, parent);
	    	baseModel.addProperty(grandparent, isParent, sibling);
	    	baseModel.addProperty(grandparent, isGrandparent, child);
	    	baseModel.addProperty(child, isKind, parent);
	    	
	    	
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
