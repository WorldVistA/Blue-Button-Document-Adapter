package org.osehra.das.repo.bluebutton;

import org.osehra.das.IFormatTS;


public abstract class AbstractAsyncMsgFormatAware {
	protected IFormatTS asyncMessageFormat;
	
	public IFormatTS getAsyncMessageFormat() {
		return asyncMessageFormat;
	}

	public void setAsyncMessageFormat(IFormatTS asyncMessageFormat) {
		this.asyncMessageFormat = asyncMessageFormat;
	}

}
