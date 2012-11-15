package org.osehra.das.repo.bluebutton;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsyncRetrieveMessageFormat extends Format {
	private static final long serialVersionUID = 1L;
	protected Format dateFormat = new SimpleDateFormat();
	protected Log logger = LogFactory.getLog(this.getClass());
	protected static String SEPARATOR = ":";

	public Format getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(Format dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public StringBuffer format(Object item, StringBuffer buffer,
			FieldPosition fieldPos) {
		if (item!=null) {
			AsyncRetrieveMessage msg = (AsyncRetrieveMessage)item;
			buffer.append(getStringOrBlank(dateFormat.format(msg.getDate())));
			buffer.append(SEPARATOR);
			buffer.append(getStringOrBlank(msg.getPatientId()));
			buffer.append(SEPARATOR);
			buffer.append(getStringOrBlank(msg.getPatientName()));
		}
		return buffer;
	}

	@Override
	public Object parseObject(String data, ParsePosition pos) {
		if (data!=null && data.length()>0) {
			String[] items = data.split(SEPARATOR);
			pos.setIndex(data.length());
			try {
				return new AsyncRetrieveMessage((Date)dateFormat.parseObject(items[0]), items[1], items[2]);
			} catch (ParseException e) {
				logger.error("parsing error with " + data, e);
			}
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
