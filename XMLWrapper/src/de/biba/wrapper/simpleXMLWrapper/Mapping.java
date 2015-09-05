package de.biba.wrapper.simpleXMLWrapper;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.DataSourceQuery.PropertyWithValue;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.BooleanDatatype;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.DatatypePropertyMapping;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.ObjectPropertyMapping;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.XMLTagExctrationInformation;

/**[XML Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
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

public class Mapping {
	

	
	Map<String, ArrayList<de.biba.wrapper.simpleXMLWrapper.dataTypes.DatatypePropertyMapping>> dm; //Das muss ich austauschen durch eine Liste!!!
	Map<String, ArrayList<de.biba.wrapper.simpleXMLWrapper.dataTypes.ObjectPropertyMapping>> om;
	Map<String, OntClass> myClasses = new HashMap<String, OntClass>();
	Map<String, DatatypePropertyCreator> myClassesInverseFunctional = new HashMap<String,  DatatypePropertyCreator>();
	Map<String, ClassMapping> cm;
	String namespace;
	public String namespace2 = "";
	public de.biba.wrapper.simpleXMLWrapper.dataTypes.Mapping m;
	
	
	
	

	public Mapping() {
		// TODO Auto-generated constructor stub
		dm = new HashMap<String, ArrayList<DatatypePropertyMapping>>();
		om = new HashMap<String, ArrayList<ObjectPropertyMapping>>();
		cm = new HashMap<String, ClassMapping>();
	}


	private int searchID (String className){
		className = className.substring(className.indexOf('#')+1);
		for (de.biba.wrapper.simpleXMLWrapper.dataTypes.ClassMapping mapping: m.getClassMapping()){
			if (mapping.getClassName().equals(className)){
				return mapping.getID();
			}
		}
		return -1;
	}

	
	public List<XMLTagExctrationInformation> collectAllTagPathes( DataSourceQuery dsg,Collection<String> collection){
		ArrayList<XMLTagExctrationInformation> result = new ArrayList<XMLTagExctrationInformation>();
		
		for (String str : dsg.getClassNames()){
			
			for (de.biba.wrapper.simpleXMLWrapper.dataTypes.ClassMapping classMapping: m.getClassMapping()){
				if (classMapping.getClassName().equals(str)){
					if (result.contains(classMapping.getXMLTag())==false){
						XMLTagExctrationInformation infos = new XMLTagExctrationInformation();
						infos.setTag(classMapping.getXMLTag());
						result.add(infos);
					}
				}
			}
			
		}
		
		
		for (String str : dsg.getPropertyNames()){
		
			for (de.biba.wrapper.simpleXMLWrapper.dataTypes.DatatypePropertyMapping classMapping: m.getDatatypePropertyMapping()){
				
				if (classMapping.getPropertyName().equals(str)){
					if (result.contains(classMapping.getXMLTag())==false){
						XMLTagExctrationInformation infos = new XMLTagExctrationInformation();
						infos.setTag(classMapping.getXMLTag());
						infos.setAttribute(classMapping.getAttributeName());
						String name = classMapping.getPropertyName();
						if(classMapping.getTokenExtraction() != null){
							infos.setExtractor(classMapping.getTokenExtraction());
						}
						if (classMapping.getReplacements() != null){
							infos.setReplacements(classMapping.getReplacements());
						}
						infos.setPropertyName(name);
						//Check whether the property is a inversal functional property
						
						if (collection.contains(classMapping.getPropertyName())){
							infos.setInversalFunctional(true);
						}
						result.add(infos);
					}
					else{
						Logger.getAnonymousLogger().log(Level.INFO, "Same tag: " + classMapping.getXMLTag());
					}
				}
			}
			for (de.biba.wrapper.simpleXMLWrapper.dataTypes.ObjectPropertyMapping classMapping: m.getObjectPropertyMapping()){
				if (classMapping.getPropertyName().equals(str)){
					if (result.contains(classMapping.getReferenceXMLTag())==false){
						XMLTagExctrationInformation infos = new XMLTagExctrationInformation();
						infos.setTag(classMapping.getReferenceXMLTag());
						result.add(infos);
					}
				}
				else{
					Logger.getAnonymousLogger().log(Level.INFO, "Same tag: " + classMapping.getReferenceXMLTag());
				}
			}
		}
		
		for (PropertyWithValue property : dsg.getPropertiesWithValues()){
			
			String str = property.getPropertyName();
			for (de.biba.wrapper.simpleXMLWrapper.dataTypes.DatatypePropertyMapping classMapping:m.getDatatypePropertyMapping()){
				if (classMapping.getPropertyName().equals(str)){
					if (result.contains(classMapping.getXMLTag())==false){
						XMLTagExctrationInformation infos = new XMLTagExctrationInformation();
						infos.setTag(classMapping.getXMLTag());
						infos.setAttribute(classMapping.getAttributeName());
						String name = classMapping.getPropertyName();
						if(classMapping.getTokenExtraction() != null){
							infos.setExtractor(classMapping.getTokenExtraction());
						}
						if (classMapping.getReplacements() != null){
							infos.setReplacements(classMapping.getReplacements());
						}
						infos.setPropertyName(name);
						result.add(infos);
					}
					else{
						Logger.getAnonymousLogger().log(Level.INFO, "Same tag: " + classMapping.getXMLTag());
					}
				}
			}
			for (de.biba.wrapper.simpleXMLWrapper.dataTypes.ObjectPropertyMapping classMapping: m.getObjectPropertyMapping()){
				if (classMapping.getPropertyName().equals(str)){
					if (result.contains(classMapping.getReferenceXMLTag())==false){
						XMLTagExctrationInformation infos = new XMLTagExctrationInformation();
						infos.setTag(classMapping.getReferenceXMLTag());
						result.add(infos);
					}
				}
			}
		}
		
		return result;
	}
	
	
	public Collection<IndidividualCreator> generateDataCreators(DataSourceQuery pDsc, OntModel model, OntModel baseModel) {
		Map<Integer, IndidividualCreator> startCreators = new HashMap<Integer, IndidividualCreator>();
		for(String className:pDsc.getClassNames()){
			ClassMapping c = cm.get(className);
			if (c==null){
				continue;
			}
			IndidividualCreator creator = startCreators.get(c.id);
			if(creator==null){
				if(c.comparator==null){
					creator = new IndidividualCreator();
				}
				else{
					ConditionalIndividualCreator cid = new ConditionalIndividualCreator();
					cid.column=c.conditionTag;
					cid.comparator = c.comparator;
					cid.value = c.value;
				}
				creator.oc = model.createOntClass(namespace, c.className);
				myClasses.put(c.className,creator.oc);
				creator.setColumn(c.XMLTag);
				startCreators.put(c.id, creator);
			}
			else{
				//TODO: Strategie einfallen lassen
			}
		}
		for(String prop : pDsc.getPropertyNames()){
			if(om.containsKey(prop)){
				for (ObjectPropertyMapping opm: om.get(prop))
				{
					
				IndidividualCreator ob = startCreators.get(opm.getObjectID());
				if(ob == null){
					ob = new IndidividualCreator();
					ob.oc = model.createOntClass(namespace, opm.getObClass());
					myClasses.put(opm.getObClass(),ob.oc);
				}
				
				
				IndidividualCreator sub = startCreators.get(opm.getSubjectID());
				if(sub==null){
					sub = new IndidividualCreator();
					sub.oc = model.createOntClass(namespace, opm.getSubClass());
					startCreators.put(opm.getSubjectID(), sub);
				}
				ObjectPropertyCreator opc = new ObjectPropertyCreator();
				opc.op = model.createObjectProperty("", opm.getPropertyName());
				ArrayList<String> allRanges = new ArrayList<String>();
				String name = opm.getObClass();
				allRanges.add(name);
				Set<String> mySet = new HashSet<String>(allRanges);
				opc.op.setRange(mySet);
				opc.ic = ob;
				sub.objectProperties.add(opc);
				opc.column = String.valueOf(opm.getReferenceXMLTag());
				}
			}
			else if(dm.containsKey(prop)){
				ArrayList<DatatypePropertyMapping> allDpms = dm.get(prop);
				for(DatatypePropertyMapping dpm:allDpms){
				
				IndidividualCreator sub = startCreators.get(searchID(dpm.getSubClass()));
				if(sub==null){
					sub = new IndidividualCreator();
					sub.oc = model.createOntClass(namespace, dpm.getSubClass());
					startCreators.put(searchID(dpm.getSubClass()), sub);
					startCreators.put(searchID(dpm.getSubClass()), sub);
				}
				DatatypePropertyCreator dpc = new DatatypePropertyCreator();
				
				dpc.column =dpm.getXMLTag();
				DatatypeProperty propD = baseModel.getDatatypeProperty( dpm.getPropertyName());
				dpc.p = propD;
				if (dpc.isPropertyInverseFunctional()){
					System.out.println("Habe ein Dataproperty gefunden, welches invers funktional ist");
					String name = sub.oc.getUri();
					myClassesInverseFunctional.put(name, dpc);
				}
				
				sub.datatypeProperties.add(dpc);
			}
			}
			
		}
		for(PropertyWithValue pwv:pDsc.getPropertiesWithValues()){
			String name = pwv.getPropertyName();
			ArrayList<DatatypePropertyMapping> allDpms = dm.get(name);
			for(DatatypePropertyMapping dpm:allDpms){
			IndidividualCreator sub = startCreators.get(searchID(dpm.getSubClass()));
			if(sub==null){
				sub = new IndidividualCreator();
				sub.oc = model.createOntClass(namespace, dpm.getSubClass());
				startCreators.put(searchID(dpm.getSubClass()), sub);
			}
			ConditionalDatatypePropertyCreator cdpc = new ConditionalDatatypePropertyCreator();
			cdpc.value = pwv.getValue();
			cdpc.comparator = pwv.getComparator();
			cdpc.column = dpm.getXMLTag();
			cdpc.p = model.createDatatypeProperty(namespace, dpm.getPropertyName());
			sub.datatypeProperties.add(cdpc);
			}
		}
		return startCreators.values();
	}
	
	private boolean validateMappingFile(String pSchema, String pMapping){
		try {
			
			FileReader schemaReader = new FileReader(pSchema);
			FileReader mappingReader = new FileReader(pMapping);
			Validator validator = SchemaFactory.newInstance(
			    XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
			    new StreamSource(schemaReader)).newValidator();
			validator.validate(new StreamSource(mappingReader));
			schemaReader.close();
			mappingReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	public boolean loadMapping(String pSchema, String pMapping){
		if(pSchema == null || pMapping==null)
			return false;
		if(!validateMappingFile(pSchema, pMapping))
			return false;
		try{
			 InputStream is = new FileInputStream( pMapping );
		       JAXBContext jc = JAXBContext.newInstance( "de.biba.wrapper.simpleXMLWrapper.dataTypes" );
		       Unmarshaller u = jc.createUnmarshaller();
		       Object o = u.unmarshal( is );
		       m = (de.biba.wrapper.simpleXMLWrapper.dataTypes.Mapping) o;
		     
		       String namespace = m.getNamespace()+"#";
		       namespace2 = namespace;
		       for ( DatatypePropertyMapping dMapping : m.getDatatypePropertyMapping()){
		    	  dMapping.setPropertyName(namespace +dMapping.getPropertyName()); 
		    	  dMapping.setSubClass(namespace +dMapping.getSubClass()); 
		    	  dMapping.setUnit(namespace +dMapping.getUnit()); 
		    	  if (dm.containsKey(dMapping.getPropertyName())==false){
		    		  ArrayList<DatatypePropertyMapping> newMappingList = new ArrayList<DatatypePropertyMapping>();
		    		  newMappingList.add(dMapping);
		    		  dm.put(dMapping.getPropertyName(), newMappingList);
		    	  }
		    	  else{
		    		  ArrayList<DatatypePropertyMapping> actualMapping = dm.get(dMapping.getPropertyName());
		    		  actualMapping.add(dMapping);
		    	  }
		    	 
		      }
		      for ( ObjectPropertyMapping oMapping : m.getObjectPropertyMapping()){
		    	  oMapping.setPropertyName(namespace +oMapping.getPropertyName()); 
		    	  oMapping.setSubClass(namespace +oMapping.getSubClass()); 
		    	  oMapping.setObClass(namespace +oMapping.getObClass()); 
		    	 
		    	
		    	  
		    	  if (om.containsKey(oMapping.getPropertyName())==false){
		    		  ArrayList<ObjectPropertyMapping> newMappingList = new ArrayList<ObjectPropertyMapping>();
		    		  newMappingList.add(oMapping);
		    		  om.put(oMapping.getPropertyName(), newMappingList);
		    	  }
		    	  else{
		    		  om.get(oMapping.getPropertyName()).add(oMapping);
		    	  }
		    	  
		      }
		       
		      for (de.biba.wrapper.simpleXMLWrapper.dataTypes.ClassMapping mapping :m.getClassMapping()){
		    	  
		    	 ClassMapping resultMapping  = new ClassMapping();
		    	 resultMapping.className = namespace+ mapping.getClassName();
		    	 resultMapping.id = mapping.getID();
		    	if (mapping.getCondition() != null) {
		    	 resultMapping.conditionTag = mapping.getXMLTag();
		    	 String type = mapping.getCondition().getValueType();
		    	 resultMapping.comparator = createComparator(mapping.getCondition().getOperator());
		    	 String value = mapping.getCondition().getValue();
		    	 if(type.equals("string")){
						resultMapping.value = new StringDatatype(value);}
					else if(type.equals("numeric"))
						resultMapping.value = new NumericDatatype(Double.parseDouble(value));
					else if(type.equals("boolean")){
					resultMapping.value = new BooleanDatatype(Boolean.parseBoolean(value));
					}
		
		    			 
		    			 
		    	}
		    	this.cm.put(resultMapping.className, resultMapping);
		      }  
		       
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return false;
	}



	private Comparator<Datatype> createComparator(String pCmp) {
		if(">=".equals(pCmp)){
			return new Comparator<Datatype>(){
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					int cmp = pO1.compareTo(pO2);
					if(cmp>=0)
						return 0;
					else
						return -1;
				}		
			};
		}
		else if(">".equals(pCmp)){
			return new Comparator<Datatype>(){
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					int cmp = pO1.compareTo(pO2);
					if(cmp>0)
						return 0;
					else
						return -1;
				}		
			};
		}
		else if("==".equals(pCmp)){
			return new Comparator<Datatype>(){
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					int cmp = pO1.compareTo(pO2);
					return cmp;
				}		
			};
		}
		else if("<=".equals(pCmp)){
			return new Comparator<Datatype>(){
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					int cmp = pO1.compareTo(pO2);
					if(cmp<=0)
						return 0;
					else
						return -1;
				}		
			};
		}
		else{
			return new Comparator<Datatype>(){
				@Override
				public int compare(Datatype pO1, Datatype pO2) {
					int cmp = pO1.compareTo(pO2);
					if(cmp<0)
						return 0;
					else
						return -1;
				}		
			};
		}
	}	
}
