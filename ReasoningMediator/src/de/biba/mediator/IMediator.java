package de.biba.mediator;

import java.util.List;

import de.biba.mediator.ReasoningMediator.OntModelAndSolution;
import de.biba.ontology.OntModel;
import de.biba.wrapper.DataSource;
import de.biba.wrapper.IDataSourceDescription;

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
 * Inferface welches die grundlegenden Funktionen eines Meditator definiert.
 * @author KRA
 *
 */
public interface IMediator {
	/**
	 * Stellt eine Query an den Mediator. Dabei kann es sich sowohl um eine Select- als auch um eine Inser-Query handeln.
	 * @param pQuery Die Anfrage in einer SPARQL-ähnlichen Syntax
	 * @param pAllowQuerying Gibt an ob Selects erlaubt sind
	 * @param pAllowWriting Gibt an ob Inserts erlaubt sind
	 * @return Das Ergebnis der Query
	 */
	IQuerySolution query(String pQuery, boolean pAllowQuerying, boolean pAllowWriting);

	/**
	 * Stellt eine Query an den Mediator. Dabei kann es sich sowohl um eine Select- als auch um eine Inser-Query handeln.
	 * @param pQuery Die Anfrage in einer SPARQL-ähnlichen Syntax
	 * @param pAllowQuerying Gibt an ob Selects erlaubt sind
	 * @param pAllowWriting Gibt an ob Inserts erlaubt sind
	 * @return Das Ergebnis der Query und das Ontologiemodell
	 */
	OntModelAndSolution queryOntModel(String pQuery, boolean pAllowQuerying, boolean pAllowWriting);

	/**
	 * Fügt eine weitere Datenquellen dem Mediator hinzu
	 * @param pDataSource Datenquelle die hinzugefügt werden soll
	 * @param lvl Level auf dem die Datenquelle hinzugefügt werden. Datenquellen mit niedrigem Level werden zuerst angefragt. 
	 * Datenquellen mit höherem Level werden später angefragt, erhalten dabei jedoch die Ergebnisse der Datenquellen niedrigeren Levels
	 */
	void addDataSource(DataSource pDataSource, int lvl);

	/**
	 * Entfernt eine Datenquelle vom Mediator
	 * @param pDataSource Datenquelle die entfernt werden soll.
	 */
	void removeDataSource(DataSource pDataSource);

	/**
	 * Liefert die Ontologie des Meditators zurück. Dies ist die Gesamtontologie, welche sich aus den Ontologien aller Wrapper zusammensetzt.
	 * @return
	 */
	OntModel getModel();

	/**
	 * Entfernt alle Datenquellen vom Mediator
	 */
	void removeAllDataSources();
	
	/**
	 * This functions returns a list of data source descriptions which are current connetced to the mediator
	 */
	 List<IDataSourceDescription> getLinkedDataSources();
}
