package org.osehra.integration.vistalink.auth;

//Here are imports
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;

//Start of the class
public class LoginContextExt extends javax.security.auth.login.LoginContext {

	public LoginContextExt(final String name) throws LoginException {
		super(name);
	}

	public LoginContextExt(final String name,
			final CallbackHandler callbackHandler) throws LoginException {
		super(name, callbackHandler);
	}

	public LoginContextExt(final String name,
			final CallbackHandler callbackHandler, final Configuration config)
			throws LoginException {
		super(name, null, callbackHandler, config);
	}

	public LoginContextExt(final String name, final Subject subject)
			throws LoginException {
		super(name, subject);
	}

	public LoginContextExt(final String name, final Subject subject,
			final CallbackHandler callbackHandler) throws LoginException {
		super(name, subject, callbackHandler);
	}

	public LoginContextExt(final String name, final Subject subject,
			final CallbackHandler callbackHandler, final Configuration config)
			throws LoginException {
		super(name, subject, callbackHandler, config);
	}

}
