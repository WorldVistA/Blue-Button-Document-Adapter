package org.osehra.integration.util.loggingaspect;

public class LogException extends Throwable {
	
	private static final long serialVersionUID = 1L;
	
	private ExceptionTypeEnum exceptionType;
	
	public LogException() {
		super();
	}
	
	public LogException(String message) {
		super(message);
		
	}
	
	public LogException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public LogException(Throwable cause) {
		super(cause);
	}
	
	public ExceptionTypeEnum getExceptionType() {
		return exceptionType;
	}


	public void setExceptionType(ExceptionTypeEnum exceptionType) {
		this.exceptionType = exceptionType;
	}
	
}
