package org.osehra.das.wrapper.nwhin;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Lob;


@Entity
@Table(name="C32_DOCUMENT")
public class C32DocumentEntity implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;
	
	@Column(name="DOCUMENT")
        @Lob
	private byte[] document;
	

	public void setId(int id) {
		this.id = id;
	}
		
	public int getId() {
		return id;
	}

	public void setDocument(String document) {
            this.document = document.getBytes();
	}

	public String getDocument() {
		return document.toString();
	}

        @Override
        public String toString() {
            return "C32DocumentEntity{" + "id=" + id + ", document=" + document + '}';
        }
        
        
}
