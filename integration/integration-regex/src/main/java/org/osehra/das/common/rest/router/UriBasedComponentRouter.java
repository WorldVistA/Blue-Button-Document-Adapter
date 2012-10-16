package org.osehra.das.common.rest.router;

import org.osehra.das.common.component.Component;
import org.osehra.das.common.component.ComponentException;
import org.osehra.das.common.router.Router;
import org.osehra.das.common.router.RouterException;
import org.osehra.das.common.validation.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Required;

public class UriBasedComponentRouter implements Router<UriInfo, Object> {

	/**
	 * @uml.property  name="expressions"
	 * @uml.associationEnd  qualifier="pattern:java.util.regex.Pattern org.osehra.das.common.component.Component"
	 */
	protected Map<Pattern, Component<UriInfo, String>> expressions;

	@Override
	public Object route(final UriInfo uriInfo) throws RouterException {
		Assert.assertNotEmpty(uriInfo, "URI cannot be null!");

		for (final Pattern expression : this.expressions.keySet()) {
			final Matcher matcher = expression.matcher(uriInfo.getRequestUri()
					.toString());
			if (matcher.matches()) {
				final Component<UriInfo, String> component = this.expressions
						.get(expression);
				try {
					return component.processInbound(uriInfo);
				} catch (final ComponentException ex) {
					throw new RouterException(ex);
				}
			}
		}
		return null;
	}

	@Required
	public void setExpressions(
			final Map<String, Component<UriInfo, String>> expressions) {
		this.expressions = new LinkedHashMap<Pattern, Component<UriInfo, String>>();
		for (final java.util.Map.Entry<String, Component<UriInfo, String>> entry : expressions
				.entrySet()) {
			final String regEx = entry.getKey();
			final Pattern pattern = Pattern.compile(regEx, Pattern.DOTALL
					| Pattern.MULTILINE);
			this.expressions.put(pattern, entry.getValue());
		}
	}

}
