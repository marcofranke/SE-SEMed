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
 * Diese Klasse berechnet ein Quadrat in einer Konvertierungsregel.
 * @author KRA
 *
 */
public class Pow implements MathObject{
	MathObject m1;
	
	/**
	 * Konstruktor. Setzt das Formelelement, von dem das Quadrat gebildet werden soll.
	 * @param p1
	 */
	public Pow(MathObject p1){
		m1=p1;
	}

	/**
	 * Berechnet das Quadrat des Formelelements. Dazu wird die calc-Funktion des Formelelements mit pValue aufgerufen und das Ergebnis quadriert.
	 * @param Wert mit dem das Formelelement als Parameter berechnet werden soll. Wenn das Formelelement null ist, dann wird dieser Wert quadriert.
	 * @return Ergebnis der Berechnung.
	 */
	public double calc(double pValue) {
		if(m1!=null){
			double d = m1.calc(pValue);
			return d*d;
		}
		else return pValue*pValue;
	}
	public String toString(){
		if(m1!=null){
			return "("+m1.toString() + ")^2";
		}
		else
			return "x^2";
	}

}
