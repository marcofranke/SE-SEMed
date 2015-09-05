package de.biba.wrapper.simpleXMLWrapper.dataTypes;

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
	
	public boolean isThereAAttribute(){
		if ((attribute != null) && (attribute.length() > 1)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isThereAPreProcessingOfValue(){
		if (extractor!= null){
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
