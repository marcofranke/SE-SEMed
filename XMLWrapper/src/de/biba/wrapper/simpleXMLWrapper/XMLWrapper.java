package de.biba.wrapper.simpleXMLWrapper;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.OWLParser;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.wrapper.DataSourceV2;
import de.biba.wrapper.ValidationReport;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.AdditionalLinkingInforamtion;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.DatatypePropertyMapping;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.XMLTagExctrationInformation;

public class XMLWrapper extends DataSourceV2 {
	protected IReader reader;
	protected boolean initialized;
	protected Mapping mapping;
	private Map<String, OntClass> myClasses = null;
	private Map<String, DatatypePropertyCreator> myClassesInverseFunctional = null;
	private Map<String, String> inversalFunctionPropertyByConceptName = null;
	private Properties properties = null;
	private String pPropertyFile = new String();
	private String contentOfXMLFile = "";

	public void initialize(String pPropertyFile) throws IOException,
			XPathExpressionException, ParserConfigurationException,
			SAXException {
		properties = new Properties();
		this.pPropertyFile = pPropertyFile;
		FileReader fr = new FileReader(pPropertyFile);
		properties.load(fr);
		initialize(properties);
		fr.close();
	}

	private void initializeAllInversalproperties(String namespace) {
		inversalFunctionPropertyByConceptName = new HashMap<String, String>();
		for (DatatypeProperty property : baseModel.tbox.datatypeProperties
				.values()) {
			if (property.isInverseFunctional()) {

				for (String domain : property.getDomain()) {
					domain = namespace + "#" + domain;
					inversalFunctionPropertyByConceptName.put(domain,
							property.getUri());
				}

			}
		}
	}

	// TODO Mit Leben füllen
	private List<XMLTagExctrationInformation> collectAllTagPathes(
			DataSourceQuery dsg, Collection<String> collection) {
		return mapping.collectAllTagPathes(dsg, collection);
	}

	private String loadXMLFile(String file) {
		try {
			StringBuffer buffer = new StringBuffer();
			BufferedReader br = new BufferedReader(new FileReader(
					new File(file)));
			String line = "";
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			br.close();
			return buffer.toString();
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE,
					e.getLocalizedMessage());
		}
		return "";
	}

	public void initialize(Properties properties) throws IOException,
			XPathExpressionException, ParserConfigurationException,
			SAXException {

		this.properties = properties;
		String fileName = properties.getProperty("Filename");
		reader = new StringReader();
		contentOfXMLFile = loadXMLFile(fileName);

		String ontModel = properties.getProperty("OntologyFile");
		loadModel(ontModel);
		String mappingFile = properties.getProperty("MappingFile");
		String mappingSchema = properties.getProperty("MappingSchema");
		mapping = new Mapping();
		mapping.loadMapping(mappingSchema, mappingFile);
		initialized = true;
		System.out.println("After it is initalized: "
				+ ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
	}

	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		if (pDsc.getPropertyNames().size() == 0) {
			return new OntModel(baseModel.getIndividualCounter());
		}
		String namespace = pDsc.getPropertyNames().get(0);
		namespace = namespace.substring(0, namespace.indexOf('#'));
		initializeAllInversalproperties(namespace); // Collect all
													// InversalFunctionalProerties
		((StringReader) reader).setText(
				contentOfXMLFile,
				collectAllTagPathes(pDsc,
						inversalFunctionPropertyByConceptName.values()));

		System.out.println(ManagementFactory.getMemoryMXBean()
				.getHeapMemoryUsage());
		ArrayList<ObjectPropertyLinkingObject> allObjectPropertiesRefrences = new ArrayList<ObjectPropertyLinkingObject>();
		HashMap<String, Individual> allSortedIndis = new HashMap<String, Individual>();
		OntModel om = new OntModel(baseModel.getIndividualCounter());
		Collection<IndidividualCreator> creators = mapping
				.generateDataCreators(pDsc, om, baseModel);
		myClasses = mapping.myClasses;
		myClassesInverseFunctional = mapping.myClassesInverseFunctional;
		if (creators.size() == 0)
			return om;
		try {
			while (reader.next()) {
				for (IndidividualCreator ic : creators) {
					ic.createIndividual(om, reader,
							allObjectPropertiesRefrences, allSortedIndis);
				}
			}

			for (ObjectPropertyLinkingObject obMapping : allObjectPropertiesRefrences) {
				String domain = obMapping.getSpecificDomain();
				if (!domain.contains("#")) {
					domain = namespace + "#" + domain;
				}
				String dataProp = this.inversalFunctionPropertyByConceptName
						.get(domain);
				String value = "";
				List<DatatypePropertyStatement> allStats = om.abox.dtProperties
						.get(dataProp);
				for (DatatypePropertyStatement st : allStats) {
					if (st.subject == obMapping.getIndi()) {
						value = st.object.getString();
						break;
					}
				}
				for (String range : obMapping.getOb().getRange()) {
					if (!range.contains("#")) {
						range = namespace + "#" + range;
					}
					String dataPropRange = this.inversalFunctionPropertyByConceptName
							.get(range);
					AdditionalLinkingInforamtion subject = ((StringReader) reader)
							.getSpecificAdditionalInformation(dataProp, value);
					List<AdditionalLinkingInforamtion> objects = ((StringReader) reader)
							.getAdditionalInformation(dataPropRange);
					List<String> allRangeIndis = ((StringReader) reader).find(
							subject, objects);
					List<DatatypePropertyStatement> allStatsRanges = om.abox.dtProperties
							.get(dataPropRange);
					if ((allRangeIndis != null) && (allStatsRanges != null)) {
						for (String str : allRangeIndis) {
							for (DatatypePropertyStatement st : allStatsRanges) {
								if (st.object.toString().equals(str)) {
									om.addProperty(obMapping.getIndi(),
											obMapping.getOb(), st.subject);
								}
							}
						}
					}

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ManagementFactory.getMemoryMXBean()
				.getHeapMemoryUsage());
		System.out.println("Number: "
				+ ManagementFactory.getMemoryMXBean()
						.getObjectPendingFinalizationCount());

		reader.reset();
		return om;
	}

	private String makeARelativePathToAnAbsolute(String filePath) {
		String os = "os.name";
		Properties prop = System.getProperties();

		if (prop.getProperty(os).contains("Windows")) {
			if (!filePath.contains(":")) // Nur absolute Pfade beinhalten dieses
											// Zeichen. Funktioniert nu runter
											// Windows
			{

				filePath = filePath.replace('\\', '/');
				File f = new File(".");
				if (filePath.charAt(0) != '/') {
					filePath = '/' + filePath;
				}
				String path = f.getAbsolutePath();
				path = path.replace(".", "");
				path = path.replace('\\', '/');
				if (path.charAt(path.length() - 1) == '/') {
					path = path.substring(0, path.length() - 1);
				}

				String result = path + filePath;
				if (result.charAt(result.length() - 1) == '/') {
					result = result.substring(0, result.length() - 1);
				}
				return result;
			}
		} else {
			System.err
					.println("Bin nicht unter Windows. Pfadanpassung funktioniert nicht!!!");
		}
		return filePath;
	}

	private ValidationReport checkConfigurationFile(String source,
			ValidationReport result) throws IOException {
		result.setConsistent(false);

		File f = new File(source);
		if (f.exists()) {
			System.out.println(f.getAbsolutePath());
			int index = f.getAbsoluteFile().toString().lastIndexOf("\\");
			if (index != -1) {
				String ordner = f.getAbsolutePath().substring(0, index);
				File f2 = new File(ordner);
				System.out.println(f2.getAbsolutePath());

				// File in Resource einlesen
				File[] files = f2.listFiles();
				String owlDatei = null;
				String mappingDatei = null;
				String csvDatei = null;

				if (files != null) {
					for (int i = 0; i < files.length; i++) {

						String ges = files[i].getAbsolutePath();
						String zwi = ges;

						if (zwi.endsWith("owl")) {
							owlDatei = zwi;
							owlDatei = owlDatei.replace('\\', '/');
						}
						if (zwi.endsWith("xml")) {
							mappingDatei = zwi;
							mappingDatei = mappingDatei.replace('\\', '/');
						}

						if (zwi.endsWith("csv")) {
							csvDatei = zwi;
							csvDatei = csvDatei.replace('\\', '/');
						}
					}
				}

				FileReader fr = new FileReader(source);
				Properties properties = new Properties();
				properties.load(fr);

				String fileN = properties.getProperty("Filename");
				String p1 = makeARelativePathToAnAbsolute(fileN);
				String fileName = p1.toLowerCase();
				String ontM = properties.getProperty("OntologyFile");
				String p2 = makeARelativePathToAnAbsolute(ontM);
				String ontModel = p2.toLowerCase();

				String mappingF = properties.getProperty("MappingFile");
				String p3 = makeARelativePathToAnAbsolute(mappingF);
				String mappingFile = p3.toLowerCase();

				if (fileName.equals(csvDatei.toLowerCase())
						&& ontModel.equals(owlDatei.toLowerCase())
						&& mappingFile.equals(mappingDatei.toLowerCase())) {
					result.setConsistent(true);
				} else {
					result.setErrorMessage("The pathes within the config file is different to the file system: ");
				}

			}
		}

		return result;
	}

	/**
	 * Check consistency of configuration
	 * 
	 * @param mappingFile
	 *            mapping.xml
	 * @param ontologyFile
	 *            Ontologie als *.owl
	 * @param result
	 * @return
	 */
	private ValidationReport testMappingAgainstOntology(String mappingFile,
			String ontologyFile, ValidationReport result) {
		result.setConsistent(true);
		try {

			// Reading ontology
			OWLParser parser = new OWLParser();
			OntModel baseModel = parser.parse(ontologyFile);
			Set<String> allClasses = baseModel.tbox.getSimpleClasses().keySet();
			Set<String> allDataProperties = baseModel.tbox.datatypeProperties
					.keySet();
			Set<String> allobProperties = baseModel.tbox.objectProperties
					.keySet();
			ArrayList<String> classesOfMapping = new ArrayList<String>();

			// Reading mapping file
			InputStream is = new FileInputStream(mappingFile);
			JAXBContext jc = JAXBContext
					.newInstance("de.biba.wrapper.tableWrapper.dataTypes");
			Unmarshaller u = jc.createUnmarshaller();
			Object o = u.unmarshal(is);
			de.biba.wrapper.simpleXMLWrapper.dataTypes.Mapping m = (de.biba.wrapper.simpleXMLWrapper.dataTypes.Mapping) o;
			for (int i = 0; i < m.getClassMapping().size(); i++) {
				de.biba.wrapper.simpleXMLWrapper.dataTypes.ClassMapping mapping = m
						.getClassMapping().get(i);
				String nameofClass = m.getNamespace() + "#"
						+ mapping.getClassName();
				// Jetzt muss ich den Klassennamne in der Ontology finden
				classesOfMapping.add(nameofClass);
				if (allClasses.contains(nameofClass) == false) {
					result.setErrorMessage("I can't find: " + nameofClass
							+ " in the ontology: " + allClasses);
					result.setConsistent(false);
				}

			}

			/*
			 * ich überprüfe ob zu jeder definierten Klasse eine ein
			 * inversefunctionalpropertty gib
			 */
			ArrayList<String> allInverseProperties = new ArrayList<String>();
			for (String str : allDataProperties) {
				DatatypeProperty dP = baseModel.tbox.datatypeProperties
						.get(str);
				if (dP.isInverseFunctional()) {
					allInverseProperties.add(str);
				}
			}

			String namespace = "";
			if (classesOfMapping.size() > 0) {
				namespace = classesOfMapping.get(0).substring(0,
						classesOfMapping.get(0).indexOf("#") + 1);
			}
			for (String str : classesOfMapping) {

				str = str.substring(str.lastIndexOf("#") + 1);
				boolean isContained = false;

				for (String dataprop : allDataProperties) {
					DatatypeProperty d = baseModel
							.getDatatypeProperty(dataprop);
					Set<String> domain = d.getDomain();
					if (domain.contains(str)) {
						if (d.isInverseFunctional()) {
							isContained = true;
						}
					}
				}

				if (!(isContained)) {
					result.setConsistent(false);
					result.setErrorMessage("There is no inverse functional property to: "
							+ str + " missing");
					return result;
				}

				isContained = false;

				for (int a = 0; a < m.getDatatypePropertyMapping().size(); a++) {

					DatatypePropertyMapping ma = m.getDatatypePropertyMapping()
							.get(a);
					if (ma.getSubClass().equals(str)) {
						// Habe ein property gefunden und mus nun tetsen ob es
						// invers ist
						// System.out.println(ma.getProperty());
						String propertyName = namespace + ma.getPropertyName();

						if (allInverseProperties.contains(propertyName)) {
							isContained = true;

						}
					}

				}
				if (!(isContained)) {
					result.setErrorMessage("There is no inverse functional property (mapping) to: "
							+ str + " in the mapping file");
					result.setConsistent(false);
					return result;
				}
			}

			for (int i = 0; i < m.getDatatypePropertyMapping().size(); i++) {
				de.biba.wrapper.simpleXMLWrapper.dataTypes.DatatypePropertyMapping mapping = m
						.getDatatypePropertyMapping().get(i);
				String nameofClass = m.getNamespace() + "#"
						+ mapping.getPropertyName();

				if (allDataProperties.contains(nameofClass) == false) {
					result.setErrorMessage("I can't find : " + nameofClass
							+ " in the ontology: " + allDataProperties);
					result.setConsistent(false);
					return result;
				}
				DatatypeProperty d = baseModel.getTbox()
						.getDatatypeProperties().get(nameofClass);

				Set<String> correspondingOntologyClass = d.getDomain();
				String nameofClassinMapping = mapping.getSubClass();
				if (correspondingOntologyClass.contains(nameofClassinMapping) == false) {
					result.setErrorMessage("The data property: "
							+ nameofClass
							+ " has a SubID which isn't the Domain in the ontology");
					result.setConsistent(false);
					return result;
				}

			}

			for (int i = 0; i < m.getObjectPropertyMapping().size(); i++) {
				de.biba.wrapper.simpleXMLWrapper.dataTypes.ObjectPropertyMapping mapping = m
						.getObjectPropertyMapping().get(i);
				String nameofClass = m.getNamespace() + "#"
						+ mapping.getPropertyName();
				if (allobProperties.contains(nameofClass) == false) {
					result.setErrorMessage("I can't find: " + nameofClass
							+ " in the ontology: " + allobProperties);
					result.setConsistent(false);
					return result;
				}
				ObjectProperty d = baseModel.getTbox().getObjectProperties()
						.get(nameofClass);
				Set<String> correspondingOntologyClass = d.getDomain();
				String nameofClassinMapping = mapping.getSubClass();
				if (correspondingOntologyClass.contains(nameofClassinMapping) == false) {
					result.setErrorMessage("The object property: "
							+ nameofClass
							+ " has a SubID which isn't the Domain in the ontology");
					result.setConsistent(false);
					return result;
				}

			}
		} catch (Exception e) {
			result.setConsistent(false);
			result.setErrorMessage(e.getMessage());
			return result;
		}
		if (result.isConsistent() == false) {
			System.err.println("There are problems between the mapping file: "
					+ mappingFile + " and " + ontologyFile);
		}
		return result;
	}

	/**
	 * 1. Überprüfe, ob es für eine Klasse mehr als ein Mapping gibt 2.
	 * Überprüfe, ob in den Properties IDs verwendet wurden, die durch das
	 * Classmapping nicht vergeben wurden 3. Überprüfe, ob zu einem zu einem
	 * Dataproperty die richitge Klasse gesetzt wurde
	 * 
	 * @param mappingFile
	 * @return
	 */
	private ValidationReport testConsistencyOfMapping(String mappingFile,
			ValidationReport result) {
		result.setConsistent(true);
		try {
			InputStream is = new FileInputStream(mappingFile);
			JAXBContext jc = JAXBContext
					.newInstance("de.biba.wrapper.tableWrapper.dataTypes");
			Unmarshaller u = jc.createUnmarshaller();
			Object o = u.unmarshal(is);
			de.biba.wrapper.simpleXMLWrapper.dataTypes.Mapping m = (de.biba.wrapper.simpleXMLWrapper.dataTypes.Mapping) o;
			int max = 0;
			HashMap<String, Integer> allClassesIndex = new HashMap<String, Integer>();
			HashMap<Integer, String> allIndexByClasses = new HashMap<Integer, String>();
			int index = -1;
			String ontClass = "";
			for (int i = 0; i < m.getClassMapping().size(); i++) {
				index = m.getClassMapping().get(i).getID();
				if (index > max) {
					max = index;
				}
				ontClass = m.getClassMapping().get(i).getClassName();
				if (allClassesIndex.containsKey(ontClass)) {
					result.setErrorMessage("Sorry, but the class is defined more than once");
					result.setConsistent(false);
				} else {
					allClassesIndex.put(ontClass, index);
					allIndexByClasses.put(index, ontClass);
				}
			}

		} catch (Exception e) {

		}

		return result;
	}

	@Override
	public ValidationReport validateConfiguration() {
		ValidationReport result = new ValidationReport(true, true);
		try {
			if (!(checkConfigurationFile(pPropertyFile, result).isConsistent())) {
				result.setConsistent(false);
				return result;
			}
			if (!(testMappingAgainstOntology(
					properties.getProperty("MappingFile"),
					properties.getProperty("OntologyFile"), result)
					.isConsistent())) {

				result.setConsistent(false);
				return result;
			}

			if (!(testConsistencyOfMapping(
					properties.getProperty("MappingFile"), result)
					.isConsistent())) {
				result.setConsistent(false);
				return result;
			}
		} catch (Exception e) {
			System.err.println(e);
			result.setConsistent(false);
			return result;
		}
		return result;
	}

	@Override
	public String getPathOfConfigurationFile() {
		// TODO Auto-generated method stub
		return pPropertyFile;
	}

}
