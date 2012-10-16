package org.osehra.integration.core.validator.xml;

import org.osehra.integration.util.NullChecker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * A utility to validate against a schema.
 * 
 * @author
 * 
 *         TODO: Implement a new SchemaFactoryImpl.
 */
public class SchemaValidationUtil {

	/**
	 * Default schema factory instance. TODO: Move to META-INF/services if
	 * possible.
	 */
	private static SchemaFactory defaultSchemaFactory = SchemaFactory
			.newInstance(
					XMLConstants.W3C_XML_SCHEMA_NS_URI,
					"com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory",
					SchemaValidationUtil.class.getClassLoader());

	/**
	 * Removed non-initialized static variable bug.
	 */
	// private static Schema schema;
	
	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) file.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @return: boolean success/failure of validation
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final File xsdFile)
			throws SAXException, IOException {

		return SchemaValidationUtil.validate(doc, xsdFile, null, null);
	}
	
	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) file.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @return: boolean success/failure of validation
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final File xsdFile,
			final ErrorHandler errorHandler)
			throws SAXException, IOException {
		
		Source schemaSource = new StreamSource(xsdFile);	
		
		return SchemaValidationUtil.validate(doc, schemaSource, errorHandler, null);
	}

	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) file.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @param schemaFactory
	 *            Schema Factory to use
	 * @return: boolean success/failure of validation
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final File xsdFile,
			final ErrorHandler errorHandler, final SchemaFactory schemaFactory)
			throws SAXException, IOException {
		
		Source schemaSource = new StreamSource(xsdFile);	
		
		return SchemaValidationUtil.validate(doc, schemaSource, errorHandler, schemaFactory);
	}

	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) file.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @return: boolean success/failure of validation
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final Resource xsdFile)
			throws SAXException, IOException {			
		
		InputStream is = xsdFile.getInputStream();
		Source schemaSource = new StreamSource(is);
		
		return SchemaValidationUtil.validate(doc, schemaSource, null, null);	
	}

	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) file.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @return: boolean success/failure of validation
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final Resource xsdFile,
			final ErrorHandler errorHandler) throws SAXException, IOException {

		InputStream is = xsdFile.getInputStream();
		Source schemaSource = new StreamSource(is);
		
		return SchemaValidationUtil.validate(doc, schemaSource, errorHandler, null);
	}

	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) file.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @param schemaFactory
	 *            Schema Factory to use
	 * @return: boolean success/failure of validation
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final Resource xsdFile,
			final ErrorHandler errorHandler, final SchemaFactory schemaFactory)
			throws SAXException, IOException {

		InputStream is = xsdFile.getInputStream();
		Source schemaSource = new StreamSource(is);		
		
		return SchemaValidationUtil.validate(doc, schemaSource, errorHandler, schemaFactory);	
	}	
	
	/**
	 * Validate the given Document against the given 
	 * XML Schema (.xsd) Source.
	 * 
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @param schemaFactory
	 *            Schema Factory to use
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean validate(final Document doc, final Source xsdFile,
			final ErrorHandler errorHandler, final SchemaFactory schemaFactory)
			throws SAXException, IOException {	
		
		if (NullChecker.isEmpty(doc)) {
			throw new SAXException("Attempted to validate a null Document doc object");
		}
		// TODO: Refactor and cache at a SchemaFactory!
		Schema schema;
		if (NullChecker.isEmpty(schemaFactory)) {		
			schema = SchemaValidationUtil.defaultSchemaFactory
					.newSchema(xsdFile);
		} else {			
			schema = schemaFactory.newSchema(xsdFile);
		}
		final javax.xml.validation.Validator validator = schema.newValidator();
		if (NullChecker.isNotEmpty(errorHandler)) {
			validator.setErrorHandler(errorHandler);
		}		
		validator.validate(new DOMSource(doc));

		return true;
	}

}
