package org.osehra.das.common.validator.xml;

import org.osehra.das.common.validation.NullChecker;
import org.osehra.das.common.validator.ValidatorException;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
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
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @return: boolean success/failure of validation
	 * @throws ValidatorException
	 */
	public static boolean validate(final Document doc, final File xsdFile)
			throws SAXException, IOException {

		return SchemaValidationUtil.validate(doc, xsdFile, null, null);
	}

	/**
	 * Validates against an XSD schema.
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
	 * @throws ValidatorException
	 */
	public static boolean validate(final Document doc, final File xsdFile,
			final ErrorHandler errorHandler, final SchemaFactory schemaFactory)
			throws SAXException, IOException {

		if (NullChecker.isEmpty(doc)) {
			throw new SAXException("Attempt to validate null Document object");
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

	/**
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @return: boolean success/failure of validation
	 * @throws ValidatorException
	 */
	public static boolean validate(final Document doc, final Resource xsdFile)
			throws SAXException, IOException {

		return SchemaValidationUtil.validate(doc, xsdFile, null, null);
	}

	/**
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @return: boolean success/failure of validation
	 * @throws ValidatorException
	 */
	public static boolean validate(final Document doc, final Resource xsdFile,
			final ErrorHandler errorHandler) throws SAXException, IOException {

		return SchemaValidationUtil.validate(doc, xsdFile.getFile(),
				errorHandler, null);

	}

	/**
	 * @param doc
	 *            : DOM document to be validated
	 * @param xsdFile
	 *            : specifies schema to validate document against
	 * @param errorHandler
	 *            : validation error handler
	 * @return: boolean success/failure of validation
	 * @throws ValidatorException
	 */
	public static boolean validate(final Document doc, final Resource xsdFile,
			final ErrorHandler errorHandler, final SchemaFactory schemaFactory)
			throws SAXException, IOException {

		return SchemaValidationUtil.validate(doc, xsdFile.getFile(),
				errorHandler, schemaFactory);

	}

}
