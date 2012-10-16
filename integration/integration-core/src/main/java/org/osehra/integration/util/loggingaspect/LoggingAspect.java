package org.osehra.integration.util.loggingaspect;

import org.osehra.integration.core.component.ComponentImpl;
import org.osehra.integration.core.context.ThreadContext;
import org.osehra.integration.util.NullChecker;

import java.text.SimpleDateFormat;
//import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 
 * 
 * @author Ben Sifuentes
 *
 */
public class LoggingAspect {
	
	private ThreadContext threadContext;
	private Boolean isTruncated = Boolean.TRUE;	
	private int truncateLength = 2000;
	private String dateFormat ="yyyy-MM-dd'T'HH:mm:ss,SSSZ";
	
	private static final String ASPECT_METHOD_NAME = "logAfterThrowing";	
	
	
	public void logBefore(JoinPoint joinPoint) {
		Logger logger = getLogger(joinPoint.getTarget().getClass());
		if (logger.isTraceEnabled()) {			
			String message = this.generateMessageParams("logBefore", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), joinPoint.getArgs(), joinPoint.getTarget());
			logger.trace(message);
		}
	}

	public void logAfter(JoinPoint joinPoint) {
		Logger logger = getLogger(joinPoint.getTarget().getClass());
		if (logger.isTraceEnabled()) {	
			String message = this.generateMessage("logAfter", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), joinPoint.getTarget()) + "\n";
			logger.trace(message);
		}
	}
	
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		Logger logger = getLogger(joinPoint.getTarget().getClass());
		if (logger.isTraceEnabled()) {			
			String message = this.generateMessageReturn("logAfterReturning", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), result, joinPoint.getTarget());
			logger.trace(message);
		}
	}
	
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		Logger logger = getLogger(joinPoint.getTarget().getClass());
		String message = this.generateMessageException("logAfterThrowing", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), error, joinPoint.getTarget());
		logger.error(message);
	}
/*	
	public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger logger = getLogger(joinPoint.getTarget().getClass());
		if (logger.isTraceEnabled()) {	
//			String message = this.getRequestId()+": logAround() is running!|" + "Advised : " + joinPoint.getSignature().getName() + "Advised arguments : " + Arrays.toString(joinPoint.getArgs());
			String message = this.generateMessageParams("logAround", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), joinPoint.getTarget());

			logger.trace(message);			

			logger.trace("Around before is running!");
		}
		
		joinPoint.proceed();		

		if (logger.isTraceEnabled()) {	
			logger.trace("Around after is running!");
		}
	}
*/		
	private String generateMessage(String aspectMethodName, Class<?> clazz, String methodName, Object target) {		
		StringBuilder retval = new StringBuilder();		
		retval.append("\n");
		if(aspectMethodName.compareTo(ASPECT_METHOD_NAME) != 0) {
			retval.append("Aspect method: ").append(aspectMethodName).append("\n");
		} else {			
			retval.append("");
		}		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(this.dateFormat);
		retval.append("Date/TimeStamp: ").append(sdf.format(date)).append("\n") 
		      .append("Request ID: ").append(this.getRequestId()).append("\n") 
		      .append("Thread ID: ").append(Thread.currentThread().getId()).append("\n")
		      .append("Class/method: ").append(clazz.getName()).append("::").append(methodName);
		if(clazz.getName().contains("ComponentImpl")) {
			retval.append("\n").append("Bean Name: ").append(((ComponentImpl)(target)).getBeanName());
		}
		return retval.toString();
	}

	private String generateMessageParams(String aspectMethodName, Class<?> clazz, String methodName, Object[] args, Object target) {		
		StringBuilder retval = new StringBuilder();			
		StringBuilder params = new StringBuilder();
		if (this.isTruncated.booleanValue()) {		
			if (0 == args.length) {
				params.append("Truncated Method parameter(s): ").append("None"); 
			} else {				
				params.append("Truncated Method parameter(s): ").append(this.generateTruncatedParams(args)); 
			}
		} else {
			if (0 == args.length) {
				params.append("Method parameter(s): ").append("None");				
			} else {
				params.append("Method parameter(s): ").append(this.generateFullParams(args));
			}
		}		
		retval.append(this.generateMessage(aspectMethodName, clazz, methodName, target)).append("\n") 
			  .append(params).append("\n");
		return retval.toString();
	}

	private String generateMessageReturn(String aspectMethodName, Class<?> clazz, String methodName, Object result, Object target) {		
		StringBuilder retval = new StringBuilder();				
		StringBuilder params = new StringBuilder();
		if (this.isTruncated.booleanValue()) {
			params.append("Truncated Return: ").append(this.generateTruncatedReturn(result));
		} else {
			params.append("Return: ").append(this.generateFullReturn(result));	
		}		
		retval.append(this.generateMessage(aspectMethodName, clazz, methodName, target)).append("\n") 
			  .append(params).append("\n");		
		return retval.toString();
	}		
	
	private String generateMessageException(String aspectMethodName, Class<?> clazz, String methodName, Throwable error, Object target) {		
		StringBuilder retval = new StringBuilder();
		String exceptionType = "Unknown";
		if (error instanceof LogException) {
			ExceptionTypeEnum exceptionTypeEnum = ((LogException) error).getExceptionType();
			switch(exceptionTypeEnum)
			{
				case Informational:
					exceptionType = "Informational";
					break;
					
				case Warning:
					exceptionType = "Warning";
					break;
				
				case Error:
					exceptionType = "Error";
					break;
					
				case Fatal:
					exceptionType = "Fatal";
					break;
					
				default:
					break;
			}			
		}		
		retval.append(this.generateMessage(aspectMethodName, clazz, methodName, target)).append("\n")
				.append("ExceptionType: ").append(exceptionType).append("\n")
				.append("Exception: ").append(error).append("\n");
		return retval.toString();
	}
	
	private String generateTruncatedParams(Object[] args) {
		StringBuilder resultParams = new StringBuilder();		
		if(0 == args.length) {
			resultParams.append("None");
		} else {
			resultParams.append("\n");
		    for (int i = 0; i < args.length; i++) {
		    	String str = String.format("%s", args[i]);
		    	resultParams.append("\t").append("Parameter ").append((i+1)).append(".:").append("\n");
		    	if (str.length() > (this.truncateLength - 1)) {
		    		resultParams.append("\t").append("[").append(str.substring(0, (this.truncateLength - 1))).append("]");
		    	} else {
		    		resultParams.append("\t").append("[").append(str).append("]");
		    	}	    	
		    	if ((args.length - 1) != i) {
		    		resultParams.append(",\n");
		    	}	    	
		    }
		}
	    return resultParams.toString();
	}
	
	private String generateFullParams(Object[] args) {
		StringBuilder resultParams = new StringBuilder();
		if(0 == args.length) {
			resultParams.append("None");
		} else {
			resultParams.append("\n");
			for (int i=0; i < args.length; i++) {
		    	String str = String.format("%s", args[i]);
		    	resultParams.append("\t").append("Parameter ").append((i+1)).append(".:").append("\n");
		    	resultParams.append("\t").append("[").append(str).append("]");
		    	if ((args.length - 1) != i) {
		    		resultParams.append(",\n");
		    	}	  
			}	
		}
		return resultParams.toString();
	}
	
	private String generateTruncatedReturn(Object result) {		
		StringBuilder finalResult = new StringBuilder();
		if(null != result) {			
		    if (result.toString().length() > (this.truncateLength - 1)) {		    	
		    	finalResult.append("[").append(result.toString().substring(0, (this.truncateLength - 1))).append("]");		    	
	    	} else {
	    		finalResult.append("[").append(result.toString()).append("]");
	    	}
		} else {			
			finalResult.append("null");
		}
		return finalResult.toString();
	}
	
	private String generateFullReturn(Object result) {		
		StringBuilder finalResult = new StringBuilder();
		if(null != result) {
			finalResult.append("[").append(result.toString()).append("]");
		} else {
			finalResult.append("null");
		}
		return finalResult.toString();
	}
	
	private static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}	

	private String getRequestId() {
		String requestId = "noRequestId";		
		if(NullChecker.isNotEmpty(this.threadContext)) {
			requestId = this.threadContext.getRequestId();
		}
		return requestId;
	}	
	
	public void setThreadContext(ThreadContext threadContext) {
		this.threadContext = threadContext;
	}	

	public void setIsTruncated(Boolean isTruncated) {
		this.isTruncated = isTruncated;
	}

	public void setTruncateLength(int truncateLength) {
		this.truncateLength = truncateLength;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}	
}