package org.osehra.das.wrapper.nwhin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name="C32_DOCUMENT")
public class C32DocumentEntity {

//	@Column(name="DOCUMENT")
	private String document;

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDocument() {
		return document;
	}
}
