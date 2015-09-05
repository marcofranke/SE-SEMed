package de.biba.mediator.converter;

import java.text.ParseException;

import de.biba.exceptions.InvalidDataException;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.NumericDatatype;

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
 * Konvertert der Daten von einer Einheit in eine andere konvertieren kann. Kann nur verwendet werden, wenn Konvertierung durch einfache Multiplikation/Division fester Werte mÃ¶glich ist.
 * @author KRA
 *
 */
public class ValueMultiplier implements Converter{
	private double factor;
	
	/**
	 * Konstruktor
	 * @param pFactor Faktor mit dem das Datum multipliziert/dividiert werden soll
	 */
	public ValueMultiplier(double pFactor){
		factor = pFactor;
	}
	public Datatype convert(Datatype pValue) throws InvalidDataException {
		if(pValue instanceof NumericDatatype){
			NumericDatatype dv = (NumericDatatype) pValue;
			try {
				return new NumericDatatype(dv.getDouble()*factor);
			} catch (ParseException e) {
				throw new InvalidDataException("Can't multiply instance of "+pValue.getClass().getName()+" with a double!");
			}
		}
		else
			throw new InvalidDataException("Can't multiply instance of "+pValue.getClass().getName()+" with a double!");
	}
	public Converter getInverseConverter() {
		return new ValueMultiplier(1.0/factor);
	}		
	
	public String toString(){
		return "*"+factor;
	}
	public Converter melt(Converter pConverter) {
		return new ValueMultiplier(factor*((ValueMultiplier) pConverter).factor);
	}
}
