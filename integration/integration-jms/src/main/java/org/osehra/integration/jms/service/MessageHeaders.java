package org.osehra.integration.jms.service;

/**
 * Message Headers.
 * 
 * @author Julian Jewel
 */
public enum MessageHeaders {
	JMS_BEA_IsUnitOfWorkEnd("JMS_BEA_IsUnitOfWorkEnd"), JMS_BEA_UnitOfWork(
			"JMS_BEA_UnitOfWork"), JMS_BEA_UnitOfWorkSequenceNumber(
			"JMS_BEA_UnitOfWorkSequenceNumber"), JMS_VA_Integration_Component(
			"JMS_VA_Integration_Component"),
			
			
	JMS_VA_Request_Id("JMS_VA_Request_Id"),			
			
	/**
	 * The type of JMS messages.
	 */
	JMS_VA_Integration_Work_Id("JMS_VA_Integration_Work_Id");

	String property;

	private MessageHeaders(final String property) {
		this.property = property;
	}

	public String getProperty() {
		return this.property;
	}
}
