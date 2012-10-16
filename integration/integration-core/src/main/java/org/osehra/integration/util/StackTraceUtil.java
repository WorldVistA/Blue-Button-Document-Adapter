package org.osehra.integration.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {

	/**
	 * Gets the exception stack trace as a string.
	 */
	public static String getStackTraceUtilAsString(final Exception exception) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		pw.print(" [ ");
		pw.print(exception.getClass().getName());
		pw.print(" ] ");
		pw.print(exception.getMessage());
		exception.printStackTrace(pw);
		return sw.toString();
	}

}
