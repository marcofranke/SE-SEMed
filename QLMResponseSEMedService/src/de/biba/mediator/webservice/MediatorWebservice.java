package de.biba.mediator.webservice;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

//import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.tools.classfile.Annotation.element_value;

import de.biba.mediator.IMediator;
import de.biba.mediator.IQuery;
import de.biba.mediator.IQuerySolution;
import de.biba.mediator.OutputQuery;
import de.biba.mediator.constraints.ClassConstraint;
import de.biba.mediator.constraints.SimpleConstraint;
import de.biba.mediator.webservice.configuration.WrapperConfiguration;
import de.biba.odf.InfoItemType;
import de.biba.odf.ObjectType;
import de.biba.odf.ObjectsType;
import de.biba.odf.QlmID;
import de.biba.odf.ValueType;
import de.biba.ontology.ComplexOntClass;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.TBox;
import de.biba.queryLanguage.ParseException;
import de.biba.queryLanguage.SimpleQueryParser;
import de.biba.wrapper.DataSource;
import de.biba.wrapper.DataSourceV2;
import de.biba.wrapper.IDataSourceDescription;
import de.biba.wrapper.newSQLWrapper.SQLWrapper;

/**
 * [QLMResponseSEMedService. This is a wrapper to tranform a xml based data base
 * schema to an ontology] Copyright (C) [2014 [Marco Franke (BIBA-Bremer
 * Institut f�r Produktion und Logistik GmbH)]
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 **/
public class MediatorWebservice {

	protected IMediator mediator = null;

	final String fSeparator = File.separator;
	final String tempDir = "QueriesTEMP" + fSeparator;
	final String propertyFile = "config.properties";
	final String mappingFile = "mapping.xml";
	final String mappingSchema = "mappingSchema.xsd";
	final String ontologyFile = "Ontology.owl";
	final String csvFile = "csvFile.csv";
	final String datatypeDefinitionFile = "datatypes.txt";
	final String configFolder = "ConfigFolder";
	protected String currentQuery = "";
	private JAXBContext context = null;

	public MediatorWebservice() {

	}

	public String[] getAllConcepts() {
		initiateStandardConfiguration();
		ArrayList<String> result = new ArrayList<String>();
		for (Entry<String, OntClass> element : mediator.getModel().tbox.simpleClasses
				.entrySet()) {
			if (result.contains(element.getValue().getUri()) == false)
				result.add(element.getValue().getUri());
		}
		for (Entry<String, ComplexOntClass> element : mediator.getModel().tbox.complexClasses
				.entrySet()) {
			if (result.contains(element.getValue().getUri()) == false)
				result.add(element.getValue().getUri());
		}
		String[] r = new String[result.size()];
		result.toArray(r);
		return r;
	}

	public String[] getAllDataPropertiesByConceptName(String name) {
		initiateStandardConfiguration();
		ArrayList<String> result = new ArrayList<String>();
		for (Entry<String, DatatypeProperty> lala : mediator.getModel()
				.getTbox().datatypeProperties.entrySet()) {
			String uri = lala.getValue().getUri();
			uri = uri.substring(0, uri.lastIndexOf("#"));
			for (String d : lala.getValue().getDomain()) {

				String toTest = uri + "#" + d;
				System.out.println(toTest);
				if (toTest.equals(name)) {
					result.add(lala.getKey());
				}
			}
		}
		String[] r = new String[result.size()];
		result.toArray(r);
		return r;
	}

	public String[] getAllObjectPropertiesByConceptName(String name) {
		initiateStandardConfiguration();
		ArrayList<String> result = new ArrayList<String>();
		for (Entry<String, ObjectProperty> lala : mediator.getModel().getTbox().objectProperties
				.entrySet()) {
			String uri = lala.getValue().getUri();
			uri = uri.substring(0, uri.lastIndexOf("#"));
			for (String d : lala.getValue().getDomain()) {

				String toTest = uri + "#" + d;
				System.out.println(toTest);
				if (toTest.equals(name)) {
					result.add(lala.getKey());
				}
			}
		}
		String[] r = new String[result.size()];
		result.toArray(r);
		return r;
	}

	public String[] getAllRangesForAObjectProperties(String name) {
		initiateStandardConfiguration();
		for (Entry<String, ObjectProperty> lala : mediator.getModel().getTbox().objectProperties
				.entrySet()) {
			String uri = lala.getValue().getUri();
			if (uri.equals(name)) {
				int size = lala.getValue().getRange().size();
				if (size > 0) {
					String[] ranges = new String[size];
					lala.getValue().getRange().toArray(ranges);
					return ranges;
				}
			}
		}

		return null;
	}

	public LightDataSourceDescription[] getCurrentConnectedDataSources() {
		initiateStandardConfiguration();
		List<IDataSourceDescription> dist = mediator.getLinkedDataSources();
		LightDataSourceDescription[] result = new LightDataSourceDescription[dist
				.size()];
		for (int i = 0; i < dist.size(); i++) {
			IDataSourceDescription des = dist.get(i);
			LightDataSourceDescription description = new LightDataSourceDescription();
			description.setNameOfClass(des.getNameOfClass());
			description.setIsConsistence(String.valueOf(des
					.isConfigurationValid()));
			description.setPathOfPropertyFile(des.getPathOfPropertyFile());
			result[i] = description;

		}
		return result;
	}

	// public DatatypeProperty[] getAllDataPropertiesByName(){
	// initiateStandardConfiguration();
	// ArrayList<DatatypeProperty> result = new ArrayList<DatatypeProperty>();
	// for (DatatypeProperty d:
	// mediator.getModel().tbox.datatypeProperties.values()){
	// result.add(d);
	// }
	// DatatypeProperty[] r = new DatatypeProperty[result.size()];
	// result.toArray(r);
	// return r;
	// }
	//
	// public ObjectProperty[] getAllObjectPropertiesByName(){
	// initiateStandardConfiguration();
	// ArrayList<ObjectProperty> result = new ArrayList<ObjectProperty>();
	// for (ObjectProperty d:
	// mediator.getModel().tbox.objectProperties.values()){
	// result.add(d);
	// }
	// ObjectProperty[] r = new ObjectProperty[result.size()];
	// result.toArray(r);
	// return r;
	// }

	private WrapperConfiguration loadConfigFile() {
		try {
			context = JAXBContext
					.newInstance("de.biba.mediator.webservice.configuration");
			Unmarshaller m = context.createUnmarshaller();
			Object ob = m.unmarshal(Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("./ExampleServices.xml"));

			if (ob == null) {
				Logger.getAnonymousLogger().log(java.util.logging.Level.INFO,
						"Config File doesn't exits ");
			}

			WrapperConfiguration result = (WrapperConfiguration) ob;
			return result;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING,
					e.toString());
		}
		return new WrapperConfiguration();
	}

	protected void initiateStandardConfiguration() {

		if (mediator == null) {
			try {
				// Das dürfte nie schiefgehen
				mediator = (IMediator) Class.forName(
						"de.biba.mediator.ReasoningMediator").newInstance();
			} catch (Exception err) {
				System.err.println("Found no ReasoningMediator" + err);
			}
		}
		// Logger barlogger = Logger.getLogger("de.biba.mediator.webservice");
		if (mediator.getModel().tbox.getSimpleClasses().size() < 2) {
			WrapperConfiguration config = loadConfigFile();
			for (de.biba.mediator.webservice.configuration.DataSource source : config
					.getDataSource()) {
				try {
					String className = source.getJavaClassForWrapper();
					Class c = Class.forName(className);
					Object o = c.newInstance();
					de.biba.wrapper.DataSource wrapper = (de.biba.wrapper.DataSource) o;
					Logger.getAnonymousLogger().log(
							java.util.logging.Level.INFO,
							"Load: " + source.getPathToConfigFile());
					wrapper.initialize(source.getPathToConfigFile());
					boolean failureFree = true;
					if (wrapper instanceof DataSourceV2) {
						if (((DataSourceV2) wrapper).validateConfiguration()
								.isConsistent() == false) {
							failureFree = false;
							Logger.getAnonymousLogger().log(
									java.util.logging.Level.WARNING,
									((DataSourceV2) wrapper)
											.validateConfiguration()
											.getErrorMessage());
							failureFree = true;// TODO �ndern
						}
						if (failureFree) {
							Logger.getAnonymousLogger().log(
									java.util.logging.Level.INFO,
									"Loaded: " + source.getPathToConfigFile());
							mediator.addDataSource(wrapper, 0);
						}
					} else {
						Logger.getAnonymousLogger().log(
								java.util.logging.Level.INFO,
								"Loaded: " + source.getPathToConfigFile());
						mediator.addDataSource(wrapper, 0);
					}
				} catch (Exception e) {
					System.err.println(e);
					Logger.getAnonymousLogger().log(
							java.util.logging.Level.WARNING,
							e.getLocalizedMessage());

				}
			}
			// SimpleHttpWrapper wrapper = new SimpleHttpWrapper();
			// AbstractCsvWrapper wraperCSV1 = new AbstractCsvWrapper();
			// AbstractCsvWrapper wraperCSV2 = new AbstractCsvWrapper();
			// AbstractCsvWrapper wraperCSV3 = new AbstractCsvWrapper();
			// SQLWrapper warpperSQL = new SQLWrapper();
			// SQLWrapper warpperSQL2 = new SQLWrapper();
			// //SimpleHttpWrapper wrapper1 = new SimpleHttpWrapper();
			// String tempconfig1 = "C:/Resources/Facebook/config.properties";
			// String tempconfig2 =
			// "C:/Resources/WetherExample2/config.properties";
			// String tempconfig3 =
			// "C:/Resources/LinkDesignCSV/config.properties";
			// String tempconfig4 =
			// "C:/Resources/LinkDesignSQL/properties.properties";
			// String tempconfig5
			// ="C:/Resources/LinkDesignXLS/config.properties";
			// String tempconfig6
			// ="C:/Resources/Mitarbeiter/properties.properties";
			// String tempconfig7 ="C:/Resources/FITMAN/config.properties";
			//
			// try {
			// if (new File(tempconfig1).exists()){
			// wrapper.initialize(tempconfig1);
			// wraperCSV1.initialize(tempconfig3);
			// wraperCSV2.initialize(tempconfig5);
			// wraperCSV3.initialize(tempconfig7);
			// warpperSQL.init(tempconfig4);
			// warpperSQL2.init(tempconfig6);
			// // barlogger
			// // .info("Loading: "+
			// tempconfig1+" ###############################!");
			// // barlogger.info("Check1: "
			// // + wrapper.validateConfiguration().isConsistent());
			// //
			// System.out.println(wrapper.validateConfiguration().getErrorMessage());
			// mediator.addDataSource(wrapper, 0);
			// mediator.addDataSource(wraperCSV1, 0);
			// mediator.addDataSource(wraperCSV2, 0);
			// mediator.addDataSource(wraperCSV3, 0);
			// mediator.addDataSource(warpperSQL, 0);
			// mediator.addDataSource(warpperSQL2, 0);
			//
			// }
			// else{
			// //barlogger.severe("The configuration file: " + tempconfig1 +
			// " doesn't exists");
			// }
			// if (new File(tempconfig2).exists()){
			// //wrapper1.initialize(tempconfig2);
			// // barlogger
			// // .info("Loading: "+
			// tempconfig2+" ###############################!");
			// // barlogger.info("Check1: "
			// // + wrapper1.validateConfiguration().isConsistent());
			// //
			// System.out.println(wrapper.validateConfiguration().getErrorMessage());
			// // mediator.addDataSource(wrapper1, 0);
			// }
			// else{
			// //barlogger.severe("The configuration file: " + tempconfig2 +
			// " doesn't exists");
			// }
			//
			// } catch (NullPointerException e) {
			// System.out.println("###############################????");
			// // barlogger.severe(e.toString());
			// e.printStackTrace();
			// }
			//
			// catch (Exception e) {
			// System.out.println("###############################????");
			// //barlogger.severe(e.toString());
			// System.out.println(e.getStackTrace());
			// }
		}
	}

	private IQuerySolution makeAQuery(String namespace, String query,
			Logger barlogger, String tempQueryDir, int[][] properties) {

		if (query.contains("BASE") == false) {
			String base = "BASE <" + namespace + "> ";
			query = base + query;
		}

		try {
			// Das dürfte nie schiefgehen
			mediator = (IMediator) Class.forName(
					"de.biba.mediator.ReasoningMediator").newInstance();
		} catch (Exception err) {
			System.err.println("sddsd" + err);
		}

		if (properties == null || properties.length < 1) {
			initiateStandardConfiguration();
		} else {
			String tempconfig = "";
			try {
				for (int i = 0; i < properties.length; i++) {
					switch (properties[i][0]) {

					case 1:
						SQLWrapper sqlWrapper = new SQLWrapper();
						tempconfig = tempQueryDir + properties[i][1]
								+ fSeparator + propertyFile;

						if (new File(tempconfig).exists()) {
							sqlWrapper.init(tempconfig);
							barlogger.info("Loading: " + tempconfig
									+ " ###############################!");
							barlogger.info("Check1: "
									+ sqlWrapper.validateConfiguration()
											.isConsistent());
							mediator.addDataSource(sqlWrapper, 0);
						} else {
							barlogger.severe("The configuration file: "
									+ tempconfig + " doesn't exists");
						}
						break;

					default:
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("###############################????");
				barlogger.severe(e.toString());
			}
		}

		try {

			return mediator.query(query, true, false);

		} catch (Exception e) {
			barlogger.severe(e.toString());
		}

		return null;
	}

	private MediatorResult helperQuery(IQuerySolution solution,
			Logger barlogger, String query) {
		MediatorResult result = null;
		if (solution != null) {
			QuerySolutionModel tmp = new QuerySolutionModel(solution);
			StringReader sr = new StringReader(query);
			SimpleQueryParser sqp = new SimpleQueryParser(sr);
			OutputQuery out = null;
			try {
				IQuery q = sqp.parse(true, false);
				if (!q.isIntputQuery())
					out = ((OutputQuery) q);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = new MediatorResult();
			List<Integer> allForbidenRows = detectDuplicates(tmp);
			String[][] data = new String[tmp.getRowCount()
					- allForbidenRows.size()][tmp.getColumnCount()];
			String[] columns = new String[tmp.getColumnCount()];
			barlogger.info("Ergebnis der Anfrage: " + tmp.getRowCount());
			int index = 0;
			for (int a = 0; a < tmp.getRowCount(); a++) {

				if (allForbidenRows.contains(a)) {
					continue;
				}
				for (int i = 0; i < tmp.getColumnCount(); i++) {

					columns[i] = tmp.getColumnName(i);
					if (out != null) {
						for (SimpleConstraint con : out.getConstraint()
								.flatToSimpleConstraints()) {
							if (con.getResultVars().contains(
									tmp.getColumnName(i))) {
								columns[i] = tmp.getColumnName(i) + ":"
										+ con.getPredicate();

							}
						}
					}

					// System.out.print(tmp.getValueAt(a,i).toString());
					data[index][i] = (String) tmp.getValueAt(a, i).toString();

				}
				index++;
				// System.out.println();
			}
			result.setColumnNames(columns);
			result.setData(data);
		}

		return result;
	}

	private int getIndexOfString(String[] columns, String serachValue) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] != null) {
				if (columns[i].equals(serachValue)) {
					return i;
				}
			}
		}
		return -1;
	}

	private String searchIversalFunctionlProperty(ArrayList<String> arrayList,
			OntModel baseModel) {
		String result = "";
		for (String str : arrayList) {
			DatatypeProperty p = baseModel.tbox.getDatatypeProperties()
					.get(str);
			if (p != null) {
				if (p.isInverseFunctional()) {
					return p.getUri();
				}
			}
		}

		return result;
	}

	/**
	 * This method shall generate a string in a qlm data model format. The qlm
	 * format is a object oriented view. For that reason I have to identify the
	 * concepts which are included in the data properties of the result
	 * 
	 * @param input
	 * @param allConstraints
	 * @param baseModel
	 * @return
	 */
	private String helperQLMMethod(MediatorResult input,
			List<SimpleConstraint> allConstraints, OntModel baseModel) {
		String result = "";
		ArrayList<String> InverserProperties = new ArrayList<String>();
		if (input.getColumnNames().length > 0) {
			if (input.getColumnNames()[0].charAt(0) == '?') {

				String[] resultColumns = new String[input.getColumnNames().length];
				for (int i = 0; i < input.getColumnNames().length; i++) {
					String str = input.getColumnNames()[i];
					resultColumns[i] = str.substring(str.indexOf(':') + 1);
				}
				input.setColumnNames(resultColumns);
			}
		}
		HashMap<String, ArrayList<String>> allAdressedConcepts = new HashMap<String, ArrayList<String>>();
		for (String name : input.getColumnNames()) {
			Set<String> conceptNames = null;
			if (baseModel.tbox.datatypeProperties.containsKey(name)) {
				conceptNames = baseModel.tbox.datatypeProperties.get(name)
						.getDomain();

			} else {
				if (baseModel.tbox.objectProperties.containsKey(name)) {
					conceptNames = baseModel.tbox.objectProperties.get(name)
							.getDomain();
				}
			}
			if (conceptNames == null) {
				Logger.getAnonymousLogger().log(Level.SEVERE,
						"There is no property for: " + name);
			} else {
				for (String conceptname : conceptNames) {
					if (allAdressedConcepts.containsKey(conceptname)) {
						ArrayList<String> dataPropertyNames = allAdressedConcepts
								.get(conceptname);
						if (!dataPropertyNames.contains(name)) {
							dataPropertyNames.add(name);
						}
					} else {
						ArrayList<String> dataPropertyNames = new ArrayList<String>();
						dataPropertyNames.add(name);
						allAdressedConcepts.put(conceptname, dataPropertyNames);
					}
				}
			}
		}
		// Now I have a list of properties for each adresses concept. Now I can
		// generate the data model
		ObjectsType type = new ObjectsType();
		for (String[] data : input.getData()) {
			for (String str : allAdressedConcepts.keySet()) {
				ObjectType OneConcept = new ObjectType();
				OneConcept.setType(str);
				QlmID id = new QlmID();
				String innverse = searchIversalFunctionlProperty(
						allAdressedConcepts.get(str), baseModel);
				int index = getIndexOfString(input.getColumnNames(), innverse);
				if (index > -1) {
					id.setValue(data[index]);
					// allAdressedConcepts.get(str).remove(innverse);
				}
				OneConcept.getId().add(id);
				type.getObject().add(OneConcept);
				// Jetzt m�ssen die fehlenden Properties zu einer Klasse erg�nzt
				// werden
				for (String propertyName : allAdressedConcepts.get(str)) {
					if (!propertyName.equals(innverse)) {
						DatatypeProperty property = baseModel.tbox
								.getDatatypeProperties().get(propertyName);
						if (!(property.isInverseFunctional())) {
							Set<String> allDomains = property.getDomain();
							if (allDomains.contains(str)) {
								InfoItemType infoItem = new InfoItemType();
								String name = "";
								if (propertyName.contains("#")) {
									name = propertyName.substring(propertyName
											.indexOf('#') + 1);
								} else {
									name = propertyName;
								}
								infoItem.setName(name);
								int indexOfProp = getIndexOfString(
										input.getColumnNames(), propertyName);
								ValueType value = new ValueType();
								value.setValue(data[indexOfProp]);
								infoItem.getValue().add(value);
								OneConcept.getInfoItem().add(infoItem);
							}
						}
					}
				}
			}
		}
		StringWriter stringWriter = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance("de.biba.qlm.qmldf");
			Marshaller m = context.createMarshaller();
			m.marshal(type, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING,
					e.toString());
		}
		return result;
	}

	public String queryAsQML(String jObject, String requestId) {
		MediatorResult result = query(jObject);
		StringReader sr = new StringReader(currentQuery);
		SimpleQueryParser sqp = new SimpleQueryParser(sr);
		List<SimpleConstraint> allConstraints = null;
		try {
			IQuery q = sqp.parse(true, false);
			OutputQuery outQuery = (OutputQuery) q;
			allConstraints = outQuery.getConstraint().flatToSimpleConstraints();
			ArrayList<String> myClasses = new ArrayList<String>();
			for (SimpleConstraint simple : allConstraints) {
				if (simple.getType() == 0) {
					// ich habe ein Class Constraint gefunden
					ClassConstraint cc = (ClassConstraint) simple;
					myClasses.add(cc.className);
				}
			}
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE,
					e.getMessage());
		}
		String resp = "";
		resp += "<qlm:qlmEnvelope xmlns:qlm=\"QLMmi.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"QLMmi.xsd QLMmi.xsd\" version=\"1.0\" ttl=\"10\">";
		resp += "<qlm:response>";
		resp += "<qlm:result msgformat=\"QLMmi\">";
		resp += "<qlm:return returnCode=\"200\"></qlm:return>";
		resp += "<qlm:requestId>" + requestId + "</qlm:requestId>";
		resp += "<qlm:msg xmlns=\"QLMmi.xsd\" xsi:schemaLocation=\"QLMmi.xsd QLMmi.xsd\">";
		resp += helperQLMMethod(result, allConstraints, mediator.getModel());
		resp += "</qlm:msg>";
		resp += "</qlm:result>";
		resp += "</qlm:response>";
		resp += "</qlm:qlmEnvelope>";
		return resp;

	}

	public String queryAsHTML(String jObject) {
		MediatorResult result = query(jObject);
		String resultHTML = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> <html> <head> <title>Beschreibung der Seite</title> </head> <body> <h1> Result of the Semantic Mediator </h1> <table border = \"1\"> <tr>";
		resultHTML += "<tr>";
		for (String str : result.getColumnNames()) {
			resultHTML += "<th>" + str + "</th>";
		}
		resultHTML += "</tr>";
		for (String[] dArray : result.getData()) {
			resultHTML += "<tr>";
			for (String str : dArray) {
				resultHTML += "<td>" + str + "</td>";

			}
			resultHTML += "</tr>";
		}
		resultHTML += "</table> </body> </html>";
		return resultHTML;
	}

	public MediatorResult query(String jObject) {
		Logger barlogger = Logger.getLogger("de.biba.mediator.webservice");
		barlogger.info("Starte jetzt die Anfrage!!!!!");
		JSONObject jO = null;
		JSONArray jA = null;
		int[][] propsettings = null;
		String namespace = "";
		String query = "";
		long timeInMillis = System.currentTimeMillis();
		String tempQueryDir = tempDir + timeInMillis + fSeparator;

		// Hier wird das JSONObjekt aus dem String gebaut, Exception falls
		// String kaputt
		try {
			jO = new JSONObject(jObject);
			query = jO.getString("query");
			namespace = jO.getString("namespace");
			if (jO.has("propList")) {
				jA = jO.getJSONArray("propList");
				propsettings = new int[jA.length()][2];
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Wenn Properties übergeben wurden dann:
		if (jO.has("propList") && jA.length() > 0) {
			Properties properties = null;
			BufferedInputStream stream = null;
			FileOutputStream fOS = null;

			// Umwandlung des Strings ins MyMessage
			WrapperMessage wrapMessage = new Gson().fromJson(jObject,
					WrapperMessage.class);

			List<WrapperProperty> wrapProperties = wrapMessage.getPropList();
			int i = 0;
			for (WrapperProperty wrapProperty : wrapProperties) {
				propsettings[i][0] = wrapProperty.getWrapperID();
				propsettings[i][1] = i;
				switch (wrapProperty.getWrapperID()) {
				case 0: // wenn es sich um HTTP Wrapper handelt
					WrapperHTTPProperty wrapHTTPProperty = new Gson().fromJson(
							property2String(jA, i), WrapperHTTPProperty.class);
					mWriter(wrapHTTPProperty.getPropertyFile(), tempQueryDir
							+ i, tempQueryDir + i + fSeparator + propertyFile);
					mWriter(wrapHTTPProperty.getMappingFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ mappingFile);
					mWriter(wrapHTTPProperty.getMappingSchema(), tempQueryDir
							+ i, tempQueryDir + i + fSeparator + mappingSchema);
					mWriter(wrapHTTPProperty.getOntologyFile(), tempQueryDir
							+ i, tempQueryDir + i + fSeparator + ontologyFile);

					properties = new Properties();
					try {
						stream = new BufferedInputStream(new FileInputStream(
								tempQueryDir + i + fSeparator + propertyFile));
						properties.load(stream);
						stream.close();
						properties.setProperty("MappingFile", tempQueryDir + i
								+ fSeparator + mappingFile);
						properties.setProperty("MappingSchema", tempQueryDir
								+ i + fSeparator + mappingSchema);
						properties.setProperty("OntologyFile", tempQueryDir + i
								+ fSeparator + ontologyFile);

						fOS = new FileOutputStream(tempQueryDir + i
								+ fSeparator + propertyFile);
						properties.store(fOS, null);
						fOS.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
					break;

				case 1: // wenn es sich um SQL Wrapper handelt
					WrapperSQLProperty wrapSQLProperty = new Gson().fromJson(
							property2String(jA, i), WrapperSQLProperty.class);
					mWriter(wrapSQLProperty.getPropertyFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ propertyFile);
					mWriter(wrapSQLProperty.getMappingFile(), tempQueryDir + i,
							tempQueryDir + i + fSeparator + mappingFile);
					mWriter(wrapSQLProperty.getMappingSchema(), tempQueryDir
							+ i, tempQueryDir + i + fSeparator + mappingSchema);
					mWriter(wrapSQLProperty.getOntologyFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ ontologyFile);

					properties = new Properties();
					try {
						stream = new BufferedInputStream(new FileInputStream(
								tempQueryDir + i + fSeparator + propertyFile));
						properties.load(stream);
						stream.close();
						properties.setProperty("MappingFile", tempQueryDir + i
								+ fSeparator + mappingFile);
						properties.setProperty("MappingSchema", tempQueryDir
								+ i + fSeparator + mappingSchema);
						properties.setProperty("OntologyFile", tempQueryDir + i
								+ fSeparator + ontologyFile);

						fOS = new FileOutputStream(tempQueryDir + i
								+ fSeparator + propertyFile);
						properties.store(fOS, null);
						fOS.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
					break;

				case 2: // wenn es sich um CSV Wrapper handelt
					WrapperCSVProperty wrapCSVProperty = new Gson().fromJson(
							property2String(jA, i), WrapperCSVProperty.class);
					mWriter(wrapCSVProperty.getPropertyFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ propertyFile);
					mWriter(wrapCSVProperty.getMappingFile(), tempQueryDir + i,
							tempQueryDir + i + fSeparator + mappingFile);
					mWriter(wrapCSVProperty.getMappingSchema(), tempQueryDir
							+ i, tempQueryDir + i + fSeparator + mappingSchema);
					mWriter(wrapCSVProperty.getOntologyFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ ontologyFile);
					mWriter(wrapCSVProperty.getDatatypeDefinitionFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ datatypeDefinitionFile);
					mWriter(wrapCSVProperty.getCsvFile(), tempQueryDir + i,
							tempQueryDir + i + fSeparator + csvFile);

					properties = new Properties();
					try {
						stream = new BufferedInputStream(new FileInputStream(
								tempQueryDir + i + fSeparator + propertyFile));
						properties.load(stream);
						stream.close();
						properties.setProperty("Filename", tempQueryDir + i
								+ fSeparator + csvFile);
						properties.setProperty("MappingFile", tempQueryDir + i
								+ fSeparator + mappingFile);
						properties.setProperty("MappingSchema", tempQueryDir
								+ i + fSeparator + mappingSchema);
						properties.setProperty("OntologyFile", tempQueryDir + i
								+ fSeparator + ontologyFile);
						properties.setProperty("DatatypeDefinitionFile",
								tempQueryDir + i + fSeparator
										+ datatypeDefinitionFile);

						fOS = new FileOutputStream(tempQueryDir + i
								+ fSeparator + propertyFile);
						properties.store(fOS, null);
						fOS.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
					break;

				case 3: // wenn es sich um XLS Wrapper handelt
					WrapperXLSProperty wrapXLSProperty = new Gson().fromJson(
							property2String(jA, i), WrapperXLSProperty.class);
					mWriter(wrapXLSProperty.getPropertyFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ propertyFile);
					mWriter(wrapXLSProperty.getMappingFile(), tempQueryDir + i,
							tempQueryDir + i + fSeparator + mappingFile);
					mWriter(wrapXLSProperty.getMappingSchema(), tempQueryDir
							+ i, tempQueryDir + i + fSeparator + mappingSchema);
					mWriter(wrapXLSProperty.getOntologyFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ ontologyFile);
					mWriter(wrapXLSProperty.getDatatypeDefinitionFile(),
							tempQueryDir + i, tempQueryDir + i + fSeparator
									+ datatypeDefinitionFile);

					properties = new Properties();
					try {
						stream = new BufferedInputStream(new FileInputStream(
								tempQueryDir + i + fSeparator + propertyFile));
						properties.load(stream);
						stream.close();
						properties.setProperty("MappingFile", tempQueryDir + i
								+ fSeparator + mappingFile);
						properties.setProperty("MappingSchema", tempQueryDir
								+ i + fSeparator + mappingSchema);
						properties.setProperty("OntologyFile", tempQueryDir + i
								+ fSeparator + ontologyFile);
						properties.setProperty("DatatypeDefinitionFile",
								tempQueryDir + i + fSeparator
										+ datatypeDefinitionFile);

						fOS = new FileOutputStream(tempQueryDir + i
								+ fSeparator + propertyFile);
						properties.store(fOS, null);
						fOS.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
					break;
				default:
					break;
				}
			}
		}

		String base = "BASE <" + namespace + "> ";
		query = base + query;
		currentQuery = query;
		IQuerySolution solution = null;
		// solution = makeAQuery(namespace, query, barlogger, tempDir + 0 +
		// fSeparator + conf, tempDir + 1 + fSeparator + conf);
		System.out.println("Propsetting sind folgende \n    "
				+ Arrays.deepToString(propsettings));
		solution = makeAQuery(namespace, query, barlogger, tempQueryDir,
				propsettings);

		System.out.println("sizeTest End");
		// File temp = new File(tempQueryDir);
		// mRemover(temp);
		System.out
				.println("Der \"TEMP\" Ordner der Query wurde wieder entfernt!");
		MediatorResult result = helperQuery(solution, barlogger, query);
		String h = "";
		for (String str : result.getColumnNames()) {
			h += str;
		}
		String tt = "";
		for (String[] jj : result.getData()) {
			for (String str : jj) {
				tt += str;
			}
		}
		System.out.println("Rückgabe: " + h);
		System.out.println("Rückgabe II: " + tt);
		return result;
	}

	/**
	 * Diese Methode macht die aus den Properties einen String ist für die
	 * Umwandlung in die jeweiligen Properties wichtig HTTP, SQL usw.
	 * 
	 * @param jA
	 *            JSONArray in dem Properties gespeichert liegen
	 * @param index
	 *            es werden ja gewöhnlich mehrere Properties übergeben.
	 * @return String der Properties
	 */
	private String property2String(JSONArray jA, int index) {
		String property = "";
		try {
			property = jA.get(index).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return property;
	}

	/**
	 * Diese Methode schreibt den übergebenen String in die Datei
	 * 
	 * @param content
	 *            Der übergebene String
	 * @param fileDir
	 *            Verzeichnis für die Datei
	 * @param fileName
	 *            Dateiname
	 */
	private void mWriter(String content, String fileDir, String fileName) {
		try {
			File tempDir = new File(fileDir);
			tempDir.mkdirs();
			File tempDatei = new File(fileName);

			BufferedWriter bw = new BufferedWriter(new FileWriter(tempDatei));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Integer> detectDuplicates(QuerySolutionModel tmp) {
		// An dieser Stelle filtere ich die doppelten Einträge raus
		ArrayList<Integer> allForbidenRows = new ArrayList<Integer>();
		for (int y = 0; y < tmp.getRowCount(); y++) {

			String str1 = "";

			for (int x = 0; x < tmp.getColumnCount(); x++) {
				str1 += tmp.getValueAt(y, x).toString() + ";";
			}
			for (int y1 = y + 1; y1 < tmp.getRowCount(); y1++) {
				String str2 = "";
				for (int x = 0; x < tmp.getColumnCount(); x++) {
					str2 += tmp.getValueAt(y1, x).toString() + ";";
				}

				if (str1.equals(str2)) {
					// System.out.println("Gelöschte Zeile: " + y1);
					if (allForbidenRows.contains(y1) == false) {
						allForbidenRows.add(y1);

					}
				}

			}
		}
		return allForbidenRows;

	}

}
