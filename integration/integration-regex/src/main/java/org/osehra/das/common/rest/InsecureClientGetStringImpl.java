package org.osehra.das.common.rest;

import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.validation.NullChecker;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.axiom.attachments.utils.IOUtils;
import org.springframework.beans.factory.InitializingBean;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class InsecureClientGetStringImpl implements
		ServiceInvoker<UriInfo, Object>, InitializingBean {

	/**
	 * @uml.property  name="baseUri"
	 */
	String baseUri;
	/**
	 * @uml.property  name="client"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="java.lang.String" qualifier="constant:java.lang.String java.util.List"
	 */
	Client client;
	/**
	 * @uml.property  name="clientConfiguration"
	 * @uml.associationEnd  
	 */
	ClientConfig clientConfiguration;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (NullChecker.isNotEmpty(this.clientConfiguration)) {
			this.client = Client.create(this.clientConfiguration);
		} else {
			final HostnameVerifier hv = this.getHostnameVerifier();
			final ClientConfig config = new DefaultClientConfig();
			final SSLContext ctx = this.getSSLContext();
			config.getProperties().put(
					HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
					new HTTPSProperties(hv, ctx));
			this.client = Client.create(config);
		}
	}

	private HostnameVerifier getHostnameVerifier() {
		final HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(final String arg0, final SSLSession arg1) {
				return true;
			}
		};
		return hv;
	}

	private SSLContext getSSLContext() throws NoSuchAlgorithmException,
			KeyManagementException {
		final SSLContext sslContext = SSLContext.getInstance("SSL");
		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(final X509Certificate[] certs,
					final String authType) {
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] certs,
					final String authType) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} }, new SecureRandom());
		return sslContext;
	}

	@Override
	public Object invoke(final UriInfo uriInfo)
			throws ServiceInvocationException {
		WebResource wr = null;
		if (NullChecker.isEmpty(this.baseUri)) {
			wr = this.client.resource(uriInfo.getRequestUri());
		} else {
			wr = this.client.resource(this.baseUri + uriInfo.getPath());
		}
		final ClientResponse response = wr.get(ClientResponse.class);
		final List<String> contentTypes = response.getHeaders().get(
				"Content-Type");
		if (NullChecker.isEmpty(contentTypes)) {
			throw new ServiceInvocationException(
					"Content type has to be supported in REST!");
		}
		if(Response.Status.NOT_FOUND.equals(response.getStatus())) {
			// TODO: BHIE Dev. Team - Handle all error types and put error entry in atom feed and return
			throw new RuntimeException("Error received " + response.getStatus() + "-" + response.getHeaders());
		}
		try {
			final InputStream in = response.getEntityInputStream();
			final byte[] bytes = IOUtils.getStreamAsByteArray(in);
			final String contentType = contentTypes.get(0);
			if (MediaType.APPLICATION_ATOM_XML.equals(contentType)
					|| MediaType.APPLICATION_XML.equals(contentType)
					|| MediaType.APPLICATION_XHTML_XML.equals(contentType)
					|| MediaType.APPLICATION_XHTML_XML.equals(contentType)
					|| MediaType.TEXT_XML.equals(contentType)
					|| MediaType.TEXT_HTML.equals(contentType)
					|| MediaType.TEXT_PLAIN.equals(contentType)
					|| MediaType.APPLICATION_JSON.equals(contentType)
					|| MediaType.APPLICATION_SVG_XML.equals(contentType)) {
				return new String(bytes);
			} else {
				return bytes;
			}
		} catch (final IOException ex) {
			throw new ServiceInvocationException(ex);
		}

	}

	/**
	 * @param baseUri
	 * @uml.property  name="baseUri"
	 */
	public void setBaseUri(final String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @param clientConfiguration
	 * @uml.property  name="clientConfiguration"
	 */
	public void setClientConfiguration(final ClientConfig clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}

}
