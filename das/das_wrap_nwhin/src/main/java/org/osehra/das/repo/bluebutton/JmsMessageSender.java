package org.osehra.das.repo.bluebutton;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.osehra.das.IFormatTS;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsMessageSender implements IMessageSendable {
	protected JmsTemplate jmsTemplate;
	protected IFormatTS asyncMessageFormat;
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public IFormatTS getAsyncMessageFormat() {
		return asyncMessageFormat;
	}

	public void setAsyncMessageFormat(IFormatTS asyncMessageFormat) {
		this.asyncMessageFormat = asyncMessageFormat;
	}
	
	@Override
	public void sendRetrieveMessage(final String patientId, final String patientName, final Date documentDate) {
		this.jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(getAsyncMessageFormat().formatObject(new AsyncRetrieveMessage(documentDate, patientId, patientName)));
			}
		});
	}

}
