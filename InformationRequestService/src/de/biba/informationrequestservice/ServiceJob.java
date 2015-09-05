package de.biba.informationrequestservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.biba.informationrequestservice.writer.IDataWriteInterface;
import de.biba.mediator.IMediator;
import de.biba.mediator.IQuerySolution;
import de.biba.mediator.QuerySolution;
import de.biba.mediator.webservice.LinkGenerator;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.simpleXMLWrapper.StringReader;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.XMLTagExctrationInformation;

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

/**
 * This class implements the functionality to resolve a query against a
 * preinitialized mediator. The mediator and additional information like the
 * query are held in the @link InformationServiveManager
 * 
 * @author Marco
 * 
 */
public class ServiceJob implements Job {

	public DefaultHttpClient httpclient;
	HttpGet httpGet;
	private boolean hasBeenFinished = false;

	public boolean isHasBeenFinished() {
		return hasBeenFinished;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		int serviceIndex = dataMap.getInt("ServiceIndex");
		try {
			InformationServiceManager.getInstance()
					.getWhichServicesAreClosable().set(serviceIndex, false);
		} catch (Exception e2) {
			e2.printStackTrace();
			Logger.getAnonymousLogger().log(Level.SEVERE,
					e2.getLocalizedMessage());
		}
		boolean isLocal = dataMap.getBoolean("isLocal");
		try {
			if ((InformationServiceManager.getInstance().getAllServices()
					.get(serviceIndex).getURLWebService() != null)
					&& (InformationServiceManager.getInstance()
							.getAllServices().get(serviceIndex)
							.getURLWebService().length() > 1)) {
				isLocal = false;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Logger.getAnonymousLogger().log(Level.SEVERE,
					"Execute Job: " + e1.getMessage());
		}

		IQuerySolution solution = null;
		if (isLocal) {
			System.out.println("Anfrage wird lokal aufgelöst: ");
			try {
				IMediator mediator = InformationServiceManager.getInstance()
						.getAllMediators().get(serviceIndex);
				String query = InformationServiceManager.getInstance()
						.getAllServices().get(serviceIndex).getQuery();
				String namespace = InformationServiceManager.getInstance()
						.getAllServices().get(serviceIndex).getNamespace();
				String base = "BASE <" + namespace + "> ";
				String finalQuery = base + query;
				solution = mediator.query(finalQuery, true, false);

			} catch (Exception e) {
				e.printStackTrace();
				new JobExecutionException(e.getMessage());
			}
		} else {
			System.out.println("Anfrage wird remote aufgelöst: ");
			try {
				String link = dataMap.getString("WebserviceLink");
				if ((link == null) || (link.length() < 2)) {
					link = InformationServiceManager.getInstance()
							.getAllServices().get(serviceIndex)
							.getURLWebService();
				}
				System.out.println(generateLink2(link, serviceIndex));
				solution = (getResultFromWebservice(
						generateLink2(link, serviceIndex), link));

			} catch (Exception e) {
				e.printStackTrace();
				throw (new JobExecutionException(e.getMessage()));
			}

		}
		Class<?> c = null;

		try {
			if (solution != null) {
				String className = InformationServiceManager.getInstance()
						.getAllServices().get(serviceIndex).getOutputWrapper()
						.get(0).getJavaClassForStorage();
				c = Class.forName(className);
				Object o = c.newInstance();
				IDataWriteInterface writer = (IDataWriteInterface) o;
				writer.writeData(solution, InformationServiceManager
						.getInstance().getAllServices().get(serviceIndex)
						.getOutputWrapper().get(0), InformationServiceManager
						.getInstance().getAllServices().get(serviceIndex)
						.getQuery(), InformationServiceManager.getInstance()
						.getAllServices().get(serviceIndex).getNamespace()); // Null
																				// wird
																				// noch
																				// ersetzt....
			}
			Logger.getAnonymousLogger().log(
					Level.INFO,
					InformationServiceManager.getInstance().getAllServices()
							.get(serviceIndex).getId()
							+ ": has finished. Detetcted no failures.");
		} catch (Exception e) {
			e.printStackTrace();
			throw (new JobExecutionException(e.getMessage()));
		}

		try {
			if (InformationServiceManager.getInstance().getAllServices()
					.get(serviceIndex).getRequestInterval() == 0) {
				InformationServiceManager.getInstance().stopOneService(
						serviceIndex);
				this.hasBeenFinished = true;
				InformationServiceManager.getInstance()
						.getWhichServicesAreClosable().set(serviceIndex, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	
	private String generateLink2(String baseLink, int serviceIndex)
			throws Exception {

		return (new LinkGenerator(InformationServiceManager.getInstance()
				.getAllServices().get(serviceIndex))).generateLink();

	}

	@SuppressWarnings({ "deprecation", "unused" })
	private String generateLink(String baseLink, int serviceIndex)
			throws Exception {

		String query = InformationServiceManager.getInstance().getAllServices()
				.get(serviceIndex).getQuery();
		String namespace = InformationServiceManager.getInstance()
				.getAllServices().get(serviceIndex).getNamespace();
		baseLink = baseLink + "?method=query&jObject=";
		baseLink = "";
		String result = "<![CDATA[{";

		// namespace adding
		result += "\"namespace\":\"" + namespace + "\",";
		query = query.replace("\"", "\\\"");
		result += "\"query\":\"" + query + "\",";
		
		result += "}";

		System.out.println(result);

		result = URLEncoder.encode(result);
		result = baseLink + result;
		System.out.println(result);
		return result;
	}

	
	private QuerySolution transformXMLString(String input) {
		StringReader reader = new StringReader();
		ArrayList<String> haeader = new ArrayList<String>();
		ArrayList<String> content = new ArrayList<String>();
		XMLTagExctrationInformation info1 = new XMLTagExctrationInformation();
		info1.setTag("Envelopee/Body/queryResponse/queryReturn/columnNames/columnNames");
		info1.setPropertyName("");
		ArrayList<XMLTagExctrationInformation> header = new ArrayList<XMLTagExctrationInformation>();
		header.add(info1);
		reader.setText(input, header);
		try {
			while (reader.next()) {

				haeader.add(reader.getValue("", info1.getTag()).toString());
			}
			reader.reset();
			info1.setTag("Envelopee/Body/queryResponse/queryReturn/data/data");

			reader.setText(input, header);
			while (reader.next()) {
				content.add(reader.getValue("", info1.getTag()).toString());
			}

			QuerySolution result = new QuerySolution();

			for (String column : haeader) {
				result.addColumn(column);
			}
			//The haeder is set, Now, it can be used
			ArrayList<Datatype> actualRow = new ArrayList<Datatype>();
			for (int i = 0; i < content.size(); i++) {
				if ((i > 0) && (i % haeader.size() == 0)) {
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

	private QuerySolution getResultFromWebservice(String link,
			String linkToWebservice) throws Exception {

		try {
			String s = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.mediator.biba.de\">   <soapenv:Header/>	   <soapenv:Body>		      <web:query>		         <web:jObject><![CDATA["
					+ link
					+ "]]></web:jObject>  </web:query>	   </soapenv:Body>	</soapenv:Envelope>";
			DefaultHttpClient httpclient = new DefaultHttpClient();
			if (linkToWebservice.contains("?")) {
				int index = linkToWebservice.indexOf("?");
				linkToWebservice = linkToWebservice.substring(0, index);
			}
			HttpPost post = new HttpPost(linkToWebservice);
			StringEntity userEntity = new StringEntity(s);
			post.setEntity(userEntity);
			post.setHeader("Accept", "text/xml, multipart/related");
			post.setHeader("Content-Type", "text/xml; charset=utf-8");
			post.setHeader("SOAPAction", "query");
			String result = "";
			HttpResponse response = httpclient.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result += (line);
			}

			return transformXMLString(result);

		} catch (MalformedURLException e) {
			throw e;

		}
	}
}
