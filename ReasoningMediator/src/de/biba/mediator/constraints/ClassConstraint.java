package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.biba.mediator.QuerySolution;
import de.biba.ontology.Individual;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;

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

/**
 * Dieses Constraint filtert alle Individuen heraus, die nicht zu einer bestimmten Klasse gehÃ¶ren
 * @author KRA
 *
 */
public class ClassConstraint extends SimpleConstraint  {
	/**
	 * Spaltenname unter dem die Ergebnisse abgelegt werden sollen
	 */
	public String subject;
	/**
	 * Name der Klasse der Individuen die gefunden werden sollen
	 */
	public String className;
	
	public ClassConstraint(String pSubject, String pObject) {
		subject = pSubject;
		className = pObject;
	}


	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		result.add(subject);
		return result;
	}


	@Override
	public QuerySolution solve(OntModel pModel) {
		QuerySolution result = new QuerySolution();
		result.columnNames.add(result.new Column(subject,"unknown"));
		
		Set<String> classNames = pModel.getAllSubOrEquivalentClassNames(className);
		for(String className: classNames){
			List<Individual> tmp = pModel.abox.individuals.get(className);
			if(tmp!=null){
				for(Individual ind : tmp){
					List<Datatype> ld = new ArrayList<Datatype>(1);
					ld.add(ind);
					result.data.add(ld);
				}
			}
		}
		return result;
	}

	@Override
	public QuerySolution solve(OntModel pModel,
			List<QuerySolution> pAffectedSolutions) {
		QuerySolution result = solve(pModel);
		for(QuerySolution qs : pAffectedSolutions)
			result.naturalJoin(qs);
		return result;
	}


	@Override
	public int getWeight() {
		return 1;
	}

	public String toString(){
		return subject + " A "+className;
	}


	@Override
	public String getPredicate() {
		return "isA";
	}


	@Override
	public int getType() {
		return 0;
	}


	@Override
	public void setSubject(String pSubject) {
		subject = pSubject;
	}

}
