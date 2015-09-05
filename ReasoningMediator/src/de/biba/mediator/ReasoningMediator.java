package de.biba.mediator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import com.sun.tools.apt.util.Bark;

import de.biba.mediator.constraints.ClassConstraint;
import de.biba.mediator.constraints.Constraint;
import de.biba.mediator.constraints.PropertyConstraint;
import de.biba.mediator.constraints.SimpleConstraint;
import de.biba.mediator.constraints.SimplePropertyConstraint;
import de.biba.mediator.constraints.insert.ClassInsertConstraint;
import de.biba.mediator.constraints.insert.InsertConstraint;
import de.biba.mediator.constraints.insert.PropertyInsertConstraint;
import de.biba.mediator.constraints.insert.SimplePropertyInsertConstraint;
import de.biba.mediator.converter.ValueConverter;
import de.biba.ontology.ABox;
import de.biba.ontology.AbstractProperty;
import de.biba.ontology.ComplexOntClass;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.OntModel;
import de.biba.queryLanguage.ParseException;
import de.biba.queryLanguage.SimpleQueryParser;
import de.biba.report.IntegrationReport;
import de.biba.wrapper.Consistency;
import de.biba.wrapper.DataSource;
import de.biba.wrapper.DataSourceDescription;
import de.biba.wrapper.DataSourceV2;
import de.biba.wrapper.IDataSourceDescription;
import de.biba.wrapper.ValidationReport;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut f�r Produktion und Logistik GmbH)]

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
 * Implementierung von IMediator. Wrapper können bei dieser Implementierung verschiedenen Ebenen zugeordnet werden. Die Ebenen bestimmen die Abfrage-Reihenfolge der Wrapper. 
 * Dadurch können Wrapper auf die Ergebnisse der vorherigen Wrapper zugreifen.
 * 
 * @author KRA
 * @see IMediator
 * @see DataSource
 */
public class ReasoningMediator extends DataSource implements IMediator {

	private ArrayList<DataSource>[] dataSources;
	private List<IDataSourceDescription> dataSourceDescriptions = null;
	private HashMap<String, Boolean> allFunctionalProperties = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> allNotFunctionalProperties = new HashMap<String, Boolean>();
	static long MAXINDIVIDUALSPERDATASOURCE = 1000000000000L;
	static final int DATASOURCELEVELS = 100;
	private int datasourceCtr = 0;
	ValueConverter converter;

	public ReasoningMediator() {
		super();
		setDataSources(new ArrayList[DATASOURCELEVELS]);
		converter = ValueConverter.getInstance();
	}

	/**
	 * Wertet eine Select-Query aus
	 * 
	 * @param pQuery Select-Query
	 * @return Ergebnis der Query
	 */
	public QuerySolution query(OutputQuery pQuery) {
		long time = System.currentTimeMillis();
		pQuery.constraint.insertComplexClassConstraints(baseModel);
		
		//Query zu einfachen Constraints machen
		List<SimpleConstraint> simpleConstraints = pQuery.constraint
				.flatToSimpleConstraints();

		List<String> classConstraints = new ArrayList<String>();
		List<SimplePropertyConstraint> simplePropertyContraints = new ArrayList<SimplePropertyConstraint>();
		List<String> propertyConstraints = new ArrayList<String>();

		//Query erweitern
		extendQuery(simpleConstraints);

		//Constraints der Query sortieren
		sortConstraints(simpleConstraints, classConstraints,
				simplePropertyContraints, propertyConstraints);

		//Für jede Ebene der Wrapper
		for(int i = 0; i < DATASOURCELEVELS; i++){
			//Wenn Wrapper in dieser Ebene vorhanden
			if(getDataSources()[i] != null){
				//Für jeden Wrapper der Ebene
				for(DataSource ds : getDataSources()[i]){
					//Wrapper-spezifische Query aus ursprünglicher Query erstellen
					DataSourceQuery dsc = generateDatasourceSpecificConstraints(
							ds.getOntModel(), classConstraints, simplePropertyContraints,
							propertyConstraints);
					
					if(dsc != null){
						//Bei Ebenen > 0 muss der Query die ABox hinzugefügt werden
						if(i > 0){
							dsc.setAbox(baseModel.abox);
						}
						//Query an Wrapper senden
						OntModel om = ds.queryData(dsc);
						if(om != null){
							ABox tmp = om.abox;
							//Ergebnis-Ontologie einarbeiten
							baseModel.abox.add(tmp, converter);
						}
					}
				}
			}
		}
		System.out.println("Zeit f�rs Abfragen der Daten:"
				+ (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		
		//Duplikate entfernen
		DuplicateRemover dr = new DuplicateRemover();
		dr.removeDuplicates(baseModel);
		System.out.println("Anzahl der Triple:" + baseModel.getTripleCount());
		System.out.println("Zeit f�rs Entfernen der Duplikate:"
				+ (System.currentTimeMillis() - time));
		
		//Constraints der Query auf Ergebnis aller Wrapper anwenden
		QuerySolution result = pQuery.solve(baseModel);

		//ABox leeren
		baseModel.abox = null;
		baseModel.abox = new ABox();
		baseModel.abox.units = new HashMap<String, String>();
		if(pQuery.isDistinct())
			result.removeDuplicates();
		return result;
	}

	
	public OntModelAndSolution queryOntModel(OutputQuery pQuery) {
		long time = System.currentTimeMillis();
		pQuery.constraint.insertComplexClassConstraints(baseModel);
		
		//Query zu einfachen Constraints machen
		List<SimpleConstraint> simpleConstraints = pQuery.constraint
				.flatToSimpleConstraints();

		List<String> classConstraints = new ArrayList<String>();
		List<SimplePropertyConstraint> simplePropertyContraints = new ArrayList<SimplePropertyConstraint>();
		List<String> propertyConstraints = new ArrayList<String>();

		//Query erweitern
		extendQuery(simpleConstraints);

		//Constraints der Query sortieren
		sortConstraints(simpleConstraints, classConstraints,
				simplePropertyContraints, propertyConstraints);

		//Für jede Ebene der Wrapper
		for(int i = 0; i < DATASOURCELEVELS; i++){
			//Wenn Wrapper in dieser Ebene vorhanden
			if(getDataSources()[i] != null){
				//Für jeden Wrapper der Ebene
				for(DataSource ds : getDataSources()[i]){
					//Wrapper-spezifische Query aus ursprünglicher Query erstellen
					DataSourceQuery dsc = generateDatasourceSpecificConstraints(
							ds.getOntModel(), classConstraints, simplePropertyContraints,
							propertyConstraints);
					
					if(dsc != null){
						//Bei Ebenen > 0 muss der Query die ABox hinzugefügt werden
						if(i > 0){
							dsc.setAbox(baseModel.abox);
						}
						//Query an Wrapper senden
						OntModel om = ds.queryData(dsc);
						if(om != null){
							ABox tmp = om.abox;
							//Ergebnis-Ontologie einarbeiten
							baseModel.abox.add(tmp, converter);
						}
					}
				}
			}
		}
		System.out.println("Zeit f�rs Abfragen der Daten:"
				+ (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		
		//Duplikate entfernen
		DuplicateRemover dr = new DuplicateRemover();
		dr.removeDuplicates(baseModel);
		System.out.println("Anzahl der Triple:" + baseModel.getTripleCount());
		System.out.println("Zeit f�rs Entfernen der Duplikate:"
				+ (System.currentTimeMillis() - time));
		
		//Constraints der Query auf Ergebnis aller Wrapper anwenden
		QuerySolution result = pQuery.solve(baseModel);
		
		OntModelAndSolution oMAS = new OntModelAndSolution(baseModel, result);

		//ABox leeren
		baseModel.abox = null;
		baseModel.abox = new ABox();
		baseModel.abox.units = new HashMap<String, String>();
		if(pQuery.isDistinct())
			result.removeDuplicates();
		return oMAS; //TODO: Überprüfen!!!!!!!!!!!
	}
	
	/**
	 * Sortiert Constraints in ClassConstraints, Property-Constraints und SimplePropertyConstraint
	 * @param pSimpleConstraints Liste mit Constraints die es einzusortieren gilt
	 * @param pClassConstraints Liste in der die ClassConstraints sortiert werden sollen
	 * @param pSimplePropertyContraints Liste in der die SimplePropertyConstraint sortiert werden sollen
	 * @param pPropertyConstraints Liste der angefragten Properties
	 */
	private void sortConstraints(List<SimpleConstraint> pSimpleConstraints,
			List<String> pClassConstraints,
			List<SimplePropertyConstraint> pSimplePropertyContraints,
			List<String> pPropertyConstraints) {
		for(SimpleConstraint sc : pSimpleConstraints){
			if(sc.getType() == 0){
				ClassConstraint cc = (ClassConstraint) sc;
				if(baseModel.tbox.complexClasses.containsKey(cc.className)){
					ComplexOntClass complex = baseModel.tbox.complexClasses
							.get(cc.className);
					for(Constraint c : complex.constraints){
						c.setSubject(cc.subject);
						sortConstraints(c.flatToSimpleConstraints(), pClassConstraints,
								pSimplePropertyContraints, pPropertyConstraints);
					}
				}else
					pClassConstraints.add(cc.className);
			}else if(sc.getType() == 1)
				pSimplePropertyContraints.add((SimplePropertyConstraint) sc);
			else if(sc.getType() == 2){
				if(!pPropertyConstraints.contains(sc.getPredicate()))
					pPropertyConstraints.add(sc.getPredicate());
			}
		}
	}
	
	
	/**
	 * Erstellt eine Wrapper-Spezifische Query einer ursprünglichen Query. Hierbei werden angefragte Konzepte/Properties aus der Query aussortiert, welche nicht von dem Wrapper beantwortet werden können.
	 * @param pOntModel Ontologie die die Datenquelle des Wrappers beschreibt
	 * @param pClassConstraints Liste mit ClassConstraints der ursprünglichen Anfrage
	 * @param pSimplePropertyContraints  Liste mit SimplePropertyConstraint der ursprünglichen Anfrage
	 * @param pPropertyConstraints Liste mit angefragten Properties der ursprünglichen Anfrage
	 * @return Neue Query, welche nur noch Constraints enthält, welche vom Wrapper bedient werden können.
	 */
	private DataSourceQuery generateDatasourceSpecificConstraints(
			OntModel pOntModel, List<String> pClassConstraints,
			List<SimplePropertyConstraint> pSimplePropertyContraints,
			List<String> pPropertyConstraints) {
		DataSourceQuery result = new DataSourceQuery();
		boolean ok = false;
		for(String className : pClassConstraints){
			Set<String> related = baseModel
					.getAllSubOrEquivalentClassNames(className);
			for(String r : related)
				if(pOntModel.tbox.containsClass(r)){
					result.classNames.add(r);
					ok = true;
				}
		}
		for(String propName : pPropertyConstraints){
			Set<String> related = baseModel.getAllSubOrEquivalentProperties(propName);
			for(String r : related){
				if(pOntModel.tbox.containsProperty(r)){
					result.propertyNames.add(r);
					ok = true;
				}
			}
		}
		for(SimplePropertyConstraint spc : pSimplePropertyContraints){
			String propName = spc.predicate;
			Set<String> related = baseModel.getAllSubOrEquivalentProperties(propName);
			for(String r : related){
				if(pOntModel.tbox.containsProperty(r)){
					result.propertiesWithValues.add(result.new PropertyWithValue(r,
							spc.value, spc.comparator));
					ok = true;
				}
			}
		}
		if(ok)
			return result;
		else
			return null;
	}

	private void removeUselessConstraints(
			List<ClassConstraint> pClassConstraints,
			List<SimplePropertyConstraint> pSimplePropertyContraints,
			List<PropertyConstraint> pPropertyConstraints) {
		// TODO:
	}

	/**
	 * Diese Methode �bertr�gt die funktionalen und nicht funktionalen Properties in HashMaps.
	 * Hierbei wird nur di tbox betrachtet, weil wir uns in der Initalisierungsphase der Wrapper befinden 
	 * @param ont Das Ontmodel, welches die Ontologie repr�sentiert.
	 */
	private void extractDataProperties(OntModel ont){
		Set<String> allKeys = ont.tbox.datatypeProperties.keySet();
		for (String key: allKeys){
			DatatypeProperty dP = ont.tbox.getDatatypeProperties().get(key);
			if (dP.isFunctional()){
				if (!(allFunctionalProperties.containsKey(dP.getUri()))){
					allFunctionalProperties.put(dP.getUri(), true);
				}
			}
			else{
				if (!(allNotFunctionalProperties.containsKey(dP.getUri()))){
					allNotFunctionalProperties.put(dP.getUri(), true);
				}
			}
				
			
		}
		
	}
	
	private IntegrationReport testMonotonicity(){
		IntegrationReport result = new IntegrationReport();
		
		return result;
	}
	
	
	@Override
	public void addDataSource(DataSource pDataSource, int lvl) {
		if(lvl >= DATASOURCELEVELS || lvl < 0)
			return;
		//Man ben�tigt die Information,welche DataProperties funktional sind
		extractDataProperties(pDataSource.getOntModel());
		baseModel.tbox.add(pDataSource.getOntModel().tbox);
		pDataSource.getOntModel().setIndividualCounter(
				MAXINDIVIDUALSPERDATASOURCE * datasourceCtr);
		datasourceCtr++;
		if(getDataSources()[lvl] == null)
			getDataSources()[lvl] = new ArrayList<DataSource>();

		getDataSources()[lvl].add(pDataSource);
		dataSourceDescriptions = null;
	}

	/**
	 * Liefert eine Map zurück, die zu jeder Klasse ihre InverseFunctional-Properties beinhaltet.
	 * @return Map in der der Key der Klassenname ist und der Value ein Set mit invers-funktionalen Properties
	 */
	private HashMap<String, Set<AbstractProperty>> getAllInverseFunctionalProperties() {
		HashMap<String, Set<AbstractProperty>> inverseFunctionalProperties = new HashMap<String, Set<AbstractProperty>>();
		for(DatatypeProperty dp : this.baseModel.tbox.datatypeProperties.values()){
			if(dp.isInverseFunctional()){
				for(String domain : dp.getDomain()){
					Set<String> ss = this.baseModel
							.getAllSubOrEquivalentClassNames(domain);
					if(ss != null){
						for(String s : ss){
							if(inverseFunctionalProperties.containsKey(s)){
								inverseFunctionalProperties.get(s).add(dp);
							}else{
								Set<AbstractProperty> data = new HashSet<AbstractProperty>();
								data.add(dp);
								inverseFunctionalProperties.put(s, data);
							}
						}
					}
				}
			}
		}
		for(ObjectProperty dp : this.baseModel.tbox.objectProperties.values()){
			if(dp.isInverseFunctional()){
				for(String domain : dp.getDomain()){
					Set<String> ss = this.baseModel
							.getAllSubOrEquivalentClassNames(domain);
					if(ss != null){
						for(String s : ss){
							if(inverseFunctionalProperties.containsKey(s)){
								inverseFunctionalProperties.get(s).add(dp);
							}else{
								Set<AbstractProperty> data = new HashSet<AbstractProperty>();
								data.add(dp);
								inverseFunctionalProperties.put(s, data);
							}
						}
					}
				}
				for(String range : dp.getRange()){
					Set<String> ss = this.baseModel
							.getAllSubOrEquivalentClassNames(range);
					if(ss != null){
						for(String s : ss){
							if(inverseFunctionalProperties.containsKey(s)){
								inverseFunctionalProperties.get(s).add(dp);
							}else{
								Set<AbstractProperty> data = new HashSet<AbstractProperty>();
								data.add(dp);
								inverseFunctionalProperties.put(s, data);
							}
						}
					}
				}
			}
		}
		return inverseFunctionalProperties;
	}

	/**
	 * Diese Methode erweitert eine Query um weitere Constraints. Diese Constraints sollen die "ID-Felder" einzelner Klassen mit abfragen.
	 * Anwendungsbeispiel: Zwei Datenquellen, beide besitzen das Konzept/die Klasse "Artikel". Diese Klasse besitzt die beiden Eigenschaften/Properties "artikelnummer" und "name".
	 * "artikelnummer" ist invers-funktional, beschreibt/identifiziert also ein individuum eindeutig! Die Abfrage des Users fragt aber nur nach der Eigenschaft "name". D.h. die Wrapper würden nur
	 * nach dem "name"-Property gefragt. Da der Mediator dann die einzelnen Individuen/Instanzen nicht mehr identifizieren kann (die ID der Instanzen wurde ja nicht mitgeliefert), muss die Query also
	 * vorher entsprechend um das ID-Attribut erweitert werden.
	 * @param simpleConstraints Liste mit den Constraints der User-Anfrage
	 */
	private void extendQuery(List<SimpleConstraint> simpleConstraints) {
		HashMap<String, Set<AbstractProperty>> inverseFunctionalProperties = getAllInverseFunctionalProperties();

		int ctr = 0;
		List<PropertyConstraint> lpc = new ArrayList<PropertyConstraint>();
		
		//Für jedes Constraint
		for(SimpleConstraint sc : simpleConstraints){
			//Wenn es sich um ein Property-Constraint handelt...
			if(sc instanceof PropertyConstraint){
				PropertyConstraint pc = (PropertyConstraint) sc;
				String prop = pc.getPredicate();
				//Wenn ein DatatypeProperty gefragt ist...
				if(this.baseModel.tbox.datatypeProperties.containsKey(prop)){
					DatatypeProperty dp = this.baseModel.tbox.datatypeProperties
							.get(prop);
					Set<String> domain = dp.getDomain();
					//Wenn Domäne des Properties nicht leer
					if(domain != null){
						for(String s : domain){
							//Wenn in der Domäne eine Klasse, die ein invers-funktionales Property besitzt
							if(inverseFunctionalProperties.containsKey(s)){
								Set<AbstractProperty> sap = inverseFunctionalProperties.get(s);
								for(AbstractProperty ap : sap){
									//... dann erstelle für dieses Property ein neues Constraint
									PropertyConstraint constraint = new PropertyConstraint(
											pc.getSubject(), ap.getUri(), "!x" + ctr);
									lpc.add(constraint);
									ctr++;
								}

							}
						}
					}
				}else if(this.baseModel.tbox.objectProperties.containsKey(prop)){
					ObjectProperty op = this.baseModel.tbox.objectProperties.get(prop);
					Set<String> domain = op.getDomain();
					if(domain != null){
						for(String s : domain){
							if(inverseFunctionalProperties.containsKey(s)){
								Set<AbstractProperty> sap = inverseFunctionalProperties.get(s);
								for(AbstractProperty ap : sap){
									PropertyConstraint constraint = new PropertyConstraint(
											pc.getSubject(), ap.getUri(), "!x" + ctr);
									lpc.add(constraint);
									ctr++;
								}

							}
						}
					}

					Set<String> range = op.getRange();
					if(range != null){
						for(String s : range){
							if(inverseFunctionalProperties.containsKey(s)){
								Set<AbstractProperty> sap = inverseFunctionalProperties.get(s);
								for(AbstractProperty ap : sap){
									PropertyConstraint constraint = new PropertyConstraint("!x"
											+ ctr, ap.getUri(), pc.getObject());
									lpc.add(constraint);
									ctr++;
								}

							}
						}
					}
				}
			}
		}
		simpleConstraints.addAll(lpc);
	}

	@Override
	public void removeAllDataSources(){
		
		for(int i = 0; i < DATASOURCELEVELS; i++){
			if(getDataSources()[i] != null){
				
				getDataSources()[i] = null;
			}
			}
		allFunctionalProperties.clear(); //Die Liste mit den funktionalen Properties muss gelerrt werden
		allNotFunctionalProperties.clear(); //Die Liste mit den nicht funktionalen Properties muss gelerrt werden
		baseModel = new OntModel();
		dataSourceDescriptions = null;
	}
	
	@Override
	public void removeDataSource(DataSource pDataSource) {
		boolean removed = false;
		for(int i = 0; i < DATASOURCELEVELS; i++){
			if(getDataSources()[i] == null)
				continue;
			if(getDataSources()[i].contains(pDataSource)){
				getDataSources()[i].remove(pDataSource);
				datasourceCtr--;
				removed = true;
				allFunctionalProperties.clear(); //Die Liste mit den funktionalen Properties muss gelerrt werden
				allNotFunctionalProperties.clear(); //Die Liste mit den nicht funktionalen Properties muss gelerrt werden
			}
			dataSourceDescriptions = null;
		}

		if(removed){
			baseModel = new OntModel();
			int ctr = 0;
			for(int i = 0; i < DATASOURCELEVELS; i++){
				if(getDataSources()[i] == null)
					continue;
				for(DataSource ds : getDataSources()[i]){
					extractDataProperties(ds.getOntModel());
					baseModel.tbox.add(ds.getOntModel().tbox);
					ds.getOntModel().setIndividualCounter(
							MAXINDIVIDUALSPERDATASOURCE * ctr);
					ctr++;
				}
			}
		}
	}

	@Override
	public OntModel queryData(DataSourceQuery pDsc) {
		return null;
	}

	@Override
	public IQuerySolution query(String pQuery, boolean pAllowQuerying,
			boolean pAllowWriting) {
		StringReader sr = new StringReader(pQuery);
		SimpleQueryParser sqp = new SimpleQueryParser(sr);
		try{
			IQuery q = sqp.parse(pAllowQuerying, pAllowWriting);
			if(!q.isIntputQuery())
				return query((OutputQuery) q);
			else
				return insert((InputQuery) q);
		}catch(ParseException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		finally{
			sr.close();			
		}
		return null;
	}
	
	@Override
	public OntModelAndSolution queryOntModel(String pQuery,	boolean pAllowQuerying, boolean pAllowWriting) {
		StringReader sr = new StringReader(pQuery);
		SimpleQueryParser sqp = new SimpleQueryParser(sr);
		try{
			IQuery q = sqp.parse(pAllowQuerying, pAllowWriting);
			if(!q.isIntputQuery())
				return queryOntModel((OutputQuery) q);
			else{
				insert((InputQuery) q);
				return null;
			}
		}catch(ParseException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		finally{
			sr.close();			
		}
		return null;
	}

	private IQuerySolution insert(InputQuery pQ) {
		for(int i = 0; i < DATASOURCELEVELS; i++){
			if(getDataSources()[i] == null)
				continue;
			for(DataSource ds : getDataSources()[i]){
				InputQuery specific = generateDatasourceSpecificInput(ds.getOntModel(),
						pQ);
				ds.insertData(specific);
			}
		}
		return null;
	}

	/**
	 * Erstellt eine Wrapper-Spezifische Query einer ursprünglichen Query. Hierbei werden einzufügende Konzepte/Properties-Statements aus der Query aussortiert, welche nicht von dem Wrapper bearbeitet werden können.
	 * @param ontModel Ontologie die die Datenquelle des Wrappers beschreibt
	 * @param pQ Ursprüngliche Query
	 * @return Neue Query, welche nur noch Constraints enthält, welche vom Wrapper bedient werden können.
	 */
	private InputQuery generateDatasourceSpecificInput(OntModel ontModel,
			InputQuery pQ) {
		InputQuery result = new InputQuery();
		boolean ok = false;

		for(InsertConstraint ic : pQ.getConstraints()){
			if(ic instanceof ClassInsertConstraint){
				ClassInsertConstraint cic = (ClassInsertConstraint) ic;
				Set<String> related = baseModel.getAllSubOrEquivalentClassNames(cic
						.getClassName());
				for(String r : related)
					if(ontModel.tbox.containsClass(r)){
						result.addConstraint(new ClassInsertConstraint(cic
								.getVariableName(), r));
						ok = true;
					}
			}else if(ic instanceof PropertyInsertConstraint){
				PropertyInsertConstraint pic = (PropertyInsertConstraint) ic;
				Set<String> related = baseModel.getAllSubOrEquivalentProperties(pic
						.getPredicate());
				for(String r : related){
					if(ontModel.tbox.containsProperty(r)){
						result.addConstraint(new PropertyInsertConstraint(pic
								.getSubVarName(), r, pic.getObVarName()));
						ok = true;
					}
				}
			}else if(ic instanceof SimplePropertyInsertConstraint){
				SimplePropertyInsertConstraint spic = (SimplePropertyInsertConstraint) ic;
				Set<String> related = baseModel.getAllSubOrEquivalentProperties(spic
						.getPredicate());
				for(String r : related){
					if(ontModel.tbox.containsProperty(r)){
						result.addConstraint(new SimplePropertyInsertConstraint(spic
								.getSubVarName(), r, spic.getData(), spic.getUnit()));
						ok = true;
					}
				}
			}
		}
		if(ok)
			return result;
		else
			return null;
	}

	@Override
	public OntModel getModel() {
		return baseModel;
	}

	public ValidationReport validateConfiguration() {
		// TODO Auto-generated method stub
		return new ValidationReport(true, true);
	}

	public ArrayList<DataSource>[] getDataSources() {
		return dataSources;
	}

	public void setDataSources(ArrayList<DataSource>[] dataSources) {
		this.dataSources = dataSources;
	}
	
	public class OntModelAndSolution {
		OntModel ontModel;
		QuerySolution querySolution;
		
		public OntModelAndSolution(OntModel ontModel, QuerySolution querySolution) {
			this.ontModel = ontModel;
			this.querySolution = querySolution;
		}

		public OntModel getOntModel() {
			return ontModel;
		}

		public void setOntModel(OntModel ontModel) {
			this.ontModel = ontModel;
		}

		public QuerySolution getQuerySolution() {
			return querySolution;
		}

		public void setQuerySolution(QuerySolution querySolution) {
			this.querySolution = querySolution;
		}
	}

	@Override
	public void initialize(String pPropertyFile) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IDataSourceDescription> getLinkedDataSources() {
		if (dataSourceDescriptions == null){
		ArrayList<IDataSourceDescription> result = new ArrayList<IDataSourceDescription>();
		if((dataSources != null) && (dataSources.length>0)){
		for (ArrayList<DataSource> dList : dataSources){
			if (dList != null){
			for (DataSource d: dList){
				DataSourceDescription description = new DataSourceDescription();
				description.setModel(d.getOntModel());
				description.setNameOfClass(d.getClass().getName());
				description.setPathOfPropertyFile(d.getPathOfConfigurationFile());
				if (d instanceof DataSourceV2){
					ValidationReport report = ((DataSourceV2) d).validateConfiguration();
					if (report.isConsistent()){
						description.setConsistence(Consistency.TRUE);
					}
					else{
						description.setConsistence(Consistency.FALSE);
						System.err.println (description.getNameOfClass() +":"+ report.getErrorMessage());
					}
				}
				
				
				result.add(description);
			}
			}
		}
		}
		dataSourceDescriptions = result;
		}
		return dataSourceDescriptions;
	}

	@Override
	public String getPathOfConfigurationFile() {
		// TODO Auto-generated method stub
		return null;
	}
}
