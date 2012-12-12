package org.osehra.das.repo.bluebutton;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.FormatTS;
import org.osehra.das.SimpleDateFormatTS;

/**
 * Thread-safe formatter to change an 
 * <code>org.osehra.das.repo.bluebutton.AsyncRetrieveMessage</code> to/from
 * a string representation.
 * 
 * @author Dept of VA
 *
 */
public class AsyncRetrieveMessageFormat implements FormatTS {
	protected FormatTS dateFormat = new SimpleDateFormatTS();
	protected Log logger = LogFactory.getLog(this.getClass());
	protected String separator = ":";

	/**
	 * @return The string used to separate the properties.  Defaults to a
	 * colon character (:).
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator The string used to separate the properties.
	 * Defaults to a colon character if this property is not set.
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return The thread-safe formatter for a <code>java.util.Date</code>
	 */
	public FormatTS getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat The thread-safe formatter for a <code>java.util.Date</code>
	 */
	public void setDateFormat(FormatTS dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Returns a string representation of an AsyncRetrieveMessage.
	 * All values are separated within the string by a delimiter 
	 * specified by the <code>separator</code> property.<p>
	 * If the message is null, returns a null string.
	 */
	@Override
	public String formatObject(Object item) {
		if (item!=null) {
			AsyncRetrieveMessage msg = (AsyncRetrieveMessage)item;
			StringBuffer buffer = new StringBuffer();
			buffer.append(getStringOrBlank(dateFormat.formatObject(msg.getDate())));
			buffer.append(getSeparator());
			buffer.append(getStringOrBlank(msg.getPatientId()));
			buffer.append(getSeparator());
			buffer.append(getStringOrBlank(msg.getPatientName()));
			return buffer.toString();
		}
		return null;
	}

	/**
	 * Parses a string and returns a new AsyncRetrieveMessage object.
	 */
	@Override
	public Object parse(String data) {
		if (data!=null && data.length()>0) {
			String[] items = data.split(getSeparator());
			return new AsyncRetrieveMessage((Date)dateFormat.parse(items[0]), items[1], items[2]);
		}
		// TODO Update for handling parsing errors
		return null;
	}

	protected static String getStringOrBlank(String aString) {
		if (aString==null) {
			return "";
		}
		return aString;
	}
}
