package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.biba.mediator.Datatypes;
import de.biba.mediator.QuerySolution;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.ObjectPropertyStatement;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.DateValue;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;

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
 * Constraint welches die Instanzen heraussucht, welche die "hasSome" Eigenschaften erfüllen
 * @author KRA
 *
 */
public class SomeConstraint extends SimpleConstraint {
	String predicate;
	String subject;
	String className;
	DatatypeVerifier muh;
	boolean isDatatypeConstraint;
	
	/**
	 * Konstruktor.
	 * @param dp Property für das die hasSome-Eigenschaft gegeben sein soll
	 * @param pType Datentyp des Properties
	 */
	public SomeConstraint(DatatypeProperty dp, Datatypes pType){
		isDatatypeConstraint = true;
		predicate = dp.getUri();
		switch(pType){
		case NUMERIC:
			muh = new DatatypeVerifier(){
				@Override
				public boolean inspect(DatatypePropertyStatement pDps) {
					if(pDps.object instanceof NumericDatatype)
						return true;	
					return false;
				}
			};
			break;
		case STRING:
			muh = new DatatypeVerifier(){
				@Override
				public boolean inspect(DatatypePropertyStatement pDps) {
					if(pDps.object instanceof StringDatatype)
						return true;	
					return false;
				}
			};
			break;
		case DATE:
			muh = new DatatypeVerifier(){
				@Override
				public boolean inspect(DatatypePropertyStatement pDps) {
					if(pDps.object instanceof DateValue)
						return true;	
					return false;
				}
			};
			break;
		case BOOLEAN:
			muh = new DatatypeVerifier(){
				@Override
				public boolean inspect(DatatypePropertyStatement pDps) {
					if(pDps.object instanceof StringDatatype)
						return true;	
					return false;
				}
			};
			break;
		}
	}
	
	public SomeConstraint(ObjectProperty op, String pClassName){
		isDatatypeConstraint = false;
		predicate = op.getUri();
		className = pClassName;
	}
	
	@Override
	public String getPredicate() {
		return predicate;
	}

	@Override
	public int getType() {
		return 2;
	}

	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		result.add(subject);
		return result;
	}

	@Override
	public int getWeight() {
		return 9;
	}

	@Override
	public void setSubject(String pSubject) {
		subject = pSubject;
	}

	@Override
	public QuerySolution solve(OntModel pModel) {
		if(isDatatypeConstraint)
			return solveDatatypeConstraint(pModel);
		else
			return solveObjectConstraint(pModel);
	}

	private QuerySolution solveObjectConstraint(OntModel pModel) {
		QuerySolution result = new QuerySolution();
		result.columnNames.add(result.new Column(subject,"unknown"));
		Set<String> equis = pModel.getAllSubOrEquivalentProperties(predicate);
		List<ObjectPropertyStatement> statements = new LinkedList<ObjectPropertyStatement>();
		for(String equi : equis){
			if(pModel.abox.oProperties.containsKey(equi)){
				statements.addAll(pModel.abox.oProperties.get(equi));
			}
		}
		Collections.sort(statements, new Comparator<ObjectPropertyStatement>(){
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
		Set<String> equiClasses = pModel.getAllSubOrEquivalentClassNames(className);
		List<Individual> indis = new LinkedList<Individual>();
		
		for(String equi : equiClasses ){
			List<Individual> tmp = pModel.abox.individuals.get(equi);
			if(tmp!=null)
				indis.addAll(tmp);
		}
		Collections.sort(indis,new Comparator<Individual>(){
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
		long last = -1;
		
		Iterator<ObjectPropertyStatement> iter1 = statements.iterator();
		Iterator<Individual> iter2 = indis.iterator();
		
		boolean getA = true;
		boolean getB = true;
		
		ObjectPropertyStatement ops = null;
		Individual ind = null;
		
		while((iter1.hasNext() ) && (iter2.hasNext() )){
			if(getA)
				ops = iter1.next();
			if(getB)
				ind = iter2.next();
			long cmp = ops.object.id - ind.id;
			if(cmp==0 && last!=ind.id){
				getB = true;
				getA = true;
				List<Datatype> tmp = new ArrayList<Datatype>(1);
				tmp.add(ind);
				result.data.add(tmp);
				last = ind.id;
			}
			else if(cmp<0){
				getA = true;
				getB = false;
			}
			else{
				getB = true;
				getA = false;
			}
		}
		
		return result;
	}

	private QuerySolution solveDatatypeConstraint(OntModel pModel) {
		QuerySolution result = new QuerySolution();
		result.columnNames.add(result.new Column(subject,"unknown"));
		Set<String> equis = pModel.getAllSubOrEquivalentProperties(predicate);
		List<DatatypePropertyStatement> statements = new LinkedList<DatatypePropertyStatement>();
		for(String equi : equis){
			if(pModel.abox.dtProperties.containsKey(equi)){
				statements.addAll(pModel.abox.dtProperties.get(equi));
			}
		}
		Collections.sort(statements, new Comparator<DatatypePropertyStatement>(){
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
		long last = -1;
		for(DatatypePropertyStatement dps : statements){
			if(dps.subject.id != last){
				if(muh.inspect(dps)){
					List<Datatype> ls = new ArrayList<Datatype>(1);
					ls.add(dps.subject);
					result.data.add(ls);
					last = dps.subject.id;
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

	private interface DatatypeVerifier{
		public boolean inspect(DatatypePropertyStatement dps);
	}
}
