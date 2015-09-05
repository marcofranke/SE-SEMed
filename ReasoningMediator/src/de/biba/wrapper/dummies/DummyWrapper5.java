package de.biba.wrapper.dummies;

import java.util.Properties;

import de.biba.mediator.DataSourceQuery;
import de.biba.ontology.ABox;
import de.biba.ontology.ComplexOntClass;
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

public class DummyWrapper5 extends DataSource {

	OntClass designedProd ;
	OntClass phyProd ;
	OntClass idInfo ;
	OntClass lifecycle ;
	OntClass kinderloserMensch;
	OntClass mol ;
	OntClass bol ;
	OntClass eol;
	ObjectProperty hasID;
	ObjectProperty hasPhase;
	ObjectProperty hasFD;
	DatatypeProperty id;
	DatatypeProperty fd_type;
	DatatypeProperty fd_id;
	DatatypeProperty value;
	ComplexOntClass benannter;
	OntClass lMOL;
	OntClass lBOL;
	OntClass lEOL;
	OntClass fData;
	private String namespace;
	private OntClass fdSource;
	private ObjectProperty prodHasFDSrc;
	private Object value_prop;
	private DatatypeProperty when_prop;
	private DatatypeProperty where_prop;
	private DatatypeProperty what_prop;
	private DatatypeProperty who_prop;
	private DatatypeProperty accuracy_prop;
	private DatatypeProperty value_type;
	private DatatypeProperty measuringUnit;
	private DatatypeProperty srcId;
	private DatatypeProperty category;
	
	public DummyWrapper5(){
		super();
		namespace = "http://biba.uni-bremen.de";
		designedProd = baseModel.createOntClass(namespace,"AS_DESIGNED_PRODUCT");
		lifecycle = baseModel.createOntClass(namespace,"LIFE_CYLCE_PHASE");
		phyProd = baseModel.createOntClass(namespace,"PHYSICAL_PRODUCT");
		lEOL = baseModel.createOntClass(namespace,"PRODUCT_EOL");
		lMOL = baseModel.createOntClass(namespace,"PRODUCT_MOL");
		lBOL = baseModel.createOntClass(namespace,"PRODUCT_BOL");
		idInfo = baseModel.createOntClass(namespace,"ID_INFO");
		fData = baseModel.createOntClass(namespace,"FIELD_DATA");
		fdSource = baseModel.createOntClass(namespace, "FD_SOURCE");
		
		hasID = baseModel.createObjectProperty(namespace,"PPHasID");
		hasPhase = baseModel.createObjectProperty(namespace,"PPHasLifeCyclePhase");
		prodHasFDSrc = baseModel.createObjectProperty(namespace, "ProdHasFDSrc");
		hasFD = baseModel.createObjectProperty(namespace,"LCPHasFD");
		
		fd_id = baseModel.createDatatypeProperty(namespace,"fd_id");
		id = baseModel.createDatatypeProperty(namespace,"info_id");	
		id.setInverseFunctional(true);
		fd_type = baseModel.createDatatypeProperty(namespace,"fd_type");
		value_type = baseModel.createDatatypeProperty(namespace,"value_type");
		value_prop = baseModel.createDatatypeProperty(namespace,"value"); 
		accuracy_prop = baseModel.createDatatypeProperty(namespace,"accuracy");
		who_prop = baseModel.createDatatypeProperty(namespace,"who");
		what_prop = baseModel.createDatatypeProperty(namespace,"what");
		where_prop = baseModel.createDatatypeProperty(namespace,"where");
		when_prop = baseModel.createDatatypeProperty(namespace,"when");
		measuringUnit = baseModel.createDatatypeProperty(namespace,"measuring_unit");
		srcId = baseModel.createDatatypeProperty(namespace,"src_id");

		value = baseModel.createDatatypeProperty(namespace,"value");
		category = baseModel.createDatatypeProperty(namespace,"category");
	}
	
	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		baseModel.setAbox(new ABox());
		Individual i0 = baseModel.createIndividual(phyProd);
		Individual i1 = baseModel.createIndividual(idInfo);
		Individual i2 = baseModel.createIndividual(lMOL);
		Individual i3 = baseModel.createIndividual(fData);
		Individual i4 = baseModel.createIndividual(fdSource);	
		
		
		baseModel.addProperty(i0, hasID, i1);
		baseModel.addProperty(i0, hasPhase, i2);
		baseModel.addProperty(i0, prodHasFDSrc, i4);
		baseModel.addProperty(i2, hasFD, i3);
		
		baseModel.addProperty(i3, what_prop, new StringDatatype("na"));
		baseModel.addProperty(i3, when_prop, new StringDatatype("14:18:40 CET "));
		baseModel.addProperty(i3, fd_id, new StringDatatype("GGA"));
		baseModel.addProperty(i3, fd_type, new StringDatatype("GPS"));
		baseModel.addProperty(i4, value_type, new StringDatatype("Geo-Coordinates"));
		baseModel.addProperty(i4, measuringUnit, new StringDatatype("na"));
		baseModel.addProperty(i3, who_prop, new StringDatatype("na"));
		baseModel.addProperty(i1, id, new StringDatatype("helloWorld"));
		baseModel.addProperty(i3, accuracy_prop, new StringDatatype("na"));
		baseModel.addProperty(i4, srcId, new StringDatatype("GPS"));
		baseModel.addProperty(i4, category, new StringDatatype("Global Positioning System Fix Data "));
		baseModel.addProperty(i3, value, new StringDatatype("60°3' 19°40'"));
		
		
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
