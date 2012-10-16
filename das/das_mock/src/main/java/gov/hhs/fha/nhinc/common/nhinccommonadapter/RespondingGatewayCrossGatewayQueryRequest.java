package gov.hhs.fha.nhinc.common.nhinccommonadapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 * <p>
 * Java class for RespondingGateway_CrossGatewayQueryRequestType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RespondingGateway_CrossGatewayQueryRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}AdhocQueryRequest"/>
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
@XmlType(name = "", propOrder = { "adhocQueryRequest", "assertion" })
@XmlRootElement(name = "RespondingGateway_CrossGatewayQueryRequest")
public class RespondingGatewayCrossGatewayQueryRequest {

	/**
	 * @uml.property name="adhocQueryRequest"
	 * @uml.associationEnd
	 */
	@XmlElement(name = "AdhocQueryRequest", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0", required = true)
	protected AdhocQueryRequest adhocQueryRequest;
	/**
	 * @uml.property name="assertion"
	 * @uml.associationEnd
	 */
	@XmlElement(required = true)
	protected AssertionType assertion;

	/**
	 * Gets the value of the adhocQueryRequest property.
	 * 
	 * @return possible object is {@link AdhocQueryRequest  }
	 * @uml.property name="adhocQueryRequest"
	 */
	public AdhocQueryRequest getAdhocQueryRequest() {
		return this.adhocQueryRequest;
	}

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
	 * Sets the value of the adhocQueryRequest property.
	 * 
	 * @param value
	 *            allowed object is {@link AdhocQueryRequest  }
	 * @uml.property name="adhocQueryRequest"
	 */
	public void setAdhocQueryRequest(final AdhocQueryRequest value) {
		this.adhocQueryRequest = value;
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

}
