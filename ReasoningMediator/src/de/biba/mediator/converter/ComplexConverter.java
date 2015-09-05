package de.biba.mediator.converter;

import java.text.ParseException;

import de.biba.exceptions.InvalidDataException;
import de.biba.math.MathObject;
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
 * Konvertert der Daten von einer Einheit in eine andere konvertieren kann. Dabei kÃ¶nnen komplexe Konvertierungsregeln angewendet werden
 * @author KRA
 *
 */
public class ComplexConverter implements Converter {
	MathObject normal;
	MathObject inverse;
	
	/**
	 * Konstruktor
	 * @param pNormal Konvertierungsformel
	 * @param pInverse Inverse Konvertierungsformel
	 */
	public ComplexConverter(MathObject pNormal, MathObject pInverse){
		normal = pNormal;
		inverse = pInverse;
	}
	

	public Datatype convert(Datatype pValue) throws InvalidDataException {
		try {
			return new NumericDatatype(normal.calc(pValue.getDouble()));
		} catch (ParseException e) {
			throw new InvalidDataException("Falsches Datenformat");
		}
	}

	public Converter getInverseConverter() {
		return new ComplexConverter(inverse,normal);
	}

//	public Converter melt(Converter pConverter) {
//		return null;
//	}
	
	public String toString(){
		return normal.toString();
	}
}
