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
			buffer.append(msg.getPatientId());
			buffer.append(':');
			buffer.append(dateFormat.format(msg.getDate()));
		}
		return buffer;
	}

	@Override
	public Object parseObject(String data, ParsePosition pos) {
		if (data!=null && data.length()>0) {
			String[] items = data.split(":");
			pos.setIndex(data.length());
			try {
				return new AsyncRetrieveMessage((Date)dateFormat.parseObject(items[1]), items[0]);
			} catch (ParseException e) {
				logger.error("parsing error with " + data, e);
			}
		}
		// TODO Update for handling parsing errors
		return null;
	}

}
