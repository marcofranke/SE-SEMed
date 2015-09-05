package de.biba.mediator;

import java.util.ArrayList;
import java.util.List;

import de.biba.mediator.QuerySolution.Column;
import de.biba.mediator.constraints.ConstraintList;
import de.biba.ontology.OntModel;

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
 * Diese Klasse stellt eine Select-Query an den Mediator dar.
 * @author KRA
 *
 */
public class OutputQuery implements IQuery{
	ConstraintList constraint;
	int limit;
	int offset;
	private List<String> columns;
	private List<String> units;
	private List<String> sortingsColumns;
	private List<Boolean> sortingOrder;
	private boolean distinct;
	
	public OutputQuery(){
		constraint = null;
		limit = -1;
		offset = -1;
		columns = new ArrayList<String>();
		units = new ArrayList<String>();
		sortingsColumns = new ArrayList<String>();
		sortingOrder = new ArrayList<Boolean>();
	}
	
	/**
	 * Fügt eine Sortierreihenfolge für eine Spalte hinzu
	 * @param pColumn Spalte die sortiert werden soll
	 * @param pAscending Gibt an ob auf- oder absteigend sortiert werden soll
	 */
	public void addOrdering(String pColumn, boolean pAscending){
		sortingsColumns.add(pColumn);
		sortingOrder.add(pAscending);
	}

	/**
	 * Liefert die Liste mit den Statements der Query zurück
	 * @return Liste mit den Statements der Query
	 */
	public ConstraintList getConstraint() {
		return constraint;
	}

	public void setConstraint(ConstraintList pConstraint) {
		constraint = pConstraint;
	}

	/**
	 * Gibt die maximale Ergebnismenge zurück
	 * @return Maximale Ergebnismenge
	 */
	public int getLimit() {
		return limit;
	}

	public void setLimit(int pLimit) {
		limit = pLimit;
	}

	/**
	 * Gibt den Offset des Ergebnisses zurück
	 * @return Offset des Ergebnisses
	 */
	public int getOffset() {
		return offset;
	}

	public void setOffset(int pOffset) {
		offset = pOffset;
	}
	
	/**
	 * Fügt der Query eine Spalte hinzu. Die Einheit dieser Spalte wird dabei auf "unknown" gesetzt.
	 * @param pColumn Name der Spalte die hinzugefügt werden soll.
	 */
	public void addColumn(String pColumn){
		columns.add(pColumn);
		units.add("unknown");
	}
	
	/**
	 * Fügt der Query eine Spalte hinzu. 
	 * @param pColumn Name der Spalte die hinzugefügt werden soll.
	 * @param pUnit Einheit der Spalte
	 */
	public void addColumn(String pColumn, String pUnit){
		columns.add(pColumn);
		units.add(pUnit);
	}

	/**
	 * Startet mit der Auswertung dieser Query
	 * @param pOm Ontologie auf die diese Query angewendet werden soll.
	 * @return Ergebnis der Query
	 */
	public QuerySolution solve(OntModel pOm) {
		QuerySolution qs = constraint.solve(pOm);
		
		//Wenn Ergebnis sortiert werden muss
		if(sortingsColumns.size()>0)
			qs.orderBy(sortingsColumns, sortingOrder);
		
		//Falls kein Ergebnis, erstelle leeres Ergebnis
		if(qs==null){
			qs = new QuerySolution();
		}
		//Liste mit Column-Objekten erstellen
		List<Column> c = new ArrayList<Column>();
		for(int i = 0; i< columns.size();i++)
			c.add(qs.new Column(columns.get(i), units.get(i)));
		
		//Projektion des Ergebnisses
		qs.project(c);
		return qs;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<columns.size();i++){
			sb.append(columns.get(i)+"["+ units.get(i)+"],");
		}
		sb.append("\n");
		sb.append(constraint);
		sb.append("\n");
		if(limit>=0)
			sb.append("LIMIT: "+limit + " ");
		if(offset>=0)
			sb.append("OFFSET: "+offset + "\n");
		if(sortingsColumns.size()>0){
			sb.append("SORTINGBY ");
			for(int i=0; i<sortingsColumns.size();i++){
				sb.append(sortingsColumns.get(i)+"[");
				if(sortingOrder.get(i))
					sb.append("ASC]");
				else
					sb.append("DESC]");
				if(i<sortingOrder.size()-1)
					sb.append(",");
			}
		}
		return sb.toString();
	}

	@Override
	public boolean isIntputQuery() {
		return false;
	}

	public void setDistinct(boolean pB) {
		distinct = pB;
	}

	/**
	 * Gibt an ob die Ergebnisse der Query eindeutig sein sollen. Ist dies der Fall, so werden doppelte Einträge im Ergebnis der Query herausgefiltert.
	 * @return
	 */
	public boolean isDistinct() {
		return distinct;
	}
}
