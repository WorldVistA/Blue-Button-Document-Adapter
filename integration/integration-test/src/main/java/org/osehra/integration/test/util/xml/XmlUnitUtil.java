package org.osehra.integration.test.util.xml;

//import org.osehra.datasharing.common.xml.DOMSerializerHelper;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.junit.Assert;
import org.w3c.dom.Document;

/**
 * Contains convenience methods for running XmlUnit compares.
 * 
 * @author Keith Roberts
 * 
 */
public class XmlUnitUtil {

	private static final Log log = LogFactory.getLog(XmlUnitUtil.class);

	private static String CDATA_BEGIN = "<![CDATA[";

	private static String CDATA_END = "]]>";

	public static String filterCDATA(final Document xmlDoc) throws Exception {
		final String xmlString = DOMSerializerHelper.serializeDocument(xmlDoc);
		return XmlUnitUtil.filterCDATA(xmlString);
	}

	/**
	 * Strips all CDATA tags out.
	 * 
	 * @param xmlString
	 *            - The input xml string.
	 * @return Returns the xml string without any CDATA tags in it.
	 */
	public static String filterCDATA(final String xmlString) {
		StringBuffer buffer = new StringBuffer(xmlString);
		int idx = 0;
		while (true) {
			int begIdx = buffer.indexOf(XmlUnitUtil.CDATA_BEGIN, idx);
			if (begIdx < 0) {
				break;
			}
			int endIdx = begIdx + XmlUnitUtil.CDATA_BEGIN.length();
			if (endIdx >= buffer.length()) {
				break;
			}
			buffer = buffer.delete(begIdx, endIdx);
			begIdx = buffer.indexOf(XmlUnitUtil.CDATA_END, idx
					- XmlUnitUtil.CDATA_BEGIN.length());
			if (begIdx < 0) {
				break;
			}
			endIdx = begIdx + XmlUnitUtil.CDATA_END.length();
			if (endIdx >= buffer.length()) {
				break;
			}
			buffer = buffer.delete(begIdx, endIdx);
			idx = endIdx + 1;

		}
		return buffer.toString();

	}

	private static void printDifferences(final DetailedDiff myDiff) {
		final List<?> list = myDiff.getAllDifferences();
		XmlUnitUtil.log
				.error("Documents are not identical, Print all differences");
		for (int i = 0; i < list.size(); i++) {
			XmlUnitUtil.log.info(list.get(i));
		}
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlDoc
	 *            - The input xml document.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffIdentical(final Document xmlDoc,
			final String validXmlString, final boolean doPrint)
			throws Exception {
		final String xmlString = DOMSerializerHelper.serializeDocument(xmlDoc);
		XmlUnitUtil.xmlDiffIdentical(xmlString, validXmlString, doPrint);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlString
	 *            - The input xml document string.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffIdentical(final String xmlString,
			final String validXmlString, final boolean doPrint)
			throws Exception {
		XmlUnitUtil.xmlDiffIdentical(xmlString, validXmlString, null, doPrint);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlString
	 *            - The input xml document string.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param ignorePaths
	 *            - List of elements to ignore when performing comparison.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffIdentical(final String xmlString,
			final String validXmlString, final String[] ignorePaths,
			final boolean doPrint) throws Exception {
		final String tempXmlString = XmlTestUtil.removeWhiteSpace(xmlString);
		final String tempValidXmlString = XmlTestUtil
				.removeWhiteSpace(validXmlString);
		final DetailedDiff myDiff = new DetailedDiff(new Diff(tempXmlString,
				tempValidXmlString));
		IgnoreXPathDifferenceListener listener = null;
		if (ignorePaths == null) {
			listener = new IgnoreXPathDifferenceListener();
		} else {
			listener = new IgnoreXPathDifferenceListener(ignorePaths);
		}
		myDiff.overrideDifferenceListener(listener);
		final Boolean identical = myDiff.identical();
		if (!identical && doPrint) {
			XmlUnitUtil.printDifferences(myDiff);
		}
		Assert.assertTrue("Xml documents are not identical", identical);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlDoc
	 *            - The input xml document.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final Document xmlDoc,
			final String validXmlString) throws Exception {
		final String xmlString = DOMSerializerHelper.serializeDocument(xmlDoc);
		XmlUnitUtil.xmlDiffSimilar(xmlString, validXmlString, null, true);
	}

	/**
	 * Performs a similar comparison as defined by XmlUnit.
	 * 
	 * @param xmlDoc
	 *            - The input xml document.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final Document xmlDoc,
			final String validXmlString, final boolean doPrint)
			throws Exception {
		final String xmlString = DOMSerializerHelper.serializeDocument(xmlDoc);
		XmlUnitUtil.xmlDiffSimilar(xmlString, validXmlString, null, doPrint);
	}

	/**
	 * Performs an similar comparison as defined by XmlUnit.
	 * 
	 * @param xmlDoc
	 *            - The input xml document.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param ignorePaths
	 *            - List of elements to ignore when performing comparison.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final Document xmlDoc,
			final String validXmlString, final String[] ignorePaths)
			throws Exception {
		final String xmlString = DOMSerializerHelper.serializeDocument(xmlDoc);
		XmlUnitUtil
				.xmlDiffSimilar(xmlString, validXmlString, ignorePaths, true);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlDoc
	 *            - The input xml document.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param ignorePaths
	 *            - List of elements to ignore when performing comparison.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final Document xmlDoc,
			final String validXmlString, final String[] ignorePaths,
			final boolean doPrint) throws Exception {
		final String xmlString = DOMSerializerHelper.serializeDocument(xmlDoc);
		XmlUnitUtil.xmlDiffSimilar(xmlString, validXmlString, ignorePaths,
				doPrint);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlString
	 *            - The input xml document string.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final String xmlString,
			final String validXmlString) throws Exception {
		XmlUnitUtil.xmlDiffSimilar(xmlString, validXmlString, null, true);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlString
	 *            - The input xml document string.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final String xmlString,
			final String validXmlString, final boolean doPrint)
			throws Exception {
		XmlUnitUtil.xmlDiffSimilar(xmlString, validXmlString, null, doPrint);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlString
	 *            - The input xml document string.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param ignorePaths
	 *            - List of elements to ignore when performing comparison.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final String xmlString,
			final String validXmlString, final String[] ignorePaths)
			throws Exception {
		XmlUnitUtil
				.xmlDiffSimilar(xmlString, validXmlString, ignorePaths, true);
	}

	/**
	 * Performs an identical comparison as defined by XmlUnit.
	 * 
	 * @param xmlString
	 *            - The input xml document string.
	 * @param validXmlString
	 *            - The xml document string to compare against.
	 * @param ignorePaths
	 *            - List of elements to ignore when performing comparison.
	 * @param doPrint
	 *            - Prints all the differences if the test fails. This includes
	 *            the differences that the difference listener ignores.
	 * @throws Exception
	 */
	public static void xmlDiffSimilar(final String xmlString,
			final String validXmlString, final String[] ignorePaths,
			final boolean doPrint) throws Exception {
		Assert.assertNotNull("Input XML is null", xmlString);
		Assert.assertTrue("Input XML length is 0", xmlString.length() > 0);
		Assert.assertNotNull("Validation XML is null", validXmlString);
		Assert.assertTrue("Validation XML length is 0",
				validXmlString.length() > 0);

		final String tempXmlString = XmlTestUtil.removeWhiteSpace(xmlString);
		final String tempValidXmlString = XmlTestUtil
				.removeWhiteSpace(validXmlString);
		final DetailedDiff myDiff = new DetailedDiff(new Diff(tempXmlString,
				tempValidXmlString));
		IgnoreXPathDifferenceListener listener = null;
		if (ignorePaths == null) {
			listener = new IgnoreXPathDifferenceListener();
		} else {
			listener = new IgnoreXPathDifferenceListener(ignorePaths);
		}
		myDiff.overrideDifferenceListener(listener);
		final Boolean similar = myDiff.similar();
		if (!similar && doPrint) {
			XmlUnitUtil.printDifferences(myDiff);
		}
		Assert.assertTrue("Xml documents are not similar", similar);
	}
}
