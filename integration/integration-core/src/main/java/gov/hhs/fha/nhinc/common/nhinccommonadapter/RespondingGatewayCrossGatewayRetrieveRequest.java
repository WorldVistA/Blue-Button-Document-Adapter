package gov.hhs.fha.nhinc.common.nhinccommonadapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RespondingGateway_CrossGatewayRetrieveRequestType complex
 * type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RespondingGateway_CrossGatewayRetrieveRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:ihe:iti:xds-b:2007}RetrieveDocumentSetRequest"/>
 *         &lt;element name="assertion" type="{urn:gov:hhs:fha:nhinc:common:nhinccommon}AssertionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * Modified By: Asha Amritraj - Created Root elements for Spring-WS and JAXB
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "retrieveDocumentSetRequest", "assertion" })
@XmlRootElement(name = "RespondingGateway_CrossGatewayRetrieveRequest")
public class RespondingGatewayCrossGatewayRetrieveRequest {

	/**
	 * @uml.property name="assertion"
	 * @uml.associationEnd
	 */
	@XmlElement(required = true)
	protected AssertionType assertion;
	/**
	 * @uml.property name="retrieveDocumentSetRequest"
	 * @uml.associationEnd
	 */
	@XmlElement(name = "RetrieveDocumentSetRequest", namespace = "urn:ihe:iti:xds-b:2007", required = true)
	protected RetrieveDocumentSetRequestType retrieveDocumentSetRequest;

	/**
	 * Gets the value of the assertion property.
	 * 
	 * @return possible object is {@link AssertionType  }
	 * @uml.property name="assertion"
	 */
	public AssertionType getAssertion() {
		return this.assertion;
	}

	/**
	 * Gets the value of the retrieveDocumentSetRequest property.
	 * 
	 * @return possible object is {@link RetrieveDocumentSetRequestType  }
	 * @uml.property name="retrieveDocumentSetRequest"
	 */
	public RetrieveDocumentSetRequestType getRetrieveDocumentSetRequest() {
		return this.retrieveDocumentSetRequest;
	}

	/**
	 * Sets the value of the assertion property.
	 * 
	 * @param value
	 *            allowed object is {@link AssertionType  }
	 * @uml.property name="assertion"
	 */
	public void setAssertion(final AssertionType value) {
		this.assertion = value;
	}

	/**
	 * Sets the value of the retrieveDocumentSetRequest property.
	 * 
	 * @param value
	 *            allowed object is {@link RetrieveDocumentSetRequestType  }
	 * @uml.property name="retrieveDocumentSetRequest"
	 */
	public void setRetrieveDocumentSetRequest(
			final RetrieveDocumentSetRequestType value) {
		this.retrieveDocumentSetRequest = value;
	}

}
