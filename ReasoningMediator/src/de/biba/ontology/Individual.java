package de.biba.ontology;

import java.text.ParseException;
import java.util.Date;

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
 * Diese Klasse stellt ein Individuum in einer Ontologie dar. Implementiert das Interface {@link Datatype} 
 * @author KRA
 *
 */
public class Individual implements Datatype{
	/**
	 * Eindeutige ID des Individuums.
	 */
	public long id;
	
	/**
	 * Konstruktor
	 * @param id ID des Individuums
	 */
	public Individual(Long id){
		this.id = id;
	}

	@Override
	public int compareTo(Datatype pO) {
		if(pO instanceof Individual){
			long result = id-((Individual)pO).id;
			if(result==0)
				return 0;
			else if(result>0)
				return 1;
			else return -1;
		}
		return -1;
	}

	@Override
	public String getString() {
		return ""+id;
	}
	
	public String toString(){
		return ""+id;
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
		throw new ParseException("", 0);
	}

	@Override
	public int getInt() throws ParseException {
		throw new ParseException("", 0);
	}
	
	public Datatype clone(){
		return new Individual(id);
	}
}
