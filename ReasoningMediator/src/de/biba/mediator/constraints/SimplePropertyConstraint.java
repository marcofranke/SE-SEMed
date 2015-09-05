package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.biba.mediator.CompareOperator;
import de.biba.mediator.QuerySolution;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
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
 * Constraint mit dem Instanzen abgerufen werden, die einen gegeben Wert fÃ¼r ein gegebenes Property besitzen.
 * @author KRA
 *
 */
public class SimplePropertyConstraint extends SimpleConstraint {
	String subject;
	public String predicate;
	public Datatype value;
	CompareOperator operator;
	public Comparator<Datatype> comparator;
	
	/**
	 * Konstruktor
	 * @param pSubject Name der Subjekt-Variablen
	 * @param pPredicate Name des Property's
	 * @param pValue Wert des Property's
	 * @param pOperator Vergleichsoperator
	 */
	public SimplePropertyConstraint(String pSubject, String pPredicate, Datatype pValue, CompareOperator pOperator){
		subject = pSubject;
		predicate = pPredicate;
		value = pValue;
		operator = pOperator;
		switch(operator){
		case EQUALS:
			comparator = new Comparator<Datatype>() {
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					return pO1.compareTo(pO2);
				}
			};
			break;
		case UNEQUALS:
			comparator = new Comparator<Datatype>() {
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					int cmp =  pO1.compareTo(pO2);
					if(cmp==0)
						return -1;
					else
						return 0;
				}
			};
			break;
		case GREATER:
			comparator = new Comparator<Datatype>() {
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					if(pO1.compareTo(pO2)>0)
						return 0;
					else
						return -1;
				}
			};
			break;
		case GREATEREQUAL:
			comparator = new Comparator<Datatype>() {
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					if(pO1.compareTo(pO2)>=0)
						return 0;
					else
						return -1;
				}
			};
			break;
		case LESS:
			comparator = new Comparator<Datatype>() {
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					if(pO1.compareTo(pO2)<0)
						return 0;
					else
						return -1;
				}
			};
			break;
		case LESSEQUAL:
			comparator = new Comparator<Datatype>() {
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					if(pO1.compareTo(pO2)<=0)
						return 0;
					else
						return -1;
				}
			};
			break;

		}
	}
	
	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		result.add(subject);
		return result;
	}

	@Override
	public int getWeight() {
		return 2;
	}

	@Override
	public QuerySolution solve(OntModel pModel) {
		QuerySolution result = new QuerySolution();
		result.columnNames.add(result.new Column(subject,"unknown"));
		
		DatatypeProperty dp = pModel.tbox.datatypeProperties.get(predicate);
		
		if(dp!=null){
			List<DatatypePropertyStatement> los = pModel.abox.dtProperties.get(predicate);
			if(los!=null){
				for(DatatypePropertyStatement ops:los){
					if(comparator.compare(ops.object,value)==0){
						List<Datatype> tmp = new ArrayList<Datatype>(1);
						tmp.add(ops.subject);
						result.data.add(tmp);
					}
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
	public String getPredicate() {
		return predicate;
	}

	@Override
	public int getType() {
		return 1;
	}

	public String toString(){
		return subject + " "+predicate +" "+operator + " "+value;
	}

	@Override
	public void setSubject(String pSubject) {
		subject = pSubject;
	}
	
	public String getSubject(){
		return this.subject;
	}
	
}
