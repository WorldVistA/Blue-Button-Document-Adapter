package org.osehra.integration.http.servletfilter;

import org.osehra.integration.core.context.ThreadContext;
import org.osehra.integration.util.NullChecker;
import org.osehra.integration.util.UUIDUtil;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author John W. May
 *
 */
public class LogFilter implements javax.servlet.Filter {

	private static final Log LOG = LogFactory
			.getLog(LogFilter.class);
				
	private ThreadContext threadContext;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {			
	}	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		LOG.trace("begin doFilter()");
		
		String requestId = "No RequestId";
		if(NullChecker.isNotEmpty(this.threadContext)) {
			requestId = UUIDUtil.generateMessageId();
			this.threadContext.setRequestId(requestId);
		}
		LOG.trace("setting requestId="+requestId);
				
		if(request instanceof HttpServletRequest) {

			HttpServletRequest httpServletRequ = ((HttpServletRequest)request);
			StringBuffer requestUrl = httpServletRequ.getRequestURL();
			LOG.info(requestId+"- Incoming Request URL: "+requestUrl );
			
			if (LOG.isTraceEnabled()) {
				LOG.trace(requestId+"- Incoming HttpServletRequest: {");
				String requestProt = httpServletRequ.getProtocol();
				LOG.trace(requestId+"- Protocol: "+requestProt);		
				String requestMethod = httpServletRequ.getMethod();
				LOG.trace(requestId+"- Method: "+requestMethod);
				String requestURI = httpServletRequ.getRequestURI();
				LOG.trace(requestId+"- URI: "+requestURI);
				LOG.trace(requestId+"- HttpServletRequest header(s): {");	
				@SuppressWarnings("unchecked")
				Enumeration<Object> headerNames = httpServletRequ.getHeaderNames();			   
			    while(headerNames.hasMoreElements()) {
			      String headerName = (String)headerNames.nextElement();
			      LOG.trace(requestId+"- ["+headerName+": "+httpServletRequ.getHeader(headerName)+"]");	      
			    }
			    LOG.trace(requestId+"- } }");
			}
		} 
		
		LOG.trace(requestId+"- Processing the request .... ");
		
		chain.doFilter(request, response);
		
		LOG.trace(requestId+"- Returning the response .... ");
		
		LOG.trace("end doFilter()");
	}

	@Override
	public void destroy() {
	}	
	
	public void setThreadContext(ThreadContext threadContext) {
		this.threadContext = threadContext;
	}

}
