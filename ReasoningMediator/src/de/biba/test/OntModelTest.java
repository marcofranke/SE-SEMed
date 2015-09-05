/**
 * 
 */
package de.biba.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.biba.mediator.DataSourceQuery;
import de.biba.ontology.ABox;
import de.biba.ontology.ComplexOntClass;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.ObjectPropertyStatement;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.DataSource;

/**
 * @author NoNaMe
 * 
 */
public class OntModelTest {

	OntModel ontModel;
	OntModel cloneOntModel1;
	OntModel cloneOntModel2;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ABox aBoxTest = new ABox();
		// Hier werden alle DatatypeProperties gehalten. Als Key ist dabei der
		// volle Name des Properties zu verwenden.
		aBoxTest.dtProperties = new HashMap<String, List<DatatypePropertyStatement>>();
		// Hier werden alle ObjectProperties gehalten. Als Key ist dabei der
		// volle Name des Properties zu verwenden.
		aBoxTest.oProperties = new HashMap<String, List<ObjectPropertyStatement>>();
		// Hier werden alle Individuen gehalten. Als Key ist dabei der volle
		// Name der Klasse zu verwenden.
		aBoxTest.individuals = new HashMap<String, List<Individual>>();
		// Hier werden die Einheiten der einzelnen Properties gehalten. Als Key
		// ist dabei der volle Name des Properties zu verwenden.
		aBoxTest.units = new HashMap<String, String>();

		List<Individual> il = new ArrayList<Individual>();
		Individual indi1 = new Individual((long) 1111);
		Individual indi2 = new Individual((long) 2222);
		il.add(indi1);
		il.add(indi2);
		aBoxTest.individuals.put("NamedIndividual", il);

		List<DatatypePropertyStatement> dpsl = new ArrayList<DatatypePropertyStatement>();
		DatatypePropertyStatement dps1 = new DatatypePropertyStatement(indi1,
				new StringDatatype("15.07.2013"));
		DatatypePropertyStatement dps2 = new DatatypePropertyStatement(indi2,
				new StringDatatype("16.07.2013"));
		dpsl.add(dps1);
		dpsl.add(dps2);
		aBoxTest.dtProperties.put("service_number", dpsl);

		List<ObjectPropertyStatement> opsl = new ArrayList<ObjectPropertyStatement>();
		ObjectPropertyStatement ops1 = new ObjectPropertyStatement(indi1, indi2);
		ObjectPropertyStatement ops2 = new ObjectPropertyStatement(indi2, indi1);
		opsl.add(ops1);
		opsl.add(ops2);
		aBoxTest.oProperties.put("eaten_by", opsl);

		aBoxTest.units.put("eaten_by", "TEST");

		//String pFileName = "Resources/Ontology1307526042333/Ontology1307526042333.owl";
		//String pFileName = "Resources/Semantic_Descriptions/Ontology_EANCOM.owl";
		String pFileName = "Resources/TestOntology/example.owl";
		DataSource dataSource = new DataSource() {

			@Override
			public OntModel queryData(DataSourceQuery pDsc) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void initialize(String pPropertyFile) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getPathOfConfigurationFile() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		dataSource.loadModel(pFileName);

		ontModel = dataSource.getOntModel();
		ontModel.abox = aBoxTest;

		// Erstelle den ersten Clone des OntologyModels
		cloneOntModel1 = new OntModel(ontModel);
		// Erstelle den zweiten Clone
		cloneOntModel2 = new OntModel(ontModel);

	}

	/**
	 * Test method for {@link de.biba.ontology.OntModel#OntModel()}.
	 */
	@Test
	public void testOntModel() {
		assertNotNull(ontModel);
		//System.out.println("\n" + ontModel.tbox + "\n");
		ontModel.abox = null;

		//System.out.println("\n" + ontModel.tbox + "\n");
		//System.out.println("\n" + cloneOntModel2.tbox + "\n");
	}

	/**
	 * Test method for
	 * {@link de.biba.ontology.OntModel#OntModel(OntModel ontModel)}.
	 */
	@Test
	public void testOntModelClone() {

		// Must be true and objects must have different memory addresses
		assertTrue(ontModel != cloneOntModel1);
		assertTrue(ontModel != cloneOntModel2);

		// As we are returning same class; so it should be true
		assertTrue(ontModel.getClass() == cloneOntModel1.getClass());
		assertTrue(ontModel.getClass() == cloneOntModel2.getClass());

		// Default equals method checks for references so it should be false.
		assertFalse(ontModel.equals(cloneOntModel1));
		assertFalse(ontModel.equals(cloneOntModel2));

		// Teste individualCounter
		ontModel.setIndividualCounter(2705);
		assertTrue(ontModel.getIndividualCounter() == 2705);
		assertFalse(cloneOntModel1.getIndividualCounter() == 2705);
		assertTrue(cloneOntModel1.getIndividualCounter() == cloneOntModel2.getIndividualCounter());
	}

	/**
	 * Test method for
	 * {@link de.biba.ontology.OntModel#OntModel(OntModel ontModel)}.
	 */
	@Test
	public void testOntModelCloneABox() {
		// ABox testen

		// 1) dtProperties
		ontModel.getAbox().dtProperties = null;
		assertNull(ontModel.getAbox().dtProperties);
		assertNotNull(cloneOntModel1.getAbox().dtProperties);
		assertSame(cloneOntModel1.getAbox().dtProperties.size(),
				cloneOntModel2.getAbox().dtProperties.size());
		assertSame(cloneOntModel1.getAbox().dtProperties.containsKey("service_number"),
				cloneOntModel2.getAbox().dtProperties.containsKey("service_number"));
		assertTrue(cloneOntModel1.getAbox().dtProperties.containsKey("service_number"));
		assertTrue(cloneOntModel1.getAbox().dtProperties.get("service_number").get(0).subject.toString().equals("1111"));
		assertTrue(cloneOntModel1.getAbox().dtProperties.get("service_number").get(1).subject.toString().equals("2222"));
		assertTrue(cloneOntModel1.getAbox().dtProperties.get("service_number").get(0).object.toString().equals("15.07.2013"));
		assertTrue(cloneOntModel1.getAbox().dtProperties.get("service_number").get(1).object.toString().equals("16.07.2013"));

		
		// 2) oProperties
		ontModel.getAbox().oProperties = null;
		assertNull(ontModel.getAbox().oProperties);
		assertNotNull(cloneOntModel1.getAbox().oProperties);
		assertSame(cloneOntModel1.getAbox().oProperties.size(),
				cloneOntModel2.getAbox().oProperties.size());
		assertSame(
				cloneOntModel1.getAbox().oProperties.containsKey("eaten_by"),
				cloneOntModel2.getAbox().oProperties.containsKey("eaten_by"));
		assertTrue(cloneOntModel1.getAbox().oProperties.containsKey("eaten_by"));
		assertTrue(cloneOntModel1.getAbox().oProperties.get("eaten_by").get(0).subject.toString().equals("1111"));
		assertTrue(cloneOntModel1.getAbox().oProperties.get("eaten_by").get(1).subject.toString().equals("2222"));
		assertTrue(cloneOntModel1.getAbox().oProperties.get("eaten_by").get(0).object.toString().equals("2222"));
		assertTrue(cloneOntModel1.getAbox().oProperties.get("eaten_by").get(1).object.toString().equals("1111"));

		

		// 3) individuals
		ontModel.getAbox().individuals = null;
		assertNull(ontModel.getAbox().individuals);
		assertNotNull(cloneOntModel1.getAbox().individuals);
		assertSame(cloneOntModel1.getAbox().individuals.size(),
				cloneOntModel2.getAbox().individuals.size());
		assertSame(
				cloneOntModel1.getAbox().individuals.containsKey("NamedIndividual"),
				cloneOntModel2.getAbox().individuals.containsKey("NamedIndividual"));
		assertTrue(cloneOntModel1.getAbox().individuals.containsKey("NamedIndividual"));
		assertTrue(cloneOntModel1.getAbox().individuals.get("NamedIndividual").get(0).toString().equals("1111"));
		assertTrue(cloneOntModel1.getAbox().individuals.get("NamedIndividual").get(1).toString().equals("2222"));

		// 4) units
		ontModel.getAbox().units = null;
		assertNull(ontModel.getAbox().units);
		assertNotNull(cloneOntModel1.getAbox().units);
		assertSame(cloneOntModel1.getAbox().units.size(),
				cloneOntModel2.getAbox().units.size());
		assertSame(cloneOntModel1.getAbox().units.containsKey("eaten_by"),
				cloneOntModel2.getAbox().units.containsKey("eaten_by"));
		assertTrue(cloneOntModel1.getAbox().units.containsKey("eaten_by"));
		assertTrue(cloneOntModel1.getAbox().units.get("eaten_by").equals("TEST"));
	}

	/**
	 * Test method for
	 * {@link de.biba.ontology.OntModel#OntModel(OntModel ontModel)}.
	 */
	@Test
	public void testOntModelCloneTBox() {
		// TBox testen

		// 1) datatypeProperties
		Map<String, DatatypeProperty> tempDatatypeProperties = ontModel.getTbox().getDatatypeProperties();
		ontModel.getTbox().setDatatypeProperties(null);
		assertNull(ontModel.getTbox().getDatatypeProperties());
		assertNotNull(cloneOntModel1.getTbox().getDatatypeProperties());
		assertSame(cloneOntModel1.getTbox().getDatatypeProperties().size(),
				cloneOntModel2.getTbox().getDatatypeProperties().size());
		//assertTrue(cloneOntModel1.getTbox().getDatatypeProperties().containsKey("http://cohse.semanticweb.org/ontologies/people#service_number"));
		assertTrue(cloneOntModel1.getTbox().getDatatypeProperties().equals(tempDatatypeProperties));
		
		// 2) objectProperties
/*		ObjectProperty tempObjectProperty = new ObjectProperty("testNAMESPACE", "testNAME");
		Set<String> tempDomains = new HashSet<String>();
		tempDomains.add("TestDomain1");
		tempDomains.add("TestDomain2");
		tempDomains.add("TestDomain3");
		Set<String> tempRange = new HashSet<String>();
		tempRange.add("TestRange1");
		tempRange.add("TestRange2");
		tempRange.add("TestRange3");
		tempObjectProperty.setDomain(tempDomains);
		tempObjectProperty.setRange(tempRange);*/
		
		Map<String, ObjectProperty> tempObjectProperties = ontModel.getTbox().getObjectProperties();
		
		//System.out.println(tempObjectProperties.get("http://cohse.semanticweb.org/ontologies/people#has_pet"));
		//tempObjectProperties.put("http://cohse.semanticweb.org/ontologies/people#has_pet", tempObjectProperty);
		//System.out.println(tempObjectProperties.get("http://cohse.semanticweb.org/ontologies/people#has_pet"));
		
		ontModel.getTbox().setObjectProperties(null);
		assertNull(ontModel.getTbox().getObjectProperties());
		assertNotNull(cloneOntModel1.getTbox().getObjectProperties());
		assertSame(cloneOntModel1.getTbox().getObjectProperties().size(),
				cloneOntModel2.getTbox().getObjectProperties().size());
		//System.out.println(cloneOntModel1.getTbox().getObjectProperties().get("http://cohse.semanticweb.org/ontologies/people#has_pet"));
		assertTrue(cloneOntModel1.getTbox().getObjectProperties().equals(tempObjectProperties));
		

		// 3) simpleClasses
		Map<String, OntClass> tempSimpleClasses = ontModel.getTbox().getSimpleClasses();
		ontModel.getTbox().setSimpleClasses(null);
		assertNull(ontModel.getTbox().getSimpleClasses());
		assertNotNull(cloneOntModel1.getTbox().getSimpleClasses());
		assertSame(cloneOntModel1.getTbox().getSimpleClasses().size(),
				cloneOntModel2.getTbox().getSimpleClasses().size());
		assertTrue(cloneOntModel1.getTbox().getSimpleClasses().equals(tempSimpleClasses));

		// 4) complexClasses
		Map<String, ComplexOntClass> tempComplexClasses = ontModel.getTbox().getComplexClasses();
		ontModel.getTbox().setComplexClasses(null);
		assertNull(ontModel.getTbox().getComplexClasses());
		assertNotNull(cloneOntModel1.getTbox().getComplexClasses());
		assertSame(cloneOntModel1.getTbox().getComplexClasses().size(),
				cloneOntModel2.getTbox().getComplexClasses().size());
		assertTrue(cloneOntModel1.getTbox().getComplexClasses().equals(tempComplexClasses));

		// 5) subClasses
		Map<String, Set<String>> tempSubClasses = ontModel.getTbox().getSubClasses();
		ontModel.getTbox().setSubClasses(null);
		assertNull(ontModel.getTbox().getSubClasses());
		assertNotNull(cloneOntModel1.getTbox().getSubClasses());
		assertSame(cloneOntModel1.getTbox().getSubClasses().size(),
				cloneOntModel2.getTbox().getSubClasses().size());
		assertTrue(cloneOntModel1.getTbox().getSubClasses().equals(tempSubClasses));
		
		// 6) equivalentClasses
		Map<String, Set<String>> tempEquivalentClasses = ontModel.getTbox().getEquivalentClasses();
		ontModel.getTbox().setEquivalentClasses(null);
		assertNull(ontModel.getTbox().getEquivalentClasses());
		assertNotNull(cloneOntModel1.getTbox().getEquivalentClasses());
		assertSame(cloneOntModel1.getTbox().getEquivalentClasses().size(),
				cloneOntModel2.getTbox().getEquivalentClasses().size());
		assertTrue(cloneOntModel1.getTbox().getEquivalentClasses().equals(tempEquivalentClasses));

		// 7) subProperties
		Map<String, Set<String>> tempSubProperties = ontModel.getTbox().getSubProperties();
		ontModel.getTbox().setSubProperties(null);
		assertNull(ontModel.getTbox().getSubProperties());
		assertNotNull(cloneOntModel1.getTbox().getSubProperties());
		assertSame(cloneOntModel1.getTbox().getSubProperties().size(),
				cloneOntModel2.getTbox().getSubProperties().size());
		assertTrue(cloneOntModel1.getTbox().getSubProperties().equals(tempSubProperties));

		// 8) equivalentProperties
		Map<String, Set<String>> tempEquivalentProperties = ontModel.getTbox().getEquivalentProperties();
		ontModel.getTbox().setEquivalentProperties(null);
		assertNull(ontModel.getTbox().getEquivalentProperties());
		assertNotNull(cloneOntModel1.getTbox().getEquivalentProperties());
		assertSame(cloneOntModel1.getTbox().getEquivalentProperties().size(),
				cloneOntModel2.getTbox().getEquivalentProperties().size());
		assertTrue(cloneOntModel1.getTbox().getEquivalentProperties().equals(tempEquivalentProperties));

		// 9) inverseProperties
		Map<String, Set<String>> testInverseProperties = ontModel.getTbox().inverseProperties;
		ontModel.getTbox().inverseProperties = null;
		assertNull(ontModel.getTbox().inverseProperties);
		assertNotNull(cloneOntModel1.getTbox().inverseProperties);
		assertSame(cloneOntModel1.getTbox().inverseProperties.size(),
				cloneOntModel2.getTbox().inverseProperties.size());
		assertTrue(cloneOntModel1.getTbox().inverseProperties.equals(testInverseProperties));
	}

}
