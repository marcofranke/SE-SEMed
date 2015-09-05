package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.biba.ontology.ComplexOntClass;
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

/**
 * Abstrakte Klasse fÃ¼r Constraints die sich aus zwei weiteren Constraints zusammensetzen.
 * @author KRA
 *
 */
public abstract class BinaryConstraint implements Constraint {
	Constraint left;
	Constraint right;
	
	
	public BinaryConstraint(Constraint pLeft, Constraint pRight){
		left = pLeft;
		right = pRight;
	}
	
	@Override
	public List<SimpleConstraint> flatToSimpleConstraints() {
		List<SimpleConstraint> result = new ArrayList<SimpleConstraint>();
		result.addAll(left.flatToSimpleConstraints());
		result.addAll(right.flatToSimpleConstraints());
		return result;
	}

	@Override
	public Set<String> getResultVars() {
		Set<String> result = new HashSet<String>();
		result.addAll(left.getResultVars());
		result.addAll(right.getResultVars());
		return result;
	}

	@Override
	public int getWeight() {
		return left.getWeight()+right.getWeight()+10;
	}

	@Override
	public void insertComplexClassConstraints(OntModel pModel) {
		if(left instanceof ClassConstraint){
			ClassConstraint cc = (ClassConstraint)left;
			if(pModel.tbox.complexClasses.containsKey(cc.className)){
				ComplexOntClass coc = pModel.tbox.complexClasses.get(cc.className);
				if(coc.constraints.length>1){
					ConstraintList cl = new ConstraintList();
					for(Constraint chc: coc.constraints){
						chc.setSubject(cc.subject);
						chc.insertComplexClassConstraints(pModel);
						cl.addExpression(chc);
					}
					left = cl;
				}
				else if(coc.constraints.length==1){
					left = coc.constraints[0];
					coc.constraints[0].setSubject(cc.subject);
					left.insertComplexClassConstraints(pModel);
				}
			}
		}
		else{
			left.insertComplexClassConstraints(pModel);
		}
		
		if(right instanceof ClassConstraint){
			ClassConstraint cc = (ClassConstraint)right;
			if(pModel.tbox.complexClasses.containsKey(cc.className)){
				ComplexOntClass coc = pModel.tbox.complexClasses.get(cc.className);
				if(coc.constraints.length>1){
					ConstraintList cl = new ConstraintList();
					for(Constraint chc: coc.constraints){
						chc.setSubject(cc.subject);
						chc.insertComplexClassConstraints(pModel);
						cl.addExpression(chc);
					}
					right = cl;
				}
				else if(coc.constraints.length==1){
					right = coc.constraints[0];
					coc.constraints[0].setSubject(cc.subject);
					right.insertComplexClassConstraints(pModel);
				}
			}
		}
		else{
			right.insertComplexClassConstraints(pModel);
		}
	}

	@Override
	public void setSubject(String pSubject) {
		left.setSubject(pSubject);
		right.setSubject(pSubject);
	}

}
