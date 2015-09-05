package de.biba.wrapper.newSQLWrapper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.biba.mediator.DataSourceQuery;
import de.biba.ontology.ABox;
import de.biba.ontology.OntModel;
import de.biba.wrapper.newSQLWrapper.SQLWrapper;

public class SQLWrapperTest {

	private SQLWrapper wrapper;
	private static String NS = "http://www.biba.uni-bremen.de/Ontology.owl#";
	
	
	@Before
	public void setUp() throws Exception {
		wrapper = new SQLWrapper();
		wrapper.init("Resources/properties.properties");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelectKunden() {
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getClassNames().add(NS+"Kunde");
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(NS+"Kunde"));
		assertEquals(3, abox.individuals.get(NS+"Kunde").size());
	}
	
	@Test
	public void testSelectArtikel() {
		String className = NS+"Artikel";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getClassNames().add(className);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(className));
		assertEquals(4, abox.individuals.get(className).size());
	}
	
	@Test
	public void testSelectArtikelUndBestellungenZusammenhangslos() {
		String artikel = NS+"Artikel";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getClassNames().add(artikel);
		String bestellung = NS+"Bestellung";
		dsg.getClassNames().add(bestellung);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(artikel));
		assertEquals(4, abox.individuals.get(artikel).size());
		assertNotNull(abox.individuals.get(bestellung));
		assertEquals(4, abox.individuals.get(bestellung).size());
	}
	
	@Test
	public void testSelectArtikelMitPreis() {
		String artikel = NS+"Artikel";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getClassNames().add(artikel);
		String preis = NS+"preis";
		dsg.getPropertyNames().add(preis);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(artikel));
		assertEquals(4, abox.individuals.get(artikel).size());
		assertNotNull(abox.dtProperties.get(preis));
		assertEquals(4, abox.dtProperties.get(preis).size());
	}
	
	@Test
	public void testSelectPreise() {
		DataSourceQuery dsg = new DataSourceQuery();
		String preis = NS+"preis";
		dsg.getPropertyNames().add(preis);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(OntModel.THING.getUri()));
		assertEquals(4, abox.individuals.get(OntModel.THING.getUri()).size());
		assertNotNull(abox.dtProperties.get(preis));
		assertEquals(4, abox.dtProperties.get(preis).size());
	}

	@Test
	public void testSelectKundenUndAnschriftZusammenhangslos() {
		String kunde = NS+"Kunde";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getClassNames().add(kunde);
		String anschrift = NS+"Anschrift";
		dsg.getClassNames().add(anschrift);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(kunde));
		assertEquals(3, abox.individuals.get(kunde).size());
		assertNotNull(abox.individuals.get(anschrift));
		assertEquals(3, abox.individuals.get(anschrift).size());
	}
	
	
	@Test
	public void testSelectENummern() {
		DataSourceQuery dsg = new DataSourceQuery();
		String enummer = NS+"e-nummer";
		dsg.getPropertyNames().add(enummer);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(OntModel.THING.getUri()));
		assertEquals(3, abox.individuals.get(OntModel.THING.getUri()).size());
		assertNotNull(abox.dtProperties.get(enummer));
		assertEquals(3, abox.dtProperties.get(enummer).size());
	}
	
	
	@Test
	public void testSelectBestellungen() {
		String className = NS+"Bestellung";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getClassNames().add(className);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(className));
		assertEquals(4, abox.individuals.get(className).size());
	}
	
	@Test
	public void testSelectHatBestellt() {
		String className = NS+"HatBestellt";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getPropertyNames().add(className);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(OntModel.THING.getUri()));
		assertEquals(7, abox.individuals.get(OntModel.THING.getUri()).size());
		assertNotNull(abox.oProperties.get(className));
		assertEquals(4, abox.oProperties.get(className).size());
	}
	@Test
	public void testSelectHatBestelltMitKunde() {
		String kunde = NS+"Kunde";
		String className = NS+"HatBestellt";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getPropertyNames().add(className);
		dsg.getClassNames().add(kunde);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.individuals.get(OntModel.THING.getUri()));
		assertEquals(4, abox.individuals.get(OntModel.THING.getUri()).size());
		assertNotNull(abox.individuals.get(kunde));
		assertEquals(3, abox.individuals.get(kunde).size());
		assertNotNull(abox.oProperties.get(className));
		assertEquals(4, abox.oProperties.get(className).size());
	}
	
	@Test
	public void testKombination() {
		String kunde = NS+"Kunde";
		String hatBestellt = NS+"HatBestellt";
		String bestellung = NS+"Bestellung";
		String preis = NS+"preis";
		String artikel = NS +"Artikel";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getPropertyNames().add(hatBestellt);
		dsg.getPropertyNames().add(preis);
		dsg.getClassNames().add(kunde);
		dsg.getClassNames().add(bestellung);
		dsg.getClassNames().add(artikel);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
//		assertNotNull(abox.individuals.get(OntModel.THING.getUri()));
//		assertEquals(4, abox.individuals.get(OntModel.THING.getUri()).size());
		assertNotNull(abox.individuals.get(kunde));
		assertEquals(3, abox.individuals.get(kunde).size());
		assertNotNull(abox.individuals.get(artikel));
		assertEquals(4, abox.individuals.get(artikel).size());
		assertNotNull(abox.individuals.get(bestellung));
		assertEquals(4, abox.individuals.get(bestellung).size());
		assertNotNull(abox.oProperties.get(hatBestellt));
		assertEquals(4, abox.oProperties.get(hatBestellt).size());
		assertNotNull(abox.dtProperties.get(preis));
		assertEquals(4, abox.dtProperties.get(preis).size());
		
	}

	@Test
	public void testSelectGehoertZurKategorie() {
		String property = NS+"GehoertZurKategorie";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getPropertyNames().add(property);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.oProperties.get(property));
		assertEquals(6, abox.oProperties.get(property).size());
	}
	
	@Test
	public void testSelectUmfasstArtikel() {
		String property = NS+"UmfasstArtikel";
		DataSourceQuery dsg = new DataSourceQuery();
		dsg.getPropertyNames().add(property);
		OntModel om = wrapper.queryData(dsg);
		assertNotNull(om);
		ABox abox = om.getAbox();
		assertNotNull(abox.oProperties.get(property));
		assertEquals(6, abox.oProperties.get(property).size());
	}
}
