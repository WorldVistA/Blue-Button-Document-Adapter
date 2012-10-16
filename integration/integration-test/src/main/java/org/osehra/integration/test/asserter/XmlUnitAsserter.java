package org.osehra.integration.test.asserter;

import org.osehra.integration.test.modifier.Modifier;
import org.osehra.integration.test.util.resource.ResourceUtil;
import org.osehra.integration.test.util.xml.DOMParserHelper;
import org.osehra.integration.test.util.xml.DOMSerializerHelper;
import org.osehra.integration.test.util.xml.XmlUnitUtil;

import java.io.IOException;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;

/**
 * An asserter that applies the XmlUnit difference test to determine whether or
 * not the document is similar to the validation document.
 *
 * @author Keith Roberts
 *
 */
public class XmlUnitAsserter implements Asserter<Document> {

	@Autowired
	private ApplicationContext applicationContext;

	private String validDocStr;
	protected Modifier<Document, Document>[] modifiers;
	private String[] ignorePaths;

	/**
	 * Applies the comparison of the specified document to the validation
	 * document. A junit assertion is applied if the documents are not similar.
	 *
	 * @param compareDoc
	 *            - The document that is compared against the validation
	 *            document.
	 */
	@Override
	public void assertTrue(final Document compareDoc) {
		try {
			if (this.getModifiers() != null) {
				Document validDoc = DOMParserHelper.parseDocument(this.validDocStr);
				for (final Modifier<Document, Document> modifier : this
						.getModifiers()) {
					validDoc = modifier.modify(validDoc);
				}
				this.validDocStr = DOMSerializerHelper.serializeDocument(validDoc);
			}
			XmlUnitUtil.xmlDiffSimilar(compareDoc, this.validDocStr, ignorePaths);
		} catch (final Exception ex) {
			Assert.fail(this.getClass().getName() + " " + ex);
		}
	}

	/**
	 * Sets the resource that will be loaded as the validation document.
	 *
	 * @param filename
	 *            - The file name of the validation document.
	 */
	@Required
	public void setResource(final String filename) {
		try {
			this.validDocStr = ResourceUtil.getTextResource(
					this.applicationContext, filename);
		} catch (final IOException e) {
			throw new RuntimeException("Loading of text resource failed: " + e);
		}
	}

	public void setIgnorePaths(String[] ignorePaths) {
		this.ignorePaths = ignorePaths;
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
