package org.osehra.integration.test.util.loggingaspect;

import org.osehra.integration.util.loggingaspect.ExceptionTypeEnum;
import org.osehra.integration.util.loggingaspect.LogException;


public class SampleClass {
		  
		  public void doSomeVoidMethod() {
			  ; //do-nothing
		  }
		  
		  public String doSomeReturnValueMethod() {
			  return "Hello World";
		  }	
		  
		  public String doSomeMethodWithException() {
			  throw new RuntimeException("sampleClass test");
		  }
		  
		  public String doSomeMethodWithExceptionWrappedError() throws LogException {
			  Throwable t = new RuntimeException("sampleClass test");
			  LogException logException = new LogException(t);
			  logException.setExceptionType(ExceptionTypeEnum.Error);
			  throw logException;			  
		  }	
		  
		  public String doSomeMethodWithExceptionWrappedFatal() throws LogException {
			  Throwable t = new RuntimeException("sampleClass test");
			  LogException logException = new LogException(t);
			  logException.setExceptionType(ExceptionTypeEnum.Fatal);
			  throw logException;			  
		  }	
		  
		  public String doSomeMethodWithExceptionWrappedWarning() throws LogException {
			  Throwable t = new RuntimeException("sampleClass test");
			  LogException logException = new LogException(t);
			  logException.setExceptionType(ExceptionTypeEnum.Warning);
			  throw logException;			  
		  }
		  
		  public String doSomeMethodWithExceptionWrappedInfo() throws LogException {
			  Throwable t = new RuntimeException("sampleClass test");
			  LogException logException = new LogException(t);
			  logException.setExceptionType(ExceptionTypeEnum.Warning);
			  throw logException;			  
		  }				  
}
