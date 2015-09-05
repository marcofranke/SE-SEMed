package de.biba.ontology.datatypes;

import java.text.ParseException;
import java.util.Date;

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
 * Definiert Methoden für Klassen die Daten kapseln. Implementierungen dieser Schnittstelle können Instanzen der Ontologie als DatatypeProperty zugeordnet werden.
 * @author KRA
 *
 */
public interface Datatype extends Comparable<Datatype>{
	
	/**
	 * Liefert eine String-Repräsentation des Wertes zurück.
	 * @return String-Repräsentation des Wertes
	 */
	String getString();
	
	/**
	 * Liefert eine int-Repräsentation des Wertes zurück.
	 * @return int-Repräsentation des Wertes
	 * @throws ParseException Tritt auf, wenn das gekapselte Element kein int ist
	 */
	int getInt() throws ParseException;
	
	/**
	 * Liefert eine double-Repräsentation des Wertes zurück.
	 * @return double-Repräsentation des Wertes
	 * @throws ParseException Tritt auf, wenn das gekapselte Element kein double ist
	 */
	double getDouble() throws ParseException;
	
	/**
	 * Liefert eine Date-Repräsentation des Wertes zurück.
	 * @return Date-Repräsentation des Wertes
	 * @throws ParseException Tritt auf, wenn das gekapselte Element kein Date ist
	 */
	Date getDate() throws ParseException;
	
	/**
	 * Liefert eine boolean-Repräsentation des Wertes zurück.
	 * @return boolean-Repräsentation des Wertes
	 * @throws ParseException Tritt auf, wenn das gekapselte Element kein boolean ist
	 */
	boolean getBoolean() throws ParseException;
	
	/**
	 * Kopiert die Instanz
	 * @return Kopie der Instanz
	 */
	Datatype clone();
	
}
