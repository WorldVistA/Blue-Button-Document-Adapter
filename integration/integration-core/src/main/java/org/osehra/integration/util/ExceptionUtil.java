package org.osehra.integration.util;

public class ExceptionUtil {
	public static String getErrorText(Exception ex) {
		String errResult="";
		if (NullChecker.isEmpty(ex)) {			
            return errResult;
		}
		
		if (ex.getMessage().compareTo(ex.getLocalizedMessage()) == 0) {
			errResult = ex.getMessage();
		}
		else {
			errResult = ex.getLocalizedMessage() + ":" + ex.getMessage();
		}
		return errResult;
	}

}
