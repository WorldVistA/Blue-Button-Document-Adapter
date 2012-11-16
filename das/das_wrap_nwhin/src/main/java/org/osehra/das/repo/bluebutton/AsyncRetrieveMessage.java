package org.osehra.das.repo.bluebutton;

import java.util.Date;

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

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return ptName;
	}
	
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
