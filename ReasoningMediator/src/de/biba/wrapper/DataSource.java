package de.biba.wrapper;

import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.InputQuery;
import de.biba.mediator.OWLParser;
import de.biba.ontology.ABox;
import de.biba.ontology.OntModel;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/

public abstract class DataSource {
	protected OntModel baseModel;
	public DataSource() {
		baseModel = new OntModel();
	}
	public OntModel getOntModel() {
		return baseModel;
	}
	/**
	 * This method loads all files of the configuration and prepare everything to * enable quering information. 
	 * For this purpose, the location of the * configuration file (*.properties) have to be inserted
		 * @param pPropertyFile
	 */
	public abstract void initialize(String pPropertyFile) throws Exception;
	/**
	 * This mathod generate to a incoming infomration request a result
	 * 
	 * @param pDsc
	 *            A DataSourceQuery, which contains all requested concepts,*. properties anfd the current ABox
	 * @return The result is an ontology
	 */
	public abstract OntModel queryData(DataSourceQuery pDsc);
	/**
	 * This methods propose the functionality to load a RDF/XML based ontology.
	 * It should be used in the @{link initialize(String pPropertyFile)} method.
	 * @param pFileName
	 */
	public void loadModel(String pFileName) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		OWLParser parser = new OWLParser();
		baseModel = parser.parse(pFileName);
	}
	/**
	 * This methods enable the adding of information to a data source
	 * @param specific
	 */
	public void insertData(InputQuery specific) {
		System.out.println("Insert:" + specific);
	}
	
		
	/**
	 * This methods enable the configuration via a Hashtable
	 * @param metaData
	 */
	public void init(Hashtable<String, String> metaData) {
	}
	/**
	 * This method return the applied configuration file
	 */
	public abstract String getPathOfConfigurationFile();
	public void insertData(ABox abox) {
		// TODO Auto-generated method stub
		
	}
}
