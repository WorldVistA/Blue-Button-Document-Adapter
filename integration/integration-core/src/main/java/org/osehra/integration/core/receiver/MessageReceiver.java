package org.osehra.integration.core.receiver;

public interface MessageReceiver<E, T> {
	T receive(E e) throws MessageReceiverException;
}
