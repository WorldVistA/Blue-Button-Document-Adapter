package org.osehra.integration.test.util.resource;

import org.osehra.integration.test.modifier.Modifier;
import org.osehra.integration.test.modifier.ModifyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public abstract class AbstractResourceFactory<V> {

	@Autowired
	protected ApplicationContext applicationContext;
	protected String filename;
	private Modifier<V,V>[] modifiers;

	/**
	 * Sets the resource filename.
	 * @param filename - The file name of the resource.
	 */
	@Required
	public void setResource( String filename )
	{
		this.filename = filename;
	}

	/**
	 * Sets a modifier to be applied before the result is returned.
	 * @param modifier
	 */
	@SuppressWarnings("unchecked")
	public void setModifier( Modifier<V,V> modifier)
	{
		this.modifiers = new Modifier[1];
		this.modifiers[0] = modifier;
	}

	/**
	 * Gets the modifier.
	 * @return
	 */
	public Modifier<V,V> getModifier()
	{
		return this.modifiers[0];
	}

	/**
	 * Sets an array of modifiers to be applied before the result is returned.
	 * @param modifier - The modifier.
	 */
	public void setModifiers( Modifier<V,V>[] modifier)
	{
		this.modifiers = modifier;
	}

	/**
	 * Gets the array of modifiers.
	 * @return The array of modifiers.
	 */
	public Modifier<V,V>[] getModifiers()
	{
		return this.modifiers;
	}

	protected V  modify( V source )
		throws ModifyException
	{
		if (this.modifiers != null)
		{
			for (Modifier<V,V> modifier : this.modifiers)
			{
				source = modifier.modify(source);
			}
		}
		return source;

	}
}
