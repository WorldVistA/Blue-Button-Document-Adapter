package org.osehra.integration.http;

import java.io.IOException;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.core.transformer.xml.XMLToString;
import org.osehra.integration.util.NullChecker;

import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;

public class XmlSoapServiceInvoker implements
		ServiceInvoker<Document, Document> {

	private WebServiceTemplate webserviceTemplate;
	private static final Log LOG = LogFactory.getLog(XmlSoapServiceInvoker.class);

	@Override
	public Document invoke(final Document input)
			throws ServiceInvocationException {
		DOMSource source = new DOMSource(input);
		DOMResult result = new DOMResult();

		if (LOG.isInfoEnabled()) {
			LOG.info("Sending SOAP Request - Default URI:"
					+ this.webserviceTemplate.getDefaultUri());
			if (NullChecker.isNotEmpty(this.webserviceTemplate.getDestinationProvider())) {
				LOG.info("Sending SOAP Request - Destination Provider"
						+ this.webserviceTemplate.getDestinationProvider().getDestination());
			}
		}

		this.webserviceTemplate.sendSourceAndReceiveToResult(source,
				new WebServiceMessageCallback() {
					@Override
					public void doWithMessage(WebServiceMessage message)
							throws IOException, TransformerException {
						if (LOG.isInfoEnabled()) {
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							message.writeTo(out);
							LOG.info("SOAP Request Payload: "
									+ new String(out.toByteArray()));
						}
					}
				}, result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Received SOAP Response from Default URI:"
					+ this.webserviceTemplate.getDefaultUri());
			if (NullChecker.isNotEmpty(this.webserviceTemplate.getDestinationProvider())) {
				LOG.info("Sending SOAP response from Destination Provider "
						+ this.webserviceTemplate.getDestinationProvider().getDestination());
			}
			if (NullChecker.isNotEmpty(result)
					&& Document.class.isInstance(result.getNode())) {
				try {
					LOG.info("Received SOAP Response:"
							+ new XMLToString().transform((Document) result.getNode()));
				} catch (org.osehra.integration.core.transformer.TransformerException ex) {
					// Do not throw exception
					LOG.error(ex);
				}
			}
		}
		return (Document) result.getNode();
	}

	@Required
	public void setWebserviceTemplate(WebServiceTemplate webserviceTemplate) {
		this.webserviceTemplate = webserviceTemplate;
	}

}
