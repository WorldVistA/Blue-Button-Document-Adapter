package org.osehra.integration.util;

import org.safehaus.uuid.UUIDGenerator;

/**
 * @author Asha Amritraj
 */
public class UUIDUtil {

	public static String generateMessageId() {
		final String uuid = UUIDGenerator.getInstance().generateTimeBasedUUID()
				.toString();
		if (NullChecker.isNotEmpty(uuid) && (uuid.indexOf('-') > 0)) {
			final String result = uuid.replaceAll("-", "").substring(0, 20);
			return result;
		}
		throw new RuntimeException("Unable to generate UUID");
	}

	public static String generateUUID() {
		final String uuid = UUIDGenerator.getInstance().generateTimeBasedUUID()
				.toString();
		if (NullChecker.isNotEmpty(uuid)) {
			return uuid;
		}
		throw new RuntimeException("Unable to generate UUID");
	}

	protected UUIDUtil() {
	}
}
