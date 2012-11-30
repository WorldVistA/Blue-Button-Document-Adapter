package org.osehra.das.wrapper.nwhin;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.osehra.das.BeanUtils;

@Entity
@Table(name = "C32_DOCUMENT")
public class C32DocumentEntity implements Serializable, Comparable<C32DocumentEntity> {
	private static final long serialVersionUID = 1L;
	
	public C32DocumentEntity() {
		super();
	}
	
	public C32DocumentEntity(String icn, String documentPatientId, Timestamp creationDate, String document) {
		this();
		setIcn(icn);
		setDocumentPatientId(documentPatientId);
		setCreateDate(creationDate);
		setDocument(document);
	}

	@Id
	@GeneratedValue
	@Column(name = "AUDIT_MESSAGE_ID")
	private int id;

	@Column(name = "DOCUMENT")
	@Lob
	private String document;

	@Column(name = "PATIENT_ID")
	private String icn ;

	@Column(name = "CREATE_DATE")
	private Timestamp createDate;

	@Column(name = "DOCUMENT_PATIENT_ID")
	private String documentPatientId;	
	
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
		return this.document;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getIcn() {
		return icn;
	}

	public void setIcn(String icn) {
		this.icn = icn;
	}

	public void setDocumentPatientId(String docPtId) {
		this.documentPatientId = docPtId;
	}

	public String getDocumentPatientId() {
		return documentPatientId;
	}	
	
	@Override
	public String toString() {
		return "C32DocumentEntity{" + "id=" + id + ", document=" + document
				+ '}';
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof C32DocumentEntity)) {
			return false;
		}
		C32DocumentEntity other = (C32DocumentEntity)obj;
		return BeanUtils.equalsNullSafe(this.getId(), other.getId()) && 
				BeanUtils.equalsNullSafe(this.getIcn(), other.getIcn()) && 
				BeanUtils.equalsNullSafe(this.getCreateDate(), other.getCreateDate());
	}
	
	@Override
	public int hashCode() {
		return hashCodeNullSafe(getId()) + hashCodeNullSafe(getIcn()) + hashCodeNullSafe(getCreateDate());
	}

	@Override
	public int compareTo(C32DocumentEntity that) {
		int result = compareNullSafe(this.getIcn(), that.getIcn());
		if (result==0) {
			result = compareNullSafe(this.getCreateDate(), that.getCreateDate());
			if (result==0) {
				result = this.getId()-that.getId();
			}
		}
		return result;
	}

	protected static int hashCodeNullSafe(Object item) {
		if (item==null) {
			return 0;
		}
		return item.hashCode();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static int compareNullSafe(Comparable item1, Comparable item2) {
		if (item1!=null && item2!=null) {
			return item1.compareTo(item2);
		}
		if (item1==null && item2==null) {
			return 0;
		}
		if (item1==null) {
			return -1;
		}
		return 1;
	}

}
