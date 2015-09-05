package de.biba.mediator.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import de.biba.mediator.QuerySolution;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.StringDatatype;
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

public class WebServiceConnection {

public static  QuerySolution getResultFromWebservice(String link, String linkToWebservice) throws Exception{
	
	Logger.getAnonymousLogger().log(Level.INFO,link);
		
		try{
			String s = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.mediator.biba.de\">   <soapenv:Header/>	   <soapenv:Body>		      <web:query>		         <web:jObject><![CDATA["+link+"]]></web:jObject>  </web:query>	   </soapenv:Body>	</soapenv:Envelope>";
					DefaultHttpClient httpclient = new DefaultHttpClient();
					if (linkToWebservice.contains("?")){
						int index = linkToWebservice.indexOf("?");
						linkToWebservice = linkToWebservice.substring(0,index);
					}
					HttpPost post = new HttpPost( linkToWebservice);
					StringEntity userEntity = new StringEntity(s);
					post.setEntity(userEntity);
					 post.setHeader("Accept", "text/xml, multipart/related");
					 post.setHeader("Content-Type", "text/xml; charset=utf-8");
					 post.setHeader("SOAPAction",
					 "query"
					  );
					 String result = "";
					 HttpResponse response = httpclient.execute(post); BufferedReader rd =
							  new BufferedReader(new InputStreamReader(
							  response.getEntity().getContent())); String line = ""; while ((line =
							  rd.readLine()) != null) { result+=(line); }	
			
		
							  return transformXMLString(result);
		
		}
		catch(MalformedURLException e){
			throw e;
			
		}
	}
private static  QuerySolution transformXMLString(String input){
	StringReader reader = new StringReader();
	ArrayList<String> xmlTags1 = new ArrayList<String>();
	ArrayList<String> xmlTags2 = new ArrayList<String>();
	xmlTags1.add ("soapenv:Envelopee/soapenv:Body/queryResponse/queryReturn/columnNames/columnNames");
	xmlTags2.add("soapenv:Envelopee/soapenv:Body/queryResponse/queryReturn/data/data");
	ArrayList<String> haeader = new ArrayList<String>();
	ArrayList<String> content = new ArrayList<String>();
	reader.setText(input, xmlTags1);
	try {
		while (reader.next()) {
			haeader.add(reader.getValue(xmlTags1.get(0)).getString());						
		}
		reader.reset();
		reader.setText(input, xmlTags2);
		while (reader.next()) {
			content.add(reader.getValue(xmlTags2.get(0)).getString());						
		}
		
			QuerySolution  result = new QuerySolution();
			
			for (String column: haeader){
				result.addColumn(column);			
			}
			
			ArrayList<Datatype> actualRow = new ArrayList<Datatype>();
			for (int i =0; i < content.size(); i++){
			if ((i > 0) && (i%haeader.size()==0)){
				result.data.add(actualRow);
				actualRow = new ArrayList<Datatype>();
			}
				StringDatatype t = new StringDatatype(content.get(i));
				actualRow.add(t);
			}
			result.data.add(actualRow);
				
			
			return result;
			
		
	} catch (Exception e) {
		e.printStackTrace();
		return new QuerySolution();
	}
}

	
}
