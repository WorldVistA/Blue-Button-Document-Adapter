package org.osehra.integration.core.validator.xml;

import java.io.File;
import java.util.Hashtable;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.springframework.context.annotation.Configuration;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * The default schemafactory implementation to cache files.
 * 
 * @author Julian Jewel
 */
@Configuration
public class SchemaFactoryImpl extends javax.xml.validation.SchemaFactory {

	/**
	 * The schema cache.
	 * 
	 * @uml.property name="schemaCache"
	 * @uml.associationEnd 
	 *                     qualifier="getAbsolutePath:java.lang.String javax.xml.validation.Schema"
	 */
	private final java.util.Hashtable<String, Schema> schemaCache = new Hashtable<String, Schema>();
	/**
	 * The schema factory.
	 * 
	 * @uml.property name="schemaFactory"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private final SchemaFactory schemaFactory;

	/**
	 * Default constructor, it uses the JAXB XML schema factory by default.
	 */
	public SchemaFactoryImpl() {

		this.schemaFactory = SchemaFactory
				.newInstance(
						XMLConstants.W3C_XML_SCHEMA_NS_URI,
						"com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory",
						this.getClass().getClassLoader());
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return this.schemaFactory.getErrorHandler();
	}

	@Override
	public LSResourceResolver getResourceResolver() {
		return this.schemaFactory.getResourceResolver();
	}

	@Override
	public boolean isSchemaLanguageSupported(final String schemaLanguage) {
		return this.schemaFactory.isSchemaLanguageSupported(schemaLanguage);
	}

	@Override
	public Schema newSchema() throws SAXException {
		return this.schemaFactory.newSchema();
	}

	/**
	 * Cache files.
	 * 
	 * @param schemaFile
	 *            the schema file to cache
	 * @return the schema
	 * @throws SAXException
	 *             an exception when creating the new schema
	 */
	@Override
	public Schema newSchema(final File schemaFile) throws SAXException {

		if (this.schemaCache.containsKey(schemaFile.getAbsolutePath())) {
			return this.schemaCache.get(schemaFile.getAbsolutePath());
		}
		final Schema schema = this.schemaFactory.newSchema(schemaFile);
		this.schemaCache.put(schemaFile.getAbsolutePath(), schema);
		return schema;
	}

	@Override
	public Schema newSchema(final Source[] schemas) throws SAXException {
		return this.schemaFactory.newSchema(schemas);
	}

	@Override
	public void setErrorHandler(final ErrorHandler errorHandler) {
		this.schemaFactory.setErrorHandler(errorHandler);
	}

	@Override
	public void setResourceResolver(
			final LSResourceResolver resourceResolver) {
		this.schemaFactory.setResourceResolver(resourceResolver);
	}
}
