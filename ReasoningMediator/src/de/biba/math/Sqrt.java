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
 * Diese Klasse berechnet die Wurzel in einer Konvertierungsregel.
 * @author KRA
 *
 */
public class Sqrt implements MathObject{
	MathObject m1;
	
	/**
	 * Konstruktor. Setzt das Formelelement, von dem die Wurzel berechnet werden soll.
	 * @param p1 Formelelement von dem die Wurzel gezogen werden soll. Kann null sein.
	 */
	public Sqrt(MathObject p1){
		m1=p1;
	}

	/**
	 * Berechnet die Wurzel des Formelelements. Dazu wird die calc-Funktion des Formelelements mit pValue aufgerufen und von dem Ergebnis die Wurzel gezogen.
	 * @param Wert mit dem das Formelelement als Parameter berechnet werden soll. Wenn das Formelelement null ist, dann wird von diesem Wert die Wurzel berechnet.
	 * @return Ergebnis der Berechnung.
	 */
	public double calc(double pValue) {
		if(m1!=null){
			double d = m1.calc(pValue);
			return Math.sqrt(d);
		}
		else return Math.sqrt(pValue);
	}
	
	public String toString(){
		if(m1!=null){
			return "sqrt("+m1.toString() + ")";
		}
		else
			return "sqrt(x)";
	}
}
