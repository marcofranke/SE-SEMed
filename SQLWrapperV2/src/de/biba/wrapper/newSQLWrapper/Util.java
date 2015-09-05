package de.biba.wrapper.newSQLWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.biba.ontology.datatypes.BooleanDatatype;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.DateValue;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.newSQLWrapper.Mapping.ColumnMapping;
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

public class Util {

	public static Datatype getDatatypeValue(String driverName, ColumnMapping cm, ResultSet rs){
		Datatype data = null;
		try{
			switch(cm.getValueType()){
				case NUMERIC:
					data = new NumericDatatype(rs.getDouble(cm.getValue()));
					break;
				case BOOLEAN:
					data = new BooleanDatatype(rs.getBoolean(cm.getValue()));
					break;
				case DATE:
					data = new DateValue(rs.getDate(cm.getValue()));
					break;
				default:
					String column = cm.getValue();
					//System.out.println((rs.getString("id"))+ "-->" + rs.getString("nombre"));
					//TODO This is a quick fix!!!!!!
					if ((driverName == null) ||( driverName.length() < 1)){
						try{
							data = new StringDatatype(rs.getString(cm.getValue().substring(cm.getValue().lastIndexOf(".")+1)));
						}
						catch(Exception e){
							data = new StringDatatype(rs.getString(cm.getValue()));
						}
					}
					if (driverName.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver")){
					data = new StringDatatype(rs.getString(cm.getValue().substring(cm.getValue().lastIndexOf(".")+1)));
					}
					else{
						data = new StringDatatype(rs.getString(cm.getValue()));
					}
					break;
			}
		}
		catch(SQLException e){
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getCause().toString());
			return null;
			
		}
		return data;
	}
	
	public static Datatype getDatatypeValue(String driverName, String column, ValueType valuetype, ResultSet rs){
		if (driverName.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver")){
			column = column.substring(column.lastIndexOf(".")+1);
		}
		Datatype data = null;
		try{
			switch(valuetype){
				case NUMERIC:
					data = new NumericDatatype(rs.getDouble(column));
					break;
				case BOOLEAN:
					data = new BooleanDatatype(rs.getBoolean(column));
					break;
				case DATE:
					data = new DateValue(rs.getDate(column));
					break;
				default:
					data = new StringDatatype(rs.getString(column));
					break;
			}
		}
		catch(SQLException e){
			return null;
		}
		return data;
	}
	
}
