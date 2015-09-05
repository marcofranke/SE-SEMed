package de.biba.math;
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
 * Diese Klasse stellt eine Multiplikation in einer Konvertierungsregel dar.
 * @author KRA
 *
 */
public class Mul implements MathObject {
	MathObject m1;
	MathObject m2;
	
	
	/**
	 * Konstruktor der die beiden Mathe-Objekte setzt, die multipliziert werden. Es dürfen nicht beide Parameter null sein!
	 * @param p1 Erstes Produkt der Multiplikation. Kann null sein.
	 * @param p2 Zweites Produkt der Multiplikation. Kann null sein.
	 */
	public Mul(MathObject p1, MathObject p2){
		m1 = p1;
		m2 = p2;
	}

	/**
	 * Führt die Multiplikation durch. Dabei wird auf den beiden Faktoren die calc-Operation mit dem gleichen Parameter aufgerufen. Die Ergebnisse der beiden Aufrufe werden dann multipliziert.
	 * Wenn einer der beiden Faktoren null ist, dann wird nur bei dem anderen die calc-Funktion mit pValue als Parameter aufgerufen und anschließend pValue auf das Ergebnis multipliziert.
	 * @param pValue Wert mit dem die calc-Funktion der Faktoren aufgerufen wird.
	 * @return Ergebnis der Berechnung
	 */
	public double calc(double pValue) {
		if(m1!=null && m2!=null)
			return m1.calc(pValue)*m2.calc(pValue);
		else if(m1!=null)
			return m1.calc(pValue)*pValue;
		else return m2.calc(pValue)*pValue;
	}

	public String toString(){
		if(m1!=null && m2!=null){
			return m1.toString() + "*"+m2.toString();
		}
		else if(m1!=null){
			return m1.toString()+"*x";
		}
		else
			return "x*"+m2.toString();
	}
}
