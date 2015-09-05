package de.biba.informationrequestservice.datatypes;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Element;

/**[InformationRequestService. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2015  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

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

public class AdditionalLinkingInforamtion {
	
	private String XMLPath;
	private Element element;
	private String value;
	private String dataPropertyName;
	private XMLTagExctrationInformation infos;
	private boolean hasBeenReduced = false;
	
	
	
	
	public boolean isHasBeenReduced() {
		return hasBeenReduced;
	}
	public void setHasBeenReduced(boolean hasBeenReduced) {
		this.hasBeenReduced = hasBeenReduced;
	}
	public XMLTagExctrationInformation getInfos() {
		return infos;
	}
	public void setInfos(XMLTagExctrationInformation infos) {
		this.infos = infos;
	}
	public String getXMLPath() {
		if ((XMLPath == null) || (XMLPath.length() <1)){
			Logger.getAnonymousLogger().log(Level.WARNING, "There is no value for XML Path: " + dataPropertyName);
		}
		return XMLPath;
	}
	public void setXMLPath(String xMLPath) {
		XMLPath = xMLPath;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDataPropertyName() {
		return dataPropertyName;
	}
	public void setDataPropertyName(String dataPropertyName) {
		this.dataPropertyName = dataPropertyName;
	}
	@Override
	public String toString() {
		return "AdditionalLinkingInforamtion [XMLPath=" + XMLPath
				+ ", element=" + element + ", value=" + value
				+ ", dataPropertyName=" + dataPropertyName + "]";
	}
	
	
	

}
