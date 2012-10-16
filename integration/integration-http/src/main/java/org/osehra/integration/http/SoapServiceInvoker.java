 package org.osehra.integration.http;

import org.osehra.integration.core.service.NamedServiceInvoker;
import org.osehra.integration.core.service.ServiceInvocationException;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponse;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ws.client.core.WebServiceTemplate;

public class SoapServiceInvoker implements NamedServiceInvoker</*UriInfo*/ Object, Object>, InitializingBean{

	private WebServiceTemplate webserviceTemplate;

	private static final Log LOG = LogFactory.getLog(SoapServiceInvoker.class);

	/**
	 * componentName property of the Component invoking this service.
	 */
	private String componentName;

	/**
	 * componentId property of the Component invoking this service.
	 */
	private String componentId;


	@Override
	public Object invoke(Object request /*UriInfo uriInfo*/) throws ServiceInvocationException {

		LOG.info("DoD Adapter Wrapper SOAP request");
/*		
		LOG.info("Path: " + uriInfo.getRequestUri().toString());

		// get second pass fields from UriInfo object
		MultivaluedMap<String, String> map = uriInfo.getPathParameters();
		String homeCommunityId = (String)map.getFirst("homeCommunityId");
		String repositoryUniqueId = (String)map.getFirst("remoteRepositoryId");
		String documentUniqueId = (String)map.getFirst("documentUniqueId");

		if (!((StringUtils.isNotBlank(homeCommunityId)) &&
			(StringUtils.isNotBlank(repositoryUniqueId)) &&
			(StringUtils.isNotBlank(documentUniqueId))))
			throw new RuntimeException("UriInfo object does not contain required information");

		// store info from UriInfo object into DocumentRequest object
		DocumentRequest docRequest = new DocumentRequest();
        docRequest.setHomeCommunityId(homeCommunityId);
        docRequest.setRepositoryUniqueId(repositoryUniqueId);
        docRequest.setDocumentUniqueId(documentUniqueId);

        // request
        RetrieveDocumentSetRequestType retrieveDocumentSetRequest = new RetrieveDocumentSetRequestType();
        retrieveDocumentSetRequest.getDocumentRequest().add(docRequest);

        LOG.info("DocumentRequest: " + retrieveDocumentSetRequest.getDocumentRequest().toString());

        RespondingGatewayCrossGatewayRetrieveRequest soapRequest = new RespondingGatewayCrossGatewayRetrieveRequest();
        soapRequest.setRetrieveDocumentSetRequest(retrieveDocumentSetRequest);

*/
		LOG.info("DoD Adapter Wrapper - preparing to send SOAP request to webserviceTemplate");

		Object response  = this.webserviceTemplate.marshalSendAndReceive(request);
		
		LOG.info("DoD Adapter Wrapper - soap response successfully returned from webserviceTemplate");

		RetrieveDocumentSetResponse soapResponse  = (RetrieveDocumentSetResponse)response;
		RetrieveDocumentSetResponseType.DocumentResponse docResponse = soapResponse.getDocumentResponse().get(0);
		LOG.info("HomeCommunityId: " + docResponse.getHomeCommunityId());
		LOG.info("RepositoryUniqueId: " + docResponse.getRepositoryUniqueId());
		LOG.info("DocumentUniqueId: " + docResponse.getDocumentUniqueId());
		LOG.info("MimeType: " + docResponse.getMimeType());

		byte[] doc = docResponse.getDocument();
		LOG.info(new String(doc));
		return response;

	}

	/**
	 * @param webserviceTemplate the webserviceTemplate to set
	 */
	@Required
	public void setWebserviceTemplate(WebServiceTemplate webserviceTemplate) {
		this.webserviceTemplate = webserviceTemplate;
	}

	/**
	 * @param componentName the componentName to set
	 */
	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * @param componentId the componentId to set
	 */
	@Override
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * @param
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
