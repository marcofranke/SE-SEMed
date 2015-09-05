package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.biba.mediator.QuerySolution;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.ObjectPropertyStatement;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut f�r Produktion und Logistik GmbH)]

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
 * Constraint welches checkt ob eine Instanz mindestens eine bestimmte Anzahl von Werten eines Properties besitzt.
 * @author KRA
 *
 */
public class CardinalityConstraint extends SimpleConstraint {
	String variable;
	String propName;
	boolean isDatatypeProp;
	int cardinality;
	
	/**
	 * Konstruktor.
	 * @param dp DatatypeProperty, welches überprüft werden soll
	 * @param cardinality Anzahl der Werte die mindestens vorhanden sein müssen
	 */
	public CardinalityConstraint(DatatypeProperty dp, int cardinality){
		propName = dp.getUri();
		isDatatypeProp = true;
		this.cardinality = cardinality;
	}
	
	/**
	 * Konstruktor.
	 * @param dp ObjectProperty, welches überprüft werden soll
	 * @param cardinality Anzahl der Werte die mindestens vorhanden sein müssen
	 */
	public CardinalityConstraint(ObjectProperty dp, int cardinality){
		propName = dp.getUri();
		isDatatypeProp = false;
		this.cardinality = cardinality;
	}
	
	@Override
	public List<SimpleConstraint> flatToSimpleConstraints() {
		List<SimpleConstraint> result = new ArrayList<SimpleConstraint>();
		result.add(this);
		return result;
	}

	@Override
	public Set<String> getResultVars() {
		Set<String> result =  new HashSet<String>();
		result.add(variable);
		return result;
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public QuerySolution solve(OntModel pModel) {
		QuerySolution result = new QuerySolution();
		result.columnNames.add(result.new Column(variable,"unknown"));
		//Es DatatypeProperty
		if(isDatatypeProp){
			if(pModel.tbox.datatypeProperties.containsKey(propName)){
				List<DatatypePropertyStatement> los = pModel.abox.dtProperties.get(propName);
				Collections.sort(los, new Comparator<DatatypePropertyStatement>(){
					@Override
					public int compare(DatatypePropertyStatement pO1,
							DatatypePropertyStatement pO2) {
						long result = pO1.subject.id - pO2.subject.id;
						if(result==0)
							return 0;
						else if(result<0)
							return -1;
						else
							return 1;
					}
				});
				
				if(cardinality==0){
					LinkedList<Individual> li = new LinkedList<Individual>();
					for(String key:pModel.abox.individuals.keySet()){
						li.addAll(pModel.abox.individuals.get(key));
					}
					Collections.sort(li,new Comparator<Individual>(){
						@Override
						public int compare(Individual pO1,
								Individual pO2) {
							long result = pO1.id - pO2.id;
							if(result==0)
								return 0;
							else if(result<0)
								return -1;
							else
								return 1;
						}
					});
					
					Iterator<DatatypePropertyStatement> iter1 = los.iterator();
					Iterator<Individual> iter2 = li.iterator();
					boolean getA = true;
					boolean getB = true;
					DatatypePropertyStatement dps = null;
					Individual ind = null;
					while((iter1.hasNext() || getB) && (iter2.hasNext() || getA)){
						if(getA)
							dps = iter1.next();
						if(getB)
							ind = iter2.next();
						long cmp = dps.subject.id - ind.id;
						if(cmp==0){
							getB = iter2.hasNext();
							getA = iter1.hasNext();
						}
						else if(cmp<0){
							if(!iter1.hasNext()){
								getA = false;
								getB = true;
								List<Datatype> tmp = new ArrayList<Datatype>(1);
								tmp.add(ind);
								result.data.add(tmp);
							}
							else{
								getA = true;
								getB = false;
							}
						}
						else{
							List<Datatype> tmp = new ArrayList<Datatype>(1);
							tmp.add(ind);
							result.data.add(tmp);
							getB = true;
							getA = false;
						}
					}
				}
				else{
					long last = -1;
					int cnt = 0;
					for(DatatypePropertyStatement dps : los){
						if(cnt==0){
							last = dps.subject.id;
						}
						else if(dps.subject.id!=last){
							if(cnt == cardinality){
								List<Datatype> tmp = new ArrayList<Datatype>(1);
								tmp.add(dps.subject);
								result.data.add(tmp);
							}
							last = dps.subject.id;
							cnt=0;
						}
						cnt++;
					}
				}
			}
		}
		else{
			if(pModel.tbox.objectProperties.containsKey(propName)){
				List<ObjectPropertyStatement> los = pModel.abox.oProperties.get(propName);
				Collections.sort(los, new Comparator<ObjectPropertyStatement>(){
					@Override
					public int compare(ObjectPropertyStatement pO1,
							ObjectPropertyStatement pO2) {
						long result = pO1.subject.id - pO2.subject.id;
						if(result==0)
							return 0;
						else if(result<0)
							return -1;
						else
							return 1;
					}
				});
				
				if(cardinality==0){
					LinkedList<Individual> li = new LinkedList<Individual>();
					for(String key:pModel.abox.individuals.keySet()){
						li.addAll(pModel.abox.individuals.get(key));
					}
					Collections.sort(li,new Comparator<Individual>(){
						@Override
						public int compare(Individual pO1,
								Individual pO2) {
							long result = pO1.id - pO2.id;
							if(result==0)
								return 0;
							else if(result<0)
								return -1;
							else
								return 1;
						}
					});
					
					Iterator<ObjectPropertyStatement> iter1 = los.iterator();
					Iterator<Individual> iter2 = li.iterator();
					boolean getA = true;
					boolean getB = true;
					ObjectPropertyStatement dps = null;
					Individual ind = null;
					while((iter1.hasNext() || getB) && (iter2.hasNext() || getA)){
						if(getA)
							dps = iter1.next();
						if(getB)
							ind = iter2.next();
						long cmp = dps.subject.id - ind.id;
						if(cmp==0){
							getB = iter2.hasNext();
							getA = iter1.hasNext();
						}
						else if(cmp<0){
							if(!iter1.hasNext()){
								getA = false;
								getB = true;
								List<Datatype> tmp = new ArrayList<Datatype>(1);
								tmp.add(ind);
								result.data.add(tmp);
							}
							else{
								getA = true;
								getB = false;
							}
						}
						else{
							List<Datatype> tmp = new ArrayList<Datatype>(1);
							tmp.add(ind);
							result.data.add(tmp);
							getB = true;
							getA = false;
						}
					}
				}
				else{
					long last = -1;
					int cnt = 0;
					for(ObjectPropertyStatement dps : los){
						if(cnt==0){
							last = dps.subject.id;
						}
						else if(dps.subject.id!=last){
							if(cnt == cardinality){
								List<Datatype> tmp = new ArrayList<Datatype>(1);
								tmp.add(dps.subject);
								result.data.add(tmp);
							}
							last = dps.subject.id;
							cnt=0;
						}
						cnt++;
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
		return propName;
	}

	@Override
	public int getType() {
		return 2;
	}

	@Override
	public void setSubject(String pSubject) {
		variable = pSubject;
	}

}
