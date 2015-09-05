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
 * Klasse die zwei Individuen einer Ontologie miteinander in Beziehung setzt. Stellt dabei eine Art Tripel (Subjekt, Prädikat, Objekt) dar, nur
 * das das Prädikat nicht hier, sondern in der A-Box {@link ABox} gehalten wird.
 * @author KRA
 *
 */
public class ObjectPropertyStatement {
	/**
	 * Subjekt-Individuum der Beziehung
	 */
	public Individual subject;
	
	/**
	 * Objekt-Individuum der Beziehung
	 */
	public Individual object;
	
	/**
	 * Konstruktor. Setzt das Subjekt und Objekt in Beziehung
	 * @param subject Subjekt-Individuum der Beziehung
	 * @param object Objekt-Individuum der Beziehung
	 */
	public ObjectPropertyStatement(Individual subject, Individual object){
		this.subject = subject;
		this.object = object;
	}
	
	@Override
	public String toString() {
		return subject.toString() + " : " + object.toString(); 
	}
}
