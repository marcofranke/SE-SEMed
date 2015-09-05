package de.biba.mediator.webservice.qlm;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.map.HashedMap;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import de.biba.mediator.IMediator;
import de.biba.mediator.webservice.configuration.WrapperConfiguration;
import de.biba.odf.InfoItemType;
import de.biba.odf.ObjectType;
import de.biba.odf.ObjectsType;
import de.biba.odf.QlmID;
import de.biba.odf.ValueType;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;

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

public class SPARQLCreator {

	private String qlmDataModelString ="";
	private String sPARQLQuery = null;
	private ObjectsType qlmRootElement = null;
	private IMediator mediator = null;
	private LinkedList<String> containedNamespaces = new LinkedList<String>();

	public String getQlmDataModelString() {
		return qlmDataModelString;
	}

	public void setQlmDataModelString(String qlmDataModelString) {
		this.qlmDataModelString = qlmDataModelString;
	}

	public SPARQLCreator(String qlmDataModelString, IMediator mediaor) {
		super();
		this.qlmDataModelString = qlmDataModelString;
		this.mediator = mediaor;
		init();
		generateSPARQL();
	}
	
	public String getsPARQLQuery() {
		return sPARQLQuery;
	}

	public LinkedList<String> getContainedNamespaces() {
		return containedNamespaces;
	}

	/**
	 * This methods takes the qlm string, generate a Java Object and finally generate the namespace and the sparql query
	 */
	private void init(){
		try{

			JAXBContext context = JAXBContext
					.newInstance("de.biba.qlm.qmldf");
			Unmarshaller m = context.createUnmarshaller();
			Object ob = m.unmarshal(new StringReader(qlmDataModelString));
			
			if (ob==null){
			Logger.getAnonymousLogger().log(java.util.logging.Level.INFO ,"Config File doesn't exits "  );
			}
			
				 java.io.StringReader input = new java.io.StringReader(qlmDataModelString);
				 SAXBuilder builder = new SAXBuilder();
				 org.jdom2.Document doc = builder.build(input);
				Element root =   doc.getRootElement();  
				ObjectsType t = new ObjectsType();
				qlmRootElement = t;
				for (Element  elem: root.getChildren()){
					if (elem.getName().equals("Object")){
						ObjectType oType = new ObjectType();
						t.getObject().add(oType);
						for (org.jdom2.Attribute attr : elem.getAttributes()){
							if (attr.getName().equals("type")){
								oType.setType(attr.getValue());
							}
						}
						for (Element subElem: elem.getChildren()){
							if (subElem.getName().equals("id")){
								QlmID id = new QlmID();
								id.setValue(subElem.getValue());
								oType.getId().add(id);
							}
						}
						for (Element subElem: elem.getChildren()){
							if (subElem.getName().equals("InfoItem")){
								InfoItemType k = new InfoItemType();
							oType.getInfoItem().add(k);
							for (org.jdom2.Attribute attr: subElem.getAttributes()){
								if (attr.getName().equals("name")){
									k.setName(attr.getValue());	
								}
							}
								for (Element subsubElem: subElem.getChildren()){
									System.out.println(subsubElem.getName());
									if (subsubElem.getName().equals("name")){
										
										k.setName(subsubElem.getValue());
									}
									
								}
								for (Element subsubElem: subElem.getChildren()){
									if (subsubElem.getName().equals("value")){
										ValueType valueT = new ValueType();
										valueT.setValue(subsubElem.getValue());
										
									}
								}
							}
						}
					}
				}
			 
			}
			catch(Exception e){
			Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING, e.toString());
			
			
			}
	}
	
	private void extractNamespaces(String name){
		containedNamespaces.clear();
		OntModel backgroundOntology = mediator.getModel();
		for (OntClass className : backgroundOntology.tbox.simpleClasses.values()){
			String uri = className.getUri();
			String c = uri.substring(uri.indexOf("#")+1);
			if (c.equals(name)){
				String namespace = uri.substring(0, uri.indexOf("#"));
				if (containedNamespaces.contains(namespace)==false){
					containedNamespaces.add(namespace);
				}
			}
		}
	}
	
	
	private  String searchInverseFunctionalProperty(String className){
		OntModel backgroundOntology = mediator.getModel();
		for (DatatypeProperty prop :  backgroundOntology.tbox.datatypeProperties.values()){
			if (prop.isInverseFunctional()){
			className = className.substring(className.indexOf("#")+1);
			if(prop.getDomain().contains(className)){
				return prop.getUri();
			}
			}
			
		}
		return "";
	}
	
	private void generateSPARQL(){
		ArrayList<String> allClassNames = new ArrayList<String>();
		ArrayList<String> allProperties = new ArrayList<String>();
		HashMap<String, String> allConstantValues = new HashMap<String, String>();
		
			if (qlmDataModelString != null){
				for (ObjectType qlmObject: qlmRootElement.getObject() ){
					String className = qlmObject.getType();
					if (!className.contains("#")){
						//There is no namespaceincluded. Have to serach
						Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING, "There is no namespace included in the qlm object: " + className);
						extractNamespaces(className);
					}
					else{
						String namespace = className.substring(0, className.indexOf("#"));
						if (containedNamespaces.contains(namespace)==false){
							containedNamespaces.add(namespace);
						}
					}
					//Now, I have extraxted or generated the namepace
					if (containedNamespaces.size() >0){
						Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING, "There is more than one applicable  namespace.  The first one is chosen.");
					}
					className = containedNamespaces.get(0) + "#"+  className;
					String property = searchInverseFunctionalProperty(className);
					if (!allClassNames.contains(className)){
						allClassNames.add(className);
					}
					allProperties.add(property);
					if ((qlmObject.getId() != null) && (qlmObject.getId().size() > 0)){
						if (qlmObject.getId().size() > 1){
							Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING, "There is more than one value for the inversal functional porperty. The programm will assign the first qlmid as value.");
						}
						String constant = qlmObject.getId().get(0).getValue();
						if ((constant != null) && (constant.length() > 0)){
							allConstantValues.put(property, constant);
						}
					}
					for (InfoItemType info: qlmObject.getInfoItem()){
					String nameOfPorpertzy = info.getName();
					if ((nameOfPorpertzy != null) && (nameOfPorpertzy.length() > 0)){
						if (!nameOfPorpertzy.contains("#")){
							nameOfPorpertzy = containedNamespaces.get(0) + "#"+ nameOfPorpertzy;
						}
						allProperties.add(nameOfPorpertzy);
						if ((info.getValue() != null) && (info.getValue().size() > 0)){
							if (info.getValue().size() > 1){
								Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING, "There is more than one value for a porperty. The programm will assign the first value as constant value in SPARQL.");
							}
						String value = info.getValue().get(0).getValue();
						allConstantValues.put(nameOfPorpertzy, value);
						}
					}
					}
					
				}
				QueryGenerator generator = new QueryGenerator(allProperties, allClassNames, allConstantValues, mediator);
				sPARQLQuery = generator.generateQuery();
			}
			else{
				Logger.getAnonymousLogger().log(Level.SEVERE, "I cannot generate the Sparql string, because null");
				
			}
	}
	
}
