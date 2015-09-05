package de.biba.wrapper.newSQLWrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.biba.ontology.Individual;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;
import de.biba.wrapper.newSQLWrapper.Mapping.ClassMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.ColumnMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.Condition;
import de.biba.wrapper.newSQLWrapper.Mapping.DatatypePropertyMapping;
import de.biba.wrapper.newSQLWrapper.Mapping.JoinedTable;
import de.biba.wrapper.newSQLWrapper.Mapping.MultiJoinedTable;
import de.biba.wrapper.newSQLWrapper.Mapping.ObjectPropertyMapping;

/**[SQL Wrapper. This is a wrapper to tranform a xml based data base schema to an ontology]
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
public class QueryExecutioner {

	private Set<String> columns = new HashSet<String>();
	private List<String> types = new ArrayList<String>();
	private String table;
	private boolean isJoinedTable;
	private boolean fullOuterJoin;
	private String driverName ="";
	private OntModel model;
	private Map<String, IndividualCreator> individualCreators =  new HashMap<String, IndividualCreator>();
	private List<ObjectPropertyCreator> objectPropertyCreators = new LinkedList<ObjectPropertyCreator>();
	private Map<String, List<IndividualCreator>> globalIndividualCreators;
	private Map<String,OntClass> mostSpecificClass;
	
	public QueryExecutioner(String drivername, ClassMapping classMapping, OntModel tBox, Map<String, List<IndividualCreator>> ic, Map<String,OntClass> m) {
		this.driverName = drivername;
		this.model = tBox;
		this.globalIndividualCreators = ic;
		this.mostSpecificClass = m;
		isJoinedTable = false;
		fullOuterJoin = false;
		table = classMapping.getTable();
		extend(classMapping);
	}

	public QueryExecutioner(String drivername, DatatypePropertyMapping pm, OntModel tBox, Map<String, List<IndividualCreator>> ic, Map<String,OntClass> m) {
		this.driverName = drivername;
		fullOuterJoin = false;
		this.model = tBox;
		this.globalIndividualCreators = ic;
		this.mostSpecificClass = m;
		
		extend(pm);
		
		if(pm.getTable() != null && !pm.getTable().isEmpty()){
			table = pm.getTable();
			isJoinedTable = false;
		}
		else if(pm.getJoin() != null){
			JoinedTable jt = pm.getJoin();
			table = buildJoin(jt);
			isJoinedTable = true;
		}
	}
	

	public QueryExecutioner(ObjectPropertyMapping pm, OntModel tBox, Map<String, List<IndividualCreator>> ic, Map<String,OntClass> m) {
		fullOuterJoin = false;
		this.model = tBox;
		this.globalIndividualCreators = ic;
		this.mostSpecificClass = m;
		
		extend(pm);
		
		if(pm.getTable() != null && !pm.getTable().isEmpty()){
			table = pm.getTable();
			isJoinedTable = false;
		}
		else if(pm.getJoin() != null){
			MultiJoinedTable jt = pm.getJoin();
			table = buildJoin(jt);
			isJoinedTable = true;
		}
	}
	
	private String buildJoin(MultiJoinedTable jt) {
		
		StringBuffer sb = new StringBuffer();
		if(jt.getThirdTable()!=null)
			sb.append("(");
		
		sb.append(jt.getFirstTable());
		sb.append(" join ");
		sb.append(jt.getSecondTable());
		sb.append(" on ");
		boolean first = true;
		for(String s : jt.getJoinCondition()){
			if(!first)
				sb.append(" AND ");
			sb.append(s);
			first = false;
		}
		if(jt.getThirdTable()!=null){
			sb.append(") join ");
			sb.append(jt.getThirdTable());
			sb.append(" on ");
			first = true;
			for(String s : jt.getExtendedCondition()){
				if(!first)
					sb.append(" AND ");
				sb.append(s);
				first = false;
			}
		}
		return sb.toString();
	}
	private String buildJoin(JoinedTable jt) {
		StringBuffer sb = new StringBuffer();
		sb.append(jt.getFirstTable());
		sb.append(" join ");
		sb.append(jt.getSecondTable());
		sb.append(" on ");
		boolean first = true;
		for(String s : jt.getJoinCondition()){
			if(!first)
				sb.append(" AND ");
			sb.append(s);
			first = false;
		}
		return sb.toString();
	}

	public void extend(ClassMapping cm) {
		if(this.table.indexOf(cm.getTable()) < 0)
			throw new RuntimeException("Query should be extended with wrong table");
		
		boolean first = true;
		StringBuffer sb = new StringBuffer();
		if(!this.model.tbox.containsClass(cm.getClassName()))
			throw new RuntimeException("Klasse nicht in Ontologie enthalten");
		for(ColumnMapping cmapping : cm.getColumn()){
			if(first){
				sb.append(cmapping.getValue());
				first = false;
			}
			else{
				sb.append("/");
				sb.append(cmapping.getValue());
			}
			if(columns.add(cmapping.getValue()))
				types.add(cmapping.getValueType().toString());
				
		}
		if(!individualCreators.containsKey(sb.toString())){
			IndividualCreator a = new IndividualCreator(driverName, this.model.tbox.getSimpleClasses().get(cm.getClassName()), cm.getColumn(), cm.getConditions());
			individualCreators.put(sb.toString(), a);
//			if(globalIndividualCreators.containsKey(sb.toString())){
//				IndividualCreator ic = globalIndividualCreators.get(sb.toString());
//				if(this.model.isSubclassOf(a.getOntclass().getUri(), ic.getOntclass().getUri())){
//					a.setOntclass(ic.getOntclass());
//				}
//				else if(this.model.isSubclassOf(ic.getOntclass().getUri(), a.getOntclass().getUri()))
//					ic.setOntclass(a.getOntclass());
//			}
			checkGlobalIndividualCreators(sb.toString(), a);
		}
		
		
		if(isJoinedTable)
			fullOuterJoin = true;
		
		
		if(cm.getConditions() != null
				&& !cm.getConditions().isEmpty()){
			// TODO:Conditions hinzufÃ¼gen
		}
	}

	
	private void checkGlobalIndividualCreators(String key, IndividualCreator ic){
		if(mostSpecificClass.containsKey(key)){
			OntClass oc = mostSpecificClass.get(key);
			if(this.model.isSubclassOf(oc.getUri(), ic.getOntclass().getUri())){
				for(IndividualCreator ic2 : this.globalIndividualCreators.get(key))
					ic2.setOntclass(ic.getOntclass());
				this.mostSpecificClass.put(key, ic.getOntclass());
			}
			else if(this.model.isSubclassOf(ic.getOntclass().getUri(), oc.getUri()))
				ic.setOntclass(oc);
		}
		else{
			mostSpecificClass.put(key, ic.getOntclass());
			this.globalIndividualCreators.put(key, new LinkedList<IndividualCreator>());
		}
		this.globalIndividualCreators.get(key).add(ic);
	}
	
	
	public void extend(DatatypePropertyMapping pm) {
		boolean first = true;
		StringBuffer sb = new StringBuffer();
		if(!this.model.tbox.containsProperty(pm.getPropertyName()))
			throw new RuntimeException("DatatypeProperty \"" + pm.getPropertyName()+"\" nicht in Ontologie enthalten");
		for(ColumnMapping cm : pm.getSubColumn()){
			if(first){
				sb.append(cm.getValue());
				first = false;
			}
			else{
				sb.append("/");
				sb.append(cm.getValue());
			}
			if(columns.add(cm.getValue()))
				types.add(cm.getValueType().toString());
				
		}
		
		if(columns.add(pm.getObColumn()))
			types.add(pm.getObType().toString());
		//Conditions behandeln
		for(Condition c: pm.getConditions()){
			if(columns.add(c.getColumn()))
				types.add(c.getValueType().toString());
		}
		String key = sb.toString();
		IndividualCreator ic = null;
		if(individualCreators.containsKey(key))
			ic = individualCreators.get(key);
		else{
			ic = new IndividualCreator(driverName, OntModel.THING, pm.getSubColumn(), null);
			individualCreators.put(key, ic);
			checkGlobalIndividualCreators(key, ic);
		}
		ic.addDataProperty(pm, this.model.tbox.getDatatypeProperties().get(pm.getPropertyName()));
	}
	
	public void extend(ObjectPropertyMapping pm) {
		boolean first = true;
		StringBuffer sb = new StringBuffer();
		if(!this.model.tbox.containsProperty(pm.getPropertyName()))
			throw new RuntimeException("ObjectProperty \"" + pm.getPropertyName()+"\" nicht in Ontologie enthalten");
		for(ColumnMapping cm : pm.getSubColumn()){
			if(first){
				sb.append(cm.getValue());
				first = false;
			}
			else{
				sb.append("/");
				sb.append(cm.getValue());
			}
			if(columns.add(cm.getValue()))
				types.add(cm.getValueType().toString());
				
		}
		String keySub = sb.toString();
		IndividualCreator icSub = null;
		if(individualCreators.containsKey(keySub))
			icSub = individualCreators.get(keySub);
		else{
			icSub = new IndividualCreator(driverName, OntModel.THING, pm.getSubColumn(), null);
			individualCreators.put(keySub, icSub);
			checkGlobalIndividualCreators(keySub, icSub);
		}
		
		sb = new StringBuffer();
		first = true;
		for(ColumnMapping cm : pm.getObColumn()){
			if(first){
				sb.append(cm.getValue());
				first = false;
			}
			else{
				sb.append("/");
				sb.append(cm.getValue());
			}
			if(columns.add(cm.getValue()))
				types.add(cm.getValueType().toString());
				
		}
		String keyOb = sb.toString();
		IndividualCreator icOb = null;
		if(individualCreators.containsKey(keyOb))
			icOb = individualCreators.get(keyOb);
		else{
			icOb = new IndividualCreator(driverName, OntModel.THING, pm.getObColumn(), null);
			individualCreators.put(keyOb, icOb);
			checkGlobalIndividualCreators(keyOb, icOb);
		}
		
		//Conditions behandeln
		for(Condition c: pm.getConditions()){
			if(columns.add(c.getColumn()))
				types.add(c.getValueType().toString());
		}
		ObjectPropertyCreator opc = new ObjectPropertyCreator(pm.getConditions());
		opc.setPredicate(this.model.tbox.getObjectProperties().get(pm.getPropertyName()));
		icSub.addSubjectListener(opc);
		icOb.addObjectListener(opc);
		this.objectPropertyCreators.add(opc);
	}
	
	private String buildQuery(){
		StringBuilder sb = new StringBuilder("SELECT ");
		boolean first = true;

		for(String s : columns){
			if(!first)
				sb.append(", ");
			sb.append(s);
			first = false;
		}
		sb.append(" FROM ");
		sb.append(this.table);
		return sb.toString();
	}
	
	public void query(Connection c, OntModel baseModel, Map<String, Map<DBKey,Individual>> mindividuals) throws SQLException {
		

		Statement s = c.createStatement();
		String query = buildQuery();
		if(s.execute(query)){
			ResultSet rs = s.getResultSet();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();
			for(int i = 1; i <= numberOfColumns; i++)
				if(i == 1)
					System.out.print(rsMetaData.getColumnLabel(i));
				else
					System.out.print("\t " + rsMetaData.getColumnLabel(i));
			System.out.println();
			
//			if(!this.isJoinedTable){
//				while(rs.next()){
//					for(IndividualCreator ic : individualCreators.values()){
//						ic.createIndividual(rs,baseModel);
//					}
//
//					for(ObjectPropertyCreator opc:this.objectPropertyCreators)
//						opc.linkIndividuals(baseModel);
//				}
//			}
//			else{
//				Map<String, Map<DBKey,Individual>> mindividuals = new HashMap<String, Map<DBKey,Individual>>();
				while(rs.next()){
					for(String key : individualCreators.keySet()){
						IndividualCreator ic = individualCreators.get(key);
						DBKey k = new DBKey();
						for(ColumnMapping cm : ic.getColumns()){
							Datatype d = Util.getDatatypeValue(driverName, cm, rs);
							if(d!=null)
								k.keys.add(d);
						}
						if(mindividuals.containsKey(key)){
							Map<DBKey,Individual> individuals = mindividuals.get(key);
							if(individuals.containsKey(k))
								ic.enrichIndividual(rs, baseModel, individuals.get(k));
							else{
								Individual ind = ic.createIndividual(rs,baseModel);
								individuals.put(k, ind);
							}
						}
						else{
							Individual ind = ic.createIndividual(rs,baseModel);
							HashMap<DBKey,Individual> individuals = new HashMap<DBKey,Individual>();
							mindividuals.put(key, individuals);
							individuals.put(k, ind);
						}
					}

					for(ObjectPropertyCreator opc:this.objectPropertyCreators)
						opc.linkIndividuals(baseModel,rs);
//				}
			}
			rs.close();
		}
	}
	
	public class DBKey{
		private List<Datatype> keys;
		
		public DBKey(){
			keys = new LinkedList<Datatype>();
		}
		
		@Override
		public int hashCode() {
			int hc = 17; 
		    int hashMultiplier = 59; 
		    for(Datatype dt : keys)
		    	hc = hc*hashMultiplier + dt.hashCode();
		    return hc; 
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof DBKey){
				DBKey other = (DBKey) obj;
				if(this.keys.size()==other.keys.size()){
					Iterator<Datatype> iter1 = keys.iterator();
					Iterator<Datatype> iter2 = other.keys.iterator();
					while(iter1.hasNext()){
						Datatype d1 = iter1.next();
						Datatype d2 = iter2.next();
						if(d1.compareTo(d2)!=0)
							return false;
					}
				}
			}
			else
				return false;
			return true;
		}
	}

}
