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
 * Stellt ein Constraint dar, mit dem eine Instanz einer definierten (Ontologie-) Klasse erstellt wird
 * @author KRA
 *
 */
public class ClassInsertConstraint implements InsertConstraint{

	private String className;
	private String variableName;

	/**
	 * Gibt den Namen der Klasse zurück, von der die Instanz erstellt werden soll
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Gibt den Variablennamen zurück unter dem die Instanz referenziert werden soll.
	 * @return
	 */
	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	/**
	 * Konstruktor...
	 * @param variable
	 * @param className
	 */
	public ClassInsertConstraint(String variable, String className) {
		this.variableName = variable;
		this.className = className;
	}
	
	@Override
	public String toString() {
		return variableName + " a <" +className+">";
	}
}
