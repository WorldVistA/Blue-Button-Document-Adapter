package org.osehra.integration.util.xsl;

import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

/**
 * Transform a XML based on the XSL specified in the resource.
 * 
 * @author Asha Amritraj
 */
public class DefaultXSLTransformer extends AbstractXSLTransformer {

	/**
	 * @uml.property name="resource"
	 * @uml.associationEnd
	 */
	private Resource resource;

	/**
	 * @param theResource
	 * @uml.property name="resource"
	 */
	@Required
	public void setResource(final Resource theResource) {
		this.resource = theResource;
	}

	@Override
	public Result transform(final Source sourceMessage)
			throws TransformerException {
		return super.transform(this.resource, sourceMessage, null);
	}

	@Override
	public Result transform(final Source sourceMessage,
			final Result targetMessage) throws TransformerException {
		return super.transform(this.resource, sourceMessage, targetMessage,
				null);
	}

	@Override
	public Result transform(final Source sourceMessage,
			final Result targetMessage,
			final Map<String, Object> dynamicParameters)
			throws TransformerException {
		return super.transform(this.resource, sourceMessage, targetMessage,
				dynamicParameters);
	}

}
