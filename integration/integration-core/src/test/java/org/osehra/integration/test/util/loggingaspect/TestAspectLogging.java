package org.osehra.integration.test.util.loggingaspect;

import org.osehra.integration.util.loggingaspect.LoggingAspect;
import org.osehra.integration.util.loggingaspect.LogException;

import javax.annotation.Resource;

import junit.framework.Assert;
import mockit.Mock;
import mockit.MockClass;
import mockit.Mockit;

import org.aspectj.lang.JoinPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * 
 * @author Ben Sifuentes
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {	
		"/someContext/aop_test.xml"
})
@TestExecutionListeners(value = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
//@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
//@IfProfileValue(name = "test-groups", values = { "integration",  "aspect", "all" })
public class TestAspectLogging {

		  @MockClass(realClass = LoggingAspect.class)
		  public static final class MockLoggingAspect
		  {
			  	@Mock(minInvocations = 1)
				public void logBefore(JoinPoint joinPoint) {
			  		System.out.println("Scenario(1) called logBefore...");
			  	}

			  	@Mock(minInvocations = 1)
				public void logAfter(JoinPoint joinPoint) {
			  		System.out.println("Scenario(1) called logAfter...");
			  	}
		  }

		  
		  @MockClass(realClass = LoggingAspect.class)
		  public static final class MockLoggingAspect2
		  {
			  	@Mock(minInvocations = 1)
				public void logBefore(JoinPoint joinPoint) {
			  		System.out.println("Scenario(2) called logBefore...");
			  	}

				@Mock(minInvocations = 1)
				public void logAfterReturning(JoinPoint joinPoint, Object result) {
			  		System.out.println("Scenario(2) called logAfterReturning...");
				}
		  }	
		  
		  @MockClass(realClass = LoggingAspect.class)
		  public static final class MockLoggingAspect3
		  {
			  	@Mock(minInvocations = 1)
				public void logBefore(JoinPoint joinPoint) {
			  		System.out.println("Scenario(3) called logBefore...");
			  	}

				@Mock(minInvocations = 1)
				public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
			  		System.out.println("Scenario(3) called logAfterException...");					
				}
		  }			  
		  
		  
		  @Resource(name = "sampleClass")		  
		  private SampleClass testClass;

		
		  @Before
		  public void setUp() throws Exception {
			  setUpMockLoggingAspect();
			  
		  }

	      @After
		  public void tearDown() throws Exception {
	    	  Mockit.tearDownMocks();

		  }		  

	  
		  private void setUpMockLoggingAspect() {
			  //do-nothing
			  
//			  Mockit.setUpMocks(MockLoggingAspect.class,
//					  MockLoggingAspect2.class,
//					  MockLoggingAspect3.class);
		  }

	      	  
		  @Test
		  public void testBeforeAndAfter() {
			  Mockit.setUpMocks(MockLoggingAspect.class);
  
			  testClass.doSomeVoidMethod();			  
				    
		  }

		  
		  @Test
		  public void testBeforeAndAfterReturning() {
			  Mockit.setUpMocks(MockLoggingAspect2.class);
			 		  
			  String val = testClass.doSomeReturnValueMethod();

			  Assert.assertEquals("Hello World", val);
		    
		  }		  

		  
		  @Test
		  @ExpectedException(RuntimeException.class)
		  public void testBeforeAndAfterThrowing() {
			  Mockit.setUpMocks(MockLoggingAspect3.class);			  
			  
			  testClass.doSomeMethodWithException();
		    
		  }		

		  @Test
		  @ExpectedException(LogException.class)
		  public void testBeforeAndAfterThrowingWrappedError() throws LogException {
			  Mockit.setUpMocks(MockLoggingAspect3.class);			  
			  
			  testClass.doSomeMethodWithExceptionWrappedError();
		    
		  }	
		  
		  @Test
		  @ExpectedException(LogException.class)
		  public void testBeforeAndAfterThrowingWrappedFatal() throws LogException {
			  Mockit.setUpMocks(MockLoggingAspect3.class);			  
			  
			  testClass.doSomeMethodWithExceptionWrappedFatal();
		    
		  }	
		  
		  @Test
		  @ExpectedException(LogException.class)
		  public void testBeforeAndAfterThrowingWrappedWarning() throws LogException {
			  Mockit.setUpMocks(MockLoggingAspect3.class);			  
			  
			  testClass.doSomeMethodWithExceptionWrappedWarning();
		    
		  }	
		  
		  @Test
		  @ExpectedException(LogException.class)
		  public void testBeforeAndAfterThrowingWrappedInfo() throws LogException {
			  Mockit.setUpMocks(MockLoggingAspect3.class);			  
			  
			  testClass.doSomeMethodWithExceptionWrappedInfo();
		    
		  }			  
}
