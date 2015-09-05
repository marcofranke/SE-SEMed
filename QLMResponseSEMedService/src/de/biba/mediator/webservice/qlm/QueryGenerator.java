package de.biba.mediator.webservice.qlm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.params.AllClientPNames;

import de.biba.mediator.IMediator;

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

public class QueryGenerator {

	private ArrayList<String> allSelectedProperties = new ArrayList<String>();
	private ArrayList<String> allSelectedClasses = new ArrayList<String>();
	private ArrayList<String> assignedFreeVariables = new ArrayList<String>();
	private HashMap<String, String> assignedVariablesForClasses = new HashMap<String, String>();
	private HashMap<String, String> constantsForProperties = new HashMap<String, String>();
	private ArrayList<Property>allGeneratedpropertiesArrayList = new ArrayList<Property>();
	private HashMap<String, String> allreservedClassNamesForObjectproperties = new HashMap<String, String>();
	private IMediator mediator = null;
	
	
	
	/**
	 * 
	 * @param allSelectedProperties
	 * @param allSelectedClasses
	 * @param constantsForProperties It is important that all adressed properties are also inserted into the property list
	 * @param mediator
	 */
	
	public QueryGenerator(List<String> allSelectedProperties,
			List<String> allSelectedClasses, HashMap<String, String> constantsForProperties, IMediator mediator) {
		super();
		this.allSelectedProperties = (ArrayList<String>) allSelectedProperties;
		this.allSelectedClasses = (ArrayList<String>) allSelectedClasses;
		this.mediator = mediator;
		this.constantsForProperties = constantsForProperties;
		init();
	}


	protected void createVariablesForClasses(){
		for (String str: allSelectedClasses){
			assignedVariablesForClasses.put(str, "?"+ generateVariableName(str));
		}
	}
	
	private void shortenTheClassNames(){
		for (int i =0; i < allSelectedClasses.size(); i++){
			allSelectedClasses.set(i, getShortenAName(allSelectedClasses.get(i)));
		}
	}
	
	private String generateVariableName(String str){
		String result = str;
		result.toLowerCase();
		result = result.replaceAll("[_[^\\w\\ ]]", "");
		return result;
	}
	
	private void prepareProperties(){
		for (int i =0; i < allSelectedProperties.size(); i++){
			//Für jedes ausgewählte Property muss die Klasse (Range)
			Property property = new Property(this);
			property.setNameOfProperty(getShortenAName(allSelectedProperties.get(i)));
			property.setObject("?"+ generateVariableName(property.getNameOfProperty()));
			 Set <String> domains = null;
			 if (mediator.getModel().getTbox().getDatatypeProperties().containsKey(allSelectedProperties.get(i))){
				 domains = mediator.getModel().getTbox().getDatatypeProperties().get(allSelectedProperties.get(i)).getDomain(); 
			 }
			 else{
				 if (mediator.getModel().getTbox().getObjectProperties().get(allSelectedProperties.get(i))==null){
					 Logger.getAnonymousLogger().log(Level.SEVERE, allSelectedProperties.get(i));
				 }
				 else{
				 domains = mediator.getModel().getTbox().getObjectProperties().get(allSelectedProperties.get(i)).getDomain();
				 property.setAObjectProperty(true);
				 Set <String> range = mediator.getModel().getTbox().getObjectProperties().get(allSelectedProperties.get(i)).getRange();
				 for (String str: range){
					 if(allSelectedClasses.contains(str)){
						this.allreservedClassNamesForObjectproperties.put(str, property.getObject());
					 }
				 }
			 }
			 }
			 //Jetzt muss ich aus ganzen möglichen Domains die Domain raususchen, die passt
			 int index = -1;
			 String className ="";
			 for (String cl: domains){		
				 cl = cl.substring(cl.lastIndexOf("#")+1);
				 if (allSelectedClasses.contains(cl)){
					 //Gefunden
				 index = allSelectedClasses.indexOf(cl);
				 className = cl;
				 break;
				 }
		}
			 if (index >=0 ){
				 property.setSubject(this.assignedVariablesForClasses.get(className));
			 }
			 else{
				 property.setSubject(this.getANewFreeVariable());
			 }
			 //Jetzt muss man die ConstantValue berücksichtigen
			 if (constantsForProperties.containsKey(property.getNameOfProperty())){
				 property.setIsaConstant(true);
				 property.setObject(constantsForProperties.get(property.getNameOfProperty()));
			 }
			 
			 //Speichern
			 allGeneratedpropertiesArrayList.add(property);
		}
	}
	
	private void init(){
		
		shortenTheClassNames();
		createVariablesForClasses();
		prepareProperties();
		correctSubjectVariablesForProperties();
		
		
	}

	public String generateQuery(){
		String result ="";
		result = "select "+searchAllVariableForResult() + " where {";
		result+= generateAllClassSnipptes();
		result+= generateAllPropertySnippets();
		result += "}";
		return result;
	}
	
	private String generateAllPropertySnippets(){
		String result ="";
		for (Property prop: allGeneratedpropertiesArrayList){
			result+= " " + prop.generateStringTuple();
		}
		return result;
	}
	
	private String generateAllClassSnipptes(){
		String result="";
		for (String str: allSelectedClasses){
			result += " " + assignedVariablesForClasses.get(str)+ " a <"+str+">.";
		}
		return result;
	}
	
	private String searchAllVariableForResult(){
		String result="";
		for (String str: allSelectedClasses){
			result += assignedVariablesForClasses.get(str) +" ";
		}
		for (Property prop: allGeneratedpropertiesArrayList){
			if (prop.getObject().length() >1){
				if (prop.getObject().charAt(0)=='?'){
				result += prop.getObject() +" ";
				}
			}
		}
		return result;
	}
	
	private void correctSubjectVariablesForProperties(){
		for (String str: this.allreservedClassNamesForObjectproperties.keySet()){
			if (assignedVariablesForClasses.containsKey(str)){
			String variable = assignedVariablesForClasses.get(str);
			String newName = allreservedClassNamesForObjectproperties.get(str);
			
			//Für die Properties
			for (Property property : allGeneratedpropertiesArrayList){
				if (property.getSubject().equals(variable)){
					property.setSubject(newName);
				}
			}
			//Löschen der Klasse aus den selectedClasses
			allSelectedClasses.remove(str);
			}
		}
	}
	
	
	public String getANewFreeVariable(){
		
		char letter = (char) (65+ assignedFreeVariables.size());
		String variable = "?" + letter;
		assignedFreeVariables.add(variable);
		return variable;
	}
	
	
	public String getShortenAName(String nameOfProperty){
		String str = nameOfProperty;
		int index = str.lastIndexOf("#");
		index++;
		return str.substring(index);
	}
	
	
}
