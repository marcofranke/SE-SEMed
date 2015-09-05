package de.biba.mediator.converter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.util.JAXBSource;

import de.biba.exceptions.InvalidDataException;
import de.biba.exceptions.MissingConversionRuleException;
import de.biba.math.Add;
import de.biba.math.Div;
import de.biba.math.MathObject;
import de.biba.math.Mul;
import de.biba.math.Pow;
import de.biba.math.SimpleMathValue;
import de.biba.math.Sqrt;
import de.biba.math.Sub;
import de.biba.mediator.QuerySolution;
import de.biba.mediator.converter.dto.ComplexRule;
import de.biba.mediator.converter.dto.Conversions;
import de.biba.mediator.converter.dto.DualExpression;
import de.biba.mediator.converter.dto.SimpleRule;
import de.biba.mediator.converter.dto.SingleExpression;
import de.biba.ontology.datatypes.Datatype;

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
 * Mit dieser Klasse können Werte in unterschiedliche Einheiten konvertiert
 * werden. Dazu kann eine Datei mit Konvertierungsregeln eingelesen werden.
 * Diese Klasse implementiert das Singleton-Pattern
 * 
 * @author KRA
 * 
 */
public class ValueConverter {
	private static ValueConverter instance = null;
	private Map<String, Map<String, Converter>> converters;

	private ValueConverter() {
		converters = new HashMap<String, Map<String, Converter>>();
	};

	/**
	 * Liefert die Instanz der Klasse zurück
	 * 
	 * @return
	 */
	public static ValueConverter getInstance() {
		if(instance == null)
			instance = new ValueConverter();
		return instance;
	}

	/**
	 * Lädt Konvertierungsregeln aus einer Datei. Löscht bereits bestehende
	 * Regeln.
	 * 
	 * @param pPath
	 *            Pfad zur Datei mit Konvertierungsregeln
	 * @throws JAXBException
	 */
	public void loadRules(String converterRulesFile) throws JAXBException {
		converters.clear();
		JAXBContext jc = JAXBContext.newInstance(Conversions.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		unmarshaller.setEventHandler(new MyValidationEventHandler());
		Conversions c = (Conversions) unmarshaller.unmarshal(new File(
				converterRulesFile));
		for(SimpleRule sr : c.getRule()){
			addSimpleConversionRule(sr);
		}
		for(ComplexRule cr : c.getComplexRule()){
			addComplexCoversionRule(cr);
		}
	}

	private void addSimpleConversionRule(SimpleRule sr) {
		Converter c = null;
		if(sr.getType().equals("Multiply")){
			c = new ValueMultiplier(sr.getValue());
		}
		else if(sr.getType().equals("Add")){
			c = new ValueAdder(sr.getValue());
		}
		addRule(sr.getFrom(), sr.getTo(), c);
	}

	private void addComplexCoversionRule(ComplexRule cr) {
		MathObject normal = buildComplexRule(cr.getNormal());
		MathObject inverse = buildComplexRule(cr.getInverse());
		addRule(cr.getFrom(), cr.getTo(), new ComplexConverter(normal, inverse));
	}

	private MathObject buildComplexRule(SingleExpression ex) {

		if(ex.getAdd() != null){
			return createAddRule(ex.getAdd());
		}
		else if(ex.getMultiply() != null){
			return createMultiplyRule(ex.getMultiply());
		}
		else if(ex.getDivide() != null){
			return createDivideRule(ex.getDivide());
		}
		else if(ex.getSub() != null){
			return createSubRule(ex.getSub());
		}
		else if(ex.getPow() != null){
			return createPowRule(ex.getPow());
		}
		else if(ex.getSqrt() != null){
			return createSqrtRule(ex.getSqrt());
		}
		else if(ex.getInput() != null){
			return null;
		}
		else{
			return new SimpleMathValue(ex.getValue());
		}
	}

	private MathObject createSqrtRule(SingleExpression singleExpression) {
		MathObject m1 = null;
		if(singleExpression.getAdd() != null){
			m1 = createAddRule(singleExpression.getAdd());
		}
		else if(singleExpression.getMultiply() != null){
			m1 = createMultiplyRule(singleExpression.getMultiply());
		}
		else if(singleExpression.getDivide() != null){
			m1 = createDivideRule(singleExpression.getDivide());
		}
		else if(singleExpression.getSub() != null){
			m1 = createSubRule(singleExpression.getSub());
		}
		else if(singleExpression.getPow() != null){
			m1 = createPowRule(singleExpression.getPow());
		}
		else if(singleExpression.getSqrt() != null){
			m1 = createSqrtRule(singleExpression.getSqrt());
		}
		else if(singleExpression.getInput() != null){
			m1 = null;
		}
		else{
			m1 = new SimpleMathValue(singleExpression.getValue());
		}
		return new Sqrt(m1);
	}

	private MathObject createPowRule(SingleExpression singleExpression) {
		MathObject m1 = null;
		if(singleExpression.getAdd() != null){
			m1 = createAddRule(singleExpression.getAdd());
		}
		else if(singleExpression.getMultiply() != null){
			m1 = createMultiplyRule(singleExpression.getMultiply());
		}
		else if(singleExpression.getDivide() != null){
			m1 = createDivideRule(singleExpression.getDivide());
		}
		else if(singleExpression.getSub() != null){
			m1 = createSubRule(singleExpression.getSub());
		}
		else if(singleExpression.getPow() != null){
			m1 = createPowRule(singleExpression.getPow());
		}
		else if(singleExpression.getSqrt() != null){
			m1 = createSqrtRule(singleExpression.getSqrt());
		}
		else if(singleExpression.getInput() != null){
			m1 = null;
		}
		else{
			m1 = new SimpleMathValue(singleExpression.getValue());
		}
		return new Pow(m1);
	}

	private MathObject createSubRule(DualExpression dualExpression) {
		MathObject m1 = buildComplexRule(dualExpression.getLeft());
		MathObject m2 = buildComplexRule(dualExpression.getRight());
		return new Sub(m1, m2);
	}

	private MathObject createDivideRule(DualExpression dualExpression) {
		MathObject m1 = buildComplexRule(dualExpression.getLeft());
		MathObject m2 = buildComplexRule(dualExpression.getRight());
		return new Div(m1, m2);
	}

	private MathObject createMultiplyRule(DualExpression dualExpression) {
		MathObject m1 = buildComplexRule(dualExpression.getLeft());
		MathObject m2 = buildComplexRule(dualExpression.getRight());
		return new Mul(m1, m2);
	}

	private MathObject createAddRule(DualExpression de) {
		MathObject m1 = buildComplexRule(de.getLeft());
		MathObject m2 = buildComplexRule(de.getRight());

		return new Add(m1, m2);
	}

	private void addRule(String from, String to, Converter c) {
		Map<String, Converter> tmp = converters.get(from);
		if(tmp == null){
			tmp = new HashMap<String, Converter>();
			converters.put(from, tmp);
		}
		tmp.put(to, c);
		tmp = converters.get(to);
		if(tmp == null){
			tmp = new HashMap<String, Converter>();
			converters.put(to, tmp);
		}
		tmp.put(from, c.getInverseConverter());
	}

	/**
	 * Konvertiert ein Datum von einer Einheit in eine andere...
	 * 
	 * @param current
	 *            Jetzige Einheit des Datums
	 * @param transformTo
	 *            Zieleinheit des Datums
	 * @param value
	 *            Datum
	 * @return Konvertiertes Datum
	 * @throws InvalidDataException
	 *             Wird geworfen, wenn das Datum nicht vom erwarteten Typ ist
	 * @throws MissingConversionRuleException
	 *             Wird geworfen, wenn keine Konvertierungsregel zwischen den
	 *             Einheiten vorhanden ist.
	 */
	public Datatype convert(String current, String transformTo, Datatype value)
			throws InvalidDataException, MissingConversionRuleException {
		if(current == transformTo)
			return value;
		Map<String, Converter> tmp = converters.get(current);
		if(tmp != null){
			Converter c = tmp.get(transformTo);
			if(c != null){
				return c.convert(value);
			}
		}
		throw new MissingConversionRuleException(
				"No Rule could be evaluated to transform " + current + " to "
						+ transformTo + ".");
	}

	/**
	 * Prüft ob es eine Konvertierungsregel zwischen zwei Einheiten gibt
	 * 
	 * @param current
	 *            Erste Einheit
	 * @param transformTo
	 *            Zweite Einheit
	 * @return true falls Regel vorhanden, sonst false
	 */
	public boolean hasConversionRule(String current, String transformTo) {
		Map<String, Converter> tmp = converters.get(current);
		if(tmp != null){
			Converter c = tmp.get(transformTo);
			if(c != null)
				return true;
		}
		return false;
	}

	private List<Converter> buildConversionRule(String from, String to) {
		return buildConversionRule(from, to, new HashSet<String>());
	}

	private List<Converter> buildConversionRule(String from, String to,
			Set<String> visited) {
		Map<String, Converter> tmp = converters.get(from);
		visited.add(from);
		if(tmp == null)
			return null;
		else{
			for(String u : tmp.keySet()){
				if(u.equals(to)){
					List<Converter> result = new ArrayList<Converter>();
					result.add(tmp.get(u));
					return result;
				}
			}
			for(String u : tmp.keySet()){
				if(visited.contains(u)){
					continue;
				}

				List<Converter> result = buildConversionRule(u, to, visited);

				if(result != null){
					result.add(0, tmp.get(u));
					return result;
				}
			}
			return null;
		}
	}

	private Datatype executeRules(Datatype v, List<Converter> converter)
			throws InvalidDataException {
		Datatype result = v.clone();
		for(Converter c : converter)
			result = c.convert(result);
		return result;
	}

	/**
	 * Konvertiert alle Werte eine Spalte in einer QuerySolution
	 * {@link QuerySolution}
	 * 
	 * @param from
	 *            Jetzige Einheit der Daten
	 * @param to
	 *            Zieleinheit der Daten
	 * @param pR
	 *            QuerySolution aus der die Daten konvertiert werden sollen
	 * @param i
	 *            Index der Spalte, dessen Daten konvertiert werden sollen
	 * @return
	 */
	public boolean convertQuerySolution(String from, String to,
			QuerySolution pR, int i) {
		List<Converter> converter = buildConversionRule(from, to);
		if(converter == null || converter.size() == 0)
			return false;
		for(List<Datatype> row : pR.data){
			try{
				row.set(i, executeRules(row.get(i), converter));
			}
			catch(InvalidDataException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public class MyValidationEventHandler implements ValidationEventHandler {
		 
	    public boolean handleEvent(ValidationEvent event) {
	        return false;
	    }
	 
	}
}
