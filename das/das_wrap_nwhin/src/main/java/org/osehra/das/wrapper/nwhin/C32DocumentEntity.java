package org.osehra.das.wrapper.nwhin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "C32_DOCUMENT")
public class C32DocumentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "DOCUMENT")
	@Lob
	private byte[] document;

	@Column(name = "PATIENT_ID")
	private String patientId;

	@Column(name = "DOCUMENT_PATIENT_ID")
	private String documentPatientId;	
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setDocument(String document) {
		C32DocumentLogic logic = new C32DocumentLogic();
		String filteredDocument = logic.filterDocument(document);
		this.document = filteredDocument.getBytes();
	}

	public String getDocument() {
		return document.toString();
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setDocumentPatientId(String document) {
		C32DocumentLogic pidLogic = new C32DocumentLogic();
		String documentPatientId = pidLogic.getPatientId(document);
		this.documentPatientId = documentPatientId;
	}

	public String getDocumentPatientId() {
		return documentPatientId;
	}	
	
	
	
	@Override
	public String toString() {
		return "C32DocumentEntity{" + "id=" + id + ", document=" + document
				+ '}';
	}

}
