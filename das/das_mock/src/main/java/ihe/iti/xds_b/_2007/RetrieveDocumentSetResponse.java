package ihe.iti.xds_b._2007;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * <p>
 * Java class for RetrieveDocumentSetResponseType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RetrieveDocumentSetResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0}RegistryResponse"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="DocumentResponse" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="mimeType" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "registryResponse", "documentResponse" })
@XmlRootElement(name = "RetrieveDocumentSetResponse")
public class RetrieveDocumentSetResponse {

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType> &lt;complexContent> &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"> &lt;sequence> &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/> &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/> &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/> &lt;element name="mimeType" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/> &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/> &lt;/sequence> &lt;/restriction> &lt;/complexContent> &lt;/complexType>
	 * </pre>
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "homeCommunityId", "repositoryUniqueId",
			"documentUniqueId", "mimeType", "document" })
	public static class DocumentResponse {

		@XmlElement(name = "Document", required = true)
		protected byte[] document;
		@XmlElement(name = "DocumentUniqueId", required = true)
		protected String documentUniqueId;
		@XmlElement(name = "HomeCommunityId")
		protected String homeCommunityId;
		@XmlElement(required = true)
		protected String mimeType;
		@XmlElement(name = "RepositoryUniqueId", required = true)
		protected String repositoryUniqueId;

		/**
		 * Gets the value of the document property.
		 * 
		 * @return possible object is byte[]
		 * @uml.property name="document"
		 */
		public byte[] getDocument() {
			return this.document;
		}

		/**
		 * Gets the value of the documentUniqueId property.
		 * 
		 * @return possible object is {@link String  }
		 * @uml.property name="documentUniqueId"
		 */
		public String getDocumentUniqueId() {
			return this.documentUniqueId;
		}

		/**
		 * Gets the value of the homeCommunityId property.
		 * 
		 * @return possible object is {@link String  }
		 * @uml.property name="homeCommunityId"
		 */
		public String getHomeCommunityId() {
			return this.homeCommunityId;
		}

		/**
		 * Gets the value of the mimeType property.
		 * 
		 * @return possible object is {@link String  }
		 * @uml.property name="mimeType"
		 */
		public String getMimeType() {
			return this.mimeType;
		}

		/**
		 * Gets the value of the repositoryUniqueId property.
		 * 
		 * @return possible object is {@link String  }
		 * @uml.property name="repositoryUniqueId"
		 */
		public String getRepositoryUniqueId() {
			return this.repositoryUniqueId;
		}

		/**
		 * Sets the value of the document property.
		 * 
		 * @param value
		 *            allowed object is byte[]
		 * @uml.property name="document"
		 */
		public void setDocument(final byte[] value) {
			this.document = (value);
		}

		/**
		 * Sets the value of the documentUniqueId property.
		 * 
		 * @param value
		 *            allowed object is {@link String  }
		 * @uml.property name="documentUniqueId"
		 */
		public void setDocumentUniqueId(final String value) {
			this.documentUniqueId = value;
		}

		/**
		 * Sets the value of the homeCommunityId property.
		 * 
		 * @param value
		 *            allowed object is {@link String  }
		 * @uml.property name="homeCommunityId"
		 */
		public void setHomeCommunityId(final String value) {
			this.homeCommunityId = value;
		}

		/**
		 * Sets the value of the mimeType property.
		 * 
		 * @param value
		 *            allowed object is {@link String  }
		 * @uml.property name="mimeType"
		 */
		public void setMimeType(final String value) {
			this.mimeType = value;
		}

		/**
		 * Sets the value of the repositoryUniqueId property.
		 * 
		 * @param value
		 *            allowed object is {@link String  }
		 * @uml.property name="repositoryUniqueId"
		 */
		public void setRepositoryUniqueId(final String value) {
			this.repositoryUniqueId = value;
		}

	}

	/**
	 * @uml.property name="documentResponse"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType=
	 *                     "ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType$DocumentResponse"
	 */
	@XmlElement(name = "DocumentResponse")
	protected List<RetrieveDocumentSetResponseType.DocumentResponse> documentResponse;

	/**
	 * @uml.property name="registryResponse"
	 * @uml.associationEnd
	 */
	@XmlElement(name = "RegistryResponse", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", required = true)
	protected RegistryResponseType registryResponse;

	/**
	 * Gets the value of the documentResponse property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the documentResponse property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getDocumentResponse().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link RetrieveDocumentSetResponseType.DocumentResponse }
	 * 
	 * 
	 */
	public List<RetrieveDocumentSetResponseType.DocumentResponse> getDocumentResponse() {
		if (this.documentResponse == null) {
			this.documentResponse = new ArrayList<RetrieveDocumentSetResponseType.DocumentResponse>();
		}
		return this.documentResponse;
	}

	/**
	 * Gets the value of the registryResponse property.
	 * 
	 * @return possible object is {@link RegistryResponseType  }
	 * @uml.property name="registryResponse"
	 */
	public RegistryResponseType getRegistryResponse() {
		return this.registryResponse;
	}

	/**
	 * Sets the value of the registryResponse property.
	 * 
	 * @param value
	 *            allowed object is {@link RegistryResponseType  }
	 * @uml.property name="registryResponse"
	 */
	public void setRegistryResponse(final RegistryResponseType value) {
		this.registryResponse = value;
	}

}
