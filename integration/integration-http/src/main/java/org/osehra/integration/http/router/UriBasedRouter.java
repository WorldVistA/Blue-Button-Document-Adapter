package org.osehra.integration.http.router;

import org.osehra.integration.core.router.Router;
import org.osehra.integration.core.router.RouterException;
import org.osehra.integration.util.Assert;
import org.osehra.integration.util.NullChecker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Required;

public class UriBasedRouter implements Router<UriInfo, Object> {

	/**
	 * @uml.property name="expressions"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     elementType="org.osehra.das.common.router.Router"
	 *                     qualifier
	 *                     ="pattern:java.util.regex.Pattern java.util.List"
	 */
	Map<Pattern, List<Router<UriInfo, String>>> expressions;

	@Override
	public Object route(final UriInfo uriInfo) throws RouterException {
		Assert.assertNotEmpty(uriInfo, "URI cannot be null!");

		final List<Object> results = new ArrayList<Object>();
		for (final Pattern expression : this.expressions.keySet()) {
			final Matcher matcher = expression.matcher(uriInfo.getRequestUri()
					.toString());
			if (matcher.matches()) {
				final List<Router<UriInfo, String>> components = this.expressions
						.get(expression);
				for (final Router<UriInfo, String> component : components) {
					try {
						final Object object = component.route(uriInfo);
						if (NullChecker.isNotEmpty(object)) {
							results.add(object);
						}
					} catch (final RouterException ex) {
						throw new RouterException(ex);
					}
				}
				break;
			}
		}
		if (results.size() == 1) {
			return results.get(0);
		} else {
			return results;
		}
	}

	@Required
	public void setExpressions(
			final Map<String, List<Router<UriInfo, String>>> expressions) {
		this.expressions = new LinkedHashMap<Pattern, List<Router<UriInfo, String>>>();
		for (final java.util.Map.Entry<String, List<Router<UriInfo, String>>> entry : expressions
				.entrySet()) {
			final String regEx = entry.getKey();
			final Pattern pattern = Pattern.compile(regEx, Pattern.DOTALL
					| Pattern.MULTILINE);
			this.expressions.put(pattern, entry.getValue());
		}
	}
}
