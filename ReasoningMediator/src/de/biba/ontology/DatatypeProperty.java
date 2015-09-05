package de.biba.ontology;

import java.util.HashSet;
import java.util.Set;

import de.biba.mediator.Datatypes;
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
 * Diese Klasse stellt ein Datatype-Property einer Ontologie dar.
 * @author KRA
 *
 */
public class DatatypeProperty extends AbstractProperty{
	private static final long serialVersionUID = 8034909889713923949L;
	private Set<Datatypes> range;
	
	/**
	 * Default-Konstruktor
	 */
	public DatatypeProperty(){
		domain = new HashSet<String>();
		range = new HashSet<Datatypes>();
	}
	
	/**
	 * Konstruktor
	 * @param namespace Namespace in dem das Datatype-Property definiert werden soll
	 * @param name Name des Properties
	 */
	public DatatypeProperty(String namespace, String name){
		this.namespace = namespace;
		this.name = name;
		domain = new HashSet<String>();
		range = new HashSet<Datatypes>();
	}
	
	/**
	 * Liefert den Range des Properties zurück
	 * @return Range des Properties. Set von Datentypen
	 */
	public Set<Datatypes> getRange() {
		return range;
	}
	/**
	 * Setzt den Range des Properties
	 * @param range Range des Properties. Set von Datentypen
	 */
	public void setRange(Set<Datatypes> range) {
		this.range = range;
	}
	
	/**
	 * Fügt die Eigenschaften eines anderen DatatypeProperties hinzu.
	 * @param pDatatypeProperty
	 */
	public void merge(DatatypeProperty pDatatypeProperty) {
		if(pDatatypeProperty.isFunctional())
			setFunctional(true);
		if(pDatatypeProperty.isInverseFunctional())
			setInverseFunctional(true);
		domain.addAll(pDatatypeProperty.domain);
		range.addAll(pDatatypeProperty.range);
	}
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer(getUri());
		String s = "";
		if(functional)
			s+=", functional";
		if(inverseFunctional)
			s+=", inverseFunctional";
		if(s.length()>2){
			s = s.substring(2);
			sb.append("\n\t"+s);
		}
		sb.append("\n\t");
		sb.append("Domain: ");
		sb.append(domain);
		sb.append("\n\t");
		sb.append("Range: ");
		sb.append(range);
		return sb.toString();
	}
}
