package de.biba.mediator;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
 * Implementierung der Schnittstelle ISolutionIterator
 * @author KRA
 * @see ISolutionIterator
 */
public class QuerySolutionIterator implements ISolutionIterator{
	private QuerySolution solution;
	private ListIterator<List<Datatype>> iter;
	private List<Datatype> current;
	private Map<String,Integer> positions;
	
	/**
	 * Konstruktor
	 * @param pQuerySolution Ergebnis durch welches iteriert werden soll.
	 */
	public QuerySolutionIterator(QuerySolution pQuerySolution) {
		solution = pQuerySolution;
		iter = solution.data.listIterator();
		positions = new HashMap<String, Integer>();
		int i=0;
		for(String s : solution.getColumnNames()){
			positions.put(s, i);
			i++;
		}
	}

	@Override
	public Datatype get(int pIndex) {
		return current.get(pIndex);
	}

	@Override
	public boolean next() {
		if(iter.hasNext()){
			current = iter.next();
			return true;
		}
		return false;
	}
	
	@Override
	public int getSize(){
		return solution.data.size();
	}

	@Override
	public boolean prev() {
		if(iter.hasPrevious()){
			current = iter.previous();
			return true;
		}
		return false;
	}

	@Override
	public Datatype get(int pColumn, int pRow) {
		return solution.data.get(pRow).get(pColumn);
	}

	@Override
	public Datatype get(String pColumnName) {
		Integer pos = positions.get(pColumnName);
		if(pos==null)
			return null;
		return current.get(pos);
	}
}
