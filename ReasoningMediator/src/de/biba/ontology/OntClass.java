package de.biba.ontology;

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
 * Diese Klasse stellt eine Klasse einer Ontologie dar. Diese Klasse erbt von {@link OntologyElement}
 * @author KRA
 *
 */
public class OntClass extends OntologyElement {
	private static final long serialVersionUID = -5800745053996380626L;

	/**
	 * Konstruktor
	 * @param namespace Namespace in dem die Klasse definiert werden soll
	 * @param name Name der Klasse
	 */
	public OntClass(String namespace, String name){
		this.name = name;
		this.namespace = namespace;
	}

	/**
	 * Mergt zwei Klassen. Leere Implementierung
	 * @param pOntClass
	 */
	public void merge(OntClass pOntClass) {
		
	}
	
	public String toString(){
		return getUri();
	}
}
