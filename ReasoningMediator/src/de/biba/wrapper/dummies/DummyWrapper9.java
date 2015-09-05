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

public class DummyWrapper9 extends DataSource {
	
	public static final int INDIVIDUALS = 50;
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
	 OntClass Student;
	 OntClass Lehrer;
	 OntClass Person;
	 OntClass Auto;
	
	 


	 DatatypeProperty name_s;
	 DatatypeProperty name_l;
	 DatatypeProperty name;
	 
	 
	 public DummyWrapper9() {
			super();
			
			Student = baseModel.createOntClass(namespace, "Student");
			Lehrer = baseModel.createOntClass(namespace, "Lehrer");
			Person = baseModel.createOntClass(namespace, "Person");
			Auto = baseModel.createOntClass(namespace, "Auto");
			
			//DataProperty InverseFunctional
			
			name_s = baseModel.createDatatypeProperty(namespace, "name_s");
			name_s.getDomain().add(namespace + "#Student");
			name_s.setInverseFunctional(true);
			
			name_l = baseModel.createDatatypeProperty(namespace, "name_l");
			name_l.getDomain().add(namespace + "#Lehrer");
			name_l.setInverseFunctional(true);
			
			name = baseModel.createDatatypeProperty(namespace, "name");
			name.getDomain().add(namespace + "#Person");
			name.getDomain().add(namespace + "#Auto");
			name.setInverseFunctional(true);

			
		 }


     // Erstellen Ontologie
		@Override
		public OntModel queryData(DataSourceQuery pDsc) {
			// TODO Auto-generated method stub
			baseModel.setAbox(new ABox());
			
			
			
			Individual lehrer1 = baseModel.createIndividual (Lehrer);
			Individual lehrer2 = baseModel.createIndividual (Lehrer);
			
			Individual student1 = baseModel.createIndividual(Student);
			Individual student2 = baseModel.createIndividual (Student);
			
			Individual person1 = baseModel.createIndividual(Person);
			Individual person2 = baseModel.createIndividual (Person);
			
			Individual auto1 = baseModel.createIndividual (Auto);
			Individual auto2 = baseModel.createIndividual (Auto);
			
			
			baseModel.addProperty (student1, name_s, new StringDatatype("S1"));
			baseModel.addProperty(student2, name_s, new StringDatatype("S2"));
			
			baseModel.addProperty (lehrer1, name_l, new StringDatatype("L1"));
			baseModel.addProperty (lehrer2, name_l, new StringDatatype("L2"));
			
			baseModel.addProperty (person1, name, new StringDatatype("P1"));
			baseModel.addProperty (person2, name, new StringDatatype("P2"));
			
			baseModel.addProperty (auto1, name, new StringDatatype("A1"));
			baseModel.addProperty (auto2, name, new StringDatatype("A2"));
			
		
			
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
