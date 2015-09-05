package de.biba.ontology;

import de.biba.mediator.constraints.Constraint;

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
 * Diese Klasse soll komplexe Klassen einer Ontologie darstellen. Dazu enthält die Klasse Constraints welche die Eigenheiten einer komplexen Klasse darstellen
 * @author KRA
 *
 */
public class ComplexOntClass extends OntClass {
	private static final long serialVersionUID = -2865907108449984744L;
	public Constraint[] constraints;
	
	ComplexOntClass(String namespace, String name, Constraint[] pConstraints) {
		super(namespace,name);
		constraints = pConstraints;
	}


	public void merge(ComplexOntClass x){
		//TODO:
	}
}
