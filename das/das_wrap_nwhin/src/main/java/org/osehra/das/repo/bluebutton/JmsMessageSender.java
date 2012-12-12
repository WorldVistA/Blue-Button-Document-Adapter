package org.osehra.das.repo.bluebutton;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.FormatTS;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * Implements a <code>MessageSendable</code>; sends a retrieval message using JMS messaging.  
 * Depends on the Spring Framework helper class 
 * <code>org.springframework.jms.core.JmsTemplate</code> to send the messages.
 *   
 * @author Dept of VA
 *
 */
public class JmsMessageSender implements MessageSendable {
	protected JmsTemplate jmsTemplate;
	protected FormatTS asyncMessageFormat;
	protected static final Log logger = LogFactory.getLog(JmsMessageSender.class);
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	/**
	 * 
	 * @param jmsTemplate Spring Frameworks JmsTemplate
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * 
	 * @return Spring Frameworks JmsTemplate
	 */
	public FormatTS getAsyncMessageFormat() {
		return asyncMessageFormat;
	}

	/**
	 * 
	 * @param asyncMessageFormat Thread-safe formatter for the AsyncMessageFormat
	 */
	public void setAsyncMessageFormat(FormatTS asyncMessageFormat) {
		this.asyncMessageFormat = asyncMessageFormat;
	}
	
	@Override
	public void sendRetrieveMessage(final String patientId, final String patientName, final Date documentDate) {
		boolean debugging = logger.isDebugEnabled();
		if (debugging) {
			logger.debug("sending JMS message id:" + patientId + " name:" + patientName + " docDate:" + documentDate);
		}
		this.jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(getAsyncMessageFormat().formatObject(new AsyncRetrieveMessage(documentDate, patientId, patientName)));
			}
		});
		if (debugging) {
			logger.debug("sent JMS message id:" + patientId + " name:" + patientName + " docDate:" + documentDate);
		}
	}

}
