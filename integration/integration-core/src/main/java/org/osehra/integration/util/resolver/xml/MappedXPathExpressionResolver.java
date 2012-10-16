package org.osehra.integration.util.resolver.xml;

import org.osehra.integration.util.resolver.Resolver;
import org.osehra.integration.util.resolver.ResolverException;
import org.osehra.integration.util.selector.SelectorException;
import org.osehra.integration.util.selector.xml.MappedXPathExpressionSelector;

import org.w3c.dom.Document;

public class MappedXPathExpressionResolver<R> extends
		MappedXPathExpressionSelector<R> implements Resolver<Document, R> {

	@Override
	public R resolve(final Document sourceData) throws ResolverException {
		try {
			return super.select(sourceData);
		} catch (final SelectorException ex) {
			throw new ResolverException(
					"Failed to resolve resource by XPathExpress", ex);
		}
	}
}
