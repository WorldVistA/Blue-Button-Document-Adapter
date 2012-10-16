package org.osehra.integration.http;

import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.NullChecker;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ClientGetStringImpl implements ServiceInvoker<String, String>,
		InitializingBean {

	/**
	 * @uml.property name="client"
	 * @uml.associationEnd
	 */
	Client client;

	/**
	 * @uml.property name="clientConfiguration"
	 * @uml.associationEnd
	 */
	ClientConfig clientConfiguration;
	/**
	 * @uml.property name="mediaType"
	 * @uml.associationEnd
	 */
	MediaType mediaType;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (NullChecker.isNotEmpty(this.clientConfiguration)) {
			this.client = Client.create(this.clientConfiguration);
		} else {
			final ClientConfig config = new DefaultClientConfig();
			this.client = Client.create(config);
		}
	}

	@Override
	public String invoke(final String url) throws ServiceInvocationException {
		final WebResource wr = this.client.resource(url);
		final String response = wr.type(this.mediaType).get(String.class);
		return response;
	}

	/**
	 * @param clientConfiguration
	 * @uml.property name="clientConfiguration"
	 */
	public void setClientConfiguration(final ClientConfig clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}

	/**
	 * @param mediaType
	 * @uml.property name="mediaType"
	 */
	@Required
	public void setMediaType(final MediaType mediaType) {
		this.mediaType = mediaType;
	}
}
