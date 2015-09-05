package de.biba.ontology;

import java.util.Set;

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
 * Basisklasse eines Properties. Stellt gemeinsame Funktionen zur Verfügung
 * @author KRA
 *
 */
public class AbstractProperty extends OntologyElement {
	private static final long serialVersionUID = 8443981213013710611L;
	protected boolean functional;
	protected boolean inverseFunctional;
	protected Set<String> domain;
	
	protected AbstractProperty(){
		
	}
	
	/**
	 * Gibt an ob es sich um ein funktionales Property handelt
	 * @return true wenn funktional, sonst false
	 */
	public boolean isFunctional() {
		return functional;
	}
	public void setFunctional(boolean pFunctional) {
		functional = pFunctional;
	}
	
	/**
	 * Gibt an ob es sich um ein invers-funktionales Property handelt. Inverse-Funktionale Properties werden zur Duplikatserkennung verwendet.
	 * @return true wenn funktional, sonst false
	 */
	public boolean isInverseFunctional() {
		return inverseFunctional;
	}
	public void setInverseFunctional(boolean pInverseFunctional) {
		inverseFunctional = pInverseFunctional;
	}
	
	/**
	 * Gibt die Menge der Klassen an, die dieses Property besitzen können.
	 * @return
	 */
	public Set<String> getDomain() {
		return domain;
	}
	public void setDomain(Set<String> domain) {
		this.domain = domain;
	}
}
