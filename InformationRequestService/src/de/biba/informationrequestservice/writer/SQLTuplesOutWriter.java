package de.biba.informationrequestservice.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.biba.informationrequestservice.datatypes.OutputWrapper;
import de.biba.informationrequestservice.datatypes.QuerySolutionModel;
import de.biba.mediator.IQuerySolution;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.newSQLWrapper.SQLWrapper;

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

public class SQLTuplesOutWriter implements IDataWriteInterface{

	private HashMap<String, String> generateAllTuples(String query){
		HashMap<String, String> result = new HashMap<String, String>();
		int index = query.indexOf("{");
		query = query.substring(index+1);
		while (query.contains("<")){
			int pre = query.indexOf('<');
			int post = query.indexOf('>');
			String nameofProperty = query.substring(pre+1, post);
			query = query.substring(post +1);
			int indexOfVar = query.indexOf('?');
			if (indexOfVar > 0){
				query = query.substring(indexOfVar);
			}
			int indexOfSpace = query.indexOf('.');
			String var = query.substring(0, indexOfSpace);
			if (!nameofProperty.contains("<")){
			result.put(var, nameofProperty);
			}
		}
		return result;
	}
	
	@Override
	public void writeData(IQuerySolution solution, OutputWrapper output, String query, String namespace) {
		 if (solution != null) {
			 HashMap<String, String> listOfVariables = generateAllTuples(query);
				QuerySolutionModel tmp = new QuerySolutionModel(solution);
				int rowCount = tmp.getRowCount();
				int colCount = tmp.getColumnCount();
				SQLWrapper wrapper = new SQLWrapper();
				try {
					wrapper.init(output.getConfigurationForStorage());
				} catch (XPathExpressionException
						| ParserConfigurationException | SAXException
						| JAXBException e) {
					e.printStackTrace();
				}
				//Now I have to identify which dataproperties are assigned
				OntModel tbox = wrapper.getOntModel();
				HashMap<String, ArrayList<Integer>> assignedElements = new HashMap<String, ArrayList<Integer>>();
				for (int i =0; i < colCount; i++){
					String str = tmp.getColumnName(i);
					if (str.contains(":")){
						str  = str.substring(0, str.indexOf(":"));
						str = str.replace(" ", "");
					}
					if (listOfVariables.containsKey(str)){
						str = listOfVariables.get(str);
					}
					if (!str.contains("#") ){
						//I have no namepace included into the variables. I have to add it
						str = namespace + "#"+ str;
					}
					DatatypeProperty prop = tbox.getDatatypeProperty(str);
					for (String domain: prop.getDomain()){
						if (!domain.contains("#")){
							domain = namespace+'#'+domain;
						}
						if (assignedElements.containsKey(domain)== false){
							ArrayList<Integer> values = new ArrayList<Integer>();
							values.add(i);
							assignedElements.put(domain, values);
						}
						else{
							assignedElements.get(domain).add(i);
						}
					}
					
				}
				OntModel result = new OntModel();
				for (int i =0; i < rowCount; i++){
					
					//for each classname...
					for (String className : assignedElements.keySet()){
						if (!className.contains("#")){
							className = namespace+'#'+className;
						}
						OntClass oc = tbox.getOntClass(className);
						Individual ind = result.createIndividual(oc);
						for (Integer index : assignedElements.get(className)){
							String str = tmp.getColumnName((int)index);
							if (str.contains(":")){
								str  = str.substring(0, str.indexOf(":"));
								str = str.replace(" ", "");
							}
							String d = listOfVariables.get(str);
							
							if (!d.contains("#")){
								d = namespace+'#' + d;
							}
						DatatypeProperty p = wrapper.getOntModel().getDatatypeProperty(d);
						StringDatatype value = new StringDatatype(String.valueOf(tmp.getValueAt(i,index)));
						result.addProperty(ind, p, value);
						}
					}
					
				}
				
		Logger.getAnonymousLogger().log(Level.INFO,result.toString());		
		 //Now, I have an ABox and can write it out
		wrapper.insertData(result.getAbox());		
		 }
	}

}
