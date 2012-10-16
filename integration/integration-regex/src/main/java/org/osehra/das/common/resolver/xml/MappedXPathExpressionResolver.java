package org.osehra.das.common.resolver.xml;

import org.osehra.das.common.resolver.Resolver;
import org.osehra.das.common.resolver.ResolverException;
import org.osehra.das.common.selector.SelectorException;
import org.osehra.das.common.selector.xml.MappedXPathExpressionSelector;

import org.w3c.dom.Document;

public class MappedXPathExpressionResolver<R> extends
		MappedXPathExpressionSelector<R> implements Resolver<Document, R> {

	@Override
	public final R resolve(final Document sourceData) throws ResolverException {
		try {
			return super.select(sourceData);
		} catch (final SelectorException ex) {
			throw new ResolverException(
					"Failed to resolve resource by XPathExpress", ex);
		}
	}
}
