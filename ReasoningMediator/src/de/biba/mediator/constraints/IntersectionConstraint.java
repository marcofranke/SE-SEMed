package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.biba.mediator.QuerySolution;
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
 * Constraint welches die Schnittmenge aus zwei weiteren Constraints bildet
 * @author KRA
 *
 */
public class IntersectionConstraint extends BinaryConstraint {
	String subject;
	
	public IntersectionConstraint(Constraint pLeft, Constraint pRight) {
		super(pLeft, pRight);
	}

	@Override
	public QuerySolution solve(OntModel pModel) {
		QuerySolution result1 = left.solve(pModel);
		QuerySolution result2 = right.solve(pModel);
		if(subject==null){
			result1.naturalJoin(result2);
			return result1;
		}
		else{
			QuerySolution result = new QuerySolution();
			result.columnNames.add(result.new Column(subject,"unknown"));
			final int posLeft = result1.columnNames.indexOf(subject);
			final int posRight = result2.columnNames.indexOf(subject);
			if(posLeft<0 || posRight<0)
				return result;
			Collections.sort(result1.data, new Comparator<List<Datatype>>(){
				@Override
				public int compare(List<Datatype> pO1, List<Datatype> pO2) {
					return pO1.get(posLeft).compareTo(pO2.get(posLeft));
				}				
			});
			Collections.sort(result2.data, new Comparator<List<Datatype>>(){
				@Override
				public int compare(List<Datatype> pO1, List<Datatype> pO2) {
					return pO1.get(posRight).compareTo(pO2.get(posRight));
				}				
			});
			
			Iterator<List<Datatype>> iter1 = result1.data.iterator();
			Iterator<List<Datatype>> iter2 = result2.data.iterator();
			
			boolean getA = true;
			boolean getB = true;
			
			Datatype d1 = null;
			Datatype d2 = null;
			
			while((iter1.hasNext() || getB) && (iter2.hasNext() || getB)){
				if(getA)
					d1 = iter1.next().get(posLeft);
				if(getB)
					d2 = iter2.next().get(posRight);
				int cmp = d1.compareTo(d2);
				if(cmp==0){
					List<Datatype> tmp = new ArrayList<Datatype>(1);
					tmp.add(d1);
					result.data.add(tmp);
					getA = true;
					getB = true;
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
	public void setSubject(String pSubject) {
		super.setSubject(pSubject);
		subject = pSubject;
	}

}
