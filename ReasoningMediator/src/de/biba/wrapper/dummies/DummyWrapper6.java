package de.biba.wrapper.dummies;




	
	import java.util.Properties;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.Datatypes;
import de.biba.mediator.constraints.SomeConstraint;
import de.biba.ontology.ABox;
import de.biba.ontology.ComplexOntClass;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.DataSource;
import de.biba.wrapper.ValidationReport;
/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/

	public class DummyWrapper6 extends DataSource {
		public static int INDIVIDUALS = 50;
		String namespace = "http://sfb637.uni-bremen.de";
		//String namespace = "http://fma.biba.uni-bremen.de.Personen.owl";
		//OntClass lebewesen ;
		//OntClass mensch ;
		//OntClass kind ;
		//OntClass kinderloserMensch;
		//ObjectProperty verwandtMit;
		//ObjectProperty kindVon;
		//ObjectProperty ElternVon;
	    //	DatatypeProperty alter;
		//DatatypeProperty name;
		//ComplexOntClass benannter;
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
		 DatatypeProperty hasName;
		 
		
		
	
	      
		 public DummyWrapper6() {
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
			hasName = baseModel.createDatatypeProperty(namespace, "hasName");
			hasName.getDomain().add(namespace+"#Person");
			hasName.setInverseFunctional(true);
			
		 }


        // Erstellen Ontologie
		@Override
		public OntModel queryData(DataSourceQuery pDsc) {
			// TODO Auto-generated method stub
			baseModel.setAbox(new ABox());
			Individual grandparent = baseModel.createIndividual(grosseltern);
			Individual parent1 = baseModel.createIndividual(eltern);
			Individual parent2 = baseModel.createIndividual(eltern);
			Individual child1 = baseModel.createIndividual(kinder);
			Individual child2 = baseModel.createIndividual(kinder);
			Individual child3 = baseModel.createIndividual(kinder);
			Individual child4 = baseModel.createIndividual(kinder);
			Individual grandchild1 = baseModel.createIndividual(kinder);
			Individual grandchild2 = baseModel.createIndividual (kinder);
			Individual fm = baseModel.createIndividual(female);
			Individual ml = baseModel.createIndividual(male);
			
			
			baseModel.addProperty(grandparent, hasName, new StringDatatype("Dave"));
			baseModel.addProperty(grandparent, hasGender, ml);
			baseModel.addProperty(parent1, hasName, new StringDatatype("Bob"));
			baseModel.addProperty(parent1, hasGender, ml);
			baseModel.addProperty(parent2, hasName, new StringDatatype("Jane"));
			baseModel.addProperty(parent2, hasGender, fm);
			baseModel.addProperty(child1, hasName, new StringDatatype("Mary"));
			baseModel.addProperty(child1, hasGender,fm);
			baseModel.addProperty(child2, hasName, new StringDatatype("Sue"));
			baseModel.addProperty(child2, hasGender, fm);
			baseModel.addProperty(child3, hasName, new StringDatatype("Anne"));
			baseModel.addProperty(child3, hasGender, fm);
			baseModel.addProperty(child4, hasName, new StringDatatype("Scott"));
			baseModel.addProperty(child4, hasGender,ml);
			baseModel.addProperty(grandchild1, hasName, new StringDatatype("Tom"));
			baseModel.addProperty(grandchild1, hasGender, ml);
			baseModel.addProperty(grandchild2, hasName, new StringDatatype("Jim"));
			baseModel.addProperty(grandchild2, hasGender, ml);
			
			
			baseModel.addProperty(parent1, isParent, child1);
			baseModel.addProperty(grandparent, isParent, parent1);
			
			baseModel.addProperty(child1, isKind, parent1);
			baseModel.addProperty(child1, isKind, parent2);
			baseModel.addProperty(child2, isKind, parent1);
			baseModel.addProperty(child2, isKind, parent2);
			baseModel.addProperty(parent1, isParent, child3);
			baseModel.addProperty(parent2, isParent, child3);
			baseModel.addProperty(parent1, isParent, child4);
			baseModel.addProperty(parent2, isParent, child4);
			
			baseModel.addProperty(child1, isParent, grandchild1);
			baseModel.addProperty(child1, isParent, grandchild2);
			baseModel.addProperty(parent1, isGrandparent,grandchild1);
			
			
			
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
	
	
	
	
	
	
	

