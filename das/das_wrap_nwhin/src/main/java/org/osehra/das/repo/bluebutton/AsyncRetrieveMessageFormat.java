package org.osehra.das.repo.bluebutton;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.IFormatTS;
import org.osehra.das.SimpleDateFormatTS;

public class AsyncRetrieveMessageFormat implements IFormatTS {
	protected IFormatTS dateFormat = new SimpleDateFormatTS();
	protected Log logger = LogFactory.getLog(this.getClass());
	protected String separator = ":";

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public IFormatTS getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(IFormatTS dateFormat) {
		this.dateFormat = dateFormat;
	}

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
