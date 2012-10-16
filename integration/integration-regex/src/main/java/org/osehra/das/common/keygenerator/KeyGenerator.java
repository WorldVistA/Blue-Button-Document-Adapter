/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.osehra.das.common.keygenerator;

/**
 *
 */
public class KeyGenerator {
	private static long nextKey;

	static {
		try {
			KeyGenerator.nextKey = (java.net.InetAddress.getLocalHost()
					.hashCode() << 32)
					+ (int) (System.currentTimeMillis() >> 2);
		} catch (final java.net.UnknownHostException e) {
			throw new IllegalStateException("Could not initialize KeyGenerator");
		}
	}

	static public synchronized long getNextKey() {
		return KeyGenerator.nextKey++;
	}
}
