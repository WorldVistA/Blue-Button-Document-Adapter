package org.osehra.integration.test.runner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osehra.integration.test.asserter.Asserter;
import org.osehra.integration.test.modifier.Modifier;
import org.osehra.integration.test.printer.Printer;
import org.osehra.integration.test.util.resource.ResourceUtil;
import org.osehra.integration.util.DOMHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;

public abstract class AbstractTestRunner {

	protected String inputFile;
	private Asserter<Object>[] asserters;
	protected final Map<String, Object> parameters = new HashMap<String, Object>();
	protected Printer<Object> inputPrinter;
	protected Printer<Object> outputPrinter;
	protected Modifier<Document, Document>[] modifiers;
	protected Object input;
	protected Object output;

	@Autowired
	protected ApplicationContext applicationContext;

	abstract boolean isXmlInput();

	protected String getInputFileText() {
		String inputFileContents;
		try {
			inputFileContents = (String) ResourceUtil.getTextResource(
					this.applicationContext, this.getInputFile());
		} catch (final IOException e) {
			throw new RuntimeException("Loading of text resource failed: "
					+ e);
		}
		return inputFileContents;
	}

	protected Document getInputFileXml () {
		Document inputFileContents = null;
		if (this.getInputFile() == null) {
			inputFileContents = (Document) DOMHelper.newDocument();
		} else {
			try {
				inputFileContents = (Document) ResourceUtil.getXmlResource(
						this.applicationContext, this.getInputFile());
			} catch (final IOException e) {
				throw new RuntimeException("Loading of XML resource failed: "
						+ e);
			}
		}
		return inputFileContents;
	}

	// To be defined in concrete subclasses
	abstract Object transduce() throws Exception;

	/**
	 * Execute the runner which calls the XSL transformation processor, passing
	 * in the specified parameters and input XML document. If a printer is set,
	 * the result is printed so that the result can be visually observed. If an
	 * asserter is supplied, the assertions will be performed.
	 *
	 * @return Returns the result document from the transformation. Usually you
	 *         wont need this, but if there is ever a need, the junit has
	 *         access.
	 * @throws Exception
	 */
	public Object execute() throws Exception {

		if (this.isXmlInput()) {
			input = getInputFileXml();

			if (this.getModifiers() != null) {
				for (final Modifier<Document, Document> modifier : this
						.getModifiers()) {
					input = modifier.modify((Document) input);
				}
			}
		}
		else {
			input = getInputFileText();
		}

		if (this.inputPrinter != null) {
			this.inputPrinter.print(input);
		}

		output = this.transduce();

		if (this.outputPrinter != null) {
			this.outputPrinter.print(output);
		}

		if (this.getAsserters() != null) {
			for (final Asserter<Object> asserter : this.getAsserters()) {
				asserter.assertTrue(output);
			}
		}

		return output;
	}

	/**
	 * Gets the input file name
	 *
	 * @return
	 */
	public String getInputFile() {
		return this.inputFile;
	}

	/**
	 * Sets the input file name.
	 *
	 * @param input
	 *            - The input filename for the transformation.
	 */
	public void setInputFile(final String inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * Gets the asserter that will validate the result.
	 *
	 * @return
	 */
	public Asserter<Object> getAsserter() {
		if (this.asserters != null) {
			return this.asserters[0];
		} else {
			return null;
		}
	}

	/**
	 * Gets the array of asserters.
	 *
	 * @return The array of asserters.
	 */
	public Asserter<Object>[] getAsserters() {
		return this.asserters;
	}

	/**
	 * Sets the asserter that will validate the result.
	 *
	 * @param asserter
	 */
	@Required
	@SuppressWarnings("unchecked")
	public void setAsserter(final Asserter<Object> asserter) {
		if (asserter != null) {
			this.asserters = new Asserter[1];
			this.asserters[0] = asserter;
		} else {
			this.asserters = null;
		}
	}

	/**
	 * Sets an array of asserters validate the result.
	 *
	 * @param asserter
	 *            - The asserter.
	 */
	public void setAsserters(final Asserter<Object>[] asserters) {
		this.asserters = asserters;
	}

	/**
	 * Sets parameters for the test.
	 *
	 * @param params
	 *            - A map where the names must match the parameter names in the
	 *            stylesheet and the values are passed as values to the
	 *            stylesheet paramters.
	 */
	public void setParameters(final Map<String, Object> params) {
		this.parameters.putAll(params);
	}

	/**
	 * Sets a printer so that the result can be printed.
	 *
	 * @param printer
	 *            - The printer strategy.
	 */
	public void setInputPrinter(final Printer<Object> printer) {
		this.inputPrinter = printer;
	}

	/**
	 * Sets a printer so that the result can be printed.
	 *
	 * @param printer
	 *            - The printer strategy.
	 */
	public void setOutputPrinter(final Printer<Object> printer) {
		this.outputPrinter = printer;
	}

	/**
	 * Gets the modifier.
	 *
	 * @return
	 */
	public Modifier<Document, Document> getModifier() {
		if (this.modifiers != null) {
			return this.modifiers[0];
		} else {
			return null;
		}
	}

	/**
	 * Gets the array of modifiers.
	 *
	 * @return The array of modifiers.
	 */
	public Modifier<Document, Document>[] getModifiers() {
		return this.modifiers;
	}

	/**
	 * Sets a modifier to be applied before the input XML document is used.
	 *
	 * @param modifier
	 */
	@SuppressWarnings("unchecked")
	public void setModifier(final Modifier<Document, Document> modifier) {
		if (modifier != null) {
			this.modifiers = new Modifier[1];
			this.modifiers[0] = modifier;
		} else {
			this.modifiers = null;
		}
	}

	/**
	 * Sets an array of modifiers to be applied before the document is used.
	 *
	 * @param modifier
	 *            - The modifier.
	 */
	public void setModifiers(final Modifier<Document, Document>[] modifier) {
		this.modifiers = modifier;
	}

}
