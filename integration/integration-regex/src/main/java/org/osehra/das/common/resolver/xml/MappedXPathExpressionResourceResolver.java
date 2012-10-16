package org.osehra.das.common.resolver.xml;

import org.osehra.das.common.resolver.Resolver;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * This implements a resource resolver that selects a resource based on message
 * source data.
 * 
 * @author Keith Roberts
 */
public class MappedXPathExpressionResourceResolver extends
		MappedXPathExpressionResolver<Resource> implements
		Resolver<Document, Resource> {

}
