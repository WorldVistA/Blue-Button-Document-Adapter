package org.osehra.das.repo.bluebutton;

import java.io.Serializable;
import java.util.Date;

/**
 * Document status object that stores the patient ID, date, and status of a retrieved document. 
 * @author Dept of VA
 *
 */
public class DocStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String _ptId;
	protected Date _dateGenerated;
	protected String _status;

	public DocStatus() {
		super();
	}
	
	public DocStatus(String ptId, Date dateGenerated, String status) {
		this();
		setPatientId(ptId);
		setDateGenerated(dateGenerated);
		setStatus(status);
	}
	
	/**
	 * 
	 * @return Patient ID
	 */
	public String getPatientId() {
		return _ptId;
	}
	
	/**
	 * 
	 * @param ptId Patient ID
	 */
	public void setPatientId(String ptId) {
		this._ptId = ptId;
	}

	/**
	 * 
	 * @return Date generated
	 */
	public Date getDateGenerated() {
		return _dateGenerated;
	}
	
	/**
	 * 
	 * @param _docDate Date generated
	 */
	public void setDateGenerated(Date _docDate) {
		this._dateGenerated = _docDate;
	}

	/**
	 * 
	 * @return Status string
	 */
	public String getStatus() {
		return _status;
	}
	
	/**
	 * 
	 * @param _status Status String
	 */
	public void setStatus(String _status) {
		this._status = _status;
	}

}
