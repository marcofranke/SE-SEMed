package de.biba.mediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import de.biba.mediator.converter.ValueConverter;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.ObjectPropertyStatement;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.StringDatatype;

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
 * Implementierung von IQuerySolution
 * @see IQuerySolution
 * @author KRA
 *
 */
public class QuerySolution implements IQuerySolution{
	/**
	 * Spalten dieser Lösung
	 */
	public List<Column> columnNames;
	
	
	public void addColumn(String columnName){
		Column c = new Column(columnName, "String");
		columnNames.add(c);
	}
	
	/**
	 * Datensätze dieser Lösung
	 */
	public List<List<Datatype>> data;
	
	public QuerySolution(){
		columnNames = new ArrayList<Column>();
		data = new LinkedList<List<Datatype>>();
	}
	
	/**
	 * Liefert die Namen der Spalten dieser Lösung zurück.
	 * @return Spaltennamen der Lösung
	 */
	public List<String> getColumnNames() {
		List<String> names = new ArrayList<String>();
		for(Column c: columnNames)
			names.add(c.name);
		return names;
	}

	/**
	 * Bildet das kartesiche Produkt aus zwei QuerySolutions. Die Aufrufer-Instanz wird modifiziert.
	 * @param pQuerySolution
	 */
	public void cartesianProduct(QuerySolution pQuerySolution) {
		columnNames.addAll(pQuerySolution.columnNames);
		ListIterator<List<Datatype>> iter = data.listIterator();
		int columns = columnNames.size() + pQuerySolution.columnNames.size();
		while(iter.hasNext()){
			List<Datatype> tmp = iter.next();
			ListIterator<List<Datatype>> iter2 = pQuerySolution.data.listIterator(1);
			while(iter2.hasNext()){
				List<Datatype> tmp2 = new ArrayList<Datatype>(columns);
				tmp2.addAll(tmp);
				tmp2.addAll(iter2.next());
				iter.add(tmp2);
			}
			tmp.addAll(pQuerySolution.data.get(0));
		}
	}

	
	/**
	 * Haengt die Ergebnisse einer QuerySolution an die aktuelle. Funktioniert nur, wenn beide Loesungen die gleichen
	 * Spalten haben.
	 * @param pR
	 */
	public void append(QuerySolution pR) {
		if(!getColumnNames().containsAll(pR.getColumnNames()))
			return;
		int size = columnNames.size();
		int pos[] = new int[size];
		List<Column> pRColumnNames = pR.columnNames;
		boolean ident = true;
		for(int i=0;i<size;i++){
			pos[i] = pRColumnNames.indexOf(columnNames.get(i));
			if(pos[i] !=i)
				ident = false;
			String unit1 = columnNames.get(i).unit;
			String unit2 = pRColumnNames.get(pos[i]).unit;
			if(!unit1.equals(unit2)){
				boolean b = ValueConverter.getInstance().convertQuerySolution(unit2, unit1, pR, pos[i]);
				if(!b){
					Logger l = Logger.getLogger(getClass());
					l.warn("Can't append solutions");
					return;
				}
			}
		}
		if(ident)
			data.addAll(pR.data);
		else{
			for(List<Datatype> ld : pR.data){
				List<Datatype> tmp = new ArrayList<Datatype>();
				for(int position:pos){
					tmp.add(ld.get(position));
				}
				data.add(tmp);
			}
		}
	}

	/**
	 * Natuerlicher Verbund zweier Loesungsmengen. Wenn keine gleichen Spalten vorhanden sind, passiert nicht.
	 * Andernfalls wird die Aufruferinstanz ver�ndert!
	 * @param pQs
	 */
	public void naturalJoin(QuerySolution pQs) {
		int sameColumnsA[]  = new int[pQs.columnNames.size()];
		int sameColumnsB[]  = new int[pQs.columnNames.size()];
		int differentColumnsB[] = new int[pQs.columnNames.size()];
		int sameColumnsCtr = 0;
		int differentCtr = 0;
		for(int i=0; i<pQs.columnNames.size();i++){
			Column col = pQs.columnNames.get(i);
			int pos = columnNames.indexOf(col);
			if(pos>=0){
				sameColumnsA[sameColumnsCtr] = pos;
				sameColumnsB[sameColumnsCtr] = i;
				sameColumnsCtr++;
				String unit1 = columnNames.get(pos).unit;
				String unit2 = pQs.columnNames.get(i).unit;
				if(!unit1.equals(unit2)){
					boolean b = ValueConverter.getInstance().convertQuerySolution(unit2, unit1, pQs, i);
					if(!b){
						Logger l = Logger.getLogger(getClass());
						l.warn("Can't append solutions");
						return;
					}
				}
			}
			else{
				differentColumnsB[differentCtr] = i;
				differentCtr++;
			}
		}
		if(sameColumnsCtr==0){
			System.err.println("QuerySolution - naturalJoin: Keine gemeinsamen Spalten");
			data.clear();
			columnNames.clear();
		}
		else{
			Collections.sort(data,new DataComparator(sameColumnsA, sameColumnsCtr));
			Collections.sort(pQs.data,new DataComparator(sameColumnsB, sameColumnsCtr));
			DataComparator2 dc = new DataComparator2(sameColumnsA, sameColumnsB, sameColumnsCtr);
			for(int i=0; i<differentCtr;i++){
				columnNames.add(pQs.columnNames.get(differentColumnsB[i]));
			}
			if(data.size()==0 || pQs.data.size()==0)
				data.clear();
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<List<Datatype>> li2 = pQs.data.listIterator();
			
			List<Datatype> d1=null;
			List<Datatype> d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = dc.compare(d1, d2);
				if(compared==0){
					List<Datatype> d1Copy =new ArrayList<Datatype>(d1);
					addValues(d1, d2, sameColumnsB,sameColumnsCtr);
					int a =1;
					while(li2.hasNext()){
						d2=li2.next();
						a++;
						compared = dc.compare(d1, d2);
						if(compared==0){
							List<Datatype> copyCopy =new ArrayList<Datatype>(d1Copy);
							addValues(copyCopy, d2, sameColumnsB,sameColumnsCtr);
							li1.add(copyCopy);
						}
						else{
							break;
						}
					}
					while(a>0){
						d2=li2.previous();
						a--;
					}
					d2=li2.next();
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}
		}
			
	}

	private void addValues(List<Datatype> p_lpv, List<Datatype> p_lpv2,
			int[] p_columns2, int size) {
		
		for(int i=0; i<p_lpv2.size();i++){
			boolean insert = true;
			for(int j = 0;j<size;j++){
				int tmp = p_columns2[j];
				if(tmp==i){
					insert = false;
				}
			}
			if(insert){
				p_lpv.add(p_lpv2.get(i));
			}
		}		
	}
	
	public String toString(){
		int DEFAULTCOLUMNSIZE = 4;
		int columns = columnNames.size();
  	int columnSize[] = new int[columns];
  	StringBuffer result = new StringBuffer();
  	for(int i=0; i<columns;i++){
  		String columnName = columnNames.get(i).name;
  		int namelength = columnName.length();
  		if(namelength>DEFAULTCOLUMNSIZE)
  			columnSize[i] = namelength;
  		else
  			columnSize[i] = DEFAULTCOLUMNSIZE;
  	}
  	
  	for(List<Datatype> lv : data){
  		for(int i=0; i<columns;i++){
  			Datatype dt = lv.get(i);
  			String a = lv.get(i).getString();
  			int length = lv.get(i).getString().length();
  			if(length>columnSize[i])
  				columnSize[i]=length;
  		}
  	}
  	int totalwidth = 1;
  	for(int i:columnSize)
  		totalwidth += i+3;
  	
  	for(int i=0; i<totalwidth;i++)
  		result.append("=");
  	result.append("\n");
  	result.append("| ");
  	for(int i=0; i<columns;i++){
  		result.append(resizeString(columnNames.get(i).name,columnSize[i]));
  		if(i<columns-1)
  			result.append(" | ");
  	}
  	result.append(" |");
  	result.append("\n");
  	for(int i=0; i<totalwidth;i++)
  		result.append("=");
  	result.append("\n");
  	for(List<Datatype> lv :data){
  		result.append("| ");
	  	for(int i=0; i<columns;i++){
	  		result.append(resizeString(lv.get(i).getString(),columnSize[i]));
	  		if(i<columns-1)
	  			result.append(" | ");
	  		
	  	}
	  	result.append(" |");
	  	result.append("\n");
  	}
  	for(int i=0; i<totalwidth;i++)
  		result.append("=");
  	result.append("\n");
  	result.append(data.size()+" rows");
  	return result.toString();
	}
	
	private String resizeString(String string, int columnSize) {
		int diff = columnSize-string.length();
		for(int i=0; i<diff;i++)
			string+=" ";
		return string;
	}
	
	public void orderBy(List<String> columns, List<Boolean> ascending){
		int indices[] = new int[columns.size()];
		int ctr = 0;
		for(String column:columns){
			int index = columnNames.indexOf(new Column(column,null));
			if(index>=0){
				indices[ctr] = index;
				ctr++;
			}
		}
		Collections.sort(data,new DataComparator3(indices, ctr, ascending));
	}
	
	private class DataComparator implements Comparator<List<Datatype>>{
		int positions[];
		int size;
		
		private DataComparator(int[] pPositions, int pcolumnCount){
			positions = pPositions;
			size = pcolumnCount;
		}
		
		@Override
		public int compare(List<Datatype> pO1, List<Datatype> pO2) {
			for(int i=0; i<size;i++){
				int pos = positions[i];
				int cmp = pO1.get(pos).compareTo(pO2.get(pos));
				if(cmp!=0)
					return cmp;
			}
			return 0;
		}
		
	}
	
	private class DataComparator3 implements Comparator<List<Datatype>>{
		int positions[];
		int size;
		List<Boolean> asc;
		
		private DataComparator3(int[] pPositions, int pcolumnCount, List<Boolean> pAsc){
			positions = pPositions;
			size = pcolumnCount;
			asc = pAsc;
		}
		
		@Override
		public int compare(List<Datatype> pO1, List<Datatype> pO2) {
			for(int i=0; i<size;i++){
				int pos = positions[i];
				int cmp = pO1.get(pos).compareTo(pO2.get(pos));
				if(cmp!=0){
					if(asc.get(i))
						return cmp;
					else
						return cmp*-1;
				}
			}
			return 0;
		}
	}
	private class DataComparator2 implements Comparator<List<Datatype>>{
		int positionsA[];
		int positionsB[];
		int size;
		
		private DataComparator2(int[] pPositionsA, int[] pPositionsB, int pcolumnCount){
			positionsA = pPositionsA;
			positionsB = pPositionsB;
			size = pcolumnCount;
		}
		
		@Override
		public int compare(List<Datatype> pO1, List<Datatype> pO2) {
			for(int i=0; i<size;i++){
				int posA = positionsA[i];
				int posB = positionsB[i];
				int cmp = pO1.get(posA).compareTo(pO2.get(posB));
				if(cmp!=0)
					return cmp;
			}
			return 0;
		}
		
	}
	
	private class DataComparator1 implements Comparator<List<Datatype>>{
		int column;
		public DataComparator1(int pColumn){
			column = pColumn;
		}
		@Override
		public int compare(List<Datatype> pO1, List<Datatype> pO2) {
			return pO1.get(column).compareTo(pO2.get(column));
		}
	}
	
	/**
	 * Projektion der Loesung in eine andere Form
	 * @param pColumns
	 */
	public void project(List<Column> pColumns) {
		if(pColumns==null)
			return;
		List<Integer> resultVars = new ArrayList<Integer>(pColumns.size());
		for(Column c : pColumns){
			int index = columnNames.indexOf(c);
			if(index<0){
				columnNames.clear();
//				columnNames.addAll(pColumns);
				for(int i=0; i<pColumns.size();i++)
					columnNames.add(pColumns.get(i));
				data.clear();
				return;
			}
			else{
				if(!c.unit.equals("unknown") && !c.unit.equals(columnNames.get(index).unit)){
					boolean b = ValueConverter.getInstance().convertQuerySolution(columnNames.get(index).unit, c.unit, this, index);
					if(!b){
						Logger l = Logger.getLogger(getClass());
						l.warn("Couldn't convert Units!");
						data.clear();
						return;						
					}
				}
			}
			resultVars.add(index);
		}
//		columnNames = pColumns;
		columnNames.clear();
		for(int i=0; i<pColumns.size();i++)
			columnNames.add(pColumns.get(i));
//		List<String> tmp = new ArrayList<String>();
//		for(int i:resultVars)
//			tmp.add(units.get(i));
//		units = tmp;
		
		if(resultVars.size()==0){
			data = new ArrayList<List<Datatype>>();
			return;
		}
		List<List<Datatype>> result = new ArrayList<List<Datatype>>(data.size());
		for(List<Datatype> lpv : data){
			List<Datatype> nlpv = new ArrayList<Datatype>(pColumns.size());
			for(Integer i: resultVars){
				nlpv.add(lpv.get(i));
			}
			result.add(nlpv);
		}
		data = result;
	}

	
	/**
	 * (Natural-)Joint dieses Ergebnis mit einer Liste von DatatypePropertyStatements.
	 * @param pLs Liste von DatatypePropertyStatements
	 * @param pSubject Name der Spalte mit dem das Subject der DatatypePropertyStatements gejoint werden soll. Entweder pSubject oder pObject müssen in diesem Ergebnis als Spalte vorkommen.
	 * @param pObject Name der Spalte mit dem das Object der DatatypePropertyStatements gejoint werden soll. Entweder pSubject oder pObject müssen in diesem Ergebnis als Spalte vorkommen.
	 * @param pObjectUnit Einheit des Objects.
	 */
	public void joinDatatypePropertyStatements(List<DatatypePropertyStatement> pLs, String pSubject,
			String pObject, String pObjectUnit) {
		//
		if (pLs != null){
			//Sollte es sich um ein Individium mehere Werte zu einem Property haben, werden diese zusammengefasst!!
			for (int i =0; i < pLs.size(); i++){
				
				long id = pLs.get(i).subject.id;
				for (int a =i+1; a < pLs.size(); a++){
				if (pLs.get(a).subject.id==id){
					String str = pLs.get(i).object.getString() ;
							if (pLs.get(i).object.getString() .equals(pLs.get(a).object.getString())==false){
								str +=  ";" +  pLs.get(a).object.getString();
							}
					StringDatatype d = new StringDatatype(str);
					pLs.get(i).object = d;
					pLs.remove(a);
					a--;
				}
				}
			}
			
			
		}
		int s = columnNames.indexOf(new Column(pSubject,null));
		int o = columnNames.indexOf(new Column(pObject,null));
		if(s>=0 && o>=0){
			if(pLs==null || pLs.isEmpty()){
				data.clear();
				return;
			}
			
			
			int positions[] = {s,o};
			Collections.sort(data, new DataComparator(positions, 2));
			Collections.sort(pLs,new Comparator<DatatypePropertyStatement>(){
				@Override
				public int compare(DatatypePropertyStatement pO1,
						DatatypePropertyStatement pO2) {
					long cmp = pO1.subject.id-pO2.subject.id;
					if(cmp==0L)
						return pO1.object.compareTo(pO2.object);
					else if (cmp>0)
						return 1;
					else
						return -1;
				}	
			});
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<DatatypePropertyStatement> li2 = pLs.listIterator();
			
			List<Datatype> d1=null;
			DatatypePropertyStatement d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = d1.get(s).compareTo(d2.subject);
				if(compared==0)
					compared = d1.get(o).compareTo(d2.object);
				if(compared==0){
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}		
		}
		else if(s>=0){
			if(pLs==null || pLs.isEmpty()){
				columnNames.add(new Column(pObject, pObjectUnit));
				data.clear();
				return;
			}
			Collections.sort(pLs,new Comparator<DatatypePropertyStatement>(){
				@Override
				public int compare(DatatypePropertyStatement pO1,
						DatatypePropertyStatement pO2) {
					long result = pO1.subject.id-pO2.subject.id;
					if(result<0)
						return -1;
					else if(result==0)
						return 0;
					else
						return 1;
				}
			});
			Collections.sort(data,new DataComparator1(s));
			columnNames.add(new Column(pObject, pObjectUnit));
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<DatatypePropertyStatement> li2 = pLs.listIterator();
			
			List<Datatype> d1=null;
			DatatypePropertyStatement d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = d1.get(s).compareTo(d2.subject);
				if(compared==0){
					List<Datatype> d1Copy =new ArrayList<Datatype>(d1);
//					addValues(d1, d2, sameColumnsB,sameColumnsCtr);
					d1.add(d2.object);
					int a =1;
					while(li2.hasNext()){
						d2=li2.next();
						a++;
						compared = d1.get(s).compareTo(d2.subject);
						if(compared==0){
							List<Datatype> copyCopy =new ArrayList<Datatype>(d1Copy);
//							addValues(copyCopy, d2, sameColumnsB,sameColumnsCtr);
							copyCopy.add(d2.object);
							li1.add(copyCopy);
						}
						else{
							break;
						}
					}
					while(a>0){
						d2=li2.previous();
						a--;
					}
					d2=li2.next();
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}		
		}
		else if(o>=0){
			if(pLs==null || pLs.size()==0){
				columnNames.add(new Column(pSubject, "unknown"));
				data.clear();
				return;
			}
			Collections.sort(pLs,new Comparator<DatatypePropertyStatement>(){
				@Override
				public int compare(DatatypePropertyStatement pO1,
						DatatypePropertyStatement pO2) {
					return pO1.object.compareTo(pO2.object);
				}
			});
			Collections.sort(data,new DataComparator1(o));
			columnNames.add(new Column(pSubject, "unknown"));
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<DatatypePropertyStatement> li2 = pLs.listIterator();
			
			List<Datatype> d1=null;
			DatatypePropertyStatement d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = d1.get(s).compareTo(d2.object);
				if(compared==0){
					List<Datatype> d1Copy =new ArrayList<Datatype>(d1);
//					addValues(d1, d2, sameColumnsB,sameColumnsCtr);
					d1.add(d2.subject);
					int a =1;
					while(li2.hasNext()){
						d2=li2.next();
						a++;
						compared = d1.get(s).compareTo(d2.object);
						if(compared==0){
							List<Datatype> copyCopy =new ArrayList<Datatype>(d1Copy);
//							addValues(copyCopy, d2, sameColumnsB,sameColumnsCtr);
							copyCopy.add(d2.subject);
							li1.add(copyCopy);
						}
						else{
							break;
						}
					}
					while(a>0){
						d2=li2.previous();
						a--;
					}
					d2=li2.next();
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}
		}
		else{
			System.err.println("QuerySolution-join: Fehler");
		}
	}
	
	/**
	 * (Natural-)Joint dieses Ergebnis mit einer Liste von ObjectPropertyStatements.
	 * @param pLs Liste von ObjectPropertyStatements
	 * @param pSubject Name der Spalte mit dem das Subject der ObjectPropertyStatement gejoint werden soll. Entweder pSubject oder pObject müssen in diesem Ergebnis als Spalte vorkommen.
	 * @param pObject Name der Spalte mit dem das Object der ObjectPropertyStatement gejoint werden soll. Entweder pSubject oder pObject müssen in diesem Ergebnis als Spalte vorkommen.
	 */
	public void joinObjectPropertyStatements(List<ObjectPropertyStatement> pLs, String pSubject,
			String pObject) {
		//
		int s = columnNames.indexOf(new Column(pSubject,null));
		int o = columnNames.indexOf(new Column(pObject,null));
		
		//Subjekt und Objekt kommen vor
		if(s>=0 && o>=0){
			int positions[] = {s,o};
			Collections.sort(data, new DataComparator(positions, 2));
			Collections.sort(pLs,new Comparator<ObjectPropertyStatement>(){
				@Override
				public int compare(ObjectPropertyStatement pO1,
						ObjectPropertyStatement pO2) {
					long cmp = pO1.subject.id-pO2.subject.id;
					if(cmp==0L)
						return pO1.object.compareTo(pO2.object);
					else if(cmp>0)
						return 1;
					else
						return -1;
				}	
			});
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<ObjectPropertyStatement> li2 = pLs.listIterator();
			
			List<Datatype> d1=null;
			ObjectPropertyStatement d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = d1.get(s).compareTo(d2.subject);
				if(compared==0)
					compared = d1.get(o).compareTo(d2.object);
				if(compared==0){
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}		
		}
		
		//Nur Subjekt kommt vor
		else if(s>=0){
			Collections.sort(pLs,new Comparator<ObjectPropertyStatement>(){
				@Override
				public int compare(ObjectPropertyStatement pO1,
						ObjectPropertyStatement pO2) {
					long result = pO1.subject.id-pO2.subject.id;
					if(result > 0)
						return 1;
					else if(result <0)
						return -1;
					else return 0;
				}
			});
			Collections.sort(data,new DataComparator1(s));
			columnNames.add(new Column(pObject, "unknown"));
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<ObjectPropertyStatement> li2 = pLs.listIterator();
			
			List<Datatype> d1=null;
			ObjectPropertyStatement d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = d1.get(s).compareTo(d2.subject);
				if(compared==0){
					List<Datatype> d1Copy =new ArrayList<Datatype>(d1);
//					addValues(d1, d2, sameColumnsB,sameColumnsCtr);
					d1.add(d2.object);
					int a =1;
					while(li2.hasNext()){
						d2=li2.next();
						a++;
						compared = d1.get(s).compareTo(d2.subject);
						if(compared==0){
							List<Datatype> copyCopy =new ArrayList<Datatype>(d1Copy);
//							addValues(copyCopy, d2, sameColumnsB,sameColumnsCtr);
							copyCopy.add(d2.object);
							li1.add(copyCopy);
						}
						else{
							break;
						}
					}
					while(a>0){
						d2=li2.previous();
						a--;
					}
					d2=li2.next();
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}		
		}
		else if(o>=0){
			Collections.sort(pLs,new Comparator<ObjectPropertyStatement>(){
				@Override
				public int compare(ObjectPropertyStatement pO1,
						ObjectPropertyStatement pO2) {
					return pO1.object.compareTo(pO2.object);
				}
			});
			Collections.sort(data,new DataComparator1(o));
			columnNames.add(new Column(pSubject,"unknown"));
			ListIterator<List<Datatype>> li1 = data.listIterator();
			ListIterator<ObjectPropertyStatement> li2 = pLs.listIterator();
			
			List<Datatype> d1=null;
			ObjectPropertyStatement d2=null;
			boolean getA = true;
			boolean getB = true;
			int compared = 0;
			while((li1.hasNext()||!getA) &&(li2.hasNext() || !getB)){
				if(getA)
					d1 = li1.next();
				if(getB)
					d2 = li2.next();
				compared = d1.get(o).compareTo(d2.object);
				if(compared==0){
					List<Datatype> d1Copy =new ArrayList<Datatype>(d1);
//					addValues(d1, d2, sameColumnsB,sameColumnsCtr);
					d1.add(d2.subject);
					int a =1;
					while(li2.hasNext()){
						d2=li2.next();
						a++;
						compared = d1.get(o).compareTo(d2.object);
						if(compared==0){
							List<Datatype> copyCopy =new ArrayList<Datatype>(d1Copy);
//							addValues(copyCopy, d2, sameColumnsB,sameColumnsCtr);
							copyCopy.add(d2.subject);
							li1.add(copyCopy);
						}
						else{
							break;
						}
					}
					while(a>0){
						d2=li2.previous();
						a--;
					}
					d2=li2.next();
					getA = true;
					getB = false;
				}
				else if(compared<0 || !li2.hasNext()){
					getA = true;
					getB = false;
					li1.remove();
				}
				else{
					getA = false;
					getB = true;
				}
			}
		}
		else{
			System.err.println("QuerySolution-join: Fehler");
		}
	}

	@Override
	public ISolutionIterator getIterator() {
		QuerySolutionIterator qsi = new QuerySolutionIterator(this);
		return qsi;
	}

	@Override
	public int size() {
		return data.size();
	}
	
	
	/**
	 * Entfernt Duplikate aus diesem Ergebnis. Haben zwei Zeilen im Ergebnis die gleichen Werte, so wird einer der Zeilen aus dem Ergebnis gelöscht.
	 */
	public void removeDuplicates(){
		Comparator<List<Datatype>> comparator = new Comparator<List<Datatype>>() {
			@Override
			public int compare(List<Datatype> pO1, List<Datatype> pO2) {
				Iterator<Datatype> iter1 = pO1.iterator();
				Iterator<Datatype> iter2 = pO2.iterator();
				while(iter1.hasNext() && iter2.hasNext()){
					int cmp = iter1.next().compareTo(iter2.next());
					if(cmp!=0)
						return cmp;
					
				}
				if(iter1.hasNext()){
					Logger logger = Logger.getLogger(this.getClass());
					logger.warn("Loesungen haben unterschiedlich viele Spalten");
					return 1;
				}
				if(iter2.hasNext()){
					Logger logger = Logger.getLogger(this.getClass());
					logger.warn("Loesungen haben unterschiedlich viele Spalten");
					return -1;
				}
				return 0;
			}
		};
		Collections.sort(data,comparator);
		
		List<Datatype> last = null;
		Iterator<List<Datatype>> iter = data.iterator();
		
		while(iter.hasNext()){
			if(last ==null){
				last = iter.next();
				continue;
			}
			List<Datatype> current = iter.next();
			if(comparator.compare(last, current)==0){
				iter.remove();
			}
			else{
				last = current;
			}
		}
	}

	/**
	 * Diese Klasse beschreibt eine Spalte in einem Query-Ergebnis des Mediators.
	 * @author KRA
	 *
	 */
	public class Column{
		/**
		 * Name der Spalte
		 */
		public String name;
		
		/**
		 * Einheit der Spalte
		 */
		public String unit;
		
		public Column(String pObject, String pObUnit) {
			name = pObject;
			unit = pObUnit;
		}

		@Override
		public boolean equals(Object pObj) {
			if(pObj instanceof Column)
				return name.equals(((Column)pObj).name);
			else
				return name.equals(pObj);
		}
	}
	
	
}
