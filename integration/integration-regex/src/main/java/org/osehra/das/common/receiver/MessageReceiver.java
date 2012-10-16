package org.osehra.das.common.receiver;

public interface MessageReceiver<E,T> {
	T receive (E e) throws MessageReceiverException;
}
