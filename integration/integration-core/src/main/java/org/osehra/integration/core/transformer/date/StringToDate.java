package org.osehra.integration.core.transformer.date;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.DateUtil;
import org.osehra.integration.util.NullChecker;

import java.util.Date;

/**
 * Convert from a string to date. Uses the SimpleDateFormat.
 * 
 * @author Asha Amritraj
 */
public class StringToDate implements Transformer<String, Date> {

	@Override
	public Date transform(final String src) throws TransformerException {
		if (NullChecker.isEmpty(src)) {
			return null;
		}
		return DateUtil.dateFromString(src);
	}
}
