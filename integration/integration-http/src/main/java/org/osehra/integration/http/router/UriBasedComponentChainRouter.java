package org.osehra.integration.http.router;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;
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

/**
 * URI Based Component Chain Router. Takes a URI input and routes it to multiple
 * components based on regular expressions.
 * 
 * @author Asha Amritraj
 * 
 */
public class UriBasedComponentChainRouter implements Router<UriInfo, Object> {

	/**
	 */
	Map<Pattern, List<Component<UriInfo, String>>> expressions;

	@Override
	public Object route(final UriInfo uriInfo) throws RouterException {
		Assert.assertNotEmpty(uriInfo, "URI cannot be null!");

		final List<String> results = new ArrayList<String>();
		for (final Pattern expression : this.expressions.keySet()) {
			final Matcher matcher = expression.matcher(uriInfo.getRequestUri()
					.toString());
			if (matcher.matches()) {
				final List<Component<UriInfo, String>> components = this.expressions
						.get(expression);
				for (final Component<UriInfo, String> component : components) {
					try {
						final String object = component.processInbound(uriInfo);
						if (NullChecker.isNotEmpty(object)) {
							results.add(object);
						}
					} catch (final ComponentException ex) {
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
			final Map<String, List<Component<UriInfo, String>>> expressions) {
		this.expressions = new LinkedHashMap<Pattern, List<Component<UriInfo, String>>>();
		for (final java.util.Map.Entry<String, List<Component<UriInfo, String>>> entry : expressions
				.entrySet()) {
			final String regEx = entry.getKey();
			final Pattern pattern = Pattern.compile(regEx, Pattern.DOTALL
					| Pattern.MULTILINE);
			this.expressions.put(pattern, entry.getValue());
		}
	}
}
