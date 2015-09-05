package de.biba.mediator.converter;

import de.biba.exceptions.InvalidDataException;
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
 * Definiert Methoden zum Konvertieren von Daten
 * @author KRA
 *
 */
public interface Converter {
	/**
	 * Konvertiert ein Datum
	 * @param pValue Datum das konvertiert werden soll
	 * @return Konvertiertes Datum
	 * @throws InvalidDataException Wird geworfen, wenn das Datum nicht vom richtigen Typ ist.
	 */
	public Datatype convert(Datatype pValue) throws InvalidDataException;
	
	/**
	 * Liefert den Inversen Converter zu diesem
	 * @return
	 */
	public Converter getInverseConverter();
	
	/**
	 * Verschmilzt den aktuellen Converter mit einem gegebenen.
	 * @param pConverter
	 * @return
	 */
//	public Converter melt(Converter pConverter);
}
