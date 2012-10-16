package org.osehra.integration.core.transformer;

/**
 * @author Asha Amritraj
 * @param <E>
 * @param <T>
 */
public interface Transformer<E, T> {

	T transform(E src) throws TransformerException;
}
