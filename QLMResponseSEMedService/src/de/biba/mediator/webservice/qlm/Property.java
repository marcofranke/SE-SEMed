package de.biba.mediator.webservice.qlm;

/**[QLMResponseSEMedService. This is a wrapper to tranform a xml based data base schema to an ontology]
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

public class Property {

	private QueryGenerator generator;
	boolean isAObjectProperty=false;
	boolean isaConstant = false;
	String subject = "";
	String object ="";
	String nameOfProperty ="";
	
	
	
	public Property(QueryGenerator generator) {
		super();
		this.generator = generator;
	}
	public boolean isAObjectProperty() {
		return isAObjectProperty;
	}
	public void setAObjectProperty(boolean isAObjectProperty) {
		this.isAObjectProperty = isAObjectProperty;
	}
	public boolean isIsaConstant() {
		return isaConstant;
	}
	public void setIsaConstant(boolean isaConstant) {
		this.isaConstant = isaConstant;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	public String getShortenedName(){
		String str = nameOfProperty;
		int index = str.lastIndexOf("#");
		index++;
		return str.substring(index);
	}
	
	public String getNameOfProperty() {
		return nameOfProperty;
	}
	public void setNameOfProperty(String nameOfProperty) {
		this.nameOfProperty = nameOfProperty;
	}
	public String generateStringTuple(){
		String result ="";
		String sub ="";
		if (subject.length() ==0){
			sub = generator.getANewFreeVariable(); 
		}
		else{
			sub = subject;
		}
		result = sub;
		result += " <";
		result+= getShortenedName() +"> ";
		if (isaConstant){
			result += "==\""+ object + "\""+".";
		}
		else{
			if((object != null) && (object.length() > 0)){
			result+= object +".";
			}
			else{
				result += "?"+ nameOfProperty +".";
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Property [isAObjectProperty=" + isAObjectProperty
				+ ", isaConstant=" + isaConstant + ", subject=" + subject
				+ ", object=" + object + "]";
	}
	
	
	
}
