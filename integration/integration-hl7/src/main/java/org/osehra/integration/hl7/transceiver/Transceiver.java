package org.osehra.integration.hl7.transceiver;

/**
 * @author Asha Amritraj
 * @param <E>
 */
public interface Transceiver<E> {
	E transceive(E payload);

}
