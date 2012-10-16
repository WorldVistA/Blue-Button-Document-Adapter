package org.osehra.das.common.bpm.engine;

import org.springframework.beans.factory.annotation.Required;

/**
 * The abstract Rule engine.
 * 
 * @author Julian Jewel
 * @param <E>
 *            usually Object
 */
public abstract class AbstractRuleEngine<E> implements ProcessEngine<E> {

	/**
	 * The name of the process.
	 * @uml.property  name="name"
	 */
	private String name;

	/**
	 * Get the name of the process.
	 * @return  the name of the process
	 * @uml.property  name="name"
	 */
	@Override
	public final String getName() {
		return this.name;
	}

	/**
	 * Set the name of the process.
	 * @param theName  the process name
	 * @uml.property  name="name"
	 */
	@Required
	public final void setName(final String theName) {
		this.name = theName;
	}
}
