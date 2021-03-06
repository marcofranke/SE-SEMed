package de.biba.wrapper;

import de.biba.ontology.OntModel;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut f�r Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/

public class DataSourceDescription implements IDataSourceDescription{

	private String nameOfClass;
	private String pathOfPropertyFile;
	private OntModel model;
	private Consistency isConsistence = Consistency.UNKNOWN;
	
	
	public void setNameOfClass(String nameOfClass) {
		this.nameOfClass = nameOfClass;
	}

	public void setPathOfPropertyFile(String pathOfPropertyFile) {
		this.pathOfPropertyFile = pathOfPropertyFile;
	}

	public void setModel(OntModel model) {
		this.model = model;
	}

	@Override
	public String getNameOfClass() {
		// TODO Auto-generated method stub
		return nameOfClass;
	}

	@Override
	public String getPathOfPropertyFile() {
		// TODO Auto-generated method stub
		return pathOfPropertyFile;
	}

	@Override
	public OntModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	
	

	public void setConsistence(Consistency isConsistence) {
		this.isConsistence = isConsistence;
	}

	

	@Override
	public String toString() {
		return "DataSourceDescription [nameOfClass=" + nameOfClass
				+ ", pathOfPropertyFile=" + pathOfPropertyFile + ", model="
				+ model + ", isConsistence=" + isConsistence + "]";
	}

	@Override
	public Consistency isConfigurationValid() {
		// TODO Auto-generated method stub
		return isConsistence;
	}

	
}
