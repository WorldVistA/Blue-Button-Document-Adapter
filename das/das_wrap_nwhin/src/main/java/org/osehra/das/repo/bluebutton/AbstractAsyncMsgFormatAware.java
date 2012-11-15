package org.osehra.das.repo.bluebutton;

import java.text.Format;

public abstract class AbstractAsyncMsgFormatAware {
	protected Format asyncMessageFormat;
	
	public Format getAsyncMessageFormat() {
		return asyncMessageFormat;
	}

	public void setAsyncMessageFormat(Format asyncMessageFormat) {
		this.asyncMessageFormat = asyncMessageFormat;
	}

}
