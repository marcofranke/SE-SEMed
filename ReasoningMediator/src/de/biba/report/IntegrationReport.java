package de.biba.report;

import java.util.ArrayList;

import de.biba.ontology.OntModel;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
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

public class IntegrationReport {

	/**
	 * @param args
	 */
	private String query;
	private OntModel parent;
	private OntModel toAggregate;
	private ArrayList<FailureTupel> allInconsistencies = new ArrayList<FailureTupel>();
	
	
	
	
	@Override
	public String toString() {
		return "IntegrationReport [query=" + query + ", parent=" + parent
				+ ", toAggregate=" + toAggregate + ", allInconsistencies="
				+ allInconsistencies + "]";
	}




	public String getQuery() {
		return query;
	}




	public void setQuery(String query) {
		this.query = query;
	}




	public OntModel getParent() {
		return parent;
	}




	public void setParent(OntModel parent) {
		this.parent = parent;
	}




	public OntModel getToAggregate() {
		return toAggregate;
	}




	public void setToAggregate(OntModel toAggregate) {
		this.toAggregate = toAggregate;
	}




	public ArrayList<FailureTupel> getAllInconsistencies() {
		return allInconsistencies;
	}




	/**
	 * This methodes returns the information whether the integration of a new @link{OntModel} provokes some failures
	 * @return a Boolean which describes the consistence of the actual ontology
	 */
	public boolean isConsistent(){
		if (allInconsistencies.size()==0){
			return true;
		}
		
		return false;
	}

}
