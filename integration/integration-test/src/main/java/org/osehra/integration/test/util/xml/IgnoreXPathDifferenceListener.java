package org.osehra.integration.test.util.xml;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.NodeDetail;
import org.w3c.dom.Node;

/**
 * Specifies the elements in an XML document that should be ignored when
 * applying an XmlUnit similar comparison.
 * 
 * @author Keith Roberts
 * 
 */
public class IgnoreXPathDifferenceListener implements DifferenceListener {

	private static final int TEXT_VALUE_ID = DifferenceConstants.TEXT_VALUE_ID;
	private String[] ignorePaths = { "MSH.7[1]/TS.1[1]", "MSH[1]/MSH.10[1]",
			"ZAD.3[1]/text()[1]", "QPD[1]/QPD.2[1]", "BHS[1]/BHS.11[1]",
			"BHS[1]/BHS.7[1]/TS.1[1]", "QAK[1]/QAK.1[1]",
			"RCP[1]/RCP.2[1]/CQ.1[1]", "VTQ[1]/VTQ.1[1]", "EVN[1]/EVN.2[1]",
			"effective-date", "dob",
			"/ZCH_Z01[1]/ZCH_Z01.PATIENT[1]/PID[1]/PID.7[1]/TS.1[1]",
			"message-time-stamp", "message-id" };

	/**
	 * The default constructor
	 */
	public IgnoreXPathDifferenceListener() {
	}

	/**
	 * Constructor
	 * 
	 * @param ignorePaths
	 *            - A list of XPath expression of nodes in document to ignore.
	 */
	public IgnoreXPathDifferenceListener(final String[] ignorePaths) {
		final String[] array = new String[ignorePaths.length];
		for (int i = 0; i < ignorePaths.length; i++) {
			array[i] = new String(ignorePaths[i]);
		}
		this.ignorePaths = array;
	}

	/**
	 * Applies the logic for using difference listener.
	 */
	@Override
	public int differenceFound(final Difference difference) {
		if (this.isIgnoredDifference(difference)) {
			return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
		} else {
			return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
		}
	}

	private boolean ignorePath(final String path) {
		for (final String element : this.ignorePaths) {
			if (path.indexOf(element) >= 0) {
				return true;
			}
		}
		return false;
	}

	private boolean isIgnoredDifference(final Difference difference) {
		final int differenceId = difference.getId();
		if (IgnoreXPathDifferenceListener.TEXT_VALUE_ID == differenceId) {

			final NodeDetail controlDetail = difference.getControlNodeDetail();
			final String controlXPath = controlDetail.getXpathLocation();
			final NodeDetail testDetail = difference.getTestNodeDetail();
			final String testXPath = testDetail.getXpathLocation();
			if (controlXPath.equals(testXPath)) {
				return this.ignorePath(controlXPath);
			}
		}
		return false;
	}

	@Override
	public void skippedComparison(final Node arg0, final Node arg1) {

	}

}
