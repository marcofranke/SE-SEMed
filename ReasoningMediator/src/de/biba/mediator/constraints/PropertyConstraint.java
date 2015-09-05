package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.biba.mediator.QuerySolution;
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
 * Filtert alle Individuen/Daten heraus, die über ein gegebenes Property miteinander verknüpft sind
 * @author KRA
 *
 */
public class PropertyConstraint extends SimpleConstraint {
	static int MAXTRANSITYLEVEL = 3;
	
	String subject;
	String predicate;
	String object;
	boolean symmetric = false;
	Map<String, List<ObjectPropertyStatement>> objectPropertyCache;
	Map<String, List<DatatypePropertyStatement>> datatypePropertyCache;
	
	/**
	 * Konstruktor.
	 * @param pSubject Name der Subjekt-Variable
	 * @param pPredicate Name des Property's
	 * @param pObject Name der Objekt-Variable
	 */
	public PropertyConstraint(String pSubject, String pPredicate, String pObject){
		subject = pSubject;
		predicate = pPredicate;
		object = pObject;
		objectPropertyCache = new HashMap<String, List<ObjectPropertyStatement>>();
		datatypePropertyCache = new HashMap<String, List<DatatypePropertyStatement>>();
	}
	
	
	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		result.add(subject);
		result.add(object);
		return result;
	}

	@Override
	public int getWeight() {
		return 5;
	}

	@Override
	public QuerySolution solve(OntModel pModel) {
		Logger logger = Logger.getLogger(this.getClass());
		QuerySolution result = new QuerySolution();
		String sUnit = pModel.abox.units.get(predicate);
		if(sUnit==null)
			sUnit = "unknown";
		result.columnNames.add(result.new Column(subject,"unknown"));
		result.columnNames.add(result.new Column(object,sUnit));
		logger.debug("L�se Constraint "+this);
		//Wenn ObjectProperty
		if(pModel.tbox.objectProperties.containsKey(predicate)){
			//Alle Statements zum Property abfragen
			List<ObjectPropertyStatement> lops = getObjectPropertyStatements(pModel, pModel.tbox.objectProperties.get(predicate), new HashSet<String>(), false, false);
			//Statements in L�sung konvertieren
			for(ObjectPropertyStatement ops:lops){
				List<Datatype> tmp = new ArrayList<Datatype>(2);
				tmp.add(ops.subject);
				tmp.add(ops.object);
				result.data.add(tmp);
			}
		}
		//Wenn DatatypeProperty
		else if(pModel.tbox.datatypeProperties.containsKey(predicate)){
			//Verwandte Properties suchen
			Set<String> equis = pModel.getAllSubOrEquivalentProperties(predicate);
			for(String equi:equis){
				//Statements abfragen...
				List<DatatypePropertyStatement> los = pModel.abox.dtProperties.get(equi);
				//... und konvertieren
				if(los!=null)
					for(DatatypePropertyStatement ops:los){
						List<Datatype> tmp = new ArrayList<Datatype>(2);
						tmp.add(ops.subject);
						tmp.add(ops.object);
						result.data.add(tmp);
					}
			}
		}

		return result;
	}



	public QuerySolution solve(OntModel pModel,
			List<QuerySolution> pAffectedSolutions) {
		int size = pAffectedSolutions.size();
		//Wenn DatatypeProperty
		if(pModel.tbox.datatypeProperties.containsKey(predicate)){
			String dpUnit = pModel.abox.units.get(predicate);
			if(dpUnit==null)
				dpUnit = "unknown";
			//Verwandte Properties abfragen
			Set<String> equis = pModel.getAllSubOrEquivalentProperties(predicate);
			//Wenn nur eine bestehende Lösung
			if(size==1){
				QuerySolution qs = pAffectedSolutions.get(0);
				//Wenn Cache Ergebnisse noch beherbergt
				if(datatypePropertyCache.containsKey(predicate)){
					qs.joinDatatypePropertyStatements(datatypePropertyCache.get(predicate), subject, object, dpUnit);
				}
				else{
					LinkedList<DatatypePropertyStatement> ls = new LinkedList<DatatypePropertyStatement>();
					for(String equi:equis){
						List<DatatypePropertyStatement> tmp =pModel.abox.dtProperties.get(equi);
						if(tmp!=null)
							ls.addAll(tmp);
					}
					datatypePropertyCache.put(predicate, ls);
					qs.joinDatatypePropertyStatements(ls,subject,object, dpUnit);
				}
				return qs;
			}
			else if(size==2){
				QuerySolution qs1 = pAffectedSolutions.get(0);
				QuerySolution qs2 = pAffectedSolutions.get(1);
				if(datatypePropertyCache.containsKey(predicate)){
					qs1.joinDatatypePropertyStatements(datatypePropertyCache.get(predicate),subject,object, dpUnit);
					qs2.joinDatatypePropertyStatements(datatypePropertyCache.get(predicate), subject, object, dpUnit);
				}
				else{
					List<DatatypePropertyStatement> ls = new LinkedList<DatatypePropertyStatement>();
					for(String equi:equis){
						List<DatatypePropertyStatement> tmp = pModel.abox.dtProperties.get(equi);
						if(tmp!=null)
							ls.addAll(tmp);
					}
					datatypePropertyCache.put(predicate, ls);
					qs1.joinDatatypePropertyStatements(ls,subject,object, dpUnit);
					qs2.joinDatatypePropertyStatements(ls, subject, object, dpUnit);
				}
			
				qs1.naturalJoin(qs2);
				return qs1;
			}
		}
		//Wenn ObjectProperty
		else if(pModel.tbox.objectProperties.containsKey(predicate)){
			//Alle Statements zum Property abfragen
			List<ObjectPropertyStatement> ls = getObjectPropertyStatements(pModel, pModel.tbox.objectProperties.get(predicate), new HashSet<String>(), false, false);
			//Wenn nur eine bestehende Lösung
			if(size==1){
				QuerySolution qs = pAffectedSolutions.get(0);
				qs.joinObjectPropertyStatements(ls,subject,object);
				return qs;
			}
			else if(size==2){
				QuerySolution qs1 = pAffectedSolutions.get(0);
				QuerySolution qs2 = pAffectedSolutions.get(1);

				qs1.joinObjectPropertyStatements(ls,subject,object);
				qs2.joinObjectPropertyStatements(ls,subject,object);

				
				qs1.naturalJoin(qs2);
				return qs1;
			}
			else{
				System.err.println("PropertyConstraint-solve2: Fehler!");
			}
		}
		return null;
	}
	
	public String toString(){
		return subject + " "+ predicate +" "+object;
	}


	@Override
	public String getPredicate() {
		return predicate;
	}


	@Override
	public int getType() {
		return 2;
	}


	public String getObject() {
		return object;
	}


	public void setObject(String object) {
		this.object = object;
	}


	public String getSubject() {
		return subject;
	}


	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}


	@Override
	public void setSubject(String pSubject) {
		subject = pSubject;
	}
	
	/*private Set<String> unfoldQuery(OntModel model, String predicate, Set<String> visited, boolean inverse){
		if(visited.contains(inverse?predicate+"'":predicate))
			return null;
		ObjectProperty op = model.getObjectProperty(predicate);
		if(op==null)
			return null;
		
		
		Set<String> propertyStatements = new HashSet<String>();
		
		Set<String> equis = model.getAllSubOrEquivalentProperties(predicate);
		if(equis!=null){
			for(String equi :equis){
				if(!visited.contains(inverse?equi+"'":equi)){
					visited.add(inverse?predicate+"'":predicate);
					if(equi.equals(predicate)){
						ObjectProperty o = model.getObjectProperty(equi);
						propertyStatements.add(inverse?predicate+"'":predicate);
						if(o.isSymmetric())
							propertyStatements.add(inverse?predicate:predicate+"'");
						Set<String> inverseProperties = model.tbox.getInverseProperties(predicate);
						if(inverseProperties!=null){
							for(String inv :inverseProperties){
								Set<String>tmp = unfoldQuery(model,inv, visited, !inverse);
								if(tmp!=null){
									propertyStatements.addAll(tmp);
								}
							}
						}
					}
					else{
						Set<String>tmp = unfoldQuery(model,equi, visited, false);
						if(tmp!=null){
							propertyStatements.addAll(tmp);
						}
					}
				}

			}
		}
		
		return propertyStatements;
	}
	*/
	
	public List<ObjectPropertyStatement> getObjectPropertyStatements(OntModel pModel, ObjectProperty op, Set<String> visited, boolean transitive, boolean inverse){
		List<ObjectPropertyStatement> result = new LinkedList<ObjectPropertyStatement>();
		String name = op.getUri();
		//Wenn das Property schon "bearbeitet" wurde
		if(visited.contains(name))
			return result;
		visited.add(op.getUri());
		boolean trans = transitive  || op.isTransitive();
		String cacheID = op.getUri()+trans+""+inverse;
		//Wenn Ergebnis im Cache vorhanden
		if(objectPropertyCache.containsKey(cacheID))
			return objectPropertyCache.get(cacheID);
		
		List<ObjectPropertyStatement> tmp = pModel.abox.oProperties.get(name);
		if(tmp!=null){
			if(!inverse || op.isSymmetric()){
				result.addAll(tmp);
			}
			if(inverse || op.isSymmetric()){
				for(ObjectPropertyStatement ops:tmp){
					result.add(new ObjectPropertyStatement(ops.object, ops.subject));
				}
			}
		}
		Set<String> inverseProperties = pModel.tbox.getInverseProperties(name);
		if(inverseProperties!=null){
			for(String inv :inverseProperties){
				List<ObjectPropertyStatement> lops = getObjectPropertyStatements(pModel, pModel.tbox.objectProperties.get(inv), visited, false, !inverse);
				result.addAll(lops);
			}
		}
		
		Set<String> equis = pModel.getAllSubOrEquivalentProperties(name);
		for(String equi :equis){
			if(equi.equals(name)){
				continue;
			}
			List<ObjectPropertyStatement> lops = getObjectPropertyStatements(pModel, pModel.tbox.objectProperties.get(equi), visited, false, inverse);
			result.addAll(lops);
		}
		if(trans){
			expandTransitivity(result);
		}
		objectPropertyCache.put(cacheID, result);
		return result;
	}


	private void expandTransitivity(List<ObjectPropertyStatement> pResult) {
		if(pResult.isEmpty())
			return;
		Comparator<ObjectPropertyStatement> comparator = new Comparator<ObjectPropertyStatement>() {
			@Override
			public int compare(ObjectPropertyStatement pO1,
					ObjectPropertyStatement pO2) {
				return pO1.subject.compareTo(pO2.subject);
			}
		};
		Collections.sort(pResult,comparator);
		List<ObjectPropertyStatement> transitiveStatements = new LinkedList<ObjectPropertyStatement>();
		Individual lastSubject = null;
		Set<Individual> directNeighbours = new HashSet<Individual>();
		Set<Individual> visited = new HashSet<Individual>();
		Map<Long,List<Individual>> muh = new HashMap<Long, List<Individual>>();
		
		List<Individual> tmp = new LinkedList<Individual>();
		for(ObjectPropertyStatement ops:pResult){
			if(!ops.subject.equals(lastSubject)	&& lastSubject !=null){
				muh.put(lastSubject.id, tmp);
				tmp=new LinkedList<Individual>();
			}
			tmp.add(ops.object);
			lastSubject = ops.subject;
		}
		muh.put(lastSubject.id, tmp);
		lastSubject = null;
		
		for(ObjectPropertyStatement ops : pResult){
			if(!ops.subject.equals(lastSubject)	&& lastSubject !=null){
				addStarements(lastSubject,directNeighbours, muh,transitiveStatements, visited);
				directNeighbours.clear();
				visited.clear();
			}
			directNeighbours.add(ops.object);
			lastSubject = ops.subject;
		}
		addStarements(lastSubject,directNeighbours, muh,transitiveStatements, visited);
		pResult.addAll(transitiveStatements);
	}


	private void addStarements(Individual pLastSubject, Set<Individual> pDirectNeighbours,
			Map<Long, List<Individual>> pMuh,
			List<ObjectPropertyStatement> pTransitiveStatements, Set<Individual> pVisited) {
		for(Individual neighbour: pDirectNeighbours){
			if(pVisited.contains(neighbour))
				continue;
			List<Individual> nnl = pMuh.get(neighbour.id);
			if(nnl!=null)
				for(Individual l : nnl){
					insertRelationShip(pLastSubject,l,pMuh,pTransitiveStatements,pDirectNeighbours,pVisited,1
							);
				}
		}
	}


	private void insertRelationShip(Individual pLastSubject, Individual pL,
			Map<Long, List<Individual>> pMuh,
			List<ObjectPropertyStatement> pTransitiveStatements,
			Set<Individual> pDirectNeighbours, Set<Individual> pVisited, int pI) {
		if(pI > MAXTRANSITYLEVEL)
			return;
		if(pVisited.contains(pL) || pDirectNeighbours.contains(pL) || pL.equals( pLastSubject))
			return;
		pVisited.add(pL);
		List<Individual> nnl = pMuh.get(pL.id);
		if(nnl!=null)
			for(Individual l : nnl){
				insertRelationShip(pLastSubject, l, pMuh, pTransitiveStatements, pDirectNeighbours, pVisited,pI+1);
			}
		pTransitiveStatements.add(new ObjectPropertyStatement(pLastSubject, pL));
	}


	


	
}
