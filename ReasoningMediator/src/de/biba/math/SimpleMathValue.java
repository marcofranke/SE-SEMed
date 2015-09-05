package de.biba.math;

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
 * Die Klasse stellt eine Konstante in einer Berechnungsformel dar.
 * @author KRA
 *
 */
public class SimpleMathValue implements MathObject {
	private double value;
	
	/**
	 * Konstruktor der die Konstante setzt
	 * @param pValue Konstante
	 */
	public SimpleMathValue(double pValue){
		value = pValue;
	}
	
	/**
	 * Gibt den Wert der Konstanten zurÃ¼ck
	 * @param pValue wird nicht verwendet.
	 * @return Wert der Konstanten
	 */
	public double calc(double pValue) {
		return value;
	}

	public String toString(){
		return ""+value;
	}
}
