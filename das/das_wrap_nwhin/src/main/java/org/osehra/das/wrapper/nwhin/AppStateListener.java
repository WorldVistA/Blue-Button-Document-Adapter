package org.osehra.das.wrapper.nwhin;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Web application lifecycle listener.
 *
 * @author David Vazquez Modified by: Asha Amritraj
 */

public class AppStateListener implements ServletContextListener {
	static private Log LOG = LogFactory.getLog(AppStateListener.class);

	@Override/*?|REVIEW FOR BHIE-4|pradeep|c1|*/
	public void contextDestroyed(final ServletContextEvent sce) {
		if (AppStateListener.LOG.isInfoEnabled()) {
			AppStateListener.LOG.info("DAS NwHIN Wrapper stopped.");
		}
	}/*|REVIEW FOR BHIE-4|pradeep|c1|?*/

	@Override/*?|sprint 4 reviews|pradeep3|c1|*/
	public void contextInitialized(final ServletContextEvent sce) {
		// Get the version from the MANIFEST.MF file
		InputStream in = sce.getServletContext().getResourceAsStream(
				"META-INF/MANIFEST.MF");
		in = sce.getServletContext()
				.getResourceAsStream("META-INF/MANIFEST.MF");
		try {
			// Get the Implementation-Version from Manifest file
			Manifest manifest = new Manifest(in);
			Attributes attributes = manifest.getMainAttributes();
			String version = attributes.getValue("Implementation-Version");
			// Put the build and version into the context
			sce.getServletContext().setAttribute("version", version);
			if (AppStateListener.LOG.isInfoEnabled()) {
				AppStateListener.LOG.info("DAS NwHIN Wrapper (" + version + ") started.");
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}/*|sprint 4 reviews|pradeep3|c1|?*/
}