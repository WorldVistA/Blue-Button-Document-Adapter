package org.osehra.integration.test.util.resource;

import org.osehra.integration.test.util.xml.DOMException;
import org.osehra.integration.test.util.xml.DOMParserHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * THe resource util to retrieve Text Resource.
 *
 * @author Julian Jewel
 */
public class ResourceUtil {

	/**
	 * Get the text resource using the application context.
	 *
	 * @param applicationContext
	 *            the application context
	 * @param fullPathFileName
	 *            the full path file name
	 * @return the text string
	 * @throws IOException
	 *             an exception when reading the resource
	 */
	public static String getTextResource(
			final ApplicationContext applicationContext,
			final String fullPathFileName) throws IOException {
		final Resource resource = applicationContext
				.getResource(fullPathFileName);
		return ResourceUtil.getTextResource(resource);
	}

	/**
	 * Get the text resource as a String
	 *
	 * @param resource
	 *            the text resource.
	 * @return the resource as a string
	 * @throws IOException
	 *             if an error occurred
	 */
	public static String getTextResource(final Resource resource)
			throws IOException {
		final InputStream in = resource.getInputStream();
		final String content = convertStreamToString(in);
		in.close();
		return content;
	}

	/**
	 * Convert an InputStream to a String
	 *
	 * @param InputStream to return as a string
	 * @return String value of InputStrinm
	 * @throws IOException if an error occurred
	 */
	static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	/**
	 * Get the text resource as input stream.
	 *
	 * @param applicationContext
	 *            the Spring application context used to locate the resource
	 * @param fullPathFileName
	 *            the file name with full path
	 * @return the input stream, application should close it after use
	 * @throws IOException
	 *             an exception occured when getting the resource
	 */
	public static InputStream getTextResourceAsStream(
			final ApplicationContext applicationContext,
			final String fullPathFileName) throws IOException {

		final Resource resource = applicationContext
				.getResource(fullPathFileName);
		final InputStream in = resource.getInputStream();
		return in;
	}

	/**
	 * Returns the XML document from the specified file.
	 *
	 * @param applicationContext
	 *            the Spring application context used to locate the resource
	 * @param filename
	 *            - The file name.
	 * @return Returns the xml document.
	 * @throws DOMException
	 */
	public static Document getXmlResource(
			final ApplicationContext applicationContext, final String filename)
			throws IOException {
		final String message = ResourceUtil.getTextResource(applicationContext,
				filename);
		try {
			return DOMParserHelper.parseDocument(message);
		} catch (final DOMException ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * Returns the XML document from the specified file.
	 *
	 * @param resource
	 *            - The resource.
	 * @return Returns the xml document.
	 * @throws DOMException
	 */
	public static Document getXmlResource(final Resource resource)
			throws IOException {
		final String message = ResourceUtil.getTextResource(resource);
		try {
			return DOMParserHelper.parseDocument(message);
		} catch (final DOMException ex) {
			throw new IOException(ex);
		}
	}

	/**
	 * Default constructor.
	 */
	protected ResourceUtil() {
	}

}
