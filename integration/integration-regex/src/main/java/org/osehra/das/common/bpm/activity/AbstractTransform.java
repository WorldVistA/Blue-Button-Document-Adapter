package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;
import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.transformer.xsl.XSLTransformer;
import org.osehra.das.common.validation.Assert;
import org.osehra.das.common.validation.NullChecker;
import org.osehra.das.common.xsl.ResultType;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

/**
 * An abstract class for transformation. Based on the result type, it transforms
 * the XML documen to XML or String. If a splitter is supplied, then the result
 * is split.
 * 
 * @author Julian Jewel
 */
public abstract class AbstractTransform extends TransitionImpl {

	/**
	 * The result type, whether XML or STRING.
	 * @uml.property  name="resultType"
	 * @uml.associationEnd  
	 */
	private ResultType resultType;
	/**
	 * The XML to String transformer.
	 * @uml.property  name="stringToXML"
	 * @uml.associationEnd  
	 */
	private Transformer<String, Document> stringToXML;

	/**
	 * Set the result type.
	 * @param theResultType  the result type XML or String.
	 * @uml.property  name="resultType"
	 */
	public final void setResultType(final ResultType theResultType) {
		this.resultType = theResultType;
	}

	/**
	 * Set the XML to String transformer.
	 * 
	 * @param theStringToXML
	 *            the xml to string transformer
	 */
	@Required
	public final void setStringToXML(
			final Transformer<String, Document> theStringToXML) {
		this.stringToXML = theStringToXML;
	}

	/**
	 * Transform the input message using an XSL transformer.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input XML document
	 * @param transformer
	 *            the XSL transformer
	 * @return the transformed object
	 * @throws ActivityException
	 *             if an error occured when transforming the XML document
	 */
	protected final Object transform(final ProcessContext context,
			final Object source, final XSLTransformer transformer)
			throws ActivityException {
		try {
			Assert.assertNotEmpty(source, "Source cannot be empty for bean: "
					+ this.getBeanName() + " -> " + this.getInput());
			Assert.assertNotEmpty(
					transformer,
					"XSL Transformer cannot be empty for bean: "
							+ this.getBeanName());
			// Add dynamic parameters to the XSL transformer. The input to this
			// process are added as parameters to the XSL transformer.
			final Map<String, Object> dynamicParameters = new HashMap<String, Object>();
			Document sourceDocument;
			// if multiple inputs are present
			if (source.getClass().isArray()) {
				final Object[] array = (Object[]) source;
				// Set the first element as the input for the XSL transformer
				sourceDocument = (Document) array[0];
				for (final String input : this.getInputs()) {
					// Get all the inputs and set them as parameters to the
					// transformer
					final Object inputObj = context.get(input);
					if (NullChecker.isNotEmpty(inputObj)) {
						dynamicParameters.put(input, inputObj);
					}
				}
			} else {
				// Single document, set it as an input
				sourceDocument = (Document) source;
			}
			// If STRING - use StreamResult - mostly used when transforming
			// messages with CDATA
			Document resultDoc;
			if (ResultType.STRING.equals(this.resultType)) {
				final StringWriter stringWriter = new StringWriter();
				transformer.transform(new DOMSource(sourceDocument),
						new StreamResult(stringWriter), dynamicParameters);
				resultDoc = this.stringToXML.transform(stringWriter.toString());
			} else { // Use DOMResult
				final DOMResult outputTarget = new DOMResult();
				transformer.transform(new DOMSource(sourceDocument),
						outputTarget, dynamicParameters);
				final DOMResult domResult = outputTarget;
				resultDoc = (Document) domResult.getNode();
			}
			return resultDoc;
		} catch (final TransformerConfigurationException ex) {
			throw new RuntimeException(ex);
		} catch (final TransformerException ex) {
			throw new ActivityException(ex);
		} catch (final javax.xml.transform.TransformerException ex) {
			throw new ActivityException(ex);
		}
	}
}
