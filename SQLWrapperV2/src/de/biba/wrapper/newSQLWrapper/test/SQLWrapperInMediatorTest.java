package de.biba.wrapper.newSQLWrapper.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.biba.mediator.IMediator;
import de.biba.mediator.IQuerySolution;
import de.biba.mediator.ISolutionIterator;
import de.biba.mediator.ReasoningMediator;
import de.biba.wrapper.newSQLWrapper.SQLWrapper;

public class SQLWrapperInMediatorTest {
	private SQLWrapper wrapper;
	private IMediator mediator;
//	private static String NS = "http://www.biba.uni-bremen.de/Ontology.owl#";
	
	@Before
	public void setUp() throws Exception {
		wrapper = new SQLWrapper();
		wrapper.init("Resources/properties.properties");
		mediator = new ReasoningMediator();
		mediator.addDataSource(wrapper, 0);
	}

	@Test
	public void testArtikel() {
		IQuerySolution solution = mediator.query("BASE <http://www.biba.uni-bremen.de/Ontology.owl> SELECT ?ArtikelNr ?Name ?Beschreibung ?Preis WHERE {?a a <Artikel>. ?a <artikelnr> ?ArtikelNr. ?a <preis> ?Preis. ?a <beschreibung> ?Beschreibung. ?a <name> ?Name.} ", true, false);
		ISolutionIterator iter = solution.getIterator();
		assertEquals(4, iter.getSize());
	}
	
	@Test
	public void testArtikelMitENummer() {
		IQuerySolution solution = mediator.query("BASE <http://www.biba.uni-bremen.de/Ontology.owl> SELECT ?ArtikelNr ?Name ?Beschreibung ?Preis ?ENummer WHERE {?a a <Artikel>. ?a <artikelnr> ?ArtikelNr. ?a <preis> ?Preis. ?a <beschreibung> ?Beschreibung. ?a <name> ?Name. ?a <e-nummer> ?ENummer.} ", true, false);
		ISolutionIterator iter = solution.getIterator();
		assertEquals(3, iter.getSize());
	}
	
	@Test
	public void testKunde() {
		IQuerySolution solution = mediator.query("BASE <http://www.biba.uni-bremen.de/Ontology.owl> SELECT ?Name ?Vorname ?Strasse ?Plz ?Ort WHERE {?a a <Kunde>. ?a <name> ?Name. ?a <vorname> ?Vorname. ?a <strasse> ?Strasse. ?a <postleitzahl> ?Plz. ?a <ort> ?Ort.} ", true, false);
		ISolutionIterator iter = solution.getIterator();
		assertEquals(3, iter.getSize());
	}
	
	@Test
	public void testKundenMitArtikel() {
		IQuerySolution solution = mediator.query("BASE <http://www.biba.uni-bremen.de/Ontology.owl> SELECT ?Name ?Vorname ?Artikelname WHERE {?a a <Kunde>. ?a <name> ?Name. ?a <vorname> ?Vorname. ?a <HatBestellt> ?b. ?b <BestellteArtikel> ?art. ?art <name> ?Artikelname.} ", true, false);
		ISolutionIterator iter = solution.getIterator();
		assertEquals(4, iter.getSize());
	}

	@Test
	public void testKundenMitArtikelEingeschraenkt() {
		IQuerySolution solution = mediator.query("BASE <http://www.biba.uni-bremen.de/Ontology.owl> SELECT ?Name ?Vorname ?Artikelname WHERE {?a a <Kunde>. ?a <name> ?Name. ?a <vorname> ?Vorname. ?a <HatBestellt> ?b. ?b <BestellteArtikel> ?art. ?art <name> ?Artikelname. ?a <name> == \"Name1\".} ", true, false);
		ISolutionIterator iter = solution.getIterator();
		assertEquals(2, iter.getSize());
	}
	
	@Test
	public void testArtikelMitKategorien() {
		IQuerySolution solution = mediator.query("BASE <http://www.biba.uni-bremen.de/Ontology.owl> SELECT ?Artikelname ?Kategorie WHERE {?art a <Artikel>. ?art <name> ?Artikelname. ?art <GehoertZurKategorie> ?kat. ?kat <bezeichnung> ?Kategorie. } ", true, false);
		ISolutionIterator iter = solution.getIterator();

		System.out.println(solution);
		assertEquals(6, iter.getSize());
	}

}
