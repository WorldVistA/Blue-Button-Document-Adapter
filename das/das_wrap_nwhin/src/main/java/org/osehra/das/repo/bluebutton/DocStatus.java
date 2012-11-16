package org.osehra.das.repo.bluebutton;

import java.io.Serializable;
import java.util.Date;

public class DocStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String _ptId;
	protected Date _dateGenerated;
	protected String _status;

	public String getPatientId() {
		return _ptId;
	}
	public void setPatientId(String ptId) {
		this._ptId = ptId;
	}

	public Date getDateGenerated() {
		return _dateGenerated;
	}
	public void setDateGenerated(Date _docDate) {
		this._dateGenerated = _docDate;
	}

	public String getStatus() {
		return _status;
	}
	public void setStatus(String _status) {
		this._status = _status;
	}

}
