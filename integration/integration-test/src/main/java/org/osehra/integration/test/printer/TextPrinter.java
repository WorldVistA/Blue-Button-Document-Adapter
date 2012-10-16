package org.osehra.integration.test.printer;

import java.io.PrintStream;

public class TextPrinter implements Printer<String> {

	private PrintStream ps = System.out;

	@Override
	public void print(final String value) {
		this.ps.println("\n\n" + value + "\n\n");
	}

	/**
	 * Sets the print stream if something other than stdout is desired.
	 * 
	 * @param ps
	 *            - The print stream in which to write.
	 */
	public void setPrintStream(final PrintStream ps) {
		this.ps = ps;
	}

}
