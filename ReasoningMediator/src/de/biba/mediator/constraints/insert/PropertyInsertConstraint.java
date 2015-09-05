package de.biba.mediator.constraints.insert;

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
 * Stellt ein Constraint dar, mit dem eine Instanz über ein Property mit einer anderen Instanz verknüpft wird
 * @author KRA
 *
 */
public class PropertyInsertConstraint implements InsertConstraint{

	private String predicate;
	private String subVarName;
	private String ObVarName;

	/**
	 * Konstruktor
	 * @param subject Variablenname der ersten Instanz
	 * @param predicate Propertyname
	 * @param object Variablenname der zweiten Instanz
	 */
	public PropertyInsertConstraint(String subject, String predicate, String object) {
		this.subVarName = subject;
		this.predicate = predicate;
		this.ObVarName = object;
	}

	/**
	 * Liefert den Propertynamen zurück
	 * @return
	 */
	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 * Liefert den Variablennamen der Instanz, der ein Property zugewiesen werden soll, zurück.
	 * @return
	 */
	public String getSubVarName() {
		return subVarName;
	}

	public void setSubVarName(String subVarName) {
		this.subVarName = subVarName;
	}

	/**
	 * Liefert den Variablennamen der zweiten Instanz zurück
	 * @return
	 */
	public String getObVarName() {
		return ObVarName;
	}

	public void setObVarName(String obVarName) {
		ObVarName = obVarName;
	}

	@Override
	public String toString() {
		return subVarName + " <"+predicate+"> "+ObVarName;
	}
}
