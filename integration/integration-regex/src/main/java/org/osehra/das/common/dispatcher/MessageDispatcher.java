package org.osehra.das.common.dispatcher;

import org.osehra.das.common.router.Router;

/**
 * A message dispatcher. It is also a router.
 * 
 * @author Julian Jewel
 * @param <T>
 *            usually java.lang.Object
 */
public interface MessageDispatcher<E, T> extends Router<E, T> {
}
