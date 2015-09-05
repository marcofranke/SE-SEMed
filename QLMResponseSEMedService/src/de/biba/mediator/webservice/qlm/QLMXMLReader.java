package de.biba.mediator.webservice.qlm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;













import de.biba.mediator.Datatypes;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.Individual;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.BooleanDatatype;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.DateValue;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;

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

/**
 * This class searches XML tags in a XML String and resturns its values. The iteration through all XML Tags is resolved by the method next. After each 'next' you can get the value of the current XML Tag through the method getValue. 
 * @author Marco Franke
 *
 */

public class QLMXMLReader implements IReader{
	private String text;
	private boolean heading;
	private char delimiter;
	private HashMap<String, Individual> allIndividuals= new HashMap<String, Individual>();
	private ArrayList<String> texts = new ArrayList<String>();
	private Datatypes valueTypes[];
	//############XML####################
	private SAXBuilder builder = new SAXBuilder();
	private Document doc = null;
	//####################Entitys##########
	private HashMap<String, ArrayList<String>> allTagsValues= new HashMap<String, ArrayList<String>>();
	private int max =0;
	private OntModel result = null ;
	private OntModel baseModel = null;
	private int index =0;
	private String namespace = "";
	
	public QLMXMLReader(OntModel ont, OntModel base){
		result = ont;
		this.baseModel = base;
		for (OntClass cl :  baseModel.getTbox().getSimpleClasses().values()){
			String str = cl.getUri();
			if (str.contains("#") && !(str.toLowerCase().contains("thing"))){
				
				str = str.substring(0, str.indexOf('#'));
				namespace = str;
				break;
			}
		}
	}
	
	
	@Override
	public void open(){
		//reader.close();
		
	}
	
	/**
	 * This method returns the the  orginial xml string
	 * @return xml string
	 */
	public String getText() {
		return text;
	}
	
	
	private String extractID(Element e){
		if (e.getChildren().size() == 0){
			return e.getValue();
		}
		else{
			Logger.getAnonymousLogger().log(Level.SEVERE, "Not expected additional sibtype of tag id of ObjectTyp");
			return "";
		}
	}
	
	private void extractInformationForAIndi(Element e){
		//das sollte ein Object sein vom Type ObjectType
		String conceptName = "";
		for (Attribute attribute : e.getAttributes()){
		
			if (attribute.getName().equals("type")){
			conceptName = attribute.getValue();
			Logger.getAnonymousLogger().log(Level.INFO,"Type of Object: " +attribute.getName());
			}
		}
		if ((conceptName == null) || (conceptName.length() < 2)){
			Logger.getAnonymousLogger().log(Level.SEVERE, "There is no type information contained. Without type information we can't choose the type of the indiviual!!!. Skip to next object in xml");
			return;
		}
		String id = "";
		Individual indi = null;
		for (Element sub: e.getChildren()){
			if (sub.getName().equals("id")){
			id = extractID(sub);	
			if ((id == null) || (id.length() < 2 )){
				id = String.valueOf( new Date().getTime());
			}
			//Testen, ob es das Indiviual schon gibt
			
			if (allIndividuals.containsKey(id)){
				indi = allIndividuals.get(id);
			}
			else{
				if ((conceptName != null ) &&( conceptName.length() > 1)){
				String conceptFullName = namespace+"#"+ conceptName;
				OntClass oc = baseModel.getTbox().getSimpleClasses().get(conceptFullName);
				if (oc == null){
					oc = baseModel.getTbox().getComplexClasses().get(conceptFullName);
				}
				if (oc == null){
					Logger.getAnonymousLogger().log(Level.SEVERE, "There type does not refer to any concept in the ontology. Without correct type information we can't choose the type of the indiviual!!!. Skip to next object in xml");
					return;
				}
				 indi = result.createIndividual(oc);
				allIndividuals.put(id, indi);
				}
				else{
					Logger.getAnonymousLogger().log(Level.SEVERE, "There is no type information in the object of the received qlm message");
				}
			}
			}
		
			if (sub.getName().equals("InfoItem")){
				//Jetzt kommen zusätzliche Properties
				String datapropertyName = sub.getAttribute("name").getValue();
				for (Element subsub : sub.getChildren()){
					if (subsub.getName().equals("value")){
						String value = subsub.getValue();
						String fullNameOfDataTypePorperty = "";
						if (!datapropertyName.contains("#")){
						 fullNameOfDataTypePorperty = namespace +"#"+ datapropertyName;
						}
						else{
							fullNameOfDataTypePorperty = datapropertyName;
						}
						DatatypeProperty p = baseModel.getDatatypeProperty(fullNameOfDataTypePorperty);
						Logger.getAnonymousLogger().log(Level.SEVERE, "Found no: " + fullNameOfDataTypePorperty + " in the current ontology");
						result.addProperty(indi, p, new StringDatatype(value));
					}
				}
			}
			
			
		}
	}
	
	
	private void traverseXML(){
		ArrayList<Element> actualElemes = new ArrayList<Element>();
		ArrayList<Element> actualElemesNew = new ArrayList<Element>();
		//Root Element als StartKnoten eintragen
		Element root = doc.getRootElement();
		actualElemes.add(root);
		
		while (actualElemes.size()> 0){
			actualElemesNew.clear();
			for (Element element: actualElemes){
				 
				if  ((element.getName().toLowerCase().equals("object"))){
										
					
					this.extractInformationForAIndi(element);
		}
		
				
				
				
				actualElemesNew.addAll(element.getChildren());
			}
			actualElemes.clear();
			actualElemes.addAll(actualElemesNew);
	}
	}
	
	
//	private int calculateDeptAndInit(){
//		String path = "Objects/Object";
//		int result =0;
//		String[] token = path.split("/");
//		ArrayList<Element> actualElemes = new ArrayList<Element>();
//		Element root = doc.getRootElement();
//		actualElemes.add(root);
//		
//		for (int i =1; i < token.length; i++ ){
//			ArrayList<Element> zwi = new ArrayList<Element>();
//			for (Element e: actualElemes){
//				System.out.println(e.getName());
//				if (e.getName().equals("Object")){
//this.extractInformationForAIndi(e);
//				}
//				boolean found = false;
//				for (Element sub:e.getChildren()){
//					
//					String n = sub.getName();
//					String str2 = token[i].substring(token[i].indexOf(':')+1);
//					if (n.equals(str2)){
//						zwi.add(sub);
//						found = true;
//					}
//				}
//				if (found == false){
//				if (e.getChildren(token[i]).size() > 0){
//			zwi.addAll(e.getChildren(token[i]));
//				}
//				else{if (token[i].indexOf(':') >-1){
//					String str = token[i].substring(token[i].indexOf(':')+1);
//					if ( e.getChildren(str).size()> 0){
//						zwi.addAll(e.getChildren(str));
//					}
//					else{
//						//Stufe 3. Sollte nie eintreten
//						for (Element es: e.getChildren()){
//							if (es.getName().equals(str)){
//								zwi.add(es);
//							}
//						}
//					}
//					}
//				}
//				}
//				
//			}
//			actualElemes.clear();
//			actualElemes.addAll(zwi);
//			if (i == token.length-2){
//				result = zwi.size();
//				ArrayList<String> r = new ArrayList<String>();
//				for (Element t: zwi){
//					
//					ArrayList<Element> allElemens = new ArrayList<Element>();
//					for (Element sub:t.getChildren()){
//						
//						String n = sub.getName();
//						String str2 = token[token.length-1].substring(token[token.length-1].indexOf(':')+1);
//						if (n.equals(str2)){
//							allElemens.add(sub);
//						}
//							
//						}
//					if ((allElemens != null) && (allElemens.size())> 0){
//					for (Element targetE: allElemens){
//						String value = targetE.getValue();
//						r.add(value);
//					}
//					
//					}
//					else{
//						String str = token[token.length-1].substring(token[token.length-1].indexOf(':')+1);
//						for (Element es: t.getChildren()){
//							if (es.getName().equals(str)){
//								String value = es.getValue();
//								r.add(value);
//							}
//						}
//					}
//				}
//				
//				
//				if ((r == null) || (r.size() ==0)){
//					System.err.println("Found no data for: " + path);
//				}
//				else{
//					result = r.size();
//				this.allTagsValues.put(path, r);
//				}
//			}
//		}
//		
//		return result;
//	}
//	
	public void setText(String text) {
		index=0;
		this.text = text;
		texts.clear();
		 try {
			 java.io.StringReader input = new java.io.StringReader(text);
			doc = builder.build(input);
			
			traverseXML(); //This methods runs through the xml and generate individual
			System.out.println("Result of XML: " + result.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#next()
	 */
	@Override
	public boolean next() throws IOException{
		index++;
		if (index <= max){
		return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#getString(int)
	 */
	@Override
	public String getString(int pPos) throws IOException{
		return "-1";
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#reset()
	 */
	@Override
	public void reset(){
		allTagsValues.clear();
		this.index =0;
		max =0;
		doc = null;
	}
	
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#getDouble(int)
	 */
	@Override
	public double getDouble(int pPos) throws IOException, ParseException {
		NumberFormat fmt = NumberFormat.getInstance();
		Number number = null; //fmt.parse(reader.get(pPos));
		return number.doubleValue();
		

	}
	
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#getDate(int)
	 */
	@Override
	public Date getDate(int pPos) throws ParseException, IOException{
		return null;//dateFormat.parse(reader.get(pPos));
	}
	
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#getInt(int)
	 */
	@Override
	public int getInt(int pPos) throws NumberFormatException, IOException{
		return 0;// Integer.parseInt(reader.get(pPos));
	}
	
	private NumericDatatype getNumericValue(String pPos) throws IOException, ParseException{
		String tmp = null;//reader.get(pPos);
		if(tmp.isEmpty())
			return null;
		else
			return null; //new NumericDatatype(getDouble(pPos));
	}
	
	private StringDatatype getStringValue(String tag) throws IOException{
		if (this.allTagsValues.containsKey(tag)){
		List<String> result = this.allTagsValues.get(tag);
		String r = result.get(index%result.size());
	
			return new StringDatatype(r);
		}
		else{
			return new StringDatatype("");
		}
	}
	
	

	
	/**
	 * This method returns the value of a XML tag at a specific position. 
	 * The specific position of the tag in the xml document is managed by the @link{next} method.
	 *  The input is the pasth of the tag. For example:  root/XMLTag/SubXMLTag
	 */
	
	@Override
	public Datatype getValue(String pos) throws ParseException{
		try{
			Datatypes type = Datatypes.STRING;
			switch(type){
			case NUMERIC:
				return getNumericValue(pos);
			case STRING:
				return getStringValue(pos);
			case DATE:
				return getStringValue(pos);
			case BOOLEAN:
				return getBooleanValue(pos);
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			return null;
		} catch (IOException e) {
			return null;
		}
		catch(NullPointerException e){
			return null;
	}
	
		return null;
	}
	
	
	private BooleanDatatype getBooleanValue(String pPos) throws IOException {
		String tmp = null; //reader.get(pPos);
		if(tmp.isEmpty())
			return null;
		else
			return null; //new BooleanDatatype(getBoolean(pPos));
	}

	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int pPos) throws IOException {
		return false; //Boolean.parseBoolean(reader.get(pPos));
	}

	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#loadDatatypeDefinition(java.lang.String)
	 */
	@Override
	public void loadDatatypeDefinition(String pFileName){
		try {
			FileReader fr = new FileReader(pFileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			String token[] = line.split(";");
			valueTypes = new Datatypes[token.length];
			for(int i=0; i<token.length;i++){
				String s = token[i];
				boolean isAssigned = false;
				if(s.equals("Numeric")){
					valueTypes[i] = Datatypes.NUMERIC;
					isAssigned = true;
				}
				else if(s.equals("Date")){
					valueTypes[i] = Datatypes.DATE;
					isAssigned = true;
				}
				else if(s.equals("String")){
					valueTypes[i] = Datatypes.STRING;
					isAssigned = true;
				}
				else if(s.equals("Boolean")){
					valueTypes[i] = Datatypes.BOOLEAN;
					isAssigned = true;
				}
				
				if (!isAssigned){
					//Der Benutzer hat einen falschen Datentyp verwendet
					System.err.println("Your data type: '" +s +"' is not supported. The set contains: {Numeric, Date, String, Boolean}");
					
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.biba.wrapper.tableWrapper.IReader#setDateFormat(java.text.DateFormat)
	 */
	@Override
	public void setDateFormat(DateFormat pDateFormat){
		//dateFormat = pDateFormat;
	}
	
	public static void main(String[] args){
		IReader reader = null;
		try {
			reader = new QLMXMLReader(null, null);
			reader.setDateFormat(new SimpleDateFormat("dd-MMM-yy"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			for(int i=0; i<10 && reader.next();i++){
				try {
					//System.out.println(reader.getDate(1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String getColumnName(int pPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	

	//###############
}
