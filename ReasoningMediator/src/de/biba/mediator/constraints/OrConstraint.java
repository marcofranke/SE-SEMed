package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.biba.mediator.QuerySolution;
import de.biba.mediator.QuerySolution.Column;
import de.biba.ontology.ComplexOntClass;
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
 * Constraint welches die Vereinigungsmenge aus zwei weiteren Constraints bildet
 * @author KRA
 *
 */
public class OrConstraint implements Constraint {
	Constraint left;
	Constraint right;
	
	public OrConstraint(Constraint pLeft, Constraint pRight){
		left = pLeft;
		right = pRight;
	}
	
	public String toString(){
		return "["+left + " OR "+right +"]";
	}

	@Override
	public int getWeight() {
		return left.getWeight()+right.getWeight();
	}

	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		result.addAll(left.getResultVars());
		result.addAll(right.getResultVars());
		return result;
	}

	@Override
	public QuerySolution solve(OntModel pModel) {
		QuerySolution l = left.solve(pModel);
		QuerySolution r = right.solve(pModel);
		if(l.columnNames.containsAll(r.columnNames) && r.columnNames.containsAll(l.columnNames)){
			l.append(r);
			return l;
		}
		else{
			List<Integer> sameColumnsA = new ArrayList<Integer>();
			List<Integer> sameColumnsB = new ArrayList<Integer>();
			QuerySolution qs = new QuerySolution();
			for(int i=0; i< l.columnNames.size();i++){
				Column col = l.columnNames.get(i);
				int index = r.columnNames.indexOf(col);
				if(index>=0){
					sameColumnsB.add(index);
					sameColumnsA.add(i);
					qs.columnNames.add(col);
				}
			}
			int size = sameColumnsA.size();
			for(List<Datatype> ld:l.data){
				List<Datatype> tmp = new ArrayList<Datatype>(size);
				for(int i:sameColumnsA){
					tmp.add(ld.get(i));
				}
				qs.data.add(tmp);
			}
			for(List<Datatype> ld:r.data){
				List<Datatype> tmp = new ArrayList<Datatype>(size);
				for(int i:sameColumnsB){
					tmp.add(ld.get(i));
				}
				qs.data.add(tmp);
			}
			return qs;
		}
	}

	@Override
	public QuerySolution solve(OntModel pModel,
			List<QuerySolution> pAffectedSolutions) {
		QuerySolution result = solve(pModel);
		for(QuerySolution qs:pAffectedSolutions){
			result.naturalJoin(qs);
		}
		return result;
	}

	@Override
	public List<SimpleConstraint> flatToSimpleConstraints() {
		List<SimpleConstraint> result = left.flatToSimpleConstraints();
		result.addAll(right.flatToSimpleConstraints());
		return result;
	}

	@Override
	public void insertComplexClassConstraints(OntModel pModel) {
		if(left instanceof ClassConstraint){
			ClassConstraint cc = (ClassConstraint)left;
			if(pModel.tbox.complexClasses.containsKey(cc.className)){
				ComplexOntClass coc = pModel.tbox.complexClasses.get(cc.className);
				if(coc.constraints.length==1){
					coc.constraints[0].setSubject(cc.subject);
					left = coc.constraints[0];
				}
				else{
					ConstraintList cl = new ConstraintList();
					for(Constraint chc : coc.constraints){
						chc.setSubject(cc.subject);
						cl.addExpression(chc);
					}
					left = cl;
				}
			}
		}
		left.insertComplexClassConstraints(pModel);
		if(right instanceof ClassConstraint){
			ClassConstraint cc = (ClassConstraint)right;
			if(pModel.tbox.complexClasses.containsKey(cc.className)){
				ComplexOntClass coc = pModel.tbox.complexClasses.get(cc.className);
				if(coc.constraints.length==1){
					coc.constraints[0].setSubject(cc.subject);
					right = coc.constraints[0];
				}
				else{
					ConstraintList cl = new ConstraintList();
					for(Constraint chc : coc.constraints){
						chc.setSubject(cc.subject);
						cl.addExpression(chc);
					}
					right = cl;
				}
			}
		}
		right.insertComplexClassConstraints(pModel);
	}

	@Override
	public void setSubject(String pSubject) {
		left.setSubject(pSubject);
		right.setSubject(pSubject);
	}
}
