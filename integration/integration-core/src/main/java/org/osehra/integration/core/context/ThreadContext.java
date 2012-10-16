package org.osehra.integration.core.context;

import org.springframework.beans.factory.DisposableBean;

public class ThreadContext implements DisposableBean {
	
	private String requestId = "noRequestId";
	
	public ThreadContext () {
		
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;			
	}

	@Override
	public void destroy() throws Exception {
		this.requestId = "";		
		//System.out.println("&&&& threadContext bean is destroyed.");	
	}	

}
