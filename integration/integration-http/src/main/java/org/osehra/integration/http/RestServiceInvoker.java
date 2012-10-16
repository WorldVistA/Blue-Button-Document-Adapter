package org.osehra.integration.http;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.NullChecker;

import javax.ws.rs.core.UriInfo;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestServiceInvoker implements ServiceInvoker<UriInfo, Object>,
		InitializingBean {

	/**
	 * @uml.property name="baseUri"
	 */
	private String baseUri;
	/**
	 * @uml.property name="credentials"
	 * @uml.associationEnd
	 */
	private Credentials credentials;
	/**
	 * @uml.property name="restTemplate"
	 * @uml.associationEnd
	 */
	private RestTemplate restTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		final CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) this.restTemplate
				.getRequestFactory();
		final HttpClient client = factory.getHttpClient();
		if (NullChecker.isNotEmpty(this.credentials)) {
			client.getState().setCredentials(AuthScope.ANY, this.credentials);
		}

	}

	@Override
	public Object invoke(final UriInfo uriInfo)
			throws ServiceInvocationException {
		final String response = this.restTemplate.getForObject(this.baseUri
				+ uriInfo.getPath(), String.class);
		return response;

	}

	/**
	 * @param baseUri
	 * @uml.property name="baseUri"
	 */
	@Required
	public void setBaseUri(final String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @param credentials
	 * @uml.property name="credentials"
	 */
	public void setCredentials(final Credentials credentials) {
		this.credentials = credentials;
	}

	/**
	 * @param restTemplate
	 * @uml.property name="restTemplate"
	 */
	@Required
	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

}
