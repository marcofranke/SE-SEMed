package de.biba.ontology;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

public class TBox {
	/**
	 * Hï¿½lt die DatatypeProperties der TBox. Key ist der vollstï¿½ndige Name des Properties, Value ist das Property
	 */
	public Map<String, DatatypeProperty> datatypeProperties;
	
	/**
	 * Hï¿½lt die ObjectProperties der TBox. Key ist der vollstï¿½ndige Name des Properties, Value ist das Property
	 */
	public Map<String, ObjectProperty> objectProperties;
	
	/**
	 * Hï¿½lt die (einfachen) Klassen der TBox. Key ist der vollstï¿½ndige Name der Klasse, Value ist die Klasse
	 */
	public Map<String, OntClass> simpleClasses;
	
	/**
	 * Hï¿½lt die komplexen Klassen der TBox. Key ist der vollstï¿½ndige Name der Klasse, Value ist die Klasse
	 */
	public Map<String, ComplexOntClass> complexClasses;
	
	/**
	 * Hï¿½lt Informationen ï¿½ber die Subklassen-Beziehungen der einzelnen Klassen. Key ist der Name der Klasse, Value ist ein Set mit direkten Subklasse
	 * der Key-Klasse
	 */
	public Map<String, Set<String>> subClasses;
	
	/**
	 * Hï¿½lt Informationen ï¿½ber die Equivalenz-Beziehungen der einzelnen Klassen. Key ist der Name der Klasse, Value ist ein Set mit equivalenten Klassen
	 * der Key-Klasse
	 */
	public Map<String, Set<String>> equivalentClasses;
	
	/**
	 * Hï¿½lt Informationen ï¿½ber die SubProperty-Beziehungen der einzelnen Properties. Key ist der Name des Properties, Value ist ein Set mit direkten Subproperties
	 * des Key-Properties
	 */
	public Map<String, Set<String>> subProperties;
	
	/**
	 * Hï¿½lt Informationen ï¿½ber die Equivalenz-Beziehungen der einzelnen Properties. Key ist der Name des Properties, Value ist ein Set mit equivalenten Properties
	 * des Key-Properties
	 */
	public Map<String, Set<String>> equivalentProperties;
	
	/**
	 * Hï¿½lt Informationen ï¿½ber die Inverse-Beziehungen der einzelnen Properties. Key ist der Name des Properties, Value ist ein Set mit inversen Properties
	 * des Key-Properties
	 */
	public Map<String, Set<String>> inverseProperties;
	
	
	/**
	 * Default-Konstruktor
	 */
	public TBox(){
		inverseProperties = new HashMap<String, Set<String>>();
		datatypeProperties = new HashMap<String, DatatypeProperty>();
		objectProperties = new HashMap<String, ObjectProperty>();
		simpleClasses = new HashMap<String, OntClass>();
		complexClasses = new HashMap<String, ComplexOntClass>();
		subClasses = new HashMap<String, Set<String>>();
		equivalentClasses = new HashMap<String, Set<String>>();
		subProperties = new HashMap<String, Set<String>>();
		equivalentProperties = new HashMap<String, Set<String>>();
	}
	
	/**
	 * Clone-Konstruktor, erstellt einen Clone des TBox Objektes
	 * 
	 * @param tBox TBox das geklont werden soll
	 */
	public TBox(TBox tBox){
		this.inverseProperties = new HashMap<String, Set<String>>(tBox.inverseProperties);
		this.datatypeProperties = new HashMap<String, DatatypeProperty>(tBox.datatypeProperties);
		this.objectProperties = new HashMap<String, ObjectProperty>(tBox.objectProperties);
		this.simpleClasses = new HashMap<String, OntClass>(tBox.simpleClasses);
		this.complexClasses = new HashMap<String, ComplexOntClass>(tBox.complexClasses);
		this.subClasses = new HashMap<String, Set<String>>(tBox.subClasses);
		this.equivalentClasses = new HashMap<String, Set<String>>(tBox.equivalentClasses);
		this.subProperties = new HashMap<String, Set<String>>(tBox.subProperties);
		this.equivalentProperties = new HashMap<String, Set<String>>(tBox.equivalentProperties);
	}
	
	/**
	 * Fï¿½gt der TBox die Konstrukte/Aussagen einer anderen TBox hinzu
	 * @param x TBox deren Elemente hinzugefï¿½gt werden sollen
	 */
	public void add(TBox x){
		for(String key:x.datatypeProperties.keySet()){
			DatatypeProperty dp = datatypeProperties.get(key);
			if(dp==null){
				datatypeProperties.put(key, x.datatypeProperties.get(key));
			}
			else{
				dp.merge(x.datatypeProperties.get(key));
			}
		}
		for(String key:x.objectProperties.keySet()){
			ObjectProperty dp = objectProperties.get(key);
			if(dp==null){
				objectProperties.put(key, x.objectProperties.get(key));
			}
			else{
				dp.merge(x.objectProperties.get(key));
			}
		}
		for(String key:x.simpleClasses.keySet()){
			OntClass dp = simpleClasses.get(key);
			if(dp==null){
				simpleClasses.put(key, x.simpleClasses.get(key));
			}
			else{
				dp.merge(x.simpleClasses.get(key));
			}
		}
		for(String key:x.complexClasses.keySet()){
			ComplexOntClass dp = complexClasses.get(key);
			if(dp==null){
				complexClasses.put(key, x.complexClasses.get(key));
			}
			else{
				dp.merge(x.complexClasses.get(key));
			}
		}
		for(String key:x.subClasses.keySet()){
			Set<String> ss = subClasses.get(key);
			if(ss==null){
				subClasses.put(key, x.subClasses.get(key));
			}
			else{
				ss.addAll(x.subClasses.get(key));
			}
		}
		for(String key:x.equivalentClasses.keySet()){
			Set<String> ss = equivalentClasses.get(key);
			if(ss==null){
				equivalentClasses.put(key, x.equivalentClasses.get(key));
			}
			else{
				ss.addAll(x.equivalentClasses.get(key));
			}
		}
		for(String key:x.subProperties.keySet()){
			Set<String> ss = subProperties.get(key);
			if(ss==null){
				subProperties.put(key, x.subProperties.get(key));
			}
			else{
				ss.addAll(x.subProperties.get(key));
			}
		}
		for(String key:x.equivalentProperties.keySet()){
			Set<String> ss = equivalentProperties.get(key);
			if(ss==null){
				equivalentProperties.put(key, x.equivalentProperties.get(key));
			}
			else{
				ss.addAll(x.equivalentProperties.get(key));
			}
		}
		for(String key:x.inverseProperties.keySet()){
			Set<String> ss = inverseProperties.get(key);
			if(ss==null)
				inverseProperties.put(key, x.inverseProperties.get(key));
			else
				ss.addAll(x.inverseProperties.get(key));
		}
	}
	
	/**
	 * Liefert die inversen Properties eines gegebenen Properties zurï¿½ck
	 * @param property Name des Properties zu dem die inversen Properties gesucht werden sollen
	 * @return Set mit Namen der inversen Properties
	 */
	public Set<String> getInverseProperties(String property){
		return inverseProperties.get(property);
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("################### Simple Classes ###################\n");
		sb.append("--------------------------------------------\n");
		for(OntClass oc:simpleClasses.values()){
			sb.append(oc);
			sb.append("\n");
			Set<String> subs = subClasses.get(oc.getUri());
			if(subs!=null){
				sb.append("\tSubClasses: ");
				sb.append(subs);
				sb.append("\n");
			}
			Set<String> equis = equivalentClasses.get(oc.getUri());
			if(equis!=null){
				sb.append("\tEquivalentClasses: ");
				sb.append(equis);
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append("################### Complex Classes ###################\n");
		sb.append("--------------------------------------------\n");
		for(OntClass oc:complexClasses.values()){
			sb.append(oc);
			sb.append("\n");
			Set<String> subs = subClasses.get(oc.getUri());
			if(subs!=null){
				sb.append("\tSubClasses: ");
				sb.append(subs);
				sb.append("\n");
			}
			Set<String> equis = equivalentClasses.get(oc.getUri());
			if(equis!=null){
				sb.append("\tEquivalentClasses: ");
				sb.append(equis);
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append("################### ObjectProperties ###################\n");
		sb.append("--------------------------------------------\n");
		for(ObjectProperty op:objectProperties.values()){
			sb.append(op);
			sb.append("\n");
			Set<String> subs = subProperties.get(op.getUri());
			if(subs!=null){
				sb.append("\tSubProperties: ");
				sb.append(subs);
				sb.append("\n");
			}
			Set<String> equis = equivalentProperties.get(op.getUri());
			if(equis!=null){
				sb.append("\tEquivalentProperties: ");
				sb.append(equis);
				sb.append("\n");
			}
			Set<String> inverse = inverseProperties.get(op.getUri());
			if(inverse!=null){
				sb.append("\tInverseProperties: ");
				sb.append(inverse);
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append("################### DatatypeProperties ###################\n");
		sb.append("--------------------------------------------\n");
		for(DatatypeProperty dp:datatypeProperties.values()){
			sb.append(dp);
			sb.append("\n");
			Set<String> subs = subProperties.get(dp.getUri());
			if(subs!=null){
				sb.append("\tSubProperties: ");
				sb.append(subs);
				sb.append("\n");
			}
			Set<String> equis = equivalentProperties.get(dp.getUri());
			if(equis!=null){
				sb.append("\tEquivalentProperties: ");
				sb.append(equis);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Gibt an ob die TBox eine bestimmte Klasse enthï¿½lt
	 * @param pClassName Vollstï¿½ndiger Name (URI) der Klasse die gesucht werden soll
	 * @return true wenn Klasse vorhanden, sonst false
	 */
	public boolean containsClass(String pClassName){
		return simpleClasses.containsKey(pClassName)||complexClasses.containsKey(pClassName);
	}
	
	/**
	 * Gibt an ob die TBox ein bestimmtes Property enthï¿½lt
	 * @param pClassName Vollstï¿½ndiger Name (URI) des Properties das gesucht werden soll
	 * @return true wenn Property vorhanden, sonst false
	 */
	public boolean containsProperty(String pClassName){
		return objectProperties.containsKey(pClassName)||datatypeProperties.containsKey(pClassName);
	}

	//Getter & Setter
	
	public Map<String, DatatypeProperty> getDatatypeProperties() {
		return datatypeProperties;	}

	
	public void setDatatypeProperties(
			Map<String, DatatypeProperty> pDatatypeProperties) {
		datatypeProperties = pDatatypeProperties;
	}

	public Map<String, ObjectProperty> getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(Map<String, ObjectProperty> pObjectProperties) {
		objectProperties = pObjectProperties;
	}

	public Map<String, OntClass> getSimpleClasses() {
		return simpleClasses;
	}

	public void setSimpleClasses(Map<String, OntClass> pSimpleClasses) {
		simpleClasses = pSimpleClasses;
	}

	public Map<String, ComplexOntClass> getComplexClasses() {
		return complexClasses;
	}

	public void setComplexClasses(Map<String, ComplexOntClass> pComplexClasses) {
		complexClasses = pComplexClasses;
	}

	public Map<String, Set<String>> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(Map<String, Set<String>> pSubClasses) {
		subClasses = pSubClasses;
	}

	public Map<String, Set<String>> getEquivalentClasses() {
		return equivalentClasses;
	}

	public void setEquivalentClasses(Map<String, Set<String>> pEquivalentClasses) {
		equivalentClasses = pEquivalentClasses;
	}

	public Map<String, Set<String>> getSubProperties() {
		return subProperties;
	}

	public void setSubProperties(Map<String, Set<String>> pSubProperties) {
		subProperties = pSubProperties;
	}

	public Map<String, Set<String>> getEquivalentProperties() {
		return equivalentProperties;
	}

	public void setEquivalentProperties(
			Map<String, Set<String>> pEquivalentProperties) {
		equivalentProperties = pEquivalentProperties;
	}
	
	
}
