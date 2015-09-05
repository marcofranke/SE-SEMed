package de.biba.mediator;

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
 * Iterator mit dem durch die Ergebnisse einer Mediator-Anfrage iteriert werden kann.
 * @author KRA
 *
 */
public interface ISolutionIterator {
	/**
	 * Springt zum nächsten Ergebnis
	 * @return true wenn erfolgreich, sonst false (kein weiteres Ergebnis vorhanden)
	 */
	public boolean next();
	
	/**
	 * Springt zum vorherigen Ergebnis
	 * @return true wenn erfolgreich, sonst false (kein vorheriges Ergebnis vorhanden)
	 */
	public boolean prev();
	
	/**
	 * Liefert den Wert an der Stelle index zurück
	 * @param index Spaltenindex des zurückzuliefernden Wertes
	 * @return Wert der im aktuellen Ergebnis an der Stelle index steht
	 */
	public Datatype get(int index);
	
	/**
	 * Liefert die Anzahl der Ergebnisse zurück
	 * @return
	 */
	public int getSize();
	
	/**
	 * Liefert den Wert der im Ergebnis in der Zeile pRow und der Spalte pColumn steht
	 * @param pColumn Spalte aus der das Ergebnis zurückgeliefert werden soll
	 * @param pRow Reihe aus der das Ergebnis zurückgeliefert werden soll
	 * @return Wert im Ergebnis in der Reihe pRow und der Spalte pColumn
	 */
	public Datatype get(int pColumn, int pRow);
	
	/**
	 * Liefert den Wert im aktuellen Ergebnis in der Spalte pColumnName zurück
	 * @param pColumnName Spaltenname aus dem der Wert zurückgegeben werden soll
	 * @return Wert aus dem aktuellen Ergebnis in der Spalte pColumnName
	 */
	public Datatype get(String pColumnName);
}
