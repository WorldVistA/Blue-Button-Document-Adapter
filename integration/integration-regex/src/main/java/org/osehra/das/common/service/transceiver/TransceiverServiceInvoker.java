package org.osehra.das.common.service.transceiver;

import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.transceiver.client.Transceiver;

import org.springframework.beans.factory.annotation.Required;

public class TransceiverServiceInvoker implements
		ServiceInvoker<Object, Object> {

	/**
	 * @uml.property  name="transceiver"
	 * @uml.associationEnd  
	 */
	Transceiver<Object> transceiver;

	@Override
	public Object invoke(final Object object) throws ServiceInvocationException {
		return this.transceiver.transceive(object);
	}

	@Required
	public void setTransceiver(final Transceiver<Object> transceiver) {
		this.transceiver = transceiver;
	}
}
