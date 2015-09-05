package de.biba.mediator;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.biba.ontology.DatatypeProperty;
import de.biba.ontology.ObjectProperty;
import de.biba.ontology.OntClass;
import de.biba.ontology.OntModel;

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
 * Diese Klasse parst eine OWL-Ontologie aus einer Datei und erstellt ein OntModel. Die Datei muss dabei im RDF-XML-Format vorliegen.
 * Generell wird nur die TBox einer Ontologie geparst. Jedes Element muss dabei über ein "id" oder "about"-Attribut verfügen.
 * Folgende OWL-Sprach-Konstrukte werden unterstützt
 * 
 * Class:
 * 	- subclassOf
 * 	- equivalentClass
 * ObjectProperties:
 * 	- subPropertyOf
 * 	- equivalentProperty
 *  - range
 * 	- domain
 * 	- inverseOf
 *  - TransitiveProperty
 *  - SymmetricProperty
 *  - FunctionalProperty
 *  - InverseFunctionalProperty
 *  DatatypeProperties:
 * 	- subPropertyOf
 * 	- equivalentProperty
 * 	- domain
 *  - FunctionalProperty
 *  - InverseFunctionalProperty
 * @author KRA
 *
 */
public class OWLParser {
	
	/**
	 * Parst eine Datei
	 * @param filename Pfad zur Datei die geparst werden soll
	 * @return Geparste Ontologie
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public OntModel parse(String filename) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		OntModel om = new OntModel();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(filename);
		NodeList childs = doc.getChildNodes();
		String base = "";
		OWLNamespaceContext context = new OWLNamespaceContext();
		for(int j=0; j<childs.getLength();j++){
			Node rdf = childs.item(j);
			NamedNodeMap nnm =rdf.getAttributes();
			if(nnm!=null)
			for(int i=0; i<nnm.getLength();i++){
				Node n = nnm.item(i);
				if(n.getNodeName().equals("xml:base"))
					base = n.getNodeValue();
				else if(n.getNodeName().startsWith("xmlns:")){
					context.addContext(n.getNodeName().substring(6), n.getNodeValue());
				}
			}
		}
		
		
		
		XPathFactory xFactory = XPathFactory.newInstance();
		XPath path = xFactory.newXPath();
		path.setNamespaceContext(context);
		
		
		addClassNames(om, doc, path, base);
		addObjectProperties(om, doc, path, base);
		addDatatypeProperties(om, doc, path, base);
		addFunctionalOrInverseFunctionalProperties(om,doc,path,base);
		return om;
	}

	private void addFunctionalOrInverseFunctionalProperties(OntModel om,
			Document doc, XPath path, String base) throws XPathExpressionException {
		String expression = "/rdf:RDF/owl:InverseFunctionalProperty";
		XPathExpression expr = path.compile(expression);
		NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		for(int i=0; i<nodes.getLength();i++){
			Node n = nodes.item(i).getAttributes().getNamedItem("rdf:ID");
			if(n!=null){
				String name = base+n.getNodeValue();
				if(om.tbox.objectProperties.containsKey(n)){
					om.tbox.objectProperties.get(name).setInverseFunctional(true);
					continue;
				}
				else if(om.tbox.datatypeProperties.containsKey(name)){
					om.tbox.objectProperties.get(name).setInverseFunctional(true);
					continue;
				}
				else{
					XPathExpression e = path.compile(expression+"/rdf:type");
					NodeList l = (NodeList)e.evaluate(doc, XPathConstants.NODESET);
					boolean functional = false;
					boolean transitive = false;
					boolean symmetric = false;
					boolean datatype = false;
					boolean object = false;
					for(int j=0; j<l.getLength();j++){
						Node no = l.item(j).getAttributes().getNamedItem("rdf:resource");
						if(no!=null){
							if(no.getNodeValue().endsWith("FunctionalProperty")){
								functional = true;
							}
							else if(no.getNodeValue().endsWith("TransitiveProperty")){
								transitive = true;
							}
							else if(no.getNodeValue().endsWith("SymmetricProperty")){
								symmetric = true;
							}
							else if(no.getNodeValue().endsWith("DatatypeProperty")){
								datatype = true;
							}
							else if(no.getNodeValue().endsWith("ObjectProperty")){
								object = true;
							}
						}
						
					}
					if(datatype){
						DatatypeProperty dp = om.createDatatypeProperty(base, n.getNodeValue());
						dp.setFunctional(functional);
						dp.setInverseFunctional(true);
//						addDomain(dp, n, pPath, base);
					}
				}
			}
		}
	}
	
	private OntClass test(OntModel om,
			Document doc, XPath xpath, String base, String path) throws XPathExpressionException {
		String expression = path+"/owl:Restriction";
		Node result = (Node)xpath.evaluate(expression, doc, XPathConstants.NODE);
		if(result!=null){
			NodeList list = result.getChildNodes();
			for(int i=0; i<list.getLength();i++){
				Node child = list.item(i);
				String childName = child.getNodeName();
				
			}
		}
		
		return null;
	}

	private void addClassNames(OntModel om, Document doc, XPath path, String base) throws XPathExpressionException{
		XPathExpression expr = path.compile("/rdf:RDF/owl:Class");
		NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		for(int i=0; i<nodes.getLength();i++){
			Node n = nodes.item(i).getAttributes().getNamedItem("rdf:ID");
			String name = null;
			if(n!=null){
				name = n.getNodeValue();
//				om.createOntClass(base, name);
			}
			else{
				n =  nodes.item(i).getAttributes().getNamedItem("rdf:about");
				if(n!=null){
					name = n.getNodeValue();
//					om.createOntClass(base, name.substring(1));
				}
			}
			if(name==null)
				continue;
			
			
			String superclass = null;
			Set<String> equivalentClasses = new HashSet<String>();
			NodeList childs = nodes.item(i).getChildNodes();
			for(int j=0; j<childs.getLength();j++){
				Node child = childs.item(j);
				if(child.getNodeName().equals("rdfs:subClassOf")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						superclass = x.getNodeValue(); 
				}
				else if(child.getNodeName().equals("owl:equivalentClass")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						equivalentClasses.add(x.getNodeValue().substring(1)); 
				}
			}
		
			String xbase = base;
			
			if(name.startsWith("#")){
			//Raute abschneiden
				name = name.substring(1);
			}
			else if(name.contains("#")){
				String[] tmp = name.split("#");
				xbase = tmp[0];
				name = tmp[1];
			}
			
			OntClass oc = null;
			if(superclass!=null)
				oc = om.createOntClass(xbase, name, superclass.substring(1));
			else
				oc = om.createOntClass(xbase, name);
			for(String equi : equivalentClasses){
				OntClass b = om.createOntClass(base, equi);
				om.markEquivalentClasses(oc, b);
			}
		}
	}
	
	private void addObjectProperties(OntModel om, Document doc, XPath path, String base) throws XPathExpressionException{
		XPathExpression expr = path.compile("/rdf:RDF/owl:ObjectProperty");
		NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		for(int i=0; i<nodes.getLength();i++){
			Node n = nodes.item(i).getAttributes().getNamedItem("rdf:ID");
			if(n==null)
				n = nodes.item(i).getAttributes().getNamedItem("rdf:about");
			if(n==null)
				continue;
			String name = n.getNodeValue();
			if(name.startsWith("#"))
				name = name.substring(1);
			else if(name.contains("#")){
				base = name.substring(0,name.indexOf('#'));
				name = name.substring(name.indexOf('#')+1);
			}
			String superPropery = null;
			Set<String> equivalentProperties = new HashSet<String>();
			Set<String> domain = new HashSet<String>();
			Set<String> range = new HashSet<String>();
			Set<String> inverse = new HashSet<String>();
			boolean functional = false;
			boolean inverseFunctional = false;
			boolean symmetric = false;
			boolean transitive = false;
//			boolean reflexive = false;
			
			NodeList childs = nodes.item(i).getChildNodes();
			for(int j=0; j<childs.getLength();j++){
				Node child = childs.item(j);
				if(child.getNodeName().equals("rdfs:subPropertyOf")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						superPropery = x.getNodeValue().substring(1); 
				}
				else if(child.getNodeName().equals("owl:equivalentProperty")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						equivalentProperties.add(x.getNodeValue().substring(1)); 
				}
				else if(child.getNodeName().equals("rdfs:range")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						range.add(x.getNodeValue().substring(1)); 
				}
				else if(child.getNodeName().equals("rdfs:domain")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						domain.add(x.getNodeValue().substring(1)); 
				}
				else if(child.getNodeName().equals("owl:inverseOf")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						inverse.add(x.getNodeValue().substring(1)); 
				}
				else if(child.getNodeName().equals("rdf:type")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null){
						String v = x.getNodeValue();
						if(v.equals("http://www.w3.org/2002/07/owl#FunctionalProperty"))
							functional = true;
						else if(v.equals("http://www.w3.org/2002/07/owl#InverseFunctionalProperty"))
							inverseFunctional = true;
						else if(v.equals("http://www.w3.org/2002/07/owl#SymmetricProperty"))
							symmetric = true;
						else if(v.equals("http://www.w3.org/2002/07/owl#TransitiveProperty"))
							transitive = true;
					}
						
				}
			}
			ObjectProperty op = null;
			if(superPropery!=null)
				op = om.createObjectProperty(base, name,superPropery);
			else
				op = om.createObjectProperty(base, name);
			op.setFunctional(functional);
			op.setInverseFunctional(inverseFunctional);
			op.setSymmetric(symmetric);
			op.setTransitive(transitive);
			for(String equi : equivalentProperties){
				ObjectProperty b = om.createObjectProperty(base, equi);
				om.markEquivalentObjectProperties(op, b);
			}
			for(String inv : inverse)
				om.markInverseProperties(op.getUri(), base+"#"+inv);
			
			op.setDomain(domain);
			op.setRange(range);
		}
		
		
	}
	
	

	private void addDatatypeProperties(OntModel om, Document doc, XPath path, String base) throws XPathExpressionException{
		XPathExpression expr = path.compile("/rdf:RDF/owl:DatatypeProperty");
		NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		for(int i=0; i<nodes.getLength();i++){
			Node n = nodes.item(i).getAttributes().getNamedItem("rdf:ID");
			if(n==null)
				n = nodes.item(i).getAttributes().getNamedItem("rdf:about");
			if(n==null)
				continue;
			String name = n.getNodeValue();
			if(name.startsWith("#"))
				name = name.substring(1);
			else if(name.contains("#")){
				base = name.substring(0,name.indexOf('#'));
				name = name.substring(name.indexOf('#')+1);
			}
				
			String superPropery = null;
			Set<String> equivalentProperties = new HashSet<String>();
			Set<String> domain = new HashSet<String>();
			boolean functional = false;
			boolean inverseFunctional = false;
			
			
			NodeList childs = nodes.item(i).getChildNodes();
			for(int j=0; j<childs.getLength();j++){
				Node child = childs.item(j);
				if(child.getNodeName().equals("rdfs:subPropertyOf")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						superPropery = x.getNodeValue().substring(1); 
				}
				else if(child.getNodeName().equals("owl:equivalentProperty")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						equivalentProperties.add(x.getNodeValue().substring(1)); 
				}
				else if(child.getNodeName().equals("rdfs:domain")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null)
						domain.add(x.getNodeValue().substring(1)); 
				}
				else if(child.getNodeName().equals("rdf:type")){
					Node x = child.getAttributes().getNamedItem("rdf:resource");
					if(x!=null){
						String v = x.getNodeValue();
						if(v.equals("http://www.w3.org/2002/07/owl#FunctionalProperty"))
							functional = true;
						else if(v.equals("http://www.w3.org/2002/07/owl#InverseFunctionalProperty"))
							inverseFunctional = true;
					}
				}
			}
			DatatypeProperty dp = null;
			if(superPropery!=null)
				dp = om.createDatatypeProperty(base, name,superPropery);
			else
				dp = om.createDatatypeProperty(base, name);
			dp.setFunctional(functional);
			dp.setInverseFunctional(inverseFunctional);
			
			for(String equi : equivalentProperties){
				DatatypeProperty b = om.createDatatypeProperty(base, equi);
				om.markEquivalentDatatypeProperties(dp, b);
			}
			dp.setDomain(domain);
		}
	}
	
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		OWLParser parser = new OWLParser();
		System.out.println(parser.parse("Resources/Semantic_Descriptions/Ontology_EANCOM.owl"));
	}
	
	private class OWLNamespaceContext implements javax.xml.namespace.NamespaceContext{
		Map<String, String> context1;
		Map<String, String> context2;
		private OWLNamespaceContext(){
			context1 = new HashMap<String,String>();
			context2 = new HashMap<String,String>();
		}
		@Override
		public String getNamespaceURI(String pPrefix) {
			return context1.get(pPrefix);
		}

		@Override
		public String getPrefix(String pNamespaceURI) {
			return context2.get(pNamespaceURI);
		}

		@Override
		public Iterator<String> getPrefixes(String pNamespaceURI) {
			return context1.keySet().iterator();
		}
		
		private void addContext(String prefix, String namespace){
			context1.put(prefix, namespace);
			context2.put(namespace, prefix);
		}
	}
}
