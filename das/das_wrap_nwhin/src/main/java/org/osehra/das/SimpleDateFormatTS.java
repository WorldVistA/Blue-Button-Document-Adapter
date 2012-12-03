package org.osehra.das;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleDateFormatTS implements FormatTS {
	protected Log logger = LogFactory.getLog(this.getClass());
	
	@Override
	public String formatObject(Object item) {
		if (item!=null) {
			return getDateFormatterInstance().format(item);
		}
		return null;
	}

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
