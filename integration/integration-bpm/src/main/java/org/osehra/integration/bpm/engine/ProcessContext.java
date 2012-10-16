package org.osehra.integration.bpm.engine;

import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.io.Serializable;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The process context which holds the state of the process. This class is
 * serializable. Ensure that all variables are serializable.
 *
 * @author Julian Jewel
 */
public class ProcessContext implements Serializable {

	/**
	 * Logger.
	 */
	private static final Log LOG = LogFactory.getLog(ProcessContext.class);
	/**
	 * This class is serializable.
	 */
	private static final long serialVersionUID = -8349055206168033454L;

	/**
	 * The activity log to log activity events. TODO: Move to a separate class.
	 *
	 * @uml.property name="activityLog"
	 * @uml.associationEnd
	 */
	private ActivityLog activityLog;

	/**
	 * The current activity name - needed to restore the process and call an
	 * activity. This variable is used when the persisted context is restored
	 * and the activity needs to continue.
	 *
	 * @uml.property name="currentActivityName"
	 */
	private String currentActivityName;

	/**
	 * The hashtable that holds all the inputs and outputs of the process.
	 *
	 * @uml.property name="properties"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     elementType="java.lang.Object" qualifier=
	 *                     "property:java.lang.String java.lang.Object"
	 */
	private Hashtable<String, Object> properties = new Hashtable<String, Object>();

	/*
	 * Default constructor
	 */
	public ProcessContext() {
	}

	/**
	 * Default constructor with process name.
	 *
	 * @param processName
	 *            the process name
	 */
	public ProcessContext(final String processName) {
		if (ProcessContext.LOG.isInfoEnabled()) {
			this.activityLog = new ActivityLog(processName);
		}
	}

	/**
	 * Get a property from the hashtable.
	 *
	 * @param property
	 *            the property name
	 * @return the value of the object
	 */
	public final Object get(final String property) {
		Assert.assertNotEmpty(property,
				"Process context cannot get a property named null!");
		if (this.properties.containsKey(property)) {
			return this.properties.get(property);
		}
		return null;
	}

	/**
	 * Get the activity log to log events.
	 *
	 * @return the activity log
	 * @uml.property name="activityLog"
	 */
	public final ActivityLog getActivityLog() {
		return this.activityLog;
	}

	/**
	 * Get the current activity name. The current activity being executed is set
	 * in the context.
	 *
	 * @return the current activity name
	 * @uml.property name="currentActivityName"
	 */
	public final String getCurrentActivityName() {
		return this.currentActivityName;
	}

	public Hashtable<String, Object> getProperties() {
		return this.properties;
	}

	/**
	 * Get the source based on input. The input can be comma separated. A lookup
	 * is performed on the hashtable for all the inputs and the object array is
	 * returned.
	 *
	 * @param input
	 *            the inputs separated by comma in a string
	 * @return the objects that are available in the hashtable.
	 */
	public Object[] getSource(final String input) {
		Object[] sources = null;
		if (NullChecker.isNotEmpty(input) && (input.indexOf(',') > 0)) {
			final String[] splitStr = input.split(",");
			if (NullChecker.isNotEmpty(splitStr)) {
				sources = new Object[splitStr.length];
				for (int i = 0; i < splitStr.length; i++) {
					final Object object = this.get(splitStr[i].trim());
					sources[i] = object;
				}
			}
		} else if (NullChecker.isNotEmpty(input)) {
			sources = new Object[1];
			sources[0] = this.get(input);
		}
		return sources;
	}

	/**
	 * Put a property and value into the context.
	 *
	 * @param property
	 *            the property name
	 * @param source
	 *            the serializable source
	 */
	public void put(final String property, final Object source) {
		Assert.assertNotEmpty(property,
				"Process context property name cannot be null!");
		Assert.assertNotEmpty(source,
				"Process context property value cannot be null for " + property);

		//Assert.assertInstance(source, Serializable.class);
		this.properties.put(property, source);
	}

	/**
	 * The current activity name that is being executed.
	 *
	 * @param theCurrentActivityName
	 *            the current activity name
	 * @uml.property name="currentActivityName"
	 */
	public void setCurrentActivityName(final String theCurrentActivityName) {
		this.currentActivityName = theCurrentActivityName;
	}

	public void setProperties(final Hashtable<String, Object> table) {
		this.properties = table;
	}
}
