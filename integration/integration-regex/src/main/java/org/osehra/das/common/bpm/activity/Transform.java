package org.osehra.das.common.bpm.activity;

import org.osehra.das.common.bpm.engine.ActivityException;
import org.osehra.das.common.bpm.engine.ProcessContext;
import org.osehra.das.common.transformer.xsl.XSLTransformer;
import org.osehra.das.common.validation.Assert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * The transform activity, it uses a XSL message transformer to transform the
 * input.
 * 
 * @author Julian Jewel
 */
public class Transform extends AbstractTransform {

	/**
	 * Flag used for splitting the input XML into multiple documents.
	 * @uml.property  name="splitInputList"
	 */
	private boolean splitInputList = false;

	/**
	 * The XSL Transformer.
	 * @uml.property  name="transformer"
	 * @uml.associationEnd  
	 */
	private XSLTransformer transformer;

	/**
	 * Execute the transform activity.
	 * 
	 * @param context
	 *            the process context
	 * @param source
	 *            the input source
	 * @throws ActivityException
	 *             an error occurred
	 * @return the transformed output
	 */
	@Override
	@SuppressWarnings(value = "unchecked")
	protected final Object executeAction(final ProcessContext context,
			final Object source) throws ActivityException {
		Assert.assertNotEmpty(source, "Source cannot be empty for bean: "
				+ this.getBeanName() + " input: " + this.getInput());
		if (this.splitInputList
				&& source.getClass().isArray()
				&& (((Object[]) source)[0] != null)
				&& java.util.List.class.isAssignableFrom(((Object[]) source)[0]
						.getClass())) {
			final List<Object> outputList = new ArrayList<Object>();
			final List<Object> inputList = (List<Object>) ((Object[]) source)[0];
			for (final Object item : inputList) {
				((Object[]) source)[0] = item;
				outputList.add(this
						.transform(context, source, this.transformer));
			}
			return outputList;
		} else if ((source.getClass().isArray() && (((Object[]) source)[0] != null))
				|| !source.getClass().isArray()) {
			return this.transform(context, source, this.transformer);
		} else {
			return null;
		}
	}

	/**
	 * @param flag
	 * @uml.property  name="splitInputList"
	 */
	public final void setSplitInputList(final boolean flag) {
		this.splitInputList = flag;
	}

	/**
	 * Set the XSL transformer.
	 * @param theXSLTransformer  the XSL transformer reference
	 * @uml.property  name="transformer"
	 */
	@Required
	public final void setTransformer(final XSLTransformer theXSLTransformer) {
		this.transformer = theXSLTransformer;
	}

}
