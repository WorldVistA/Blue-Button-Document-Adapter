package org.osehra.das.mock.adapter;

import java.io.Serializable;
import java.util.Date;

public class Document implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3536047695910192199L;
	/**
	 * @uml.property name="beginDate"
	 */
	private Date beginDate;
	/**
	 * @uml.property name="classCode"
	 */
	private String classCode;
	/**
	 * @uml.property name="classCodeDisplayName"
	 */
	private String classCodeDisplayName;
	/**
	 * @uml.property name="classCodeScheme"
	 */
	private String classCodeScheme;
	/**
	 * @uml.property name="creationTime"
	 */
	private Date creationTime;
	/**
	 * @uml.property name="documentId"
	 */
	private Long documentId;
	/**
	 * @uml.property name="documentUniqueId"
	 */
	private String documentUniqueId;
	/**
	 * @uml.property name="endDate"
	 */
	private Date endDate;
	/**
	 * @uml.property name="hash"
	 */
	private String hash;
	/**
	 * @uml.property name="lastAccessedTime"
	 */
	private Date lastAccessedTime;
	/**
	 * @uml.property name="mimeType"
	 */
	private String mimeType;
	/**
	 * @uml.property name="patientId"
	 */
	private String patientId;
	/**
	 * @uml.property name="rawData" multiplicity="(0 -1)" dimension="1"
	 */
	private byte[] rawData;
	/**
	 * @uml.property name="size"
	 */
	private Integer size;
	/**
	 * @uml.property name="title"
	 */
	private String title;

	public Document() {
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Document)) {
			return false;
		}
		final Document doc = (Document) obj;
		if ((this != doc)
				&& ((this.documentId == null) || (doc.documentId == null) || !this.documentId
						.equals(doc.documentId))) {
			return false;
		}
		return true;
	}

	/**
	 * @return
	 * @uml.property name="beginDate"
	 */
	public Date getBeginDate() {
		return this.beginDate;
	}

	/**
	 * @return
	 * @uml.property name="classCode"
	 */
	public String getClassCode() {
		return this.classCode;
	}

	/**
	 * @return
	 * @uml.property name="classCodeDisplayName"
	 */
	public String getClassCodeDisplayName() {
		return this.classCodeDisplayName;
	}

	/**
	 * @return
	 * @uml.property name="classCodeScheme"
	 */
	public String getClassCodeScheme() {
		return this.classCodeScheme;
	}

	/**
	 * @return
	 * @uml.property name="creationTime"
	 */
	public Date getCreationTime() {
		return this.creationTime;
	}

	/**
	 * @return
	 * @uml.property name="documentId"
	 */
	public Long getDocumentId() {
		return this.documentId;
	}

	/**
	 * @return
	 * @uml.property name="documentUniqueId"
	 */
	public String getDocumentUniqueId() {
		return this.documentUniqueId;
	}

	/**
	 * @return
	 * @uml.property name="endDate"
	 */
	public Date getEndDate() {
		return this.endDate;
	}

	/**
	 * @return
	 * @uml.property name="hash"
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * @return
	 * @uml.property name="lastAccessedTime"
	 */
	public Date getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	/**
	 * @return
	 * @uml.property name="mimeType"
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * @return
	 * @uml.property name="patientId"
	 */
	public String getPatientId() {
		return this.patientId;
	}

	/**
	 * @return
	 * @uml.property name="rawData"
	 */
	public byte[] getRawData() {
		return this.rawData;
	}

	/**
	 * @return
	 * @uml.property name="size"
	 */
	public Integer getSize() {
		return this.size;
	}

	/**
	 * @return
	 * @uml.property name="title"
	 */
	public String getTitle() {
		return this.title;
	}

	@Override
	public int hashCode() {
		int ret = 0;
		ret += (this.documentId != null ? this.documentId.hashCode() : 0);
		return ret;
	}

	/**
	 * @param beginDate
	 * @uml.property name="beginDate"
	 */
	public void setBeginDate(final Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @param classCode
	 * @uml.property name="classCode"
	 */
	public void setClassCode(final String classCode) {
		this.classCode = classCode;
	}

	/**
	 * @param classCodeDisplayName
	 * @uml.property name="classCodeDisplayName"
	 */
	public void setClassCodeDisplayName(final String classCodeDisplayName) {
		this.classCodeDisplayName = classCodeDisplayName;
	}

	/**
	 * @param classCodeScheme
	 * @uml.property name="classCodeScheme"
	 */
	public void setClassCodeScheme(final String classCodeScheme) {
		this.classCodeScheme = classCodeScheme;
	}

	/**
	 * @param creationTime
	 * @uml.property name="creationTime"
	 */
	public void setCreationTime(final Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @param documentId
	 * @uml.property name="documentId"
	 */
	public void setDocumentId(final Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @param documentUniqueId
	 * @uml.property name="documentUniqueId"
	 */
	public void setDocumentUniqueId(final String documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}

	/**
	 * @param endDate
	 * @uml.property name="endDate"
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param hash
	 * @uml.property name="hash"
	 */
	public void setHash(final String hash) {
		this.hash = hash;
	}

	/**
	 * @param lastAccessedTime
	 * @uml.property name="lastAccessedTime"
	 */
	public void setLastAccessedTime(final Date lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	/**
	 * @param mimeType
	 * @uml.property name="mimeType"
	 */
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @param patientId
	 * @uml.property name="patientId"
	 */
	public void setPatientId(final String patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param rawData
	 * @uml.property name="rawData"
	 */
	public void setRawData(final byte[] rawData) {
		this.rawData = rawData;
	}

	/**
	 * @param size
	 * @uml.property name="size"
	 */
	public void setSize(final Integer size) {
		this.size = size;
	}

	/**
	 * @param title
	 * @uml.property name="title"
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
}
