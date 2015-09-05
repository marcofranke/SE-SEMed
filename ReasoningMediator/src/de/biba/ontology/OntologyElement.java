package de.biba.ontology;

import java.io.Serializable;

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
 * Basisklasse für Elemente einer TBox
 * @author KRA
 *
 */
public abstract class OntologyElement implements Serializable {
	private static final long serialVersionUID = -6742502493935820699L;
	
	/**
	 * Name des Elements
	 */
	String name;
	
	/**
	 * Namespace in dem das Element definiert wurde
	 */
	String namespace;
	
	/**
	 * Liefert den vollständigen Namen bestehend aus Namespace und Namen in Form einer URI zurück
	 * @return URI des vollständigen Namens
	 */
	public String getUri(){
		if(namespace != null && !namespace.equals(""))
			return namespace+"#"+name;
		else
			return name;
	}
}
