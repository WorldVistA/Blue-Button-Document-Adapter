package org.osehra.integration.test.printer;

import java.io.PrintStream;


import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;

/**
 * Prints an xml document to a print stream.
 *
 * @author Keith Roberts
 *
 */
public class AtomFeedPrinter implements Printer<Feed> {

	private PrintStream ps = System.out;

	/**
	 * Prints the serialized feed value to the print stream.
	 *
	 * @param value
	 *            - The atom feed to print.
	 */
	@Override
	public void print(final Feed value) {
		try {
			if (value == null) {
				this.ps.println("Input value is null");
				return;
			}

			final String output =
				(String) new Abdera().getWriterFactory().getWriter("prettyxml").write(value);
			this.ps.println("\n\n" + output + "\n\n");

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
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
