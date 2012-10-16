package org.osehra.integration.core.transformer.array;

import org.osehra.integration.core.transformer.Transformer;
import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class ConvertToObjectArray implements Transformer<Object, Object>{

	List<String> staticProperties;

	@Override
	public Object transform(Object src) throws TransformerException {

		if (NullChecker.isEmpty(src)) {
			return null;
		}
		java.util.List<Object> properties = new ArrayList<Object>();
		//Loop and Copy all the items from the static properties into the above properties
		for (String s : staticProperties) {
    		properties.add(s);
		}
		// Add src object into the array
		properties.add(src);

		return properties.toArray();

	}

	@Required
	public void setStaticProperties(List<String> staticProperties) {
		this.staticProperties = staticProperties;
	}
}

