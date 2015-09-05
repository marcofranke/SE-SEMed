package de.biba.wrapper.newSQLWrapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.OntModel;
import de.biba.wrapper.newSQLWrapper.Mapping.Condition;

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

public class ObjectPropertyCreator {
	private Individual subject;
	private ObjectProperty predicate;
	private Individual object;
	private List<ConditionChecker> conditions;
	
	
	public ObjectPropertyCreator(List<Condition> conditions) {
		if(conditions!=null && conditions.size()>0){
			this.conditions = new ArrayList<ConditionChecker>();
			for(Condition c: conditions)
				this.conditions.add(new ConditionChecker(c));
		}
	}
	public Individual getSubject() {
		return subject;
	}
	public void setSubject(Individual subject) {
		this.subject = subject;
	}
	public ObjectProperty getPredicate() {
		return predicate;
	}
	public void setPredicate(ObjectProperty predicate) {
		this.predicate = predicate;
	}
	public Individual getObject() {
		return object;
	}
	public void setObject(Individual object) {
		this.object = object;
	}
	
	public boolean linkIndividuals(OntModel om, ResultSet rs){
		if(this.conditions!=null){
			for(ConditionChecker c : this.conditions){
				if(c.check(rs))
					return false;
			}
		}
		if(subject!=null && object!=null){
			om.addProperty(subject, predicate, object);
			subject = null;
			object = null;
			return true;
		}
		subject = null;
		object = null;
		return false;
	}
}
