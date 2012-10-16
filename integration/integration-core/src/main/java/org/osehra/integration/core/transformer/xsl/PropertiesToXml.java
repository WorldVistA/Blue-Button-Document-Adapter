package org.osehra.integration.core.transformer.xsl;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * Transform a XSL stylesheet with the optional paramters.
 * 
 * @author Asha Amritraj
 * 
 */
public class PropertiesToXml implements
		Transformer<Map<String, Object>, Document> {

	/**
	 * @uml.property name="transformer"
	 * @uml.associationEnd
	 */
	DefaultXSLTransformer transformer;

	/**
	 * @param transformer
	 * @uml.property name="transformer"
	 */
	@Required
	public void setTransformer(final DefaultXSLTransformer transformer) {
		this.transformer = transformer;
	}

	@Override
	public Document transform(final Map<String, Object> parameters)
			throws TransformerException {
		Assert.assertNotEmpty(parameters, "Parameters cannot be empty!");
		try {

			final Result result = this.transformer.transform(new DOMSource(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.newDocument()), new DOMResult(
					DocumentBuilderFactory.newInstance().newDocumentBuilder()
							.newDocument()), parameters);

			if (NullChecker.isNotEmpty(result)
					&& NullChecker.isInstance(result, DOMResult.class)) {
				return (Document) ((DOMResult) result).getNode();
			}

		} catch (final javax.xml.transform.TransformerException ex) {
			throw new TransformerException(ex);
		} catch (final ParserConfigurationException ex) {
			throw new TransformerException(ex);
		}
		return null;
	}

}
