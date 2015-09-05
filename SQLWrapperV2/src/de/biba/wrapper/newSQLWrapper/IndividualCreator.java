package de.biba.wrapper.newSQLWrapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.wrapper.newSQLWrapper.Mapping.ColumnMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.Condition;
import de.biba.wrapper.newSQLWrapper.Mapping.DatatypePropertyMapping;
/**[SQL Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
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

public class IndividualCreator {
	private OntClass ontclass;
	private List<ColumnMapping> columns;
	private List<DatatypePropertyCreator> dataCreators;
	
	private List<ObjectPropertyCreator> subjectListeners;
	private List<ObjectPropertyCreator> objectListeners;
	private String driverName ="";
	
	public IndividualCreator(String driverName, OntClass ontClass, List<ColumnMapping> list, List<Condition> conditions){
		this.ontclass = ontClass;
		this.setColumns(list);
		this.dataCreators = new ArrayList<DatatypePropertyCreator>();
		subjectListeners = new LinkedList<ObjectPropertyCreator>();
		objectListeners = new LinkedList<ObjectPropertyCreator>();
		 this.driverName =  driverName;
	}

	public Individual createIndividual(ResultSet rs, OntModel baseModel) {
		Individual ind = baseModel.createIndividual(ontclass);
		
		enrichIndividual(rs, baseModel, ind);
		return ind;
	}
	
	public void addSubjectListener(ObjectPropertyCreator opc){
		this.subjectListeners.add(opc);
	}
	public void addObjectListener(ObjectPropertyCreator opc){
		this.objectListeners.add(opc);
	}
	
	public void enrichIndividual(ResultSet rs, OntModel baseModel, Individual ind) {
		for(DatatypePropertyCreator dpc : dataCreators){
			dpc.createData(rs,ind,baseModel);
		}	
		for(ObjectPropertyCreator opc : subjectListeners)
			opc.setSubject(ind);
		for(ObjectPropertyCreator opc : objectListeners)
			opc.setObject(ind);
	}

	public void addDataProperty(DatatypePropertyMapping pm, DatatypeProperty dp) {
		dataCreators.add(new DatatypePropertyCreator(driverName, pm,dp));
	}

	public List<ColumnMapping> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnMapping> columns) {
		this.columns = columns;
	}

	public OntClass getOntclass() {
		return ontclass;
	}

	public void setOntclass(OntClass ontclass) {
		this.ontclass = ontclass;
	}
}
