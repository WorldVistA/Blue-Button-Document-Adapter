package org.osehra.das.common.transformer.xsl;

import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

/**
 * Interface for transforming using XSL.
 * 
 * @author Asha Amritraj
 */
public interface XSLTransformer {
	Result transform(final Source sourceMessage) throws TransformerException;

	Result transform(final Source sourceMessage, final Result targetMessage)
			throws TransformerException;

	Result transform(final Source sourceMessage, final Result targetMessage,
			final Map<String, Object> dynamicParameters)
			throws TransformerException;

}
