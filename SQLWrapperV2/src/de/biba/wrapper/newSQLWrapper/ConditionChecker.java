package de.biba.wrapper.newSQLWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.biba.ontology.datatypes.BooleanDatatype;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.DateValue;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.newSQLWrapper.Mapping.Comparator;
import de.biba.wrapper.newSQLWrapper.Mapping.Condition;
import de.biba.wrapper.newSQLWrapper.Mapping.ValueType;

/**[SQL Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
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

public class ConditionChecker {
	
	private String column;
	private Datatype data;
	private Comparator comparator;
	private ValueType valueType;
	
	public ConditionChecker(Condition c){
		this.column = c.getColumn();
		this.comparator = c.getComparator();
		this.valueType = c.getValueType();
		switch(c.getValueType()){
			case NUMERIC:
				data = new NumericDatatype(Double.parseDouble(c.getValue()));
				break;
			case DATE:
				SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				try {
					data = new DateValue(f.parse(c.getValue()));
				} catch (ParseException e) {
					throw new RuntimeException("Fehlerhaftes Datumsformat in Mapping-Datei!");
				}
				break;
			case BOOLEAN:
				data = new BooleanDatatype(Boolean.parseBoolean(c.getValue()));
			default:
				data = new StringDatatype(c.getValue());
				break;
		}
	}
	
	public boolean check(ResultSet rs){
		Datatype d = null;
		try {
			switch(valueType){
				case NUMERIC:
					d = new NumericDatatype(rs.getDouble(this.column));
					break;
				case DATE:
					d = new DateValue(rs.getDate(this.column));
					break;
				case BOOLEAN:
					d = new BooleanDatatype(rs.getBoolean(this.column));
				default:
					d = new StringDatatype(rs.getString(this.column));
					break;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		int compared = this.data.compareTo(d);
		switch(this.comparator){
		case LT:
			return compared < 0;
		case LE:
			return compared <=0;
		case EQ:
			return compared ==0;
		case GT:
			return compared>0;
		case GE:
			return compared>=0;
		case NE:
			return compared!=0;
		}
		return false;
	}
	
}
