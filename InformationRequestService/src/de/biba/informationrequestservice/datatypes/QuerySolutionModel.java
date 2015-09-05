package de.biba.informationrequestservice.datatypes;


import javax.swing.table.DefaultTableModel;

import de.biba.mediator.IQuerySolution;
import de.biba.mediator.ISolutionIterator;

/**[InformationRequestService. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2015  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

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

public class QuerySolutionModel extends DefaultTableModel {

	private static final long serialVersionUID = 7230652541592370017L;
	private IQuerySolution solution;
	private ISolutionIterator iter;
	
	
	public QuerySolutionModel(IQuerySolution pSolution) {
		super();
		if(pSolution==null)
			return;
		solution = pSolution;
		iter = solution.getIterator();
		
	}

	@Override
	public int getColumnCount() {
		if(iter==null)
			return 0;
		return solution.getColumnNames().size();
	}

	@Override
	public int getRowCount() {
		if(iter==null)
			return 0;
		return iter.getSize();
	}
	
	@Override
	public Object getValueAt(int pRow, int pColumn) {
		if(solution== null || pColumn>=solution.getColumnNames().size())
			return null;
		return iter.get(pColumn,pRow);
	}
	
	@Override
	public String getColumnName(int pColumn) {
		if(solution==null)
			return "";
		return solution.getColumnNames().get(pColumn);
	}
	
	@Override
	public boolean isCellEditable(int pRow, int pColumn) {
		return false;
	}	
	
	public String toString(){
		String result = "";
		for (String str: solution.getColumnNames()){
			System.out.print("|" + str);
		}
		System.out.println("|");
		for (int y =0; y < getRowCount(); y++){
		for (int x =0; x < getColumnCount(); x++){
			System.out.print("|"+ getValueAt(y, x));
		}
		System.out.println("|");
		}
		return result;
	}
}
