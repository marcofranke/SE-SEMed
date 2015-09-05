package de.biba.mediator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.ObjectPropertyStatement;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;

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
 * Diese Klasse sucht und entfernt Duplikate in einer Ontologie. Dazu werden alle invers-funktionalen Properties verwendet. Für jedes invers-funktionale Property werden alle
 * zugehörigen Datensätze in der ABox verglichen. Haben zwei Datensätze für ein solches Property den gleichen Wert, so sind sie identisch und die Individuen werden gemergt.
 * @author KRA
 *
 */
public class DuplicateRemover {
	
	/**
	 * Sucht und entfernt Duplikate (=gleiche Instanzen) in einer Ontologie
	 * @param pModel Ontologie in der Duplikate entfernt werden sollen.
	 * Dabei werden die DataproperyStatements, die zuviel sind, gel�scht.
	 * 
	 */
	public void removeDuplicates(OntModel pModel){
		
		Comparator<DatatypePropertyStatement> comparator1 = new Comparator<DatatypePropertyStatement>(){
			@Override
			public int compare(DatatypePropertyStatement pO1,
					DatatypePropertyStatement pO2) {
				return pO1.object.compareTo(pO2.object);
			}			
		};
		Comparator<ObjectPropertyStatement> comparator2 = new Comparator<ObjectPropertyStatement>(){
			@Override
			public int compare(ObjectPropertyStatement pO1,
					ObjectPropertyStatement pO2) {
				return pO1.object.compareTo(pO2.object);
			}			
		};
		
		//Alle invers-funktionalen DatatypeProperties durchgehen
		for(DatatypeProperty dp : pModel.tbox.datatypeProperties.values()){
			if(dp.isInverseFunctional()){
				List<DatatypePropertyStatement> tmp = pModel.abox.dtProperties.get(dp.getUri());
				if(tmp!=null && tmp.size()>1){
					Collections.sort(tmp,comparator1);
					
					ListIterator<DatatypePropertyStatement> iter = tmp.listIterator();
					DatatypePropertyStatement last = iter.next();
					
					while(iter.hasNext()){
						DatatypePropertyStatement current = iter.next();
						int cmp = comparator1.compare(last, current);
						if(cmp==0){
							current.subject.id = last.subject.id;
							iter.remove();
							continue;
						}
						last = current;
					}
				}
			}
		}
	//Alle invers-funktionalen ObjectProperties durchgehen
		for(ObjectProperty op : pModel.tbox.objectProperties.values()){
			if(op.isInverseFunctional()){
				List<ObjectPropertyStatement> tmp = pModel.abox.oProperties.get(op.getUri());
				if(tmp!=null){
					Collections.sort(tmp,comparator2);
					ListIterator<ObjectPropertyStatement> iter = tmp.listIterator();
					
					ObjectPropertyStatement last = iter.next();
					
					while(iter.hasNext()){
						ObjectPropertyStatement current = iter.next();
						int cmp = comparator2.compare(last, current);
						if(cmp==0){
							current.subject.id = last.subject.id;
							iter.remove();
							continue;
						}
						last = current;
					}
				}
			}
		}
		for(OntClass oc:pModel.tbox.simpleClasses.values()){
			List<Individual> tmp = pModel.abox.individuals.get(oc.getUri());
			if(tmp!=null && tmp.size()>1){
				Collections.sort(tmp);
				ListIterator<Individual> iter = tmp.listIterator();
				Individual last = iter.next();
				while(iter.hasNext()){
					Individual current = iter.next();
					if(current.compareTo(last)==0){
						iter.remove();
						continue;
					}
					last = current;
				}
			}
		}
	}
	
}
