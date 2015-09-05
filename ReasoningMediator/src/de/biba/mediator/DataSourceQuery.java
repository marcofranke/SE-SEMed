package de.biba.mediator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.biba.ontology.ABox;
import de.biba.ontology.datatypes.Datatype;

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
 * Diese Klasse stellt eine Query an eine DataSource dar. Sie enthält in Containern die Klassen- und Property-Namen, zu denen die Datenquelle liefern soll. 
 * Zusätzlich kann dem Objekt eine ABox mitgegeben werden, welche Ergebnisse aus anderen Wrappern enthalten kann.
 * @author KRA
 *
 */
public class DataSourceQuery {
	List<String> classNames;
	List<String> propertyNames;
	List<PropertyWithValue> propertiesWithValues;
	ABox abox;
	
	public DataSourceQuery(){
		classNames = new ArrayList<String>();
		propertyNames = new ArrayList<String>();
		propertiesWithValues = new LinkedList<PropertyWithValue>();
		
	}
	
	
	/**
	 * Diese Klasse stellt ein Teil einer Query dar, mit dem nach Properties gefragt wird, die einen bestimmten Wert haben.
	 * @author KRA
	 *
	 */
	public class PropertyWithValue{
		/**
		 * Konstruktor
		 * @param pR Property das die Eigenschaften erfüllen soll
		 * @param pValue Wert der Erfüllt werden soll
		 * @param pComparator Vergleichsoperator. Damit ist es z.B. möglich alle Werte abzufragen, die Größer als ein bestimmter Wert sind.
		 */
		public PropertyWithValue(String pR, Datatype pValue,Comparator<Datatype> pComparator) {
			propertyName = pR;
			value = pValue;
			comparator = pComparator;
		}
		Comparator<Datatype> comparator;
		String propertyName;
		Datatype value;
		
		/**
		 * 
		 * @return Name des Properties
		 */
		public String getPropertyName() {
			return propertyName;
		}
		public void setPropertyName(String pPropertyName) {
			propertyName = pPropertyName;
		}
		
		/**
		 * 
		 * @return Wert der verglichen werden soll
		 */
		public Datatype getValue() {
			return value;
		}
		public void setValue(Datatype pValue) {
			value = pValue;
		}
		
		/**
		 * 
		 * @return Vergleichsoperator
		 */
		public Comparator<Datatype> getComparator() {
			return comparator;
		}
	}

	/**
	 * 
	 * @return Alle Namen der angeforderten Klassen
	 */
	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> pClassNames) {
		classNames = pClassNames;
	}

	/**
	 * 
	 * @return Alle Namen der angeforderten Properties
	 */
	public List<String> getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(List<String> pPropertyNames) {
		propertyNames = pPropertyNames;
	}

	/**
	 * 
	 * @return Alle Property-Vergleiche
	 */
	public List<PropertyWithValue> getPropertiesWithValues() {
		return propertiesWithValues;
	}

	public void setPropertiesWithValues(
			List<PropertyWithValue> pPropertiesWithValues) {
		propertiesWithValues = pPropertiesWithValues;
	}

	/**
	 * 
	 * @return ABox mit den Ergebnissen aus anderen Wrappern
	 */
	public ABox getAbox() {
		return abox;
	}

	public void setAbox(ABox abox) {
		this.abox = abox;
	}
	
}
