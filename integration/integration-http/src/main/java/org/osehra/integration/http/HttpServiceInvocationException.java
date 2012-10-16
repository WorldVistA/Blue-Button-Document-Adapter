package org.osehra.integration.http;

import java.net.URI;

import org.osehra.integration.core.service.ServiceInvocationException;

/**
 * 
 * @author John W. May
 *
 */
public class HttpServiceInvocationException extends ServiceInvocationException {

	
	private static final long serialVersionUID = -5081089886931588867L;

	private int errorCode = 404;
	
	private URI remoteResourceURI = null;
		
	public HttpServiceInvocationException() {
		super();
	}

	public HttpServiceInvocationException(final String message) {
		super(message);
	}
	
	public HttpServiceInvocationException(final int errorCode) {
		super();
		this.errorCode = errorCode;
	}
		
	public HttpServiceInvocationException(final Throwable cause) {
		super(cause);
	}
	
	public HttpServiceInvocationException(final int errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public HttpServiceInvocationException(final String message, final Throwable cause) {
		super(message, cause);
	}
		
	public HttpServiceInvocationException(final String message, final URI remoteResourceURI) {
		super(message);
		this.remoteResourceURI = remoteResourceURI;
	}
	
	public HttpServiceInvocationException(final int errorCode, final Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	public HttpServiceInvocationException(final int errorCode, final URI remoteResourceURI) {
		super();
		this.errorCode = errorCode;
		this.remoteResourceURI = remoteResourceURI;
	}
	
	public HttpServiceInvocationException(final int errorCode, final String message, final URI remoteResourceURI) {
		super(message);
		this.errorCode = errorCode;
		this.remoteResourceURI = remoteResourceURI;
	}
	
	public HttpServiceInvocationException(final int errorCode, final String message, final Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public HttpServiceInvocationException(final int errorCode, final URI remoteResourceURI, final Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.remoteResourceURI = remoteResourceURI;
	}
	
	public HttpServiceInvocationException(final String message, final URI remoteResourceURI, final Throwable cause) {
		super(message, cause);		
		this.remoteResourceURI = remoteResourceURI;
	}
	
	public HttpServiceInvocationException(final int errorCode, final String message, final URI remoteResourceURI, final Throwable cause) {
		super(message, cause);	
		this.errorCode = errorCode;
		this.remoteResourceURI = remoteResourceURI;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(final int errorCode) {
		this.errorCode = errorCode;
	}

	public URI getRemoteResourceURI() {
		return remoteResourceURI;
	}

	public void setRemoteResourceURI(final URI remoteResourceURI) {
		this.remoteResourceURI = remoteResourceURI;
	}

	@Override
	public String toString() {
		String errorCod = new Integer(this.errorCode).toString();
		String remoteResUri = this.remoteResourceURI.toString();
		String superStr = super.toString();
			
		return "Http Status Code: "+errorCod+"; remoteResourceURI: "+remoteResUri+"; "+superStr;
	}
	
}
