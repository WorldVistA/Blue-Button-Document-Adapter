package org.osehra.integration.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.Resource;

/**
 * @author Asha Amritraj
 */
public class FileUtil {

	public static String getResource(final Resource resource)
			throws IOException {
		final StringBuffer fileData = new StringBuffer(1000);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				resource.getInputStream()));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			final String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static byte[] getResourceAsBytes(final Resource resource)
			throws IOException {
		final StringBuffer fileData = new StringBuffer(1000);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				resource.getInputStream()));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			final String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString().getBytes();
	}

	protected FileUtil() {
	}
}
