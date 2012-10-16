package org.osehra.das.common.resolver;

/**
 * @author Asha Amritraj
 */
public interface Resolver<V, R> {

	R resolve(V file) throws ResolverException;

}
