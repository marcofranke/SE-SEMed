package de.biba.wrapper.newSQLWrapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.DataSourceQuery.PropertyWithValue;
import de.biba.ontology.ABox;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.Individual;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.DataSource;
import de.biba.wrapper.ValidationReport;
import de.biba.wrapper.newSQLWrapper.QueryExecutioner.DBKey;
import de.biba.wrapper.newSQLWrapper.Mapping.ClassMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.ColumnMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.DatatypePropertyMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.JoinedTable;
import de.biba.wrapper.newSQLWrapper.Mapping.Mapping;
import de.biba.wrapper.newSQLWrapper.Mapping.MappingLoader;
import de.biba.wrapper.newSQLWrapper.Mapping.MultiJoinedTable;
import de.biba.wrapper.newSQLWrapper.Mapping.ObjectPropertyMapping;

/**
 * [SQL Wrapper. This is a wrapper to tranform a xml based data base schema to
 * an ontology] Copyright (C) [2014 [Marco Franke (BIBA-Bremer Institut für
 * Produktion und Logistik GmbH)]
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
public class SQLWrapper extends DataSource {
	private Mapping mapping;
	private Map<String, List<ClassMapping>> cMappings;
	private Map<String, List<ObjectPropertyMapping>> oMappings;
	private Map<String, List<DatatypePropertyMapping>> dMappings;
	private String db;
	private String dbUser;
	private String dbPass;
	private String driverName;
	private boolean allowJoins;
	private boolean initialized;
	private Connection connection;
	private String pathToConfigFile;

	/**
	 * Load configuration
	 * 
	 * @param pathToResources
	 *            path to config file.
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public void init(String pathToConfigFile) throws XPathExpressionException,
			ParserConfigurationException, SAXException, JAXBException {
		this.pathToConfigFile = pathToConfigFile;
		Properties props = new Properties();
		FileReader fr = null;
		try {
			fr = new FileReader(pathToConfigFile);
			props.load(fr);
			init(props);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fr != null)
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void init(Properties props) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException,
			JAXBException {
		db = props.getProperty("Database");
		dbUser = props.getProperty("User");
		dbPass = props.getProperty("Password");
		driverName = props.getProperty("Driver");
		allowJoins = Boolean.parseBoolean(props.getProperty("AllowJoins"));
		String mappingFile = props.getProperty("MappingFile");
		String ontFile = props.getProperty("OntologyFile");
		if (mappingFile != null) {
			mapping = MappingLoader.loadMapping(mappingFile);
			cMappings = new HashMap<String, List<ClassMapping>>();
			oMappings = new HashMap<String, List<ObjectPropertyMapping>>();
			dMappings = new HashMap<String, List<DatatypePropertyMapping>>();
			for (ClassMapping cm : mapping.getClassMapping()) {
				if (cMappings.containsKey(cm.getClassName()))
					cMappings.get(cm.getClassName()).add(cm);
				else {
					List<ClassMapping> lcm = new LinkedList<ClassMapping>();
					lcm.add(cm);
					cMappings.put(cm.getClassName(), lcm);
				}
			}
			for (ObjectPropertyMapping cm : mapping.getObjectPropertyMapping()) {
				if (oMappings.containsKey(cm.getPropertyName()))
					oMappings.get(cm.getPropertyName()).add(cm);
				else {
					List<ObjectPropertyMapping> lpm = new LinkedList<ObjectPropertyMapping>();
					lpm.add(cm);
					oMappings.put(cm.getPropertyName(), lpm);
				}
			}
			for (DatatypePropertyMapping cm : mapping
					.getDatatypePropertyMapping()) {
				if (dMappings.containsKey(cm.getPropertyName()))
					dMappings.get(cm.getPropertyName()).add(cm);
				else {
					List<DatatypePropertyMapping> lpm = new LinkedList<DatatypePropertyMapping>();
					lpm.add(cm);
					dMappings.put(cm.getPropertyName(), lpm);
				}
			}
		} else
			return;
		if (ontFile != null)
			loadModel(ontFile);
		else
			return;

		initialized = true;

	}

	/**
	 * Baut fï¿½r jede Tabelle / Join einen Query-Executioner, der die Daten
	 * darauf in die Ontologie transferiert
	 * 
	 * @param pQuery
	 * @return
	 */
	private Map<String, QueryExecutioner> buildExecutioners(
			DataSourceQuery pQuery) {
		Map<String, QueryExecutioner> executioners = new HashMap<String, QueryExecutioner>();
		Map<String, List<IndividualCreator>> ic = new HashMap<String, List<IndividualCreator>>();
		Map<String, OntClass> mostSpecificClass = new HashMap<String, OntClass>();
		Set<String> processedProperties = new HashSet<String>();
		// Fï¿½r alle angefragten Klassen aus der Query
		for (String s : pQuery.getClassNames()) {
			// Wenn es ein Mapping dafï¿½r gibt
			if (cMappings.containsKey(s)) {
				// Fï¿½r jedes Mapping
				for (ClassMapping cm : cMappings.get(s)) {
					// Wenn die Tabelle des Mapping schon von einem
					// QueryExecutioner bearbeitet wird...
					if (executioners.containsKey(cm.getTable())) {
						QueryExecutioner qe = executioners.get(cm.getTable());
						// ..erweitere diesen
						qe.extend(cm);
					} else {
						// ansonsten erzeuge einen neuen!
						QueryExecutioner qe = new QueryExecutioner(driverName,
								cm, this.baseModel, ic, mostSpecificClass);
						executioners.put(cm.getTable(), qe);
					}
				}
			}
		}

		// Fï¿½r jedes angefragte Property
		for (String s : pQuery.getPropertyNames()) {
			if (processedProperties.contains(s))
				continue;
			processedProperties.add(s);
			// Wenn DatatypePropertyMapping vorhanden ist
			if (dMappings.containsKey(s)) {
				// Fï¿½r jedes DatatypeProperty
				for (DatatypePropertyMapping pm : dMappings.get(s)) {
					// Wenn Mapping nur auf eine Tabelle zielt
					if (pm.getTable() != null && !pm.getTable().isEmpty()) {
						// Wenn Tabelle des Mappings bereits von einem
						// QueryExecutioner bearbeitet wird...
						if (executioners.containsKey(pm.getTable())) {
							QueryExecutioner qe = executioners.get(pm
									.getTable());
							// ...erweitere diesen
							qe.extend(pm);
						} else {
							// ansonsten lege einen neuen an
							QueryExecutioner qe = new QueryExecutioner(
									driverName, pm, this.baseModel, ic,
									mostSpecificClass);
							executioners.put(pm.getTable(), qe);
						}
					} else if (pm.getJoin() != null) {
						JoinedTable jt = pm.getJoin();
						String key = jt.getFirstTable() + "/"
								+ jt.getSecondTable();
						if (executioners.containsKey(key)) {
							QueryExecutioner qe = executioners.get(key);
							qe.extend(pm);
						} else {
							QueryExecutioner qe = new QueryExecutioner(
									driverName, pm, this.baseModel, ic,
									mostSpecificClass);
							executioners.put(key, qe);
						}
					}
				}
			}
			// Wenn ObjectPropertyMapping vorhanden ist
			else if (oMappings.containsKey(s)) {
				for (ObjectPropertyMapping pm : oMappings.get(s)) {
					if (pm.getTable() != null && !pm.getTable().isEmpty()) {
						if (executioners.containsKey(pm.getTable())) {
							QueryExecutioner qe = executioners.get(pm
									.getTable());
							qe.extend(pm);
						} else {
							QueryExecutioner qe = new QueryExecutioner(pm,
									this.baseModel, ic, mostSpecificClass);
							executioners.put(pm.getTable(), qe);
						}
					} else if (pm.getJoin() != null) {
						MultiJoinedTable jt = pm.getJoin();
						String key = jt.getFirstTable() + "/"
								+ jt.getSecondTable();
						if (jt.getThirdTable() != null)
							key += "/" + jt.getThirdTable();
						if (executioners.containsKey(key)) {
							QueryExecutioner qe = executioners.get(key);
							qe.extend(pm);
						} else {
							QueryExecutioner qe = new QueryExecutioner(pm,
									this.baseModel, ic, mostSpecificClass);
							executioners.put(key, qe);
						}
					}
					// }
				}
			}
		}

		for (PropertyWithValue pwv : pQuery.getPropertiesWithValues()) {
			if (processedProperties.contains(pwv.getPropertyName()))
				continue;
			processedProperties.add(pwv.getPropertyName());
			if (dMappings.containsKey(pwv.getPropertyName())) {
				for (DatatypePropertyMapping pm : dMappings.get(pwv
						.getPropertyName())) {
					if (pm.getTable() != null && !pm.getTable().isEmpty()) {
						if (executioners.containsKey(pm.getTable())) {
							QueryExecutioner qe = executioners.get(pm
									.getTable());
							qe.extend(pm);
						} else {
							QueryExecutioner qe = new QueryExecutioner(
									driverName, pm, this.baseModel, ic,
									mostSpecificClass);
							executioners.put(pm.getTable(), qe);
						}
					} else if (pm.getJoin() != null) {
						JoinedTable jt = pm.getJoin();
						String key = jt.getFirstTable() + "/"
								+ jt.getSecondTable();
						if (executioners.containsKey(key)) {
							QueryExecutioner qe = executioners.get(key);
							qe.extend(pm);
						} else {
							QueryExecutioner qe = new QueryExecutioner(
									driverName, pm, this.baseModel, ic,
									mostSpecificClass);
							executioners.put(key, qe);
						}
					}
				}
			}
		}
		return executioners;
	}

	@Override
	public OntModel queryData(DataSourceQuery pQuery) {
		Map<String, QueryExecutioner> executioners = buildExecutioners(pQuery);
		openConnection();
		this.baseModel.abox = new ABox();
		this.baseModel.resetIndividualCounter();
		Map<String, Map<DBKey, Individual>> mindividuals = new HashMap<String, Map<DBKey, Individual>>();
		for (QueryExecutioner qe : executioners.values())
			try {
				qe.query(connection, this.baseModel, mindividuals);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		closeConnection();
		System.out.println(baseModel.toString());
		return this.baseModel;
	}

	public boolean openConnection() {

		/**
		 * Driver d = (Driver)
		 * Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"
		 * ).newInstance(); String connectionUrl =
		 * "jdbc:sqlserver://localhost;database=AdventureWorks;integratedSecurity=true;"
		 * Connection con = d.connect(connectionUrl, new Properties());
		 */
		try {
			if (dbUser != null) {
				Logger.getAnonymousLogger().log(Level.INFO,
						"Trying to open the data base connection...:" + db);
				if (driverName.equals("com.mysql.jdbc.Driver")) {
					DriverManager.registerDriver(new com.mysql.jdbc.Driver());
					connection = DriverManager
							.getConnection(db, dbUser, dbPass);

				}
				if (driverName
						.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver")) {
					DriverManager
							.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
					connection = DriverManager
							.getConnection(db, dbUser, dbPass);
				}
			} else {
				connection = DriverManager.getConnection(db);
			}

			if (connection != null) {
				Logger.getAnonymousLogger().log(Level.INFO,
						"data base connection is established");
			} else {
				Logger.getAnonymousLogger().log(Level.INFO,
						"data base connection is NOT established");
			}
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

	public boolean closeConnection() {
		try {
			connection.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public ValidationReport validateConfiguration() {
		// TODO Auto-generated method stub
		return new ValidationReport(true, true);
	}

	@Override
	public void initialize(String pPropertyFile) throws Exception {
		// TODO Auto-generated method stub
		init(pPropertyFile);
	}

	@Override
	public String getPathOfConfigurationFile() {
		// TODO Auto-generated method stub
		return pathToConfigFile;
	}

	public void sortDependencies(List<Dependencies> allDependencies) {
		for (int i = 0; i < allDependencies.size(); i++) {

			if (allDependencies.get(i).getAllPrimaryProperties().size() > 0) {
				for (DatatypePropertyMapping mapping : allDependencies.get(i)
						.getAllPrimaryProperties()) {
					for (int a = 0; a < allDependencies.size(); a++) {
						if (i != a) {
							for (DatatypePropertyMapping mapping2 : allDependencies
									.get(a).getAllNonPrimaryProperties()) {
								if (mapping2.getPropertyName().equals(
										mapping.getPropertyName())) {
									System.out
											.println("Nun muss getestet werden");
									if (a < i) {
										// Das geht nun, weil ich zuerst die
										// Tabelle mit dem Fremdschlüssel
										// erstellen muss
										allDependencies.add(i + 1,
												allDependencies.get(a));
										a--;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void addDependencies(String propertyName,
			List<Dependencies> allDependencies) {
		Dependencies depen = new Dependencies();
		depen.setPropertyName(propertyName);
		List<DatatypePropertyMapping> onePropertyD = new ArrayList<DatatypePropertyMapping>();
		for (DatatypePropertyMapping dMapping : mapping
				.getDatatypePropertyMapping()) {
			String name = dMapping.getPropertyName();

			if (name.contains("#")) {
				if (propertyName.equals(name)) {
					onePropertyD.add(dMapping);
				}
			}
		}
		for (int i = 0; i < onePropertyD.size(); i++) {
			boolean isContained = false;
			List<ColumnMapping> c2 = onePropertyD.get(i).getSubColumn();
			for (ColumnMapping mm : c2) {
				if (onePropertyD.get(i).getObColumn().equals(mm.getValue())) {
					depen.getAllPrimaryProperties().add(onePropertyD.get(i));
					isContained = true;
					continue;
				}
			}
			if (!isContained) {
				depen.getAllNonPrimaryProperties().add(onePropertyD.get(i));
			}
		}
		boolean isContained = false;
		for (Dependencies dep : allDependencies) {
			if (dep.getPropertyName().equals(propertyName)) {
				isContained = true;
				break;
			}
		}
		if (!isContained) {
			allDependencies.add(depen);
		}
	}

	/**
	 * This methods takes the Abox and insert it into a sql database
	 * 
	 * @param abox
	 */
	@Override
	public void insertData(ABox abox) {

		for (List<DatatypePropertyStatement> dtList : abox.dtProperties
				.values()) {
			for (DatatypePropertyStatement dt : dtList) {
				Datatype value = dt.object;
				if (value instanceof StringDatatype) {
					String str = value.getString();
					if ((str != null) && (str.length() > 0)) {
						if (str.charAt(0) == ';') {
							str = str.substring(1);
						}
						if (str.charAt(str.length() - 1) == ';') {
							str = str.substring(0, str.length() - 1);
						}
						StringDatatype str2 = new StringDatatype(str);
						dt.object = str2;
					}
				}
			}
		}

		List<Dependencies> allDependencies = new ArrayList<Dependencies>();
		HashMap<String, List<String>> allConceptsForATable = new HashMap<String, List<String>>();
		List<String> wholeAdressedProps = new ArrayList<String>();
		for (ClassMapping cl : mapping.getClassMapping()) {
			if (!allConceptsForATable.containsKey(cl.getTable())) {
				ArrayList<String> listofConcepts = new ArrayList<String>();
				listofConcepts.add(cl.getClassName());
				allConceptsForATable.put(cl.getTable(), listofConcepts);
			} else {
				List<String> allConcepts = allConceptsForATable.get(cl
						.getTable());
				if (!allConcepts.contains(cl.getClassName())) {
					allConcepts.add(cl.getClassName());

				}
			}
		}
		// Now, I have the information which concepts are necessary for a table
		HashMap<String, List<String>> allSQLInserts = new HashMap<String, List<String>>();
		HashMap<String, List<String>> allSQLUpdates = new HashMap<String, List<String>>();
		for (String table : allConceptsForATable.keySet()) {
			String inversefunctionalproperty = "";
			String inversefunctionalColumn = "";
			int inverseFunctionalIndex = -1;
			String inversefunctionalValue = "";
			for (DatatypeProperty prop : baseModel.tbox.datatypeProperties
					.values()) {
				if (prop.isInverseFunctional()) {
					for (String t : allConceptsForATable.get(table)) {
						String tabel2 = t.substring(t.indexOf('#') + 1);
						if (prop.getDomain().contains(tabel2)) {
							inversefunctionalproperty = prop.getUri();
							break;
						}
					}
				}
			}
			allSQLInserts.put(table, new ArrayList<String>());
			allSQLUpdates.put(table, new ArrayList<String>());
			ArrayList<String> adressedProperties = new ArrayList<String>();
			ArrayList<String> insertColumns = new ArrayList<String>();
			for (DatatypePropertyMapping k : mapping
					.getDatatypePropertyMapping()) {
				if (k.getTable().equals(table)) {
					if (abox.dtProperties.containsKey(k.getPropertyName())) {
						adressedProperties.add(k.getPropertyName());
						insertColumns.add(k.getObColumn().substring(
								k.getObColumn().lastIndexOf('.') + 1));
						if (k.getPropertyName().equals(
								inversefunctionalproperty)) {
							inversefunctionalColumn = k
									.getObColumn()
									.substring(
											k.getObColumn().lastIndexOf('.') + 1);
							inverseFunctionalIndex = insertColumns.size() - 1;
						}
						if (!wholeAdressedProps.contains(k.getPropertyName())) {
							wholeAdressedProps.add(k.getPropertyName());
						} else {
							// No I have to create the dependencies
							addDependencies(k.getPropertyName(),
									allDependencies);
						}
					}
				}
			}

			if (adressedProperties.size() > 0) {
				String namespace = adressedProperties.get(0).substring(0,
						adressedProperties.get(0).indexOf("#"));
				int amountOfIndi = 0;
				for (String str : allConceptsForATable.get(table)) {
					if ((abox.individuals.get(str) != null)
							&& (abox.individuals.get(str).size() > 0)) {
						amountOfIndi = abox.individuals.get(str).size();
						break;
					}
				}

				for (int i = 0; i < amountOfIndi; i++) {
					String sql = "INSERT INTO " + table + "(";
					String update = "UPDATE " + table + " SET ";
					boolean canbeStored = true;
					for (String column : insertColumns) {
						sql += column + ",";
					}
					if (sql.charAt(sql.length() - 1) == ',') {
						sql = sql.substring(0, sql.length() - 1);
					}
					sql += ") VALUES (";
					int index = 0;
					for (String property : adressedProperties) {
						List<DatatypePropertyStatement> statements = abox.dtProperties
								.get(property);
						if (abox.dtProperties.get(property).size() <= i) {
							Logger.getAnonymousLogger().log(
									Level.WARNING,
									"There is no value for: " + property
											+ " at #: " + i);
							// canbeStored = false;
							String column = insertColumns.get(index) + ",";
							String sql2 = "";
							sql2 = sql.substring(0, sql.indexOf(column))
									+ sql.substring(sql.indexOf(column)
											+ column.length());
							sql = sql2;
						}

						String value = abox.dtProperties.get(property).get(i).object
								.toString();
						if ((value == null) || (value.length() < 1)) {
							// canbeStored=false;
							String column = insertColumns.get(index) + ",";
							String sql2 = "";
							sql2 = sql.substring(0, sql.indexOf(column))
									+ sql.substring(sql.indexOf(column)
											+ column.length());
							sql = sql2;
						} else {
							sql += "'" + value + "',";
							update += insertColumns.get(index) + "= '" + value
									+ "',";
							if (insertColumns.get(index).equals(
									inversefunctionalColumn)) {
								inversefunctionalValue = value;
							}
						}
						index++;
					}
					if (sql.charAt(sql.length() - 1) == ',') {
						sql = sql.substring(0, sql.length() - 1);
					}
					if (update.charAt(update.length() - 1) == ',') {
						update = update.substring(0, update.length() - 1);
					}
					sql += ");";
					if (inversefunctionalValue.length() > 2) {
						update += "WHERE " + inversefunctionalColumn + "='"
								+ inversefunctionalValue + "';";
					} else {
						update += ";";
					}
					if (canbeStored) {
						if (sql != null && sql.length() > 5) {
							allSQLInserts.get(table).add(sql);
							allSQLUpdates.get(table).add(update);
						}

					} else {
						Logger.getAnonymousLogger().log(Level.WARNING,
								"Invalid query generated: " + sql);
					}

				}
			}
		}

		// Now, I have a list of sql queries
		sortDependencies(allDependencies);
		// Sorting
		ArrayList<String> sequence = new ArrayList<String>();
		for (Dependencies dep : allDependencies) {

			for (DatatypePropertyMapping map : dep.getAllPrimaryProperties()) {
				if (sequence.contains(map.getTable()) == false) {
					sequence.add(map.getTable());
				}
			}

			for (DatatypePropertyMapping map : dep.getAllNonPrimaryProperties()) {
				if (sequence.contains(map.getTable()) == false) {
					sequence.add(map.getTable());
				}
			}
		}
		for (String str : allSQLInserts.keySet()) {
			if (sequence.contains(str) == false) {
				sequence.add(str);
			}
		}
		// Writen the queries into the database
		openConnection();
		int index = 0;
		for (String query : sequence) {
			Logger.getAnonymousLogger().log(Level.INFO,
					"DB Statement: " + allSQLInserts.get(query));
			Statement s;
			try {
				int queryIndex = 0;
				for (String q : allSQLInserts.get(query)) {
					if (q != null && q.length() > 5) {
						s = connection.createStatement();
						try {
							s.execute(q);

						} catch (SQLException sql) {
							Logger.getAnonymousLogger().log(Level.WARNING,
									sql.getLocalizedMessage());
							try {
								s.execute(allSQLUpdates.get(query).get(
										queryIndex));
							} catch (Exception et) {
								Logger.getAnonymousLogger().log(Level.SEVERE,
										et.getLocalizedMessage());
							}
						}
						s.close();
						queryIndex++;
						index++;
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Logger.getAnonymousLogger().log(Level.SEVERE, e1.getMessage());
				try {
					s = connection.createStatement();
					s.close();
				} catch (SQLException e) {
					Logger.getAnonymousLogger().log(Level.SEVERE,
							e.getMessage());
				}
				index++;

			}
		}

		closeConnection();

	}

	public void insertData_Save(ABox abox) {
		ArrayList<String> allInsertSQLStatemenets = new ArrayList<String>();
		ArrayList<String> allUpdateSQLStatemenets = new ArrayList<String>();
		HashMap<String, String> allInsertQueries = new HashMap<String, String>();
		ArrayList<Dependencies> allDependencies = new ArrayList<Dependencies>();
		for (OntClass c : this.baseModel.tbox.getSimpleClasses().values()) {
			String urlofConcept = c.getUri();
			ArrayList<String> allRelevantDTProperties = new ArrayList<String>();
			for (DatatypeProperty dProp : baseModel.tbox.datatypeProperties
					.values()) {
				Set<String> allDomains = dProp.getDomain();
				if ((allDomains != null) && (allDomains.size() > 0)) {
					for (String str : allDomains) {
						if (str.contains("#")) {
							if (str.equals(urlofConcept)) {
								if (!allRelevantDTProperties.contains(dProp
										.getUri())) {
									allRelevantDTProperties.add(dProp.getUri());
								}
								break;
							}
						} else {
							String conceptName = urlofConcept
									.substring(urlofConcept.indexOf("#") + 1);
							if (str.equals(conceptName)) {
								if (!allRelevantDTProperties.contains(dProp
										.getUri())) {
									allRelevantDTProperties.add(dProp.getUri());
								}
								break;
							}
						}
					}
				}

			}
			// Now I have a List of all relevant and have to remove all
			// irrelevant ones
			ArrayList<String> insertproperties = new ArrayList<String>();

			for (String str : allRelevantDTProperties) {
				if (abox.dtProperties.containsKey(str)) {
					insertproperties.add(str);

				}
			}
			//
			ArrayList<DatatypePropertyMapping> allPrimaryAndFoereignKeyList = new ArrayList<DatatypePropertyMapping>();
			ArrayList<ArrayList<String>> insertColumns = new ArrayList<ArrayList<String>>();
			for (String str : insertproperties) {
				ArrayList<String> oneProperty = new ArrayList<String>();
				ArrayList<DatatypePropertyMapping> onePropertyD = new ArrayList<DatatypePropertyMapping>();
				for (DatatypePropertyMapping dMapping : mapping
						.getDatatypePropertyMapping()) {
					String name = dMapping.getPropertyName();

					if (name.contains("#")) {
						if (str.equals(name)) {
							String columnName = dMapping.getObColumn();
							if (columnName.contains(".")) {
								columnName = columnName.substring(columnName
										.indexOf(".") + 1);
							}
							oneProperty.add(columnName);
							onePropertyD.add(dMapping);
							// break; It has to be possible to have more than
							// one mapping to a datatypeproperty, because we
							// want to supoort primary and foreign keys
						}
					} else {
						if (str.contains("#")) {
							str = str.substring(str.indexOf("#") + 1);
							if (str.equals(name)) {
								String columnName = dMapping.getObColumn();
								if (columnName.contains(".")) {
									columnName = columnName
											.substring(columnName.indexOf(".") + 1);
								}
								oneProperty.add(columnName);
								onePropertyD.add(dMapping);
								// break;
							}
						}

					}

				}
				if (oneProperty.size() == 2) {
					Dependencies depen = new Dependencies();
					// If I have more than one column in this list, the same
					// property is assigned to more than one column.
					// That means, we can have the following situtations:
					// 1. Primary Key /Foreign Key
					// Only a redundency
					// Testen, was der primäre key ist.
					for (int i = 0; i < onePropertyD.size(); i++) {
						List<ColumnMapping> c2 = onePropertyD.get(i)
								.getSubColumn();
						for (ColumnMapping mm : c2) {
							if (onePropertyD.get(i).getObColumn()
									.equals(mm.getValue())) {
								depen.getAllPrimaryProperties().add(
										onePropertyD.get(i));
								continue;
							}
						}
						depen.getAllNonPrimaryProperties().add(
								onePropertyD.get(i));
					}
					allDependencies.add(depen);
				}
				insertColumns.add(oneProperty);
			}
			ArrayList<String> allAdressesTables = new ArrayList<String>();
			// Search the classname
			String tableName = "";
			String namespace = "";
			for (ClassMapping cMaping : mapping.getClassMapping()) {
				String str = cMaping.getClassName();
				if (str.contains("#")) {
					namespace = str.substring(0, str.indexOf('#'));
					if (str.equals(urlofConcept)) {
						tableName = cMaping.getTable();
						allAdressesTables.add(tableName);
					}
				} else {
					tableName = urlofConcept.substring(urlofConcept
							.indexOf("#") + 1);
					allAdressesTables.add(tableName);
				}
			}
			// T´This loop dtermines the number and the set of IDs
			for (int m = 0; m < allAdressesTables.size(); m++) {
				tableName = allAdressesTables.get(m);

				// Now I have to sort the list
				for (int i = 0; i < allDependencies.size(); i++) {

					if (allDependencies.get(i).getAllPrimaryProperties().size() > 0) {
						for (DatatypePropertyMapping mapping : allDependencies
								.get(i).getAllPrimaryProperties()) {
							for (int a = 0; a < allDependencies.size(); a++) {
								if (i != a) {
									for (DatatypePropertyMapping mapping2 : allDependencies
											.get(a)
											.getAllNonPrimaryProperties()) {
										if (mapping2.getPropertyName().equals(
												mapping.getPropertyName())) {
											System.out
													.println("Nun muss getestet werden");
											if (a < i) {
												// Das geht nun, weil ich zuerst
												// die Tabelle mit dem
												// Fremdschlüssel erstellen muss
												allDependencies.add(i + 1,
														allDependencies.get(a));
												a--;
												break;
											}
										}
									}
								}
							}
						}
					}
				}

				ArrayList<ArrayList<Integer>> allIndi = new ArrayList<ArrayList<Integer>>();
				for (int i = 0; i < insertColumns.size(); i++) {
					ArrayList<Integer> currentL = new ArrayList<Integer>();
					allIndi.add(currentL);
					List<DatatypePropertyStatement> allStatments = abox.dtProperties
							.get(insertproperties.get(i));
					for (DatatypePropertyStatement stat : allStatments) {
						try {
							int id = Integer.valueOf(stat.subject.toString());
							if (currentL.contains(id) == false) {
								currentL.add(id);
							}
						} catch (Exception e) {
							Logger.getAnonymousLogger().log(
									Level.SEVERE,
									"Individual has an unknown sytntax of its id: "
											+ stat.subject.toString());
						}
					}
				}
				for (int i = 0; i < allIndi.size() - 1; i++) {
					if (allIndi.get(i).size() != allIndi.get(i + 1).size()) {
						Logger.getAnonymousLogger()
								.log(Level.WARNING,
										"The number of values for each data property is not equal for the indiciudal");
					}
				}
				if (allIndi.size() > 0) {
					for (int x = 0; x < allIndi.get(0).size(); x++) {
						// At this point, I can generate the SQL INSERT INTO
						// INSERT INTO table_name (column1,column2,column3,...)
						// VALUES (value1,value2,value3,...);
						String query = "INSERT INTO " + tableName + "(";
						String update = "UPDATE " + tableName + " SET ";
						boolean isC = false;
						for (int i = 0; i < insertColumns.size(); i++) {
							ArrayList<String> insertColumns2 = insertColumns
									.get(i);
							for (String columnName : insertColumns2) {
								if (isPropertyPartofConcept(columnName,
										tableName)) {
									query += columnName;
									isC = true;
									// query += insertColumns.get(i);
									if (i < insertColumns.size() - 1) {
										query += ",";
									}
								}
							}

						}
						if (query.charAt(query.length() - 1) == ',') {
							query = query.substring(0, query.length() - 1);
						}
						boolean canBeInserted = true;
						query += ") VALUES (";
						for (int i = 0; i < insertColumns.size(); i++) {
							// TODO The case in which a property has more than
							// one value is not cosinder yet
							if (insertproperties.size() == i) {
								Logger.getAnonymousLogger().log(Level.SEVERE,
										"Error,   + Index is too high: " + i);
								continue;
							}
							if (x >= allIndi.get(0).size()) {

								Logger.getAnonymousLogger().log(Level.SEVERE,
										"Error,   + Index is too high: " + x);
								continue;
							}
							String value = abox.dtProperties.get(
									insertproperties.get(i)).get(
									allIndi.get(0).get(x)).object.getString();
							ArrayList<String> insertColumns2 = insertColumns
									.get(i);
							for (String columnName : insertColumns2) {
								if (isPropertyPartofConcept(columnName,
										tableName)) {
									query += "'" + value + "'";
									if ((value == null) || (value.length() < 1)) {
										canBeInserted = false;
									}
									update += columnName + "= '" + value + "'";
									if (i < insertColumns.size() - 1) {
										query += ",";
										update += ",";
									}
								}
							}

						}
						if (query.charAt(query.length() - 1) == ',') {
							query = query.substring(0, query.length() - 1);
						}
						query += ");";
						update += ";";
						if ((insertColumns.size() > 0) && (canBeInserted)
								&& (isC)) {
							allInsertSQLStatemenets.add(query);
							allInsertQueries.put(tableName, query);
							allUpdateSQLStatemenets.add(update);
						}
					}
				}
			}
		}
		// Jetzt muss ich die Depencies liste abbarbeiten und dann den Rest
		ArrayList<String> sequence = new ArrayList<String>();
		for (Dependencies dep : allDependencies) {

			for (DatatypePropertyMapping map : dep.getAllPrimaryProperties()) {
				if (sequence.contains(map.getTable()) == false) {
					sequence.add(map.getTable());
				}
			}

			for (DatatypePropertyMapping map : dep.getAllNonPrimaryProperties()) {
				if (sequence.contains(map.getTable()) == false) {
					sequence.add(map.getTable());
				}
			}
		}
		for (String str : allInsertQueries.keySet()) {
			if (sequence.contains(str) == false) {
				sequence.add(str);
			}
		}

		// TODO The DB can have primary and foreihn keys. To handle this, 1. the
		// objectporperties have to be implemented; 2. the sequence of inserts
		// has to be considered
		openConnection();
		int index = 0;
		for (String query : sequence) {
			Logger.getAnonymousLogger().log(Level.INFO,
					"DB Statement: " + allInsertQueries.get(query));
			Statement s;
			try {
				s = connection.createStatement();
				s.execute(allInsertQueries.get(query));
				s.close();
				index++;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Logger.getAnonymousLogger().log(Level.SEVERE, e1.getMessage());
				try {
					s = connection.createStatement();
					// s.execute(allUpdateSQLStatemenets.get(index));
					s.close();
					Logger.getAnonymousLogger().log(
							Level.INFO,
							"Executed SQL: "
									+ allUpdateSQLStatemenets.get(index));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					Logger.getAnonymousLogger().log(Level.SEVERE,
							e.getMessage());
				}
				index++;

			}
		}

		closeConnection();
	}

	boolean isPropertyPartofConcept(String propertyName, String tableName) {
		for (DatatypePropertyMapping m : mapping.getDatatypePropertyMapping()) {
			if (m.getObColumn().contains(tableName)
					&& m.getObColumn().contains(propertyName)) {
				return true;
			}
		}

		return false;
	}
}
