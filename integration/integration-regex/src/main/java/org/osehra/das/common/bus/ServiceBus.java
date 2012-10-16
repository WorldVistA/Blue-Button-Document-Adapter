package org.osehra.das.common.bus;

public interface ServiceBus {
	Object process(String endpointName, Object obj);
}
