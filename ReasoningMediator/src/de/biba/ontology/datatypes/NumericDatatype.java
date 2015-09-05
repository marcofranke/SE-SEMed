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
 * Diese Klasse kapselt einen Datum vom Typ double
 * @author KRA
 *
 */
public class NumericDatatype implements Datatype {
	double value;
	
	public NumericDatatype(double d){
		value = d;
	}
	
	@Override
	public String getString() {
		return ""+value;
	}

	@Override
	public int compareTo(Datatype pO) {
		if(pO instanceof NumericDatatype){
			double d = value - ((NumericDatatype) pO).value;
			if(d > 0.00001)
				return 1;
			else if(d<-0.00001)
				return -1;
			else
				return 0;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return value+"";
	}

	@Override
	public boolean getBoolean() throws ParseException {
		throw new ParseException("", 0);
	}

	@Override
	public Date getDate() throws ParseException {
		throw new ParseException("", 0);
	}

	@Override
	public double getDouble() throws ParseException {
		return value;
	}

	@Override
	public int getInt() throws ParseException {
		return (int) value;
	}
	public Datatype clone(){
		return new NumericDatatype(value);
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(this.value).hashCode();
	}
}
