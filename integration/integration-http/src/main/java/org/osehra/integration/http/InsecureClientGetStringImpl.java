package org.osehra.integration.http;

import org.osehra.integration.core.context.ThreadContext;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;
import org.osehra.integration.util.NullChecker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.axiom.attachments.utils.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class InsecureClientGetStringImpl implements
		ServiceInvoker<UriInfo, Object>, InitializingBean {

	private static final Log LOG = LogFactory
			.getLog(InsecureClientGetStringImpl.class);
	
	/**
	 * @uml.property name="baseUri"
	 */
	String baseUri;
	/**
	 * @uml.property name="client"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     elementType="java.lang.String" qualifier=
	 *                     "constant:java.lang.String java.util.List"
	 */
	Client client;
	/**
	 * @uml.property name="clientConfiguration"
	 * @uml.associationEnd
	 */
	ClientConfig clientConfiguration;
	
	Map<String, String>headerValueMap;
	
	private ThreadContext threadContext;

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
		try {			
		String requestId = "";
		if(NullChecker.isNotEmpty(this.threadContext)) {
			requestId = this.threadContext.getRequestId()+"-";
		}	
		
		WebResource wr = null;
		WebResource.Builder wrb = null;
		ClientResponse response = null;
		
		
		if (NullChecker.isEmpty(this.baseUri)) {			
			wr = this.client.resource(uriInfo.getRequestUri());
			LOG.trace(requestId+"For request URI: ");
			logURI(wr.getURI());
		} else {
			// construct the requestUri with any existing query parameters using the given baseUri
			String requestUri = contructRequestUriStrWithNewBaseUriAndQueryParams(uriInfo);			
			wr = this.client.resource(requestUri);
			LOG.trace(requestId+"-For request URI: ");
			logURI(wr.getURI());
		}
		
		try {	
			LOG.trace(requestId + " ...adding HTTP Header Values ");
			wrb = addHeaderValues(wr);
			
			LOG.trace(requestId+" ...making REST GET call to Producer...");
			response = wrb.get(ClientResponse.class);
			this.logClientResponseMetadata(response);
		} catch (final ClientHandlerException ex) {
			throw new HttpServiceInvocationException(404,  
					requestId+"-GET call for URI: "
					+ wr.getURI()
					+ "; Error was received: " 
					+ ex.getLocalizedMessage(), 
					wr.getURI(), ex);	
		}
		
		final List<String> contentTypes = response.getHeaders().get(
				"Content-Type");
		if (NullChecker.isEmpty(contentTypes)) {
			throw new HttpServiceInvocationException(
					requestId+"-GET call for URI: " 
					+ wr.getURI()
					+ "; Error was received: " 
					+ "Content-Type value was not received: "
					+ "Content type has to be supported in REST!", 
					wr.getURI());	
		}	
		
		if (response.getStatus() != Response.Status.OK.getStatusCode()) { 
			// Throw an exception for anything but a successful response
			// e.g. 404 - no answer from remote server for url		
			throw new HttpServiceInvocationException(response.getStatus(), 
					requestId+"-GET call For URI: " 
					+ wr.getURI()
					+ "; Error was received: " 
					+ "Status: " 
					+ response.getStatus() + "-" + response.getHeaders(), 
					wr.getURI());			
		}	
		
		try {			
			final InputStream in = response.getEntityInputStream();
			final String contentType = contentTypes.get(0);
			if ((NullChecker.isNotEmpty(contentType) && 
					(contentType.contains(MediaType.APPLICATION_ATOM_XML)
					|| contentType.contains(MediaType.APPLICATION_XHTML_XML)
					|| contentType.contains(MediaType.APPLICATION_XML)
					|| contentType.contains(MediaType.TEXT_XML)
					|| contentType.contains(MediaType.TEXT_HTML)
					|| contentType.contains(MediaType.TEXT_PLAIN)
					|| contentType.contains(MediaType.APPLICATION_JSON)
					|| contentType.contains(MediaType.APPLICATION_SVG_XML))))  {

				final byte[] bytes = IOUtils.getStreamAsByteArray(in);
				String contentRecd = new String(bytes);
				LOG.trace(requestId+"-Received response of checked, text content-type: ["+contentType+"] from Producer at: ["+wr.getURI()+"] with content: ["+contentRecd+"]");
				return contentRecd;
			} else {
				// Return the input stream.							
				LOG.trace(requestId+"-Received response of unchecked content-type: ["+contentType+"] from Producer at: ["+wr.getURI()+"]");				
				return in;
			}
		} catch (final IOException ex) {
			LOG.error(ex.getMessage(), ex);
			throw new HttpServiceInvocationException(
				requestId+"-In returning data from URI: " + wr.getURI() 
				+ "; IOException was thrown: " 
				+ ex.getLocalizedMessage(), 
				wr.getURI(), ex);
		}
		
		} catch (Exception ex) {
			throw new ServiceInvocationException(ex);
		}
	}
	
	private String contructRequestUriStrWithNewBaseUriAndQueryParams(UriInfo uriInfo) {
		StringBuilder sb = null;
		String queryParameters = null;
		final MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		if (!queryParams.isEmpty()) {
			sb = new StringBuilder("?");
			boolean first = true;
			for (final Entry<String, List<String>> entry : queryParams.entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append("&");
				}
				sb.append(entry.getKey() + "=" + entry.getValue().get(0));
			}
			queryParameters = sb.toString();
		}			
		// verify the path used does not start with a /
		String pathNoOpenSlash = null;
		String uriPath = uriInfo.getPath();
		if(NullChecker.isNotEmpty(uriPath)){
			if(uriPath.startsWith("/")) {
				pathNoOpenSlash = uriPath.substring(uriPath
						.indexOf("/") + 1);
			} else {
				pathNoOpenSlash = uriPath;
			}
		}
		StringBuilder requestUriSb = new StringBuilder(this.baseUri + pathNoOpenSlash);
		if (queryParameters != null) {
			requestUriSb.append(queryParameters);
		}
		return requestUriSb.toString();
	}
	
	private WebResource.Builder addHeaderValues(WebResource wr) {
		WebResource.Builder builder = wr.getRequestBuilder();
		if ((headerValueMap != null) && (!headerValueMap.isEmpty())) {
			for (final Entry<String, String> entry : headerValueMap.entrySet()) {
				LOG.trace("Add header - key: " + entry.getKey() + " value: " + entry.getValue());
				builder = builder.header(entry.getKey(), entry.getValue());
			}
		}
		return builder;
	}
	
	private void logURI(URI uri) {
		// this method was added to prevent the logger from truncating the URI information
		if(NullChecker.isNotEmpty(uri)){
			String requestId = "";
			if(NullChecker.isNotEmpty(this.threadContext)) {
				requestId = this.threadContext.getRequestId()+"-";
			}
			LOG.trace(requestId+"{");
			if(NullChecker.isNotEmpty(uri.getScheme())) {
				LOG.trace(requestId+"URI scheme: "+uri.getScheme()+"://");
			}	
			if(NullChecker.isNotEmpty(uri.getAuthority())) {
				LOG.trace(requestId+"URI authority: "+uri.getAuthority());
			}		
			if(NullChecker.isNotEmpty(uri.getPath())) {
				LOG.trace(requestId+"URI path: "+uri.getPath());
			}
			if(NullChecker.isNotEmpty(uri.getQuery())) {
				LOG.trace(requestId+"URI query: "+uri.getQuery());
			}
			if(NullChecker.isNotEmpty(uri.getFragment())) {
				LOG.trace(requestId+"URI fragment: "+uri.getFragment());
			}
			LOG.trace(requestId+"}");
		}
	}

	private void logClientResponseMetadata(ClientResponse response) {
		if(NullChecker.isNotEmpty(response)) {
			String requestId = "";
			if(NullChecker.isNotEmpty(this.threadContext)) {
				requestId = this.threadContext.getRequestId()+"-";
			}
			LOG.trace(requestId+"For Incoming GET ClientResponse received... ");			
			// response status
			LOG.trace(requestId+"ClientResponse status: "+response.getStatus()+" "+response.getClientResponseStatus());
			// response location
			LOG.trace(requestId+"ClientResponse location: "+response.getLocation());
			// response headers Mvm
			LOG.trace(requestId+"ClientResponse Headers: {");
			MultivaluedMap<String,String> headersMvm = response.getHeaders();
			for(Map.Entry<String, List<String>> entry: headersMvm.entrySet()) {
				String key = entry.getKey();
				List<String> valueList = entry.getValue();
				StringBuilder valueSb = new StringBuilder();
				int currentValuePos = 0;
				for(String curValue: valueList) {
					valueSb.append(curValue);
					if(currentValuePos < (valueList.size()-1)) {
						valueSb.append(", ");
					}
					currentValuePos++;
				}
				String value = valueSb.toString();
				LOG.trace(requestId+"[header name: "+key+" header value(s): "+value+"]");				
			}
			LOG.trace(requestId+"}");
		}
	}
	
	/**
	 * @param baseUri
	 * @uml.property name="baseUri"
	 */
	public void setBaseUri(final String baseUri) {
		// verify the baseUri ends with a /
		if(!baseUri.trim().endsWith("/")) {
			this.baseUri = baseUri.trim().concat("/");
		} else {
			this.baseUri = baseUri.trim();
		}	
	}

	/**
	 * @param clientConfiguration
	 * @uml.property name="clientConfiguration"
	 */
	public void setClientConfiguration(final ClientConfig clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}

	public void setThreadContext(ThreadContext threadContext) {
		this.threadContext = threadContext;
	}

	public void setHeaderValueMap(Map<String, String> headerValueMap) {
		this.headerValueMap = headerValueMap;
	}

}
