package org.osehra.integration.core.transformer.xml;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.w3c.dom.Document;

/**
 * @author David Ellis
 */
public class JSONToXML implements Transformer<String, Document> {

	private XMLSerializer serializer;

	public JSONToXML() {
	}

	@Override
	public Document transform(final String JSONString)
			throws TransformerException {

		if (NullChecker.isEmpty(JSONString)) {
			return null;
		}

		serializer = new XMLSerializer();
		JSON json = JSONSerializer.toJSON(JSONString);
		serializer.setRootName("JSON");
		serializer.setArrayName("JSON-array");
		serializer.setElementName("JSON-element");
		serializer.setTypeHintsEnabled(false);
		String xmlString = serializer.write(json);

		// StringToXML transformation may throw TransformerException
		Document xml = new StringToXML().transform(xmlString);

		return xml;
	}

}
