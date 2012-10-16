package org.osehra.integration.test.util.xml;

import java.io.IOException;

import org.osehra.integration.test.modifier.ModifyException;
import org.osehra.integration.test.util.resource.AbstractResourceFactory;
import org.osehra.integration.test.util.resource.ResourceUtil;
import org.osehra.integration.util.Assert;

import org.springframework.beans.factory.FactoryBean;
import org.w3c.dom.Document;

public class XmlResourceFactoryBean extends AbstractResourceFactory<Document> implements FactoryBean<Document> {

	@Override
	public Document getObject() throws Exception {
		Assert.assertNotEmpty(this.applicationContext,"Application context is empty.");
		Document doc = null;
		try
		{
			String contents = ResourceUtil.getTextResource(applicationContext, this.filename);
        	doc = DOMParserHelper.parseDocument(contents);
		} catch (IOException e ) {
			throw new DOMException(e);
		}
		try
		{
			return this.modify(doc);
		} catch (ModifyException ex) {
			throw new DOMException(ex);
		}
	}

	@Override
	public Class<Document> getObjectType() {
		return Document.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
