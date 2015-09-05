package de.biba.ontology;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.biba.mediator.constraints.Constraint;
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
 * Diese Klasse stellt eine Ontologie dar. Sie besteht aus einer TBox und einer
 * ABox.
 * 
 * @author KRA
 * 
 */
public class OntModel {
	/**
	 * ABox der Ontologie.
	 */
	public ABox abox;

	/**
	 * TBox der Ontologie
	 */
	public TBox tbox;

	/**
	 * Zï¿½hler der die Individuen in der Ontologie zï¿½hlt
	 */
	long individualCounter;
	long individualCounterStart;

	/**
	 * Basisklasse der Ontologie. Alle neuen Klasse der Ontologie erben davon.
	 */
	public static OntClass THING = new OntClass(
			"http://www.w3.org/2002/07/owl", "Thing");

	/**
	 * Default Konstruktor. Erstellt eine Ontologie in der nur die Basisklasse
	 * "Thing" erstellt wurde.
	 */
	public OntModel() {
		abox = new ABox();
		tbox = new TBox();
		tbox.simpleClasses.put("THING", THING);
		individualCounter = 0L;
		individualCounterStart = 0L;
	}

	/**
	 * Konstruktor. Erstellt eine Ontologie in der nur die Basisklasse "Thing"
	 * erstellt wurde.
	 * 
	 * @param individualCounter
	 *            Setzt den IndividualCounter der Klasse auf einen Wert. Die IDs
	 *            der Individuen starten bei diesem Wert
	 */
	public OntModel(long individualCounter) {
		abox = new ABox();
		tbox = new TBox();
		this.individualCounter = individualCounter;
		individualCounterStart = individualCounter;
	}
	
	/**
	 * Clone-Konstruktor, erstellt einen Clone des Ontologie Objektes
	 * 
	 * @param ontModel Das Modell das geklont werden soll
	 */
	public OntModel(OntModel ontModel){
		this.abox = new ABox(ontModel.abox);
		this.tbox = new TBox(ontModel.tbox);
		this.individualCounter = ontModel.individualCounter;
		this.individualCounterStart = ontModel.individualCounterStart;
	}

	/**
	 * Erstellt eine Klasse in der Ontologie die von der Klasse Thing erbt
	 * 
	 * @param namespace
	 *            Namespace in der die Klasse erstellt werden soll
	 * @param name
	 *            Name der Klasse
	 * @return Erstellte Klasse
	 */
	public OntClass createOntClass(String namespace, String name) {
		return createOntClass(namespace, name, THING.getUri());
	}

	/**
	 * Liefert ein DataProperty der Ontologie zurï¿½ck
	 * 
	 * @param name
	 *            Vollstï¿½ndiger Name (inklusive Namespace) des angeforderten
	 *            Properties
	 * @return Angefordertes Property sofern vorhanden, sonst null
	 */
	public DatatypeProperty getDatatypeProperty(String name) {
		return tbox.datatypeProperties.get(name);
	}

	/**
	 * Liefert ein ObjectProperty der Ontologie zurï¿½ck
	 * 
	 * @param name
	 *            Vollstï¿½ndiger Name (inklusive Namespace) des angeforderten
	 *            Properties
	 * @return Angefordertes Property sofern vorhanden, sonst null
	 */
	public ObjectProperty getObjectProperty(String name) {
		return tbox.objectProperties.get(name);
	}

	/**
	 * Liefert eine Klasse der Ontologie zurï¿½ck
	 * 
	 * @param name
	 *            Vollstï¿½ndiger Name (inklusive Namespace) der angeforderten
	 *            Klasse
	 * @return Angeforderte Klasse sofern vorhanden, sonst null
	 */
	public OntClass getOntClass(String name) {
		OntClass oc = tbox.simpleClasses.get(name);
		if (oc == null)
			oc = tbox.complexClasses.get(name);
		return oc;
	}

	/**
	 * Erstellt eine Klasse in der Ontologie
	 * 
	 * @param namespace
	 *            Namespace in der die Klasse erstellt werden soll
	 * @param name
	 *            Name der Klasse
	 * @param superClass
	 *            Name der Basisklasse der zu erstellenden Klasse
	 * @return Erstellte Klasse
	 */
	public OntClass createOntClass(String namespace, String name,
			String superClass) {
		String uri;
		String uriSuper;
		if (namespace != null && !namespace.equals("")) {
			uri = namespace + "#" + name;
			if (!superClass.contains("#"))
				uriSuper = namespace + "#" + superClass;
			else
				uriSuper = superClass;
		} else {
			uriSuper = superClass;
			uri = name;
		}
		OntClass oc = tbox.simpleClasses.get(uri);
		if (oc == null)
			oc = tbox.complexClasses.get(uri);
		if (oc == null) {
			oc = new OntClass(namespace, name);
			tbox.simpleClasses.put(uri, oc);
			Set<String> subclasses = tbox.subClasses.get(uriSuper);
			if (subclasses == null) {
				subclasses = new HashSet<String>();
				tbox.subClasses.put(uriSuper, subclasses);
			}
			subclasses.add(uri);
		}
		return oc;
	}

	/**
	 * Erstellt ein Individuum/eine Instanz einer gegebenen Klasse
	 * 
	 * @param oc
	 *            Klasse von der eine Instanz erstellt werden soll
	 * @return Erstelltes Individuum
	 */
	public Individual createIndividual(OntClass oc) {
		List<Individual> li = abox.individuals.get(oc.getUri());
		if (li == null) {
			li = new LinkedList<Individual>();
			abox.individuals.put(oc.getUri(), li);
		}
		Individual ind = new Individual(individualCounter);
		li.add(ind);
		individualCounter++;
		return ind;
	}

	/**
	 * Erstellt ein Object-Property
	 * 
	 * @param namespace
	 *            Namespace in dem das ObjectProperty definiert werden soll
	 * @param name
	 *            Name des Properties
	 * @return erstelltes Object-Property
	 */
	public ObjectProperty createObjectProperty(String namespace, String name) {
		String uri;
		if (namespace != null && !namespace.equals(""))
			uri = namespace + "#" + name;
		else
			uri = name;
		ObjectProperty op = tbox.objectProperties.get(uri);
		if (op == null) {
			op = new ObjectProperty();
			op.name = name;
			op.namespace = namespace;
			tbox.objectProperties.put(op.getUri(), op);
		}
		return op;
	}

	/**
	 * Erstellt ein Object-Property
	 * 
	 * @param namespace
	 *            Namespace in dem das ObjectProperty definiert werden soll
	 * @param name
	 *            Name des Properties
	 * @param superProperty
	 *            Name des Basis-Properties vom zu erstellenden Property
	 * @return erstelltes Object-Property
	 */
	public ObjectProperty createObjectProperty(String namespace, String name,
			String superProperty) {
		String uri;
		if (namespace != null && !namespace.equals(""))
			uri = namespace + "#" + name;
		else
			uri = name;
		ObjectProperty op = tbox.objectProperties.get(uri);
		if (op == null) {
			op = new ObjectProperty();
			op.name = name;
			op.namespace = namespace;
			tbox.objectProperties.put(op.getUri(), op);
			Set<String> subs = tbox.subProperties.get(namespace + "#"
					+ superProperty);
			if (subs == null) {
				subs = new HashSet<String>();
				tbox.subProperties.put(namespace + "#" + superProperty, subs);
			}
			subs.add(uri);
		}
		return op;
	}

	/**
	 * Erstellt ein Datatype-Property
	 * 
	 * @param namespace
	 *            Namespace in dem das DatatypeProperty definiert werden soll
	 * @param name
	 *            Name des Properties
	 * @return erstelltes Datatype-Property
	 */
	public DatatypeProperty createDatatypeProperty(String namespace, String name) {
		String uri = namespace + "#" + name;
		DatatypeProperty op = tbox.datatypeProperties.get(uri);
		if (op == null) {
			op = new DatatypeProperty();
			op.name = name;
			op.namespace = namespace;
			tbox.datatypeProperties.put(op.getUri(), op);
		}
		return op;
	}

	/**
	 * Erstellt ein Datatype-Property
	 * 
	 * @param namespace
	 *            Namespace in dem das DatatypeProperty definiert werden soll
	 * @param name
	 *            Name des Properties
	 * @param superProperty
	 *            Name des Basis-Properties vom zu erstellenden Property
	 * @return erstelltes Datatype-Property
	 */
	public DatatypeProperty createDatatypeProperty(String namespace,
			String name, String superProperty) {
		String uri = namespace + "#" + name;
		DatatypeProperty op = tbox.datatypeProperties.get(uri);
		if (op == null) {
			op = new DatatypeProperty();
			op.name = name;
			op.namespace = namespace;
			tbox.datatypeProperties.put(op.getUri(), op);
			Set<String> subs = tbox.subProperties.get(namespace + "#"
					+ superProperty);
			if (subs == null) {
				subs = new HashSet<String>();
				tbox.subProperties.put(namespace + "#" + superProperty, subs);
			}
			subs.add(uri);
		}
		return op;
	}

	/**
	 * Fï¿½gt einem Individuum eine DatatypeProperty hinzu
	 * 
	 * @param ind
	 *            Individuum dem das Proeprty hinzugefï¿½gt werden soll
	 * @param property
	 *            Property
	 * @param value
	 *            Wert den dem Individuum ï¿½ber das Property zugeordnet werden
	 *            soll
	 */
	public void addProperty(Individual ind, DatatypeProperty property,
			Datatype value) {
		List<DatatypePropertyStatement> ldtp = abox.dtProperties.get(property
				.getUri());
		if (ldtp == null) {
			ldtp = new LinkedList<DatatypePropertyStatement>();
			abox.dtProperties.put(property.getUri(), ldtp);
		}
		ldtp.add(new DatatypePropertyStatement(ind, value));
	}

	/**
	 * Fï¿½gt einem Individuum eine ObjectProperty hinzu
	 * 
	 * @param ind
	 *            Individuum dem das Proeprty hinzugefï¿½gt werden soll
	 * @param property
	 *            Property
	 * @param value
	 *            Individum mit dem das Individuum ï¿½ber das Property zugeordnet
	 *            werden soll
	 */
	public void addProperty(Individual ind, ObjectProperty property,
			Individual value) {
		List<ObjectPropertyStatement> ldtp = abox.oProperties.get(property
				.getUri());
		if (ldtp == null) {
			ldtp = new LinkedList<ObjectPropertyStatement>();
			abox.oProperties.put(property.getUri(), ldtp);
		}
		ldtp.add(new ObjectPropertyStatement(ind, value));
	}

	/**
	 * Exploriert Equivalentsbeziehungen zwischen Klassen einer Ontologie. Die
	 * Transitivitï¿½t der Equivalenzbeziehung zwischen zwei Klassen wird mit
	 * dieser Methode aufgelï¿½st und intern festgehalten.
	 * 
	 * @param a
	 *            Erste Klasse der Ontologie zu der die Equivalenz
	 *            geprï¿½ft/exploriert werden soll
	 * @param b
	 *            Zweite Klasse der Ontologie zu der die Equivalenz
	 *            geprï¿½ft/exploriert werden soll
	 */
	public void markEquivalentClasses(OntClass a, OntClass b) {
		Set<String> equi1 = tbox.equivalentClasses.get(a.getUri());
		Set<String> equi2 = tbox.equivalentClasses.get(b.getUri());

		if (equi1 == null && equi2 == null) {
			equi1 = new HashSet<String>();
			equi1.add(a.getUri());
			equi1.add(b.getUri());
			tbox.equivalentClasses.put(a.getUri(), equi1);
			tbox.equivalentClasses.put(b.getUri(), equi1);
		} else if (equi1 == null) {
			equi2.add(a.getUri());
			tbox.equivalentClasses.put(a.getUri(), equi2);
		} else if (equi2 == null) {
			equi1.add(b.getUri());
			tbox.equivalentClasses.put(b.getUri(), equi1);
		} else {
			int sizeA = equi1.size();
			int sizeB = equi2.size();
			if (sizeA > sizeB) {
				tbox.equivalentClasses.put(b.getUri(), equi1);
				equi1.addAll(equi2);
			} else {
				tbox.equivalentClasses.put(a.getUri(), equi2);
				equi2.addAll(equi1);
			}
		}
	}

	/**
	 * Exploriert Equivalentsbeziehungen zwischen ObjectProperties einer
	 * Ontologie. Die Transitivitï¿½t der Equivalenzbeziehung zwischen zwei
	 * ObjectProperties wird mit dieser Methode aufgelï¿½st und intern
	 * festgehalten.
	 * 
	 * @param a
	 *            Erstes ObjectProperty der Ontologie zu dem die Equivalenz
	 *            geprï¿½ft/exploriert werden soll
	 * @param b
	 *            Zweites ObjectProperty der Ontologie zu dem die Equivalenz
	 *            geprï¿½ft/exploriert werden soll
	 */
	public void markEquivalentObjectProperties(ObjectProperty a,
			ObjectProperty b) {
		Set<String> equi1 = tbox.equivalentProperties.get(a.getUri());
		Set<String> equi2 = tbox.equivalentProperties.get(b.getUri());

		if (equi1 == null && equi2 == null) {
			equi1 = new HashSet<String>();
			equi1.add(a.getUri());
			equi1.add(b.getUri());
			tbox.equivalentProperties.put(a.getUri(), equi1);
			tbox.equivalentProperties.put(b.getUri(), equi1);
		} else if (equi1 == null) {
			equi2.add(a.getUri());
			tbox.equivalentProperties.put(a.getUri(), equi2);
		} else if (equi2 == null) {
			equi1.add(b.getUri());
			tbox.equivalentProperties.put(b.getUri(), equi1);
		} else {
			int sizeA = equi1.size();
			int sizeB = equi2.size();
			if (sizeA > sizeB) {
				tbox.equivalentProperties.put(b.getUri(), equi1);
				equi1.addAll(equi2);
			} else {
				tbox.equivalentProperties.put(a.getUri(), equi2);
				equi2.addAll(equi1);
			}
		}
	}

	/**
	 * Exploriert Equivalentsbeziehungen zwischen DatatypeProperties einer
	 * Ontologie. Die Transitivitï¿½t der Equivalenzbeziehung zwischen zwei
	 * ObjectProperties wird mit dieser Methode aufgelï¿½st und intern
	 * festgehalten.
	 * 
	 * @param a
	 *            Erstes DatatypeProperty der Ontologie zu dem die Equivalenz
	 *            geprï¿½ft/exploriert werden soll
	 * @param b
	 *            Zweites DatatypeProperty der Ontologie zu dem die Equivalenz
	 *            geprï¿½ft/exploriert werden soll
	 */
	public void markEquivalentDatatypeProperties(DatatypeProperty a,
			DatatypeProperty b) {
		Set<String> equi1 = tbox.equivalentProperties.get(a.getUri());
		Set<String> equi2 = tbox.equivalentProperties.get(b.getUri());

		if (equi1 == null && equi2 == null) {
			equi1 = new HashSet<String>();
			equi1.add(a.getUri());
			equi1.add(b.getUri());
			tbox.equivalentProperties.put(a.getUri(), equi1);
			tbox.equivalentProperties.put(b.getUri(), equi1);
		} else if (equi1 == null) {
			equi2.add(a.getUri());
			tbox.equivalentProperties.put(a.getUri(), equi2);
		} else if (equi2 == null) {
			equi1.add(b.getUri());
			tbox.equivalentProperties.put(b.getUri(), equi1);
		} else {
			int sizeA = equi1.size();
			int sizeB = equi2.size();
			if (sizeA > sizeB) {
				tbox.equivalentProperties.put(b.getUri(), equi1);
				equi1.addAll(equi2);
			} else {
				tbox.equivalentProperties.put(a.getUri(), equi2);
				equi2.addAll(equi1);
			}
		}
	}

	public boolean isSubclassOf(String parent, String child) {
		Set<String> result = new HashSet<String>();
		return isSubclassOf(parent, child, result);
	}

	private boolean isSubclassOf(String parent, String child,
			Set<String> visited) {
		if (visited.contains(parent))
			return false;
		Set<String> childs = tbox.subClasses.get(parent);
		if (childs != null) {
			for (String c : childs) {
				if (child.equals(c))
					return true;
				boolean b = isSubclassOf(c, child);
				if (b)
					return true;
			}
		}
		return false;
	}

	/**
	 * Liefert die Namen aller equivalenten Klassen und aller Kindklassen einer
	 * gegebenen Klasse der Ontologie zurï¿½ck
	 * 
	 * @param pClassName
	 *            Name der Klasse zu der die equivalenten Klassen und
	 *            Kindklassen gesucht werden
	 * @return
	 */
	public Set<String> getAllSubOrEquivalentClassNames(String pClassName) {
		Set<String> result = new HashSet<String>();
		getAllSubOrEquivalentClassNames(pClassName, result);
		return result;
	}

	private void getAllSubOrEquivalentClassNames(String pName,
			Set<String> pVisited) {
		pVisited.add(pName);
		Set<String> subs = tbox.subClasses.get(pName);
		if (subs != null) {
			for (String sub : subs) {
				if (!pVisited.contains(sub)) {
					getAllSubOrEquivalentClassNames(sub, pVisited);
				}
			}
		}
		Set<String> equis = tbox.equivalentClasses.get(pName);
		if (equis != null) {
			for (String equi : equis) {
				if (!pVisited.contains(equi)) {
					getAllSubOrEquivalentClassNames(equi, pVisited);
				}
			}
		}
	}

	/**
	 * Liefert ein Set mit Equivalenten oder Subproperties zurï¿½ck. Dabei
	 * befindet sich auch das Ausganspropertie in der Menge, die Menge hat also
	 * immer mindestens 1 Element!
	 * 
	 * @param pPropName
	 * @return
	 */
	public Set<String> getAllSubOrEquivalentProperties(String pPropName) {
		Set<String> result = new HashSet<String>();
		getAllSubOrEquivalentProperties(pPropName, result);
		return result;
	}

	private void getAllSubOrEquivalentProperties(String pName,
			Set<String> pVisited) {
		pVisited.add(pName);
		Set<String> subs = tbox.subProperties.get(pName);
		if (subs != null) {
			for (String sub : subs) {
				if (!pVisited.contains(sub)) {
					getAllSubOrEquivalentProperties(sub, pVisited);
				}
			}
		}
		Set<String> equis = tbox.equivalentProperties.get(pName);
		if (equis != null) {
			for (String equi : equis) {
				if (!pVisited.contains(equi)) {
					getAllSubOrEquivalentProperties(equi, pVisited);
				}
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TBox:\n");
		sb.append("--------------------------------------------\n");
		sb.append(tbox);
		sb.append("ABox:\n");
		sb.append("--------------------------------------------\n");
		sb.append(abox);
		return sb.toString();
	}

	/**
	 * Liefert die ABox der Ontologie zurï¿½ck
	 * 
	 * @return ABox der Ontologie
	 */
	public ABox getAbox() {
		return abox;
	}

	/**
	 * Setzt die ABox der Ontologie
	 * 
	 * @param pAbox
	 *            ABox die gesetzt werden soll
	 */
	public void setAbox(ABox pAbox) {
		abox = pAbox;
	}

	/**
	 * Liefert die TBox einer Ontologie zurï¿½ck
	 * 
	 * @return TBox der Ontologie
	 */
	public TBox getTbox() {
		return tbox;
	}

	/**
	 * Setzt die TBox einer Ontologie
	 * 
	 * @param pTbox
	 *            TBox die gesetzt werden soll
	 */
	public void setTbox(TBox pTbox) {
		tbox = pTbox;
	}

	/**
	 * Liefert den Individuen-Zï¿½hlstand zurï¿½ck
	 * 
	 * @return
	 */
	public long getIndividualCounter() {
		return individualCounter;
	}

	/**
	 * Setzt den Individuen-Zï¿½hlstand.
	 * 
	 * @param pIndividualCounter
	 */
	public void setIndividualCounter(long pIndividualCounter) {
		individualCounter = pIndividualCounter;
		individualCounterStart = pIndividualCounter;
	}

	/**
	 * Resettet den Individuen-Zï¿½hlstand auf seinen ursprï¿½nglichen Wert
	 */
	public void resetIndividualCounter() {
		individualCounter = individualCounterStart;
	}

	/**
	 * Erstellt eine komplexe Klasse in der Ontologie
	 * 
	 * @param namespace
	 *            Namespace in dem die komplexe Klasse erstellt werden soll
	 * @param name
	 *            Name der komplexen Klasse
	 * @param constraints
	 *            Eigenheiten der Klasse in Form von {@link Constraint}
	 * @return
	 */
	public ComplexOntClass createComplexOntClass(String namespace, String name,
			Constraint... constraints) {
		String n = namespace + "#" + name;
		if (tbox.complexClasses.containsKey(name))
			return tbox.complexClasses.get(n);
		ComplexOntClass result = new ComplexOntClass(namespace, name,
				constraints);
		tbox.complexClasses.put(n, result);
		return result;
	}

	/**
	 * Ordnet ein Individuum einer weiteren Klasse hinzu.
	 * 
	 * @param pInd
	 * @param pFrau
	 */
	public void addAdditionalType(Individual ind, OntClass oc) {
		List<Individual> li = abox.individuals.get(oc.getUri());
		if (li == null) {
			li = new LinkedList<Individual>();
			abox.individuals.put(oc.getUri(), li);
		}
		li.add(ind);
	}

	/**
	 * Markiert zwei ObjectProperties als invers
	 * 
	 * @param p1
	 *            Name des ersten Properties
	 * @param p2
	 *            Name des zweiten Properties
	 */
	public void markInverseProperties(String p1, String p2) {
		Set<String> s1 = tbox.getInverseProperties(p1);
		if (s1 == null) {
			s1 = new HashSet<String>();
			tbox.inverseProperties.put(p1, s1);
		}
		Set<String> s2 = tbox.getInverseProperties(p2);
		if (s2 == null) {
			s2 = new HashSet<String>();
			tbox.inverseProperties.put(p2, s2);
		}
		s1.add(p2);
		s2.add(p1);

	}

	/**
	 * Liefert die Anzahl an Tripeln in der ABox der Ontologie
	 * 
	 * @return
	 */
	public long getTripleCount() {
		return abox.getTripleCount();
	}
}
