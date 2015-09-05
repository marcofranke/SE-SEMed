package de.biba.mediator.webservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;

import de.biba.informationrequestservice.datatypes.*;

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

public class LinkGenerator {
	
	private Service service;

	public LinkGenerator(Service service) {
		super();
		this.service = service;
	}
	
	
	private Properties loadPropertioes (String file){
		Properties properties = new Properties();
		try{
		File f = new File(file);
		if (f.exists() == false) {
			InputStream kk = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(file);
			properties.load(kk);
		} else {
			FileReader fr = new FileReader(file);
			properties.load(fr);
			fr.close();
		}
		}
		catch(IOException e){
			System.err.println (e);
		}
		return properties;
	}
	
	static String file2String (String fName){

		StringBuilder sB = new StringBuilder();
		BufferedReader br = null;
		String sep = System.getProperty("line.separator");
		
		try {
			br = new BufferedReader(new FileReader(new File(fName)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if(sB.length() != 0) sB.append(sep);
				sB.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sB.toString();
	}

	
	public String generateLink(){
		WrapperMessage wrapMessage = new WrapperMessage();
		wrapMessage.setNamespace(service.getNamespace());
		String query = service.getQuery();
		wrapMessage.setQuery(query);
		List<WrapperProperty> pList = new ArrayList<WrapperProperty>();
		for (int i =0; i < service.getDataSource().size(); i++){
		Properties prop = loadPropertioes(service.getDataSource().get(i).getPathToConfigFile());
		if (service.getDataSource().get(i).getJavaClassForWrapper().equals("de.biba.wrapper.wrapper.newSQLWrapper.SQLWrapper")){
			WrapperSQLProperty prop1 = new WrapperSQLProperty();
			prop1.setWrapperID(1);
			prop1.setPropertyFile(file2String(service.getDataSource().get(i).getPathToConfigFile()));
			
			prop1.setMappingFile(file2String(prop.getProperty("MappingFile")));
			prop1.setMappingSchema(file2String(prop.getProperty("MappingSchema")));
			prop1.setOntologyFile(file2String(prop.getProperty("OntologyFile")));
			pList.add(prop1);
		}
		
		
		}
		
		wrapMessage.setPropList(pList);
		String gsonString = new Gson().toJson(wrapMessage);
		return gsonString;
	}
	
	

}
