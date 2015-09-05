package de.biba.ontology;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.biba.exceptions.InvalidDataException;
import de.biba.exceptions.MissingConversionRuleException;
import de.biba.mediator.converter.ValueConverter;

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
 * Die Klasse stellt die ABox einer Ontologie dar. Neben den normalen ABox-Daten, wie man sie aus Ontologien kennt, beinhaltet diese Klasse auch noch ein Feld, 
 * in dem man die Einheiten der einzelnen Datensätze angeben kann. Dabei ist zu beachten, dass diese Angabe auf Property-Ebene gemacht werden. Dies bedeutet, dass alle Werte
 * eines Properties in der gleichen Einheit gemacht werden müssen.
 * @author KRA
 *
 */
public class ABox {
	/**
	 * Hier werden alle DatatypeProperties gehalten. Als Key ist dabei der volle Name des Properties zu verwenden.
	 */
	public Map<String, List<DatatypePropertyStatement>> dtProperties;
	/**
	 * Hier werden alle ObjectProperties gehalten. Als Key ist dabei der volle Name des Properties zu verwenden.
	 */
	public Map<String, List<ObjectPropertyStatement>> oProperties;
	/**
	 * Hier werden alle Individuen gehalten. Als Key ist dabei der volle Name der Klasse zu verwenden.
	 */
	public Map<String, List<Individual>> individuals;
	
	/**
	 * Hier werden die Einheiten der einzelnen Properties gehalten. Als Key ist dabei der volle Name des Properties zu verwenden.
	 */
	public Map<String, String> units;
	Logger logger;
	
	
	public ABox(){
		dtProperties = new HashMap<String, List<DatatypePropertyStatement>>();
		oProperties = new HashMap<String, List<ObjectPropertyStatement>>();
		individuals = new HashMap<String, List<Individual>>();
		units = new HashMap<String, String>();
		logger = Logger.getLogger(this.getClass());
	}
	
	/**
	 * Clone-Konstruktor, erstellt einen Clone des ABox Objektes
	 * 
	 * @param aBox ABox das geklont werden soll
	 */
	public ABox(ABox aBox){
		this.dtProperties = new HashMap<String, List<DatatypePropertyStatement>>(aBox.dtProperties);
		this.oProperties = new HashMap<String, List<ObjectPropertyStatement>>(aBox.oProperties);
		this.individuals = new HashMap<String, List<Individual>>(aBox.individuals);
		this.units = new HashMap<String, String>(aBox.units);
		this.logger = aBox.logger;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("DatatypePropertyStatements: \n\n");
		for(String key : dtProperties.keySet()){
			sb.append(key+":\n");
			List<DatatypePropertyStatement> ldps = dtProperties.get(key);
			for(DatatypePropertyStatement dps:ldps){
				sb.append(dps+"\n");
			}
		}
		sb.append("\nObjectPropertyStatements: \n\n");
		for(String key : oProperties.keySet()){
			sb.append(key+":\n");
			List<ObjectPropertyStatement> ldps = oProperties.get(key);
			for(ObjectPropertyStatement dps:ldps){
				sb.append(dps+"\n");
			}
		}
		sb.append("\nClassStatements: \n\n");
		for(String key : individuals.keySet()){
			sb.append(key+":\n");
			List<Individual> ldps = individuals.get(key);
			for(Individual dps:ldps){
				sb.append(dps+"\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Fügt die Daten einer anderen ABox zu dieser hinzu.
	 * @param x Die ABox die zu dieser Instanz hinzugefügt werden soll
	 * @param converter Ein Konverter um die Daten bei unterschiedlichen Einheiten konvertieren zu können
	 */
	public void add(ABox x, ValueConverter converter){
		for(String key : x.dtProperties.keySet()){
			String unit1 = units.get(key);
			String unit2 = x.units.get(key);
			
			List<DatatypePropertyStatement> tmp = dtProperties.get(key);
			if(tmp==null){
				dtProperties.put(key, x.dtProperties.get(key));
				if(unit2!=null)
					units.put(key, unit2);
			}
			else{
				boolean a = unit1==null || unit1.equals("unknown");
				boolean b = unit2==null || unit2.equals("unknown");
				if(a && b)
					tmp.addAll(x.dtProperties.get(key));
				else if(!a && b){
//					tmp.addAll(x.dtProperties.get(key));
					logger.warn("Merging datatypeProperty "+key+" with missing unit");
				}
				else if(a && !b){
//					tmp.addAll(x.dtProperties.get(key));
					dtProperties.put(key, x.dtProperties.get(key));
					units.put(key, unit2);
					logger.warn("Merging datatypeProperty "+key+" with missing unit");
				}
				else{
					if(unit1.equals(unit2)){
						tmp.addAll(x.dtProperties.get(key));
					}
					else{
						if(converter.hasConversionRule(unit1, unit2)){
							List<DatatypePropertyStatement> ldps = x.dtProperties.get(key);
							for(DatatypePropertyStatement dps : ldps){
								try {
									dps.object = converter.convert(unit2, unit1, dps.object);
									tmp.add(dps);
								} catch (InvalidDataException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MissingConversionRuleException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						else{
//							tmp.addAll(x.dtProperties.get(key));
							logger.warn("Merging datatypeProperty "+key+" with missing conversion rule: from("+unit1+") to ("+unit2+")");
						}
					}
				}
			}
		}
		for(String key:x.oProperties.keySet()){
			List<ObjectPropertyStatement> tmp = oProperties.get(key);
			if(tmp==null){
				oProperties.put(key, x.oProperties.get(key));
			}
			else
				tmp.addAll(x.oProperties.get(key));
		}
		for(String key:x.individuals.keySet()){
			List<Individual> tmp = individuals.get(key);
			if(tmp==null){
				individuals.put(key, x.individuals.get(key));
			}
			else
				tmp.addAll(x.individuals.get(key));
		}
		
	}

	/**
	 * Gibt die Anzahl der RDF-Triple/Statements in der ABox zurück
	 * @return
	 */
	public long getTripleCount() {
		long result = 0;
		for(String key:individuals.keySet()){
			result += individuals.get(key).size();
		}
		for(String key:dtProperties.keySet()){
			result += dtProperties.get(key).size();
		}
		for(String key:oProperties.keySet()){
			result += oProperties.get(key).size();
		}
		return result;
	}
}
