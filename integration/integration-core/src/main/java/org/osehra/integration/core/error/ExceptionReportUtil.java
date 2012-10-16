package org.osehra.integration.core.error;

public class ExceptionReportUtil {

	public static Throwable getReportException( Exception ex, Class<Exception> clzz)
	{
		if (clzz == null)
		{
			return ex;
		}
		Throwable curEx = ex;
		while( curEx != null)
		{
			if (clzz.isInstance(curEx))
			{
				return curEx;
			}
			curEx = curEx.getCause();
		}
		return ex;
	}
	

}
