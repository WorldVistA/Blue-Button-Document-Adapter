package org.osehra.integration.test.runner;

import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;

import javax.ws.rs.core.UriInfo;

import org.w3c.dom.Document;

public class RestComponentTestRunner extends ComponentTestRunner {

	private Transformer<Document, UriInfo> uriMaker;
	private UriInfo inputURI;

	@Override
	boolean isXmlInput() {
		return true;
	}

	@Override
	protected Object transduce() {
		Object result;
		try {
			inputURI = uriMaker.transform((Document) input);
			result = this.component.processInbound(inputURI);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		} catch (ComponentException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public void setUriMaker(Transformer<Document, UriInfo> uriMaker) {
		this.uriMaker = uriMaker;
	}

}
