package org.osehra.integration.util;

import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.core.transformer.bytes.Base64ToBytes;
import org.osehra.integration.core.transformer.string.Base64ToString;
import org.osehra.integration.core.transformer.string.StringToBase64;

import org.osehra.integration.core.transformer.xml.StringToXML;
import org.osehra.integration.core.transformer.xml.XMLToString;
import org.w3c.dom.Document;

public class Base64Util {

	private static Base64ToString base64ToString = null;
	private static Base64ToBytes base64ToBytes = null;
	private static StringToBase64 stringToBase64 = null;

	protected static Base64ToString getBase64ToString() {
		if (base64ToString == null) {
			base64ToString = new Base64ToString();
		}
		return base64ToString;
	}

	protected static Base64ToBytes getBase64ToBytes() {
		if (base64ToBytes == null) {
			base64ToBytes = new Base64ToBytes();
		}
		return base64ToBytes;
	}

	protected static StringToBase64 getStringToBase64() {
		if (stringToBase64 == null) {
			stringToBase64 = new StringToBase64();
		}
		return stringToBase64;
	}

	public static String base64ToString(String b64String) {
		try {
			return getBase64ToString().transform(b64String);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] base64ToBytes(String b64String) {
		try {
			return getBase64ToBytes().transform(b64String);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	public static String stringToBase64(String textString) {
		try {
			return getStringToBase64().transform(textString);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
	public static String encodeStringFromNode(Document doc)
			throws TransformerException {
		if (NullChecker.isEmpty(doc)) {
			return null;
		}
		String text = new XMLToString().transform(doc);
		if (NullChecker.isEmpty(text)) {
			return null;
		}
		String encodedText = org.apache.xerces.impl.dv.util.Base64.encode(text
				.getBytes());
		return encodedText;
	}
	public static Document decodeStringToDocument(String base64)
			throws TransformerException {
		if (NullChecker.isEmpty(base64)) {
			return null;
		}
		byte[] bytes = org.apache.xerces.impl.dv.util.Base64.decode(base64
				.trim());
		if (NullChecker.isEmpty(bytes)) {
			return null;
		}
		String base64Text = new String(bytes);
		Document doc = new StringToXML().transform(base64Text);
		return doc;
	}

}
