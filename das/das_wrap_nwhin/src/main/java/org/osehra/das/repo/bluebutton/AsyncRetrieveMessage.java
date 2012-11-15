package org.osehra.das.repo.bluebutton;

import java.util.Date;

public class AsyncRetrieveMessage {
	protected Date date;
	protected String patientId;
	
	public AsyncRetrieveMessage() {
		super();
	}
	
	public AsyncRetrieveMessage(Date date, String patientId) {
		this();
		this.date = date;
		this.patientId = patientId;
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

}
