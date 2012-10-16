package org.osehra.das.common.interceptor;

import org.osehra.das.common.validation.Assert;

import java.util.logging.Level;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Prints the argument to the console. Mostly used for testing purposes.
 * 
 * @author Julian Jewel
 * 
 */
public class ConsoleInterceptor implements Interceptor<Object, Object> {

	private static final Log LOG = LogFactory.getLog(ConsoleInterceptor.class);

	/**
	 * Set the default level to INFO. JDK logging is used, since commons logging does not expose the levels.
	 * @uml.property  name="level"
	 */
	private Level level;

	@Override
	public Object intercept(final Object arg) throws InterceptorException {
		// Log based on the level - only the following is supported
		// INFO - Commons Logging Info
		// SEVERE - Commons Logging Error
		// WARNING - Commons Logging Warn
		// FINEST - COmmons Logging Debug

		Assert.assertNotEmpty(this.level, "LEVEL has to be set");

		if (Level.INFO.intValue() == this.level.intValue()) {
			if (ConsoleInterceptor.LOG.isInfoEnabled()) {
				ConsoleInterceptor.LOG.info("Intercepted " + arg);
			}
		} else if (Level.SEVERE.intValue() == this.level.intValue()) {
			if (ConsoleInterceptor.LOG.isErrorEnabled()) {
				ConsoleInterceptor.LOG.error("Intercepted " + arg);
			}
		} else if (Level.WARNING.intValue() == this.level.intValue()) {
			if (ConsoleInterceptor.LOG.isWarnEnabled()) {
				ConsoleInterceptor.LOG.warn("Intercepted " + arg);
			}
		} else if (Level.FINEST.intValue() == this.level.intValue()) {
			if (ConsoleInterceptor.LOG.isDebugEnabled()) {
				ConsoleInterceptor.LOG.debug("Intercepted " + arg);
			}
		}
		return arg;
	}

	/**
	 * Set the logging level. Based on JDK logging.
	 * @param level  the level - INFO, SEVERE, WARNING or FINEST.
	 * @uml.property  name="level"
	 */
	@Required
	public void setLevel(final Level level) {
		this.level = level;
	}
}
