package org.osehra.integration.bpm;

import org.osehra.integration.bpm.engine.ProcessEngine;
import org.osehra.integration.bpm.engine.ProcessException;
import org.osehra.integration.core.service.ServiceInvocationException;
import org.osehra.integration.core.service.ServiceInvoker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * A process invoker, which starts the process engine.
 * 
 * @author Julian Jewel
 */
public class ProcessInvoker implements ServiceInvoker<Object, Object> {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(ProcessInvoker.class);

	/**
	 * The process rule engine. Usually a process like dodNotifyADCProcess.
	 * 
	 * @uml.property name="ruleEngine"
	 * @uml.associationEnd
	 */
	private ProcessEngine<Object> ruleEngine;

	/**
	 * Invoke the process. The rule engine is executed.
	 * 
	 * @param object
	 *            - the input to the rule engine
	 * @return the return value, usually the reply from the process
	 * @throws ServiceInvocationException
	 *             an exception when executing the process
	 */
	@Override
	public Object invoke(final Object object)
			throws ServiceInvocationException {

		try {
			if (ProcessInvoker.LOG.isInfoEnabled()) {
				ProcessInvoker.LOG.info("Executing Process "
						+ this.ruleEngine.getName());
			}
			// Invoke the process with the input
			return this.ruleEngine.processRequest(object);
		} catch (final ProcessException ex) {
			throw new ServiceInvocationException(ex);
		}
	}

	/**
	 * Set the rule engine. Usually a process name.
	 * 
	 * @param theRuleEngine
	 *            the rule engine
	 */
	@Required
	public void setRuleEngine(final ProcessEngine<Object> theRuleEngine) {
		this.ruleEngine = theRuleEngine;
	}
}
