package org.osehra.das;

/**
 * Simple class to hold a C32 (XML string) and the patient ID.
 * @author Dept of VA
 *
 */
public class C32Document {
	protected String _ptId;
	protected String _doc;

	/**
	 * @param ptId Patient ID
	 */
	public void setPatientId(String ptId) {
		_ptId = ptId;
	}

	/**
	 * @return Patient ID
	 */
	public String getPatientId() {
		return _ptId;
	}
	
	/**
	 * @param document XML string of a C32 document
	 */
	public void setDocument(String document) {
		_doc = document;
	}
	
	/**
	 * @return XML string of a C32 document
	 */
	public String getDocument() {
		return _doc;
	}
}
