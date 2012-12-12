package org.osehra.das;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Thread-safe implementation of a formatter for a <code>java.util.Date</code>
 * to/from a string of <code>yyyyMMddkkmmss</code>.  Example, the date 
 * December 10, 2012 2:35:00pm would be rendered as 
 * <code>20121210143500</code>.
 * 
 * Both methods delegate their work to a new instance
 * of a <code>java.text.SimpleDateFormat</code>.
 * 
 * @author Dept of VA
 *
 */
public class SimpleDateFormatTS implements FormatTS {
	protected Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Formats a date.  If the date is null, returns a null string.
	 */
	@Override
	public String formatObject(Object item) {
		if (item!=null) {
			return getDateFormatterInstance().format(item);
		}
		return null;
	}

	/**
	 * Parses a date.  If the date is null, returns a null string.
	 */
	@Override
	public Object parse(String data) {
		if (data!=null && data.length()>0) {
			try {
				return getDateFormatterInstance().parseObject(data);
			} catch (ParseException e) {
				logger.error("error parsing date string:" + data, e);
			}
		}
		return null;
	}

	protected static Format getDateFormatterInstance() {
		return new SimpleDateFormat("yyyyMMddkkmmss");
	}
}
