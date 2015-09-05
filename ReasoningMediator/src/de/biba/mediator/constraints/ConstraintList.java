package de.biba.mediator.constraints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.biba.mediator.QuerySolution;
import de.biba.ontology.ComplexOntClass;
import de.biba.ontology.OntModel;
/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut f�r Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
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
 * Eine Liste von Constraints, welche linear nacheinander abgearbeitet werden.
 * @author KRA
 *
 */
public class ConstraintList implements Constraint {
	List<Constraint> expressions;
	int weight;
	
	public ConstraintList(){
		expressions = new ArrayList<Constraint>();
		weight = 0;
	}
	
	/**
	 * Fügt der Liste ein Constraint hinzu
	 * @param e
	 */
	public void addExpression(Constraint e){
		expressions.add(e);
		weight +=e.getWeight();
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("(");
		for(int i=0; i<expressions.size();i++){
			Constraint e = expressions.get(i);
			sb.append(e);
			if(i<expressions.size()-1)
				sb.append(" AND ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	
	@Override
	public QuerySolution solve(OntModel abox){
		Logger logger = Logger.getLogger(this.getClass());
		long time = System.currentTimeMillis();
		
		//Constraint entsprechend ihrer Komplexität sortieren (einfache zuerst)
		Collections.sort(expressions,new Comparator<Constraint>(){
			@Override
			public int compare(Constraint pO1, Constraint pO2) {
				return pO1.getWeight() - pO2.getWeight();
			}
		});
		
		
		List<QuerySolution> results = new ArrayList<QuerySolution>();
		
		//Für jedes Constraint...
		for(int i=0; i<expressions.size();i++){
			Constraint expr = expressions.get(i);
			logger.debug("Wende Constraint "+expr +" an");
			//Beim ersten constraint
			if(i==0){
				//Constraint auf Ontologie anwenden und Ergebnis der Ergebnisliste hinzufügen
				results.add(expr.solve(abox));
				continue;
			}
			//Gebundene Variablennamen des Constraints abfragen
			Set<String> exprResultVars = expr.getResultVars();
			List<QuerySolution> affectedSolutions = new ArrayList<QuerySolution>();
			
			//Bestehende Ergebnisse durchsuchen ob es bereits ein Ergebnis gibt, welches die Variablennamen des Constraints beinhaltet
			for(int j=0; j<results.size();j++){
				QuerySolution qs = results.get(j);
				//Spaltennamen des Ergebnisses abfragen
				List<String> qsResultVars = qs.getColumnNames();
				if(!Collections.disjoint(exprResultVars, qsResultVars)){
					logger.debug("Bisheriges Ergebnis wird eingeschr�nkt");
					//zur Liste der beeinflussten ERgebnisse hinzufügen
					affectedSolutions.add(qs);
					results.remove(j);
					j--;
				}
			}
			//Wenn es Ergebnisse gibt die von diesem Constraint beeinflusst werden...
			if(affectedSolutions.size()>0){
				//Constraint anwenden, mit Rücksicht auf bestehende Ergebnisse
				QuerySolution tmp =expr.solve(abox,affectedSolutions);
				if(tmp!=null){
					results.add(tmp);
				}
			}
			else{
				QuerySolution tmp = expr.solve(abox);
				if(tmp!=null)
					results.add(tmp);
			}
		}
		
		//Wenn nach Anwendung aller Constraints mehr als 1 Ergebnis in der Ergebnisliste vorhanden, dann müssen kartesische Produkte gebildet werden
		QuerySolution result = null;
		for(int i=0; i<results.size();i++){
			if(i==0){
				result = results.get(i);
			}
			else{
				result.cartesianProduct(results.get(i));
			}
		}
		logger.info("Ingesamt dauerte es "+(System.currentTimeMillis()-time)+" Millisekunden");
		return result;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		for(Constraint ex : expressions)
			result.addAll(ex.getResultVars());
		return result;
	}

	@Override
	public QuerySolution solve(OntModel pAbox,
			List<QuerySolution> pAffectedSolutions) {
		List<QuerySolution> results = new ArrayList<QuerySolution>();
		for(Constraint ex : expressions){
			List<QuerySolution> affectedSolutions = new ArrayList<QuerySolution>();
			for(int i=0; i<pAffectedSolutions.size();i++ ){
				QuerySolution affectedSolution = pAffectedSolutions.get(i);
				if(!Collections.disjoint(ex.getResultVars(), affectedSolution.getColumnNames())){
					affectedSolutions.add(affectedSolution);
					pAffectedSolutions.remove(i);
				}
			}
			for(int i=0; i<results.size();i++ ){
				QuerySolution affectedSolution = results.get(i);
				if(!Collections.disjoint(ex.getResultVars(), affectedSolution.getColumnNames())){
					affectedSolutions.add(affectedSolution);
					results.remove(i);
				}
			}
			if(affectedSolutions.size()>0)
				results.add(ex.solve(pAbox, affectedSolutions));
			else
				results.add(ex.solve(pAbox));
		}
		QuerySolution result = null;
		for(int i=0; i<results.size();i++){
			if(i==0){
				result = results.get(i);
			}
			else{
				result.cartesianProduct(results.get(i));
			}
		}
		return result;
	}
	
	@Override
	public List<SimpleConstraint> flatToSimpleConstraints(){
		List<SimpleConstraint> result = new ArrayList<SimpleConstraint>();
		for(Constraint c:expressions){
			result.addAll(c.flatToSimpleConstraints());
		}
		return result;
	}

	@Override
	public void insertComplexClassConstraints(OntModel pModel) {
		
		for(int i=0; i<expressions.size();i++){
			Constraint c  = expressions.get(i);
			if(c instanceof ClassConstraint){
				ClassConstraint cc = (ClassConstraint)c;
				if(pModel.tbox.complexClasses.containsKey(cc.className)){
					ComplexOntClass coc = pModel.tbox.complexClasses.get(cc.className);
					expressions.remove(i);
					for(Constraint chc: coc.constraints){
						chc.setSubject(cc.subject);
						chc.insertComplexClassConstraints(pModel);
						expressions.add(chc);
					}
				}
			}
			else{
				c.insertComplexClassConstraints(pModel);
			}
		}
	}

	@Override
	public void setSubject(String pSubject) {
		for(Constraint c : expressions)
			c.setSubject(pSubject);
	}
}
