package org.osehra.integration.jms.service;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public class ReplyToMessagePostProcessor implements MessagePostProcessor{

	JmsTemplate replyToJmsTemplate;
	
	@Override
	public Message postProcessMessage(Message message) throws JMSException {
		message.setJMSReplyTo(replyToJmsTemplate.getDefaultDestination());
		return message;
	}
	
	@Required
	public void setReplyToJmsTemplate(JmsTemplate replyToJmsTemplate) {
		this.replyToJmsTemplate = replyToJmsTemplate;
	}
}
