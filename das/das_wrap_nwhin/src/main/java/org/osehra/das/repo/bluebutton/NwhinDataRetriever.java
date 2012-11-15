package org.osehra.das.repo.bluebutton;

import java.text.ParseException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.wrapper.nwhin.WrapperResource;

//@MessageDriven(messageListenerInterface=MessageListener.class)
public class NwhinDataRetriever extends AbstractAsyncMsgFormatAware implements MessageListener {
	protected WrapperResource _nwhinResource;
	protected Log logger = LogFactory.getLog(this.getClass());
	
	public void setWrapperResource(WrapperResource resource) {
		_nwhinResource = resource;
	}

	@Override
	public void onMessage(Message msg) {
		TextMessage tMsg = (TextMessage)msg;
		AsyncRetrieveMessage aMsg;
		try {
			aMsg = (AsyncRetrieveMessage)getAsyncMessageFormat().parseObject(tMsg.getText());
			_nwhinResource.getDomainXml(aMsg.getPatientId(), getUserName(aMsg.getPatientId()));
		} catch (ParseException e) {
			logger.error("parse exception for " + msg, e);
		} catch (JMSException e) {
			logger.error("JMS exception for " + msg, e);
		} 
	}

	protected String getUserName(String msg) {
		// TODO Get the requirements on how this will be determined
		return "NWHIN_GUY";
	}

}
