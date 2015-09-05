package de.biba.wrapper.simpleXMLWrapper.dataTypes;

import org.jdom2.Element;

public class AdditionalLinkingInforamtion {
	
	private String XMLPath;
	private Element element;
	private String value;
	private String dataPropertyName;
	private XMLTagExctrationInformation infos;
	
	
	public XMLTagExctrationInformation getInfos() {
		return infos;
	}
	public void setInfos(XMLTagExctrationInformation infos) {
		this.infos = infos;
	}
	public String getXMLPath() {
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
