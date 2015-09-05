package de.biba.wrapper.newSQLWrapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;
import de.biba.wrapper.newSQLWrapper.Mapping.Condition;
import de.biba.wrapper.newSQLWrapper.Mapping.DatatypePropertyMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.ValueType;

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

public class DatatypePropertyCreator {

	private DatatypeProperty property;
	private String column;
	private ValueType datatype;
	private List<ConditionChecker> conditions;
	private  String driverName;

	public DatatypePropertyCreator(String drivername, DatatypePropertyMapping pm, DatatypeProperty dp) {
		this.property = dp;
		this.column = pm.getObColumn();
		this.datatype = pm.getObType();
		this.driverName = drivername;
		
		
		if(pm.getConditions()!=null && pm.getConditions().size()>0){
			this.conditions = new ArrayList<ConditionChecker>();
			for(Condition c : pm.getConditions()){
				this.conditions.add(new ConditionChecker(c));
			}
		}
	}

	public void createData(ResultSet rs, Individual ind, OntModel baseModel) {
		if(this.conditions!=null){
			for(ConditionChecker c: this.conditions){
				if(!c.check(rs))
					return;
			}
		}
		Datatype data = Util.getDatatypeValue(driverName, column, datatype, rs);
		if(data!=null)
			baseModel.addProperty(ind, property, data);
	}

}
