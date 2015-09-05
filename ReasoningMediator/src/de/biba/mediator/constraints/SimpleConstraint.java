package de.biba.mediator.constraints;

import java.util.ArrayList;
import java.util.List;

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
 * Abstrakte Klasse, die als Basis für einfache Constraints dient
 * @author KRA
 *
 */
public abstract class SimpleConstraint implements Constraint {

	@Override
	public List<SimpleConstraint> flatToSimpleConstraints() {
		List<SimpleConstraint> result = new ArrayList<SimpleConstraint>(1);
		result.add(this);
		return result;
	}
	
	/**
	 * Liefert den Namen des Prädikat des Triples zurück. Im allgemeinen ist dies der Name des Property's.
	 * @return
	 */
	public abstract String getPredicate();
	
	/**
	 * Gibt den Typ des Constraints an, genauer gesagt welche Art von Daten f�r dieses Constraint ben�tigt werden.
	 * 0 = classConstraint
	 * 1 = SimplePropertyConstraint (Constraint bei dem Individuen gesucht werden, die einen bestimmten Wert haben)
	 * 2 = normale PropertyConstraints (der Rest)
	 * @return
	 */
	public abstract int getType();
	
	@Override
	public void insertComplexClassConstraints(OntModel pModel) {
		
	}
}
