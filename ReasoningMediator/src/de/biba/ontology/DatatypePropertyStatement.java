package de.biba.ontology;

import de.biba.mediator.PropertyStatement;
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
 * Objekt welches einem Individuum ein Datum zuordnet.
 * @author KRA
 *
 */
public class DatatypePropertyStatement implements PropertyStatement{
	/**
	 * Individuum dem Datum zuordnet wird.
	 */
	public Individual subject;
	
	/**
	 * Datum das dem Individuum zugeordnet wird.
	 */
	public Datatype object;
	
	/**
	 * Konstruktor. Setzt das Individuum und Datum
	 * @param subject Individuum dem Datum zuordnet wird.
	 * @param object Datum das dem Individuum zugeordnet wird.
	 */
	public DatatypePropertyStatement(Individual subject, Datatype object){
		this.subject = subject;
		this.object = object;
	}
	
	@Override
	public String toString() {
		return subject.toString() + " : " + object.toString(); 
	}
}
