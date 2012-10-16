package org.osehra.das.common.bpm.engine;

/**
 * The interface for any activity in the BPM.
 * @author  Julian Jewel
 * @param  < T  >  Usually a java.lang.Object
 */
public interface Activity<T> {
	/**
	 * Execute the activity.
	 * 
	 * @param arg
	 *            the input for the activity
	 * @return the output of the activity
	 * @throws ActivityException
	 *             exception when executing the activity
	 */
	T execute(T arg) throws ActivityException;

	/**
	 * @uml.property  name="name"
	 */
	String getName();

}
