package de.biba.wrapper.simpleXMLWrapper.test;


import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;

import javax.xml.ws.Endpoint;

import org.junit.Before;
import org.junit.Test;

import de.biba.mediator.DataSourceQuery;
import de.biba.mediator.DataSourceQuery.PropertyWithValue;
import de.biba.ontology.DatatypePropertyStatement;
import de.biba.ontology.OntModel;
import de.biba.ontology.datatypes.Datatype;
import de.biba.ontology.datatypes.StringDatatype;
import de.biba.wrapper.simpleXMLWrapper.XMLWrapper;


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

public class TestWrapper {
	
	
private XMLWrapper wrapper = null;
	
	
	@Before
	public void init(){
		try{
		
		 wrapper = new  XMLWrapper();
		wrapper.initialize("Resources/Auto/config.properties");
		
		}
		catch(Exception e){
			org.junit.Assert.fail(e.toString());
		}
	}
	
	@Test
	public void test1(){
		DataSourceQuery dsg = new DataSourceQuery();
		String NS = "http://FITMAN.eu#";
		
		dsg.getPropertyNames().add(NS+"id");
		dsg.getPropertyNames().add(NS+"leistung");
		
		OntModel ont = wrapper.queryData(dsg);
		System.out.println(ont.toString());
		int amount =0;
		int value = 1;
		
		

		
	}

	@Test
	public void test2(){
		DataSourceQuery dsg = new DataSourceQuery();
		String NS = "http://FITMAN.eu#";
		
		dsg.getPropertyNames().add(NS+"id");
		dsg.getPropertyNames().add(NS+"gewicht");
		
		OntModel ont = wrapper.queryData(dsg);
		System.out.println(ont.toString());
		int amount =0;
		
		

		
	}
	
}
