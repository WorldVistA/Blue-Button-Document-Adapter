package org.osehra.das.common.rest;

import org.osehra.das.common.component.ComponentException;
import org.osehra.das.common.component.ComponentImpl;
import org.osehra.das.common.receiver.MessageReceiver;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class ResourceMessageReceiver extends ComponentImpl implements
		MessageReceiver<UriInfo, Object> {

	/**
	 * Reverse Proxy Cache for storing results.
	 */
	String REVERSE_PROXY_CACHE = "org.osehra.das.core.ReverseProxyCache";

	/**
	 * The spring application context for convenience.
	 */
	@Autowired
	protected ApplicationContext applicationContext;
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
			return this.processInbound(this.uriInfo);
		} catch (ComponentException ex) {
			throw new InternalServerError(ex.getMessage());
		}
	}
}
