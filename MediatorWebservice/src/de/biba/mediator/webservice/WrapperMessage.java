package de.biba.mediator.webservice;

import java.util.List;

/**[MediatorWebService. This is a web service which can request queries against the Reasoning Mediator]
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


public class WrapperMessage {
	String namespace;
	String query;
	List<WrapperProperty> propList;

	public WrapperMessage() {
	}

	public WrapperMessage(String namespace, String query,
			List<WrapperProperty> propList) {
		this.namespace = namespace;
		this.query = query;
		this.propList = propList;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<WrapperProperty> getPropList() {
		return propList;
	}

	public void setPropList(List<WrapperProperty> propList) {
		this.propList = propList;
	}
}

class WrapperProperty {
	int wrapperID;

	public int getWrapperID() {
		return wrapperID;
	}

	public void setWrapperID(int wrapperID) {
		this.wrapperID = wrapperID;
	}
}

class WrapperHTTPProperty extends WrapperProperty {
	String propertyFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;

	public WrapperHTTPProperty() {
	}

	public WrapperHTTPProperty(int wrapperID, String propertyFile,
			String mappingFile, String mappingSchema, String ontologyFile) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}
}

class WrapperSQLProperty extends WrapperProperty {
	String propertyFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;

	public WrapperSQLProperty() {
	}

	public WrapperSQLProperty(int wrapperID, String propertyFile,
			String mappingFile, String mappingSchema, String ontologyFile) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}
}

class WrapperCSVProperty extends WrapperProperty {
	String propertyFile;
	String csvFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;
	String datatypeDefinitionFile;

	public WrapperCSVProperty() {
	}

	public WrapperCSVProperty(String csvFile, int wrapperID,
			String propertyFile, String mappingFile, String mappingSchema,
			String ontologyFile, String datatypeDefinitionFile) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
		this.datatypeDefinitionFile = datatypeDefinitionFile;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	public String getDatatypeDefinitionFile() {
		return datatypeDefinitionFile;
	}

	public void setDatatypeDefinitionFile(String datatypeDefinitionFile) {
		this.datatypeDefinitionFile = datatypeDefinitionFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}
}

class WrapperXLSProperty extends WrapperProperty {
	String propertyFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;
	String datatypeDefinitionFile;
	String configFolder;

	public WrapperXLSProperty() {
	}

	public WrapperXLSProperty(int wrapperID, String propertyFile,
			String mappingFile, String mappingSchema, String ontologyFile,
			String datatypeDefinitionFile, String configFolder) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
		this.datatypeDefinitionFile = datatypeDefinitionFile;
		this.configFolder = configFolder;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getDatatypeDefinitionFile() {
		return datatypeDefinitionFile;
	}

	public void setDatatypeDefinitionFile(String datatypeDefinitionFile) {
		this.datatypeDefinitionFile = datatypeDefinitionFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}

	public String getConfigFolder() {
		return configFolder;
	}

	public void setConfigFolder(String configFolder) {
		this.configFolder = configFolder;
	}
}