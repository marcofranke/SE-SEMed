package de.biba.mediator.constraints;

import java.util.List;

import de.biba.mediator.QuerySolution;
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
 * Constraint welches die Ergebnisse zweier weiterer Constraints konkateniert.
 * @author KRA
 *
 */
public class UnionConstraint extends BinaryConstraint {
//	String subject;
	

	public UnionConstraint(Constraint pLeft, Constraint pRight){
		super(pLeft,pRight);
	}
	

	@Override
	public QuerySolution solve(OntModel pModel) {
		QuerySolution result = left.solve(pModel);
		result.append(right.solve(pModel));
		return result;
	}

	@Override
	public QuerySolution solve(OntModel pModel,
			List<QuerySolution> pAffectedSolutions) {
		QuerySolution result = left.solve(pModel, pAffectedSolutions);
		result.append(right.solve(pModel, pAffectedSolutions));
		return result;
	}

	
	

}
