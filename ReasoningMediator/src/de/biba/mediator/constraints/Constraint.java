package de.biba.mediator.constraints;

import java.util.List;
import java.util.Set;

import de.biba.mediator.QuerySolution;
import de.biba.ontology.OntModel;

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
 * Dieses Interface definiert Methoden eines Constraints, welche zur Auswertung einer Query herangezogen werden.
 * @author KRA
 *
 */
public interface Constraint {
	/**
	 * Liefert das Gewicht eines Constraints zurück. 
	 * @return Gewicht des Constraints, welches angibt wie rechenintensiv die Auswertung des Constraints ist.
	 */
	public abstract int getWeight();
	
	/**
	 * Setzt das Subject des Constraints
	 * @param pSubject Name der Spalte die Subjekt des Constraints sein soll
	 */
	public void setSubject(String pSubject);
	
	/**
	 * Wendet dieses Constraint auf eine Ontologie an
	 * @param pModel Ontologie auf der das Constraint angewendet werden soll
	 * @return Ergebsis der Anwendung des Constraints
	 * @see QuerySolution
	 */
	public abstract QuerySolution solve(OntModel pModel);

	/**
	 * Gibt die Spaltennamen zurück, die durch dieses Constraint betroffen sind
	 * @return Spaltennamen die durch dieses Constraint betroffen sind
	 */
	public abstract Set<String> getResultVars();

	/**
	 * Wendet dieses Constraint unter Berücksichtigung bestehender Lösungen/Ergebnisse auf eine Ontologie an
	 * @param pModel Ontologie auf der das Constraint angewendet werden soll
	 * @param pAffectedSolutions List mit bestehenden Lösungen
	 * @return Ergebsis der Anwendung des Constraints
	 * @see QuerySolution
	 */
	public abstract QuerySolution solve(OntModel pModel,
			List<QuerySolution> pAffectedSolutions);

	/**
	 * Wandelt dieses Constraint in eine Liste von einfachen Constraints um
	 * @return Liste mit einfachen Constraints
	 * @see SimpleConstraint
	 */
	public abstract List<SimpleConstraint> flatToSimpleConstraints();
	
	/**
	 * Fügt Constraints, die sich aus Complexen Klassen ergeben in dieses Constraint ein.
	 * @param pModel
	 */
	public abstract void insertComplexClassConstraints(OntModel pModel);
}
