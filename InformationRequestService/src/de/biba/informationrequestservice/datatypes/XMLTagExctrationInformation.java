package de.biba.informationrequestservice.datatypes;

/**
 * [InformationRequestService. This is a mediation based data integration
 * approach which aggregate data sources via Wrapper] Copyright (C) [2015 [Marco
 * Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]
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

public class XMLTagExctrationInformation {

	private String tag;
	private String attribute;
	private String propertyName;
	private boolean isInversalFunctional = false;
	private TokenExtraction extractor = null;
	private Replacements replacements = null;

	public Replacements getReplacements() {
		return replacements;
	}

	public void setReplacements(Replacements replacements) {
		this.replacements = replacements;
	}

	public TokenExtraction getExtractor() {
		return extractor;
	}

	public void setExtractor(TokenExtraction extractor) {
		this.extractor = extractor;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean isInversalFunctional() {
		return isInversalFunctional;
	}

	public void setInversalFunctional(boolean isInversalFunctional) {
		this.isInversalFunctional = isInversalFunctional;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public boolean isThereAAttribute() {
		if ((attribute != null) && (attribute.length() > 1)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isThereAPreProcessingOfValue() {
		if (extractor != null) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "XMLTagExctrationInformation [tag=" + tag + ", attribute="
				+ attribute + ", propertyName=" + propertyName
				+ ", isInversalFunctional=" + isInversalFunctional
				+ ", extractor=" + extractor + ", replacements=" + replacements
				+ "]";
	}

}
