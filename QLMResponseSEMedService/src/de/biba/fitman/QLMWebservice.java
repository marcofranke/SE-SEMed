package de.biba.fitman;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

import de.biba.mediator.IMediator;
import de.biba.mediator.IQuery;
import de.biba.mediator.OutputQuery;
import de.biba.mediator.constraints.ClassConstraint;
import de.biba.mediator.constraints.SimpleConstraint;
import de.biba.mediator.webservice.MediatorResult;
import de.biba.mediator.webservice.MediatorWebservice;
import de.biba.mediator.webservice.WrapperMessage;
import de.biba.mediator.webservice.qlm.SPARQLCreator;
import de.biba.holonix.odf.Description;
import de.biba.holonix.odf.InfoItemType;
import de.biba.holonix.odf.ObjectType;
import de.biba.holonix.odf.ObjectsType;
import de.biba.holonix.odf.QlmID;
import de.biba.holonix.odf.ValueType;
import de.biba.holonix.omi.IdType;
import de.biba.holonix.omi.OmiEnvelope;
import de.biba.holonix.omi.RequestResultType;
import de.biba.holonix.omi.ResponseListType;
import de.biba.holonix.omi.ReturnType;
import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.OntModel;
import de.biba.queryLanguage.SimpleQueryParser;

/**
 * [QLMResponseSEMedService. This is a wrapper to tranform a xml based data base
 * schema to an ontology] Copyright (C) [2014 [Marco Franke (BIBA-Bremer
 * Institut für Produktion und Logistik GmbH)]
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

public class QLMWebservice extends MediatorWebservice {

	private int getIndexOfString(String[] columns, String serachValue) {
		int result = -1;
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] != null) {
				if (columns[i].equals(serachValue)) {
					result = i;
				}
			}
		}
		return result;
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

	private String searchDescriptionProperty(
			ArrayList<String> arrayListProperties, OntModel baseModel) {
		String result = "";
		for (String str : arrayListProperties) {
			DatatypeProperty p = baseModel.tbox.getDatatypeProperties()
					.get(str);
			if (p != null) {
				String uri = p.getUri();
				uri = uri.substring(uri.indexOf('#') + 1);

				if (uri.toLowerCase().equals("description")) {
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
	
	@SuppressWarnings("unchecked")
	private ObjectsType helperQLMMethodASODF(MediatorResult input,
			List<SimpleConstraint> allConstraints, OntModel baseModel,
			boolean justOneConcept) {
		ArrayList<String> allConcepts = new ArrayList<String>();
		for (SimpleConstraint con : allConstraints) {
			if (con.getPredicate().equals("isA")) {
				System.out.println(con);
				String[] allTokens = con.toString().split(" ");

				allConcepts.add(String.valueOf(allTokens[2]
						.substring(allTokens[2].indexOf("#") + 1)));
			}
		}

		if ((input.getColumnNames().length > 0)
				&& (input.getColumnNames()[0] != null)
				&& (input.getColumnNames()[0].length() > 0)) {
			if (input.getColumnNames()[0].charAt(0) == '?') {

				String[] resultColumns = new String[input.getColumnNames().length];
				for (int i = 0; i < input.getColumnNames().length; i++) {
					String str = input.getColumnNames()[i];
					resultColumns[i] = str.substring(str.indexOf(':') + 1);
				}
				input.setColumnNames(resultColumns);
			}
		} else {

			return null;
		}
		HashMap<String, ArrayList<String>> allAdressedConcepts = new HashMap<String, ArrayList<String>>();
		ArrayList<String> allInverse = new ArrayList<String>();
		for (String name : input.getColumnNames()) {
			Set<String> conceptNames = null;
			if (baseModel.tbox.datatypeProperties.containsKey(name)) {
				conceptNames = baseModel.tbox.datatypeProperties.get(name)
						.getDomain();
				if (baseModel.tbox.datatypeProperties.get(name)
						.isInverseFunctional()) {
					allInverse.add(name);
				}

			} else {
				if (baseModel.tbox.objectProperties.containsKey(name)) {
					conceptNames = baseModel.tbox.objectProperties.get(name)
							.getDomain();
				}
			}
			if (justOneConcept) {
				if (allConcepts.size() > 0) {
					Logger.getAnonymousLogger()
							.log(Level.WARNING,
									"There is more than one concept available. I just take the first one");
					String str = allConcepts.get(0);
					allConcepts.clear();
					allConcepts.add(str);

				}

			} else {
				for (String conceptname : conceptNames) {
					allConcepts.add(conceptname);
				}
			}
			if (allConcepts == null) {
				Logger.getAnonymousLogger().log(Level.SEVERE,
						"There is no property for: " + name);
			} else {
				for (String conceptname : allConcepts) {
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
				String description = searchDescriptionProperty(
						allAdressedConcepts.get(str), baseModel);
				int index = getIndexOfString(input.getColumnNames(), innverse);
				if (index > -1) {
					if (!justOneConcept) {
						id.setValue(data[index]);
					} else {
						String resultID = "";
						int in = 0;
						for (String name : allInverse) {
							int index2 = getIndexOfString(
									input.getColumnNames(), name);
							if (index2 != -1) {
								resultID += data[index2];
								if (in < allInverse.size() - 1) {
									resultID += "_";
								}
							}
							in++;
						}
						id.setValue(resultID);
					}
				}
				OneConcept.getId().add(id);
				type.getObject().add(OneConcept);
				// Jetzt müssen die fehlenden Properties zu einer Klasse ergänzt
				// werden
				for (String propertyName : allAdressedConcepts.get(str)) {
					if (propertyName.equals(description)) {
						// Jetzt habe ich das Property mit der Description
						int indexOfProp = getIndexOfString(
								input.getColumnNames(), propertyName);
						String value = data[indexOfProp];
						Description des = new Description();
						des.setValue(value);
						OneConcept.setDescription(des);
					} else {

						if (!propertyName.equals(innverse) || (justOneConcept)) {
							DatatypeProperty property = baseModel.tbox
									.getDatatypeProperties().get(propertyName);
							if (property != null)
								if (!(property.isInverseFunctional())
										|| (justOneConcept == true)) {
									Set<String> allDomains = property
											.getDomain();
									if (allDomains.contains(str)
											|| justOneConcept == true) {
										InfoItemType infoItem = new InfoItemType();
										String name = "";
										if (propertyName.contains("#")) {
											name = propertyName
													.substring(propertyName
															.indexOf('#') + 1);
										} else {
											name = propertyName;
										}
										infoItem.setOptionalName((name)); // TODO
																			// this
																			// is
																			// not
																			// correct
										int indexOfProp = getIndexOfString(
												input.getColumnNames(),
												propertyName);
										ValueType value = new ValueType();
										value.setValue(data[indexOfProp]);
										infoItem.getValue().add(value);
										OneConcept.getInfoItem().add(infoItem);
									}
								}
						} else {
							Logger.getAnonymousLogger().log(Level.WARNING,
									"Problems while matching: " + propertyName);
						}
					}
				}
			}
		}
		StringWriter stringWriter = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance("de.biba.odf");
			Marshaller m = context.createMarshaller();
			m.marshal(type, stringWriter);
			// return str;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING,
					e.toString());
		}
		return type;
	}

	public String queryAsQML(String jObject, String requestId,
			boolean justOneConcept) {

		if (jObject.contains("<Object")) {
			// It is no JSON objtct
			initiateStandardConfiguration();
			SPARQLCreator creator = new SPARQLCreator(jObject, mediator);
			String namespace = creator.getContainedNamespaces().get(0);
			String query = creator.getsPARQLQuery();
			try {

				WrapperMessage wrapMessage = new WrapperMessage();
				wrapMessage.setNamespace(namespace);
				wrapMessage.setQuery(query);
				jObject = new Gson().toJson(wrapMessage);

			} catch (Exception e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
			}
		}

		MediatorResult result = query(jObject);
		Logger.getAnonymousLogger().log(Level.INFO,
				result.getColumnNames().toString());
		Logger.getAnonymousLogger()
				.log(Level.INFO, result.getData().toString());
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
		OmiEnvelope envelope = new OmiEnvelope();
		ResponseListType t = new ResponseListType();
		RequestResultType r = new RequestResultType();

		ReturnType rType = new ReturnType();
		rType.setReturnCode("200");
		r.setReturn(rType);
		r.setMsgformat("odf");
		IdType id = new IdType();
		id.setValue(requestId);
		r.setRequestId(id);
		r.setMsg(helperQLMMethodASODF(result, allConstraints,
				mediator.getModel(), justOneConcept));
		envelope.setResponse(t);
		t.getResult().add(r);
		StringWriter stringWriter = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(OmiEnvelope.class);
			Marshaller m = context.createMarshaller();
			m.marshal(envelope, stringWriter);
			String str = stringWriter.toString();
			System.out.println(str);
			return str;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(java.util.logging.Level.WARNING,
					e.toString());
		}

		String resp = "";
		resp += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
		resp += "<omi:omiEnvelope xmlns:omi=\"omi.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"omi.xsd\" version=\"1.0\" ttl=\"10\">";
		resp += "<omi:response>";
		resp += "<omi:result msgformat=\"odf\">";
		resp += "<omi:return returnCode=\"200\"></omi:return>";
		resp += "<omi:requestId>" + requestId + "</omi:requestId>";
		resp += "<omi:msg xmlns=\"odf.xsd\" xsi:schemaLocation=\"odf.xsd\">";
		// resp+= helperQLMMethod(result, allConstraints,mediator.getModel());
		resp += "</omi:msg>";
		resp += "</omi:result>";
		resp += "</omi:response>";
		resp += "</omi:omiEnvelope>";
		return resp;

	}
}
