package de.biba.mediator.constraints.insert;

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
 * Stellt ein Constraint dar, mit dem einer Instanz ein Wert als Property zugewiesen wird
 * @author KRA
 *
 */
public class SimplePropertyInsertConstraint implements InsertConstraint{

	/**
	 * Liefert den Variablennamen der Instanz
	 * @return
	 */
	public String getSubVarName() {
		return subVarName;
	}

	public void setSubVarName(String subVarName) {
		this.subVarName = subVarName;
	}

	/**
	 * Liefert den Propertynamen
	 * @return
	 */
	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 * Liefert das Datum/den Wert, der der Instanz zugewiesen werden soll
	 * @return
	 */
	public Datatype getData() {
		return data;
	}

	public void setData(Datatype data) {
		this.data = data;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	private String subVarName;
	private String predicate;
	private Datatype data;
	private String unit;

	public SimplePropertyInsertConstraint(String variable, String property,
			Datatype dt, String unit) {
		this.subVarName = variable;
		this.predicate = property;
		this.data = dt;
		this.unit = unit;
	}

	@Override
	public String toString() {
		return subVarName +" <"+predicate+"> "+data.toString()+(unit!=null?"["+unit+"]":"");
	}
}
