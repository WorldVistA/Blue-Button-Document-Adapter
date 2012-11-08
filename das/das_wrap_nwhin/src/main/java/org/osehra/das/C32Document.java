package org.osehra.das;

public class C32Document {
	protected String _ptId;
	protected String _doc;

	public void setPatientId(String ptId) {
		_ptId = ptId;
	}
	
	public String getPatientId() {
		return _ptId;
	}
	
	public void setDocument(String document) {
		_doc = document;
	}
	
	public String getDocument() {
		return _doc;
	}
}
