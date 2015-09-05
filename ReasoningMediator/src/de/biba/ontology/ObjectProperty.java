package de.biba.ontology;

import java.util.HashSet;
import java.util.Set;

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
 * Diese Klasse stellt ein ObjectProperty einer Ontologie dar. Die Klasse erbt von {@link AbstractProperty}
 * @author KRA
 *
 */
public class ObjectProperty extends AbstractProperty{
	private static final long serialVersionUID = -2520511282699493782L;
	private boolean transitive;
	private boolean symmetric;
	
	private Set<String> range;
	
	/**
	 * Konstruktor
	 */
	public ObjectProperty(){
		domain = new HashSet<String>();
		range = new HashSet<String>();
	}
	
	/**
	 * Konstruktor
	 * @param namespace Namespace in dem das ObjectProperty definiert werden soll
	 * @param name Name des Properties
	 */
	public ObjectProperty(String namespace, String name){
		this.name = name;
		this.namespace = namespace;
		domain = new HashSet<String>();
		range = new HashSet<String>();
	}
	
	/**
	 * Liefert den Range des Properties zurück.
	 * @return Liste mit Klassennamen die den Range des Properties darstellen
	 */
	public Set<String> getRange() {
		return range;
	}
	
	/**
	 * Setzt den Range des Properties
	 * @param range Liste mit Klassennamen die den Range des Properties darstellen
	 */
	public void setRange(Set<String> range) {
		this.range = range;
	}

	/**
	 * Gibt an ob es sich um transitives Property handelt
	 * @return
	 */
	public boolean isTransitive() {
		return transitive;
	}
	
	/**
	 * Setzt ob es sich um ein transitives Property handelt
	 * @param pTransitive 
	 */
	public void setTransitive(boolean pTransitive) {
		transitive = pTransitive;
	}
	
	/**
	 * Gibt an ob es sich um ein symetrisches Property handelt
	 * @return
	 */
	public boolean isSymmetric() {
		return symmetric;
	}
	
	/**
	 * Setzt ob es sich um ein symetrisches Property handelt
	 * @param pSymmetric
	 */
	public void setSymmetric(boolean pSymmetric) {
		symmetric = pSymmetric;
	}
	
	/**
	 * Fügt die Eigenschaften eines anderen ObjectProperties hinzu.
	 * @param pObjectProperty
	 */
	public void merge(ObjectProperty pObjectProperty) {
		if(pObjectProperty.isFunctional())
			functional = true;
		if(pObjectProperty.isInverseFunctional())
			inverseFunctional = true;
		if(pObjectProperty.isSymmetric())
			symmetric = true;
		if(pObjectProperty.isTransitive())
			transitive = true;
		domain.addAll(pObjectProperty.domain);
		range.addAll(pObjectProperty.range);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer(getUri());
		String s = "";
		if(functional)
			s+=", functional";
		if(inverseFunctional)
			s+=", inverseFunctional";
		if(transitive)
			s+=", transitive";
		if(symmetric)
			s+=", symmetric";
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
