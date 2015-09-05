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

public class DummyWrapperWeather extends DataSourceV2 {
	public static int INDIVIDUALS = 50;
	String namespace = "http://www.FITMAN.eu";
	OntClass WeatherInformation ;

	DatatypeProperty requestedlongitude;
	DatatypeProperty requestedlatitude;
	DatatypeProperty name;
	ComplexOntClass benannter;
	
	public DummyWrapperWeather(){
		super();
		
		WeatherInformation = baseModel.createOntClass(namespace, "WetherInformation");
		
		requestedlongitude = baseModel.createDatatypeProperty(namespace, "requestedlongitude");
		requestedlongitude.getDomain().add(namespace+"#WetherInformation");
		requestedlatitude = baseModel.createDatatypeProperty(namespace, "requestedlatitude");
		
		requestedlatitude.getDomain().add(namespace+"#WetherInformation");
		
		name = baseModel.createDatatypeProperty(namespace, "name");
		name.setInverseFunctional(true);
		name.getDomain().add(namespace+"#WetherInformation");
		
		
	}
	
	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		boolean insertAlter = pDsc.getPropertyNames().contains(namespace+"#alter");
		boolean insertName = pDsc.getPropertyNames().contains(namespace+"#name");
		baseModel.setAbox(new ABox());
		baseModel.resetIndividualCounter();
		Individual jesus = baseModel.createIndividual(WeatherInformation);
		baseModel.addProperty(jesus, requestedlatitude , new StringDatatype("40"));
		baseModel.addProperty(jesus, requestedlongitude , new StringDatatype("40"));
		baseModel.addProperty(jesus, name , new StringDatatype("blupp"));
		
		return baseModel;
	}
	
	

	public static void main(String[] args){
		DummyWrapperWeather dw = new DummyWrapperWeather();
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
