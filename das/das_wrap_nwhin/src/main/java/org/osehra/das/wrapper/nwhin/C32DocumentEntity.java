package org.osehra.das.wrapper.nwhin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;


@Entity
@Table(name="C32_DOCUMENT")
public class C32DocumentEntity {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name="DOCUMENT")
	private String document;
	

	public void setId(int id) {
		this.id = id;
	}
		
	public int getId() {
		return id;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getDocument() {
		return document;
	}
}
