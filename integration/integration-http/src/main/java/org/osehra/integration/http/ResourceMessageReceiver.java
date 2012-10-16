package org.osehra.integration.http;

import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.core.component.ComponentImpl;
import org.osehra.integration.core.receiver.MessageReceiver;
import org.osehra.integration.stream.StreamingInputStreamImpl;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class ResourceMessageReceiver extends ComponentImpl implements
		MessageReceiver<UriInfo, Object> {

	private static final Log LOG = LogFactory
			.getLog(ResourceMessageReceiver.class);

	/**
	 * The spring application context for convenience.
	 */
	@Autowired
	protected ApplicationContext applicationContext;

	/**
	 * Reverse Proxy Cache for storing results.
	 */
	String REVERSE_PROXY_CACHE = "org.osehra.das.core.ReverseProxyCache";
	/**
	 * The URI Info Context.
	 */
	@Context
	protected UriInfo uriInfo;

	/**
	 * Helper method to process incoming messages. Will be directed to the super
	 * class in the future after error handling is done.
	 *
	 * @return results
	 */
	protected Object processIncoming() {
		try {
			Object stream = this.processInbound(this.uriInfo);
			if (InputStream.class.isInstance(stream)) {
				StreamingOutput streamingOutput = new StreamingInputStreamImpl(
						(InputStream) stream);
				return streamingOutput;
			} else {
				return Response.ok().entity(stream).build();
			}
		} catch (ComponentException ex) {
			throw new RuntimeException(ex);
		}
	}
}
