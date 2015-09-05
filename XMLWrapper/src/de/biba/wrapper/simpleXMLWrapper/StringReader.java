package de.biba.wrapper.simpleXMLWrapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import de.biba.mediator.Datatypes;
import de.biba.ontology.datatypes.BooleanDatatype;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.NumericDatatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.AdditionalLinkingInforamtion;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.Replacement;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.Replacements;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.TokenExtraction;
import de.biba.wrapper.simpleXMLWrapper.dataTypes.XMLTagExctrationInformation;

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

/**
 * This class searches XML tags in a XML String and resturns its values. The
 * iteration through all XML Tags is resolved by the method next. After each
 * 'next' you can get the value of the current XML Tag through the method
 * getValue.
 * 
 * @author Marco Franke
 * 
 */

public class StringReader implements IReader {
	private String text;
	private ArrayList<String> texts = new ArrayList<String>();
	private Datatypes valueTypes[];
	// ############XML####################
	private SAXBuilder builder = new SAXBuilder();
	private Document doc = null;
	// ####################Entitys##########
	private HashMap<String, ArrayList<String>> allTagsValues = new HashMap<String, ArrayList<String>>();
	private HashMap<String, Integer> numbersOfIndiviuals = new HashMap<String, Integer>();
	private int max = 0;
	private int index = 0;
	// This is necessary for later binding of object properties. In this map all
	private HashMap<String, ArrayList<AdditionalLinkingInforamtion>> additionalInfos = new HashMap<String, ArrayList<AdditionalLinkingInforamtion>>();

	@Override
	public void open() {

	}

	/**
	 * This method returns the the orginial xml string
	 * 
	 * @return xml string
	 */
	public String getText() {
		return text;
	}

	public List<AdditionalLinkingInforamtion> getAdditionalInformation(
			String inverseDatapropertyName) {
		return additionalInfos.get(inverseDatapropertyName);
	}

	/**
	 * This methods takes the parent (domain) iterate two layers in the xml
	 * strucutre down ro identify possible individuals of the ...
	 * 
	 * @param parent
	 * @param possibleChildren
	 * @return a list of values of the inversefunctional dataproperty
	 */
	public List<String> find(AdditionalLinkingInforamtion parent,
			List<AdditionalLinkingInforamtion> possibleChildren) {
		String targetXMLPath = "";
		String result = "";
		ArrayList<String> resultList = new ArrayList<String>();
		String sourceXMLPath = parent.getXMLPath();
		if ((possibleChildren != null) && (possibleChildren.size() > 0)) {
			// The structure should be equal by all entities of this set
			targetXMLPath = possibleChildren.get(0).getXMLPath();
			boolean isAAtribute = possibleChildren.get(0).getInfos()
					.isThereAAttribute();
			String attribute = possibleChildren.get(0).getInfos()
					.getAttribute();
			if (parent.getInfos().isThereAAttribute() == false) {
				sourceXMLPath = sourceXMLPath.substring(0,
						sourceXMLPath.lastIndexOf("/"));
				parent.setElement(parent.getElement().getParentElement());
			}
			targetXMLPath = targetXMLPath.replace(sourceXMLPath + "/", "");
			String[] allTokens = targetXMLPath.split("/");
			int counter = 0;
			ArrayList<Element> allNodes = new ArrayList<Element>();
			ArrayList<Element> allLeafes = new ArrayList<Element>();
			Element current = parent.getElement();
			for (Element child : current.getChildren()) {
				if (child.getName().equals(allTokens[counter])) {
					allNodes.add(child);
				}
			}
			if (allTokens.length > 1) {
				for (Element current2 : allNodes) {
					for (Element child : current2.getChildren()) {
						if (child.getName().equals(allTokens[counter + 1])) {
							allLeafes.add(child);
						}
					}
				}
			}
			for (Element current3 : allLeafes) {
				// Now I have the important element
				if (isAAtribute == false) {
					String value = current3.getValue();
					resultList.add(value);

				} else {
					for (Attribute attr : current3.getAttributes()) {
						if (attr.getName().equals(attribute)) {
							String value = attr.getValue();
							resultList.add(value);
						}
					}
				}

			}
		}
		return resultList;
	}

	public AdditionalLinkingInforamtion getSpecificAdditionalInformation(
			String inverseDatapropertyName, String valueOfDataProperty) {
		List<AdditionalLinkingInforamtion> list = additionalInfos
				.get(inverseDatapropertyName);
		if ((list == null) || (list.size() == 0)) {
			return null;
		}
		for (AdditionalLinkingInforamtion info : list) {
			if (info.getValue().equals(valueOfDataProperty)) {
				return info;
			}
		}

		return null;

	}

	private void insertInformation(Element element,
			XMLTagExctrationInformation information, String value) {
		if (information.isInversalFunctional()) {
			AdditionalLinkingInforamtion infos = new AdditionalLinkingInforamtion();
			infos.setDataPropertyName(information.getPropertyName());
			infos.setInfos(information);
			infos.setElement(element);
			if (information.isThereAPreProcessingOfValue()) {
				String result = extractInformationWithinAString(
						infos.getDataPropertyName(), value,
						information.getExtractor(),
						information.getReplacements());
				infos.setValue(result);
			} else {
				infos.setValue(value);
			}
			infos.setXMLPath(information.getTag());

			if (additionalInfos.containsKey(information.getPropertyName())) {
				additionalInfos.get(information.getPropertyName()).add(infos);
			} else {
				ArrayList<AdditionalLinkingInforamtion> list = new ArrayList<AdditionalLinkingInforamtion>();
				list.add(infos);
				additionalInfos.put(information.getPropertyName(), list);
			}
		}
	}

	private void iterarate(Element root) {
		ArrayList<Element> actualElemes = new ArrayList<Element>();
		ArrayList<Element> actualElemes2 = new ArrayList<Element>();
		actualElemes.add(root);
		while (actualElemes.size() > 0) {
			for (Element e : actualElemes) {

				actualElemes2.addAll(e.getChildren());
			}
			actualElemes.clear();
			actualElemes.addAll(actualElemes2);
			actualElemes2.clear();
		}
	}

	private int calculateDeptAndInit(XMLTagExctrationInformation information) {
		String path = information.getTag();

		int result = 0;
		String[] token = path.split("/");
		ArrayList<Element> actualElemes = new ArrayList<Element>();
		Element root = doc.getRootElement();
		iterarate(root);
		actualElemes.add(root);
		if (token.length == 1) {
			ArrayList<String> r = new ArrayList<String>();
			// Information is included in the root node
			if (information.isThereAAttribute() == false) {
				String value = root.getValue();
				if (information.isThereAPreProcessingOfValue()) {
					value = extractInformationWithinAString(
							information.getPropertyName(), value,
							information.getExtractor(),
							information.getReplacements());

				}
				r.add(value);
				insertInformation(root, information, value);

			} else {
				for (Attribute attr : root.getAttributes()) {
					if (attr.getName().equals(information.getAttribute())) {
						String value = attr.getValue();
						if (information.isThereAPreProcessingOfValue()) {
							value = extractInformationWithinAString(
									information.getPropertyName(), value,
									information.getExtractor(),
									information.getReplacements());

						}
						r.add(value);
						insertInformation(root, information, value);
					}
				}
			}
			if ((r == null) || (r.size() == 0)) {
				System.err.println("Found no data for: " + path);
			} else {
				result = r.size();
				this.allTagsValues.put(information.getPropertyName() + path, r);
			}

		}
		for (int i = 1; i < token.length; i++) {
			ArrayList<Element> zwi = new ArrayList<Element>();
			for (Element e : actualElemes) {

				boolean found = false;
				for (Element sub : e.getChildren()) {

					String n = sub.getName();
					String str2 = token[i].substring(token[i].indexOf(':') + 1);
					if (n.equals(str2)) {
						zwi.add(sub);
						found = true;
					}
				}
				if (found == false) {
					if (e.getChildren(token[i]).size() > 0) {
						zwi.addAll(e.getChildren(token[i]));
					} else {
						if (token[i].indexOf(':') > -1) {
							String str = token[i].substring(token[i]
									.indexOf(':') + 1);
							if (e.getChildren(str).size() > 0) {
								zwi.addAll(e.getChildren(str));
							} else {
								// Stufe 3. Sollte nie eintreten
								for (Element es : e.getChildren()) {
									if (es.getName().equals(str)) {
										zwi.add(es);
									}
								}
							}
						}
					}
				}

			}
			actualElemes.clear();
			actualElemes.addAll(zwi);
			if (i >= token.length - 2) {
				result = zwi.size();
				ArrayList<String> r = new ArrayList<String>();
				for (Element t : zwi) {

					if (t.getName().equals(
							token[token.length - 1]
									.substring(token[token.length - 1]
											.indexOf(':') + 1))) {
						if (information.isThereAAttribute() == false) {

							String value = t.getValue();
							if (information.isThereAPreProcessingOfValue()) {
								value = extractInformationWithinAString(
										information.getPropertyName(), value,
										information.getExtractor(),
										information.getReplacements());

							}
							r.add(value);
							insertInformation(t, information, value);
						} else {
							for (Attribute attr : t.getAttributes()) {
								if (attr.getName().equals(
										information.getAttribute())) {
									String value = attr.getValue();
									if (information
											.isThereAPreProcessingOfValue()) {
										value = extractInformationWithinAString(
												information.getPropertyName(),
												value,
												information.getExtractor(),
												information.getReplacements());

									}
									r.add(value);
									insertInformation(t, information, value);
								}
							}
						}
					}

					ArrayList<Element> allElemens = new ArrayList<Element>();

					for (Element sub : t.getChildren()) {

						String n = sub.getName();
						String str2 = token[token.length - 1]
								.substring(token[token.length - 1].indexOf(':') + 1);
						if (n.equals(str2)) {
							allElemens.add(sub);
						}

					}
					if ((allElemens != null) && (allElemens.size()) > 0) {
						for (Element targetE : allElemens) {
							if (information.isThereAAttribute() == false) {
								String value = targetE.getValue();
								if (information.isThereAPreProcessingOfValue()) {
									value = extractInformationWithinAString(
											information.getPropertyName(),
											value, information.getExtractor(),
											information.getReplacements());

								}
								r.add(value);
								insertInformation(targetE, information, value);
							} else {
								for (Attribute attr : targetE.getAttributes()) {
									if (attr.getName().equals(
											information.getAttribute())) {
										String value = attr.getValue();
										if (information
												.isThereAPreProcessingOfValue()) {
											value = extractInformationWithinAString(
													information
															.getPropertyName(),
													value, information
															.getExtractor(),
													information
															.getReplacements());

										}
										r.add(value);
										insertInformation(targetE, information,
												value);
									}
								}
							}
						}

					} else {
						String str = token[token.length - 1]
								.substring(token[token.length - 1].indexOf(':') + 1);
						for (Element es : t.getChildren()) {
							if (es.getName().equals(str)) {
								if (information.isThereAAttribute() == false) {
									String value = es.getValue();
									if (information
											.isThereAPreProcessingOfValue()) {
										value = extractInformationWithinAString(
												information.getPropertyName(),
												value,
												information.getExtractor(),
												information.getReplacements());

									}
									r.add(value);
									insertInformation(es, information, value);
								} else {
									// Attribute
									for (Attribute attr : es.getAttributes()) {
										if (attr.getName().equals(
												information.getAttribute())) {
											String value = attr.getValue();
											if (information
													.isThereAPreProcessingOfValue()) {
												value = extractInformationWithinAString(
														information
																.getPropertyName(),
														value,
														information
																.getExtractor(),
														information
																.getReplacements());

											}
											r.add(value);
											insertInformation(es, information,
													value);
										}
									}
								}
							}
						}
					}
				}

				if ((r == null) || (r.size() == 0)) {
					System.err.println("Found no data for: " + path);
				} else {
					result = r.size();
					this.allTagsValues.put(
							information.getPropertyName() + path, r);
				}
			}
		}
		numbersOfIndiviuals.put(
				information.getPropertyName() + information.getTag(), result);
		return result;
	}

	public void setText(String text, List<XMLTagExctrationInformation> list) {
		index = 0;
		this.text = text;
		texts.clear();
		try {
			java.io.StringReader input = new java.io.StringReader(text);
			doc = builder.build(input);
			Element party = doc.getRootElement();

			for (XMLTagExctrationInformation str : list) {
				int z = calculateDeptAndInit(str);
				if (z > max) {
					max = z;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#next()
	 */
	@Override
	public boolean next() throws IOException {
		index++;
		if (index <= max) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#getString(int)
	 */
	@Override
	public String getString(int pPos) throws IOException {
		return "-1";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#reset()
	 */
	@Override
	public void reset() {
		allTagsValues.clear();
		numbersOfIndiviuals.clear();
		this.index = 0;
		max = 0;
		doc = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#getDouble(int)
	 */
	@Override
	public double getDouble(int pPos) throws IOException, ParseException {
		NumberFormat fmt = NumberFormat.getInstance();
		Number number = null; // fmt.parse(reader.get(pPos));
		return number.doubleValue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#getDate(int)
	 */
	@Override
	public Date getDate(int pPos) throws ParseException, IOException {
		return null;// dateFormat.parse(reader.get(pPos));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#getInt(int)
	 */
	@Override
	public int getInt(int pPos) throws NumberFormatException, IOException {
		return 0;// Integer.parseInt(reader.get(pPos));
	}

	private NumericDatatype getNumericValue(String pPos) throws IOException,
			ParseException {
		String tmp = null;// reader.get(pPos);
		if (tmp.isEmpty())
			return null;
		else
			return null; // new NumericDatatype(getDouble(pPos));
	}

	private StringDatatype getStringValue(String datatype, String tag)
			throws IOException {
		if (this.allTagsValues.containsKey(datatype + tag)) {
			List<String> result = this.allTagsValues.get(datatype + tag);
			int max = this.numbersOfIndiviuals.get(datatype + tag);
			if (index <= max) {
				String r = result.get(index % result.size());

				return new StringDatatype(r);
			}
		} else {
			if ((datatype == null) || (datatype.length() == 0)) {
				for (String str : allTagsValues.keySet()) {
					if (str.contains(tag)) {
						List<String> result = this.allTagsValues.get(str);
						int max = this.numbersOfIndiviuals.get(str);
						if (index <= max) {
							String r = result.get(index % result.size());

							return new StringDatatype(r);
						}
					}
				}
			}
		}
		return new StringDatatype("");

	}

	/**
	 * This method returns the value of a XML tag at a specific position. The
	 * specific position of the tag in the xml document is managed by the
	 * @link{next} method. The input is the pasth of the tag. For example:
	 * root/XMLTag/SubXMLTag
	 */

	@Override
	public Datatype getValue(String datatypeName, String pos)
			throws ParseException {
		try {
			Datatypes type = Datatypes.STRING;
			switch (type) {
			case NUMERIC:
				return getNumericValue(pos);
			case STRING:
				return getStringValue(datatypeName, pos);
			case DATE:
				return getStringValue(datatypeName, pos);
			case BOOLEAN:
				return getBooleanValue(pos);
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}

		return null;
	}

	private BooleanDatatype getBooleanValue(String pPos) throws IOException {
		String tmp = null; // reader.get(pPos);
		if (tmp.isEmpty())
			return null;
		else
			return null; // new BooleanDatatype(getBoolean(pPos));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.wrapper.tableWrapper.IReader#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(int pPos) throws IOException {
		return false; // Boolean.parseBoolean(reader.get(pPos));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.wrapper.tableWrapper.IReader#loadDatatypeDefinition(java.lang
	 * .String)
	 */
	@Override
	public void loadDatatypeDefinition(String pFileName) {
		try {
			FileReader fr = new FileReader(pFileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			String token[] = line.split(";");
			valueTypes = new Datatypes[token.length];
			for (int i = 0; i < token.length; i++) {
				String s = token[i];
				boolean isAssigned = false;
				if (s.equals("Numeric")) {
					valueTypes[i] = Datatypes.NUMERIC;
					isAssigned = true;
				} else if (s.equals("Date")) {
					valueTypes[i] = Datatypes.DATE;
					isAssigned = true;
				} else if (s.equals("String")) {
					valueTypes[i] = Datatypes.STRING;
					isAssigned = true;
				} else if (s.equals("Boolean")) {
					valueTypes[i] = Datatypes.BOOLEAN;
					isAssigned = true;
				}

				if (!isAssigned) {

					System.err
							.println("Your data type: '"
									+ s
									+ "' is not supported. The set contains: {Numeric, Date, String, Boolean}");

				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.wrapper.tableWrapper.IReader#setDateFormat(java.text.DateFormat)
	 */
	@Override
	public void setDateFormat(DateFormat pDateFormat) {
		// dateFormat = pDateFormat;
	}

	public static void main(String[] args) {
		IReader reader = null;
		try {
			reader = new StringReader();
			reader.setDateFormat(new SimpleDateFormat("dd-MMM-yy"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			for (int i = 0; i < 10 && reader.next(); i++) {
				try {
					// System.out.println(reader.getDate(1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String getColumnName(int pPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	private String doReplacements(Replacements replacements, String value) {
		if ((replacements == null)
				|| (replacements.getReplacement().size() == 0)) {
			return value;
		}
		for (Replacement r : replacements.getReplacement()) {
			value = value.replaceAll(r.getSource(), r.getTarget());
		}
		return value;
	}

	/**
	 * This methods takes a string transforms it into a set of substrings.
	 * Finally it takes the element at position i and transform it
	 * 
	 * @param value
	 *            Input string (value of an xml element)
	 * @param extraction
	 *            description how to transform the string
	 * @return transformed string at a specific position
	 */
	private String extractInformationWithinAString(String name, String value,
			TokenExtraction extraction, Replacements replacements) {
		String result = "";
		String[] valueList = null;
		boolean justATrim = false;
		String seperator = "";
		String seperator2 = "";
		if ((extraction.getSeperator() != null)
				&& (extraction.getSeperator().length() > 0)) {
			seperator = extraction.getSeperator();
			justATrim = true;
		}
		if ((extraction.getRegularExpression() != null)
				&& (extraction.getRegularExpression().length() > 0)) {
			seperator2 = extraction.getRegularExpression();
			if (seperator.length() == 0) {
				seperator = seperator2;
				seperator2 = "";
			}
		}
		if (justATrim) {
			valueList = value.split(seperator);
			if ((seperator2 == null) || (seperator2.length() == 0)) {
				if (extraction.getIndex() < valueList.length) {
					String str = valueList[extraction.getIndex()];
					return doReplacements(replacements, str);
				} else {
					Logger.getAnonymousLogger().log(
							Level.SEVERE,
							name + " :Error index is out of range."
									+ extraction.getIndex());
				}
			} else {
				String str = valueList[extraction.getIndex()];
				Pattern pattern = Pattern.compile(extraction
						.getRegularExpression());
				Matcher matcher = pattern.matcher(str);
				while (matcher.find()) {
					return doReplacements(replacements,
							str.substring(matcher.start(), matcher.end()));
				}
			}
		} else {
			ArrayList<String> allResults = new ArrayList<String>();

			Pattern pattern = Pattern
					.compile(extraction.getRegularExpression());
			Matcher matcher = pattern.matcher(value);
			while (matcher.find()) {
				allResults.add(value.substring(matcher.start(), matcher.end()));
			}
			if (extraction.getIndex() < allResults.size()) {
				return doReplacements(replacements,
						allResults.get(extraction.getIndex()));
			} else {
				Logger.getAnonymousLogger().log(
						Level.SEVERE,
						name + " :Error index is out of range."
								+ extraction.getIndex());
			}

		}
		return result;
	}
}
