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
import de.biba.wrapper.DataSourceV2;
import de.biba.wrapper.ValidationReport;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/

public class DummyWrapper1 extends DataSourceV2 {
	public static int INDIVIDUALS = 50;
	String namespace = "http://sfb637.uni-bremen.de";
	OntClass lebewesen ;
	OntClass mensch ;
	OntClass kind ;
	OntClass kinderloserMensch;
	ObjectProperty verwandtMit;
	ObjectProperty kindVon;
	ObjectProperty ElternVon;
	DatatypeProperty alter;
	DatatypeProperty name;
	ComplexOntClass benannter;
	
	public DummyWrapper1(){
		super();
		
		lebewesen = baseModel.createOntClass(namespace, "Lebewesen");
		mensch = baseModel.createOntClass(namespace, "Mensch", "Lebewesen");
		kind = baseModel.createOntClass(namespace, "Kind", "Mensch");
		verwandtMit = baseModel.createObjectProperty(namespace, "VerwandtMit");
		verwandtMit.setTransitive(true);
		verwandtMit.setSymmetric(true);
		kindVon = baseModel.createObjectProperty(namespace, "KindVon", "VerwandtMit");
//		kindVon.setSymmetric(true);
		ElternVon = baseModel.createObjectProperty(namespace, "ElternVon", "VerwandtMit");
		baseModel.markInverseProperties(kindVon.getUri(), ElternVon.getUri());
		alter = baseModel.createDatatypeProperty(namespace, "alter");
		alter.getDomain().add(namespace+"#Mensch");
		name = baseModel.createDatatypeProperty(namespace, "name");
		name.setInverseFunctional(true);
		name.getDomain().add(namespace+"#Mensch");
		baseModel.createOntClass(namespace, "testklasse");
		benannter = baseModel.createComplexOntClass(namespace, "BenanntesIndividuum", new SomeConstraint(name, Datatypes.STRING));
	}
	
	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		boolean insertAlter = pDsc.getPropertyNames().contains(namespace+"#alter");
		boolean insertName = pDsc.getPropertyNames().contains(namespace+"#name");
		baseModel.setAbox(new ABox());
		baseModel.resetIndividualCounter();
		Individual jesus = baseModel.createIndividual(mensch);
		baseModel.addProperty(jesus, name , new StringDatatype("Jesus"));
		baseModel.abox.units.put(alter.getUri(), "year");
		for(int i=0; i<INDIVIDUALS;i++){
			Individual parent = baseModel.createIndividual(mensch);
			if(insertAlter)
				baseModel.addProperty(parent, alter, new NumericDatatype(30+i));
			if(insertName)
				baseModel.addProperty(parent, name, new StringDatatype("Elter"+i));
			baseModel.addProperty(parent, verwandtMit, jesus);
			Individual child = baseModel.createIndividual(kind);
			baseModel.addProperty(child, alter, new NumericDatatype(i));
			baseModel.addProperty(child, name, new StringDatatype("Kind"+i));
			baseModel.addProperty(child, kindVon, parent);
		}
		
		return baseModel;
	}
	
	

	public static void main(String[] args){
		DummyWrapper1 dw = new DummyWrapper1();
		OntModel om = dw.queryData(null);
	}

	@Override
	public ValidationReport validateConfiguration() {
		// TODO Auto-generated method stub
		return new ValidationReport(false, false);
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
