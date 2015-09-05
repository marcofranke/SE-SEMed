package de.biba.mediator;

import java.util.ArrayList;

import de.biba.mediator.constraints.insert.InsertConstraint;

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
 * Stellt ein Insert-Befehl dar.
 * @author KRA
 *
 */
public class InputQuery implements IQuery {

	private ArrayList<InsertConstraint> constraints;

	/**
	 * Liefert die Insert-Statements der Query zurück
	 * @return Liste mit Insert-Statements
	 */
	public ArrayList<InsertConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(ArrayList<InsertConstraint> constraints) {
		this.constraints = constraints;
	}

	public InputQuery(){
		constraints = new ArrayList<InsertConstraint>();
	}
	
	@Override
	public boolean isIntputQuery() {
		return true;
	}
	
	/**
	 * Fügt ein Insert-Statement der Query hinzu
	 * @param ic Insert-Statement das hinzugefügt werden soll.
	 */
	public void addConstraint(InsertConstraint ic) {
		constraints.add(ic);
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(InsertConstraint ic : constraints)
			sb.append(ic.toString()+".\n");
		return sb.toString();
	}
}
