package org.osehra.das.common.transceiver.client;

/**
 * @author Asha Amritraj
 * @param <E>
 */
public interface Transceiver<E> {
	E transceive(E payload);

}
