package org.osehra.das.repo.bluebutton;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osehra.das.wrapper.nwhin.WrapperResource;

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
			aMsg = (AsyncRetrieveMessage)getAsyncMessageFormat().parse(tMsg.getText());
			if (logger.isDebugEnabled()) {
				logger.debug("calling getDomainXml for " + aMsg);
			}
			_nwhinResource.getDomainXml(aMsg.getPatientId(), aMsg.getPatientName());
		} catch (JMSException e) {
			logger.error("JMS exception for " + msg, e);
		} catch (Exception e) {
			logger.error("general exception for " + msg, e);
		}
	}

}
