package org.osehra.das.common.transformer.date;

import org.osehra.das.common.date.DateUtil;
import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

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
