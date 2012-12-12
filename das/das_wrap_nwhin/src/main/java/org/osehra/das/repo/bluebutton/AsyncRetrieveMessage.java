package org.osehra.das.repo.bluebutton;

import java.util.Date;

/**
 * Contains parameters for an asynchronous data retrieval request.
 * 
 * @author Dept of VA
 *
 */
public class AsyncRetrieveMessage {
	protected Date date;
	protected String patientId;
	protected String ptName;
	
	public AsyncRetrieveMessage() {
		super();
	}
	
	public AsyncRetrieveMessage(Date date, String patientId, String name) {
		this();
		this.date = date;
		this.patientId = patientId;
		this.ptName = name;
	}

	/**
	 * @return Document date 
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @param date Document date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return Patient ID
	 */
	public String getPatientId() {
		return patientId;
	}
	
	/**
	 * @param patientId Patient ID
	 */
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return Patient name
	 */
	public String getPatientName() {
		return ptName;
	}

	/**
	 * @param name Patient name
	 */
	public void setPatientName(String name) {
		ptName = name;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(getDate());
		str.append(':');
		str.append(getPatientId());
		str.append(':');
		str.append(getPatientName());
		return str.toString();
	}
}
