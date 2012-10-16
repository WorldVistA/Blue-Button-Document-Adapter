package org.osehra.das.common.service.vista;

import org.osehra.das.common.auth.context.LoginContextExt;
import org.osehra.das.common.service.ServiceInvocationException;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.validation.NullChecker;
import org.osehra.exception.FoundationsException;
import org.osehra.vistalink.adapter.cci.VistaLinkConnection;
import org.osehra.vistalink.rpc.RpcRequest;
import org.osehra.vistalink.rpc.RpcRequestFactory;
import org.osehra.vistalink.rpc.RpcRequestParams;
import org.osehra.vistalink.rpc.RpcResponse;
import org.osehra.vistalink.security.VistaKernelPrincipalImpl;

import java.util.List;
import java.util.Map;

import javax.resource.ResourceException;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Required;

/**
 * VistALink Endpoint to make RPC calls into VistA systems.
 * 
 * @author Asha Amritraj
 */
public class VistALinkServiceInvoker implements ServiceInvoker<Object, String> {

	/**
	 * @uml.property  name="callbackHandler"
	 * @uml.associationEnd  
	 */
	private CallbackHandler callbackHandler;
	/**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  
	 */
	private Configuration configuration;
	/**
	 * @uml.property  name="loginContextName"
	 */
	private String loginContextName;
	/**
	 * @uml.property  name="parameterTypes"
	 * @uml.associationEnd  qualifier="valueOf:java.lang.Integer java.lang.String"
	 */
	private Map<Integer, String> parameterTypes;
	/**
	 * @uml.property  name="rpcContext"
	 */
	private String rpcContext;
	/**
	 * @uml.property  name="rpcName"
	 */
	private String rpcName;
	/**
	 * @uml.property  name="timeout"
	 */
	private int timeout;

	/**
	 * @return
	 * @uml.property  name="rpcContext"
	 */
	public String getRpcContext() {
		return this.rpcContext;
	}

	/**
	 * @return
	 * @uml.property  name="rpcName"
	 */
	public String getRpcName() {
		return this.rpcName;
	}

	/**
	 * @return
	 * @uml.property  name="timeout"
	 */
	public int getTimeout() {
		return this.timeout;
	}

	@Override
	public String invoke(final Object args) throws ServiceInvocationException {

		VistaLinkConnection connection = null;
		LoginContext loginContext = null;
		try {
			// Login using the access/verify code set in the callback handler
			loginContext = new LoginContextExt(this.loginContextName,
					this.callbackHandler, this.configuration);
			loginContext.login();
			final VistaKernelPrincipalImpl userPrincipal = VistaKernelPrincipalImpl
					.getKernelPrincipal(loginContext.getSubject());
			connection = userPrincipal.getAuthenticatedConnection();
			connection.setTimeOut(this.timeout);
			final RpcRequest rpcRequest = RpcRequestFactory.getRpcRequest(
					this.rpcContext, this.rpcName);
			final RpcRequestParams params = rpcRequest.getParams();

			if (List.class.isInstance(args)) {
				final List<?> argList = (List<?>) args;
				for (int i = 0; i < argList.size(); i++) {
					params.setParam(i + 1, this.parameterTypes.get(i + 1),
							argList.get(i));
				}
			} else if (String.class.isInstance(args)) {
				final String argValue = (String) args;
				params.setParam(1, this.parameterTypes.get(1), argValue);
			}

			final RpcResponse rpcResponse = connection.executeRPC(rpcRequest);
			final String results = rpcResponse.getResults();
			return results;
		} catch (final LoginException ex) {
			throw new RuntimeException(ex);
		} catch (final FoundationsException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (NullChecker.isNotEmpty(loginContext)) {
				try {
					// Always logout
					loginContext.logout();
				} catch (final LoginException ex) {
					// TODO: Log!
					// Ignore Exception!!
				}
			}
			if (NullChecker.isNotEmpty(connection)) {
				try {
					connection.close();
				} catch (final ResourceException ex) {
					// TODO: Log!
					// Ignore Exception!!
				}
			}
		}
	}

	/**
	 * @param callbackHandler
	 * @uml.property  name="callbackHandler"
	 */
	@Required
	public void setCallbackHandler(final CallbackHandler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

	/**
	 * @param configuration
	 * @uml.property  name="configuration"
	 */
	@Required
	public void setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * @param loginContextName
	 * @uml.property  name="loginContextName"
	 */
	@Required
	public void setLoginContextName(final String loginContextName) {
		this.loginContextName = loginContextName;
	}

	@Required
	public void setParameterTypes(final Map<Integer, String> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	/**
	 * @param rpcContext
	 * @uml.property  name="rpcContext"
	 */
	@Required
	public void setRpcContext(final String rpcContext) {
		this.rpcContext = rpcContext;
	}

	/**
	 * @param rpcName
	 * @uml.property  name="rpcName"
	 */
	@Required
	public void setRpcName(final String rpcName) {
		this.rpcName = rpcName;
	}

	/**
	 * @param timeout
	 * @uml.property  name="timeout"
	 */
	@Required
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}
}
