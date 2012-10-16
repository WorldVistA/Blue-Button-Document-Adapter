package org.osehra.integration.core.filter;

/**
 * A no operation filter. Sometimes used instead of null.
 * 
 * @author Julian Jewel
 */
public class NoOpFilter implements Filter<Object, Object> {

	/**
	 * Filter the message.
	 * 
	 * @param object
	 *            the input message
	 * @return null if the message needs to be discarded or the object otherwise
	 * @throws FilterException
	 *             an exception when filtering the message
	 */
	@Override
	public Object filter(final Object object) throws FilterException {
		return object;
	}
}
