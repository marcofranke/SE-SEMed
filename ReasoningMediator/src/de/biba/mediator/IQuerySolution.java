package de.biba.mediator;

import java.util.List;

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
 * Solution of a Query against the Mediator
 * @author KRA
 *
 */
public interface IQuerySolution {
	
	/**
	 * Retrieves the list of ColumnNames of this QuerySolution. The order of the retrieved names
	 * correlates to their appearance in the solution.
	 * @return List of ColumnNames
	 */
	public List<String> getColumnNames();
	
	/**
	 * Retrieves the size of this solution.
	 * @return Number of results in this solution
	 */
	public int size();
	
	/**
	 * Retrieves the iterator for iterating through the results of the query.
	 * @return Result-Iterator
	 */
	public ISolutionIterator getIterator();
}
