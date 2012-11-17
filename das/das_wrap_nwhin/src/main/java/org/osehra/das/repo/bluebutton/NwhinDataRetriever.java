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
	protected C32DocumentDao _c32dao;
	protected Log logger = LogFactory.getLog(this.getClass());
	
	public void setWrapperResource(WrapperResource resource) {
		_nwhinResource = resource;
	}
	
	public void setC32DocumentDao(C32DocumentDao c32dao) {
		_c32dao = c32dao;
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
			String patientId = aMsg.getPatientId();
			String xml = (String)_nwhinResource.getDomainXml(patientId, aMsg.getPatientName());
			logger.error("attempting to persist domain XML: " + xml);
			_c32dao.createAndPersistC32(patientId, xml);
		} catch (JMSException e) {
			logger.error("JMS exception for " + msg, e);
		} catch (Exception e) {
			logger.error("general exception for " + msg, e);
		}
	}

}
