package org.osehra.das.common.rest.url;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class UriInfoImpl implements UriInfo {

	/**
	 * @uml.property  name="absolutePath"
	 */
	URI absolutePath;
	/**
	 * @uml.property  name="baseUri"
	 */
	URI baseUri;
	/**
	 * @uml.property  name="multiValuedMapPathParameters"
	 * @uml.associationEnd  
	 */
	MultivaluedMap<String, String> multiValuedMapPathParameters;
	/**
	 * @uml.property  name="multiValuedMapQueryParameters"
	 * @uml.associationEnd  
	 */
	MultivaluedMap<String, String> multiValuedMapQueryParameters;
	/**
	 * @uml.property  name="path"
	 */
	String path;
	/**
	 * @uml.property  name="requestUri"
	 */
	URI requestUri;

	/**
	 * @return
	 * @uml.property  name="absolutePath"
	 */
	@Override
	public URI getAbsolutePath() {
		return this.absolutePath;
	}

	@Override
	public UriBuilder getAbsolutePathBuilder() {
		throw new RuntimeException("Method not supported!");
	}

	/**
	 * @return
	 * @uml.property  name="baseUri"
	 */
	@Override
	public URI getBaseUri() {
		return this.baseUri;
	}

	@Override
	public UriBuilder getBaseUriBuilder() {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public List<Object> getMatchedResources() {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public List<String> getMatchedURIs() {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public List<String> getMatchedURIs(final boolean decode) {
		throw new RuntimeException("Method not supported!");
	}

	/**
	 * @return
	 * @uml.property  name="path"
	 */
	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public String getPath(final boolean decode) {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public MultivaluedMap<String, String> getPathParameters() {
		return this.multiValuedMapPathParameters;
	}

	@Override
	public MultivaluedMap<String, String> getPathParameters(final boolean decode) {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public List<PathSegment> getPathSegments() {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public List<PathSegment> getPathSegments(final boolean decode) {
		throw new RuntimeException("Method not supported!");
	}

	@Override
	public MultivaluedMap<String, String> getQueryParameters() {
		return this.multiValuedMapQueryParameters;
	}

	@Override
	public MultivaluedMap<String, String> getQueryParameters(
			final boolean decode) {
		throw new RuntimeException("Method not supported!");
	}

	/**
	 * @return
	 * @uml.property  name="requestUri"
	 */
	@Override
	public URI getRequestUri() {
		return this.requestUri;
	}

	@Override
	public UriBuilder getRequestUriBuilder() {
		throw new RuntimeException("Method not supported!");
	}

	/**
	 * @param uri
	 * @uml.property  name="absolutePath"
	 */
	public void setAbsolutePath(final URI uri) {
		this.absolutePath = uri;
	}

	/**
	 * @param uri
	 * @uml.property  name="baseUri"
	 */
	public void setBaseUri(final URI uri) {
		this.baseUri = uri;
	}

	/**
	 * @param path
	 * @uml.property  name="path"
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	public void setPathParameters(
			final MultivaluedMap<String, String> parameters) {
		this.multiValuedMapPathParameters = parameters;
	}

	public void setQueryParameters(
			final MultivaluedMap<String, String> parameters) {
		this.multiValuedMapQueryParameters = parameters;
	}

	/**
	 * @param uri
	 * @uml.property  name="requestUri"
	 */
	public void setRequestUri(final URI uri) {
		this.requestUri = uri;
	}

}
