package org.osehra.das.common.atom.transformer;

import org.osehra.das.common.transformer.Transformer;
import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.validation.NullChecker;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Content.Type;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Text;
import org.springframework.beans.factory.annotation.Required;

/**
 * Transform from Uri to a navigation feed to display atom entries when
 * navigating through URLs.
 * 
 * @author Asha Amritraj
 * 
 */
public class UriToDefaultNavigationFeed implements Transformer<UriInfo, Feed> {

	/**
	 * @uml.property  name="baseUri"
	 */
	String baseUri;
	/**
	 * @uml.property  name="defaultAuthor"
	 */
	String defaultAuthor;

	/**
	 * @uml.property  name="defaultContributor"
	 */
	String defaultContributor;
	/**
	 * Spring injected links.
	 * @uml.property  name="links"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="java.util.Map" qualifier="constant:java.lang.String java.lang.String"
	 */
	Map<String, Map<String, Map<String, String>>> links;

	/**
	 * @uml.property  name="notSupportedContent"
	 */
	String notSupportedContent;
	/**
	 * @uml.property  name="notSupportedTitle"
	 */
	String notSupportedTitle;

	/**
	 * @uml.property  name="subTitle"
	 */
	String subTitle;
	/**
	 * @uml.property  name="title"
	 */
	String title;

	/**
	 * @uml.property  name="uriSyntaxContent"
	 */
	String uriSyntaxContent;

	/**
	 * @uml.property  name="uriSyntaxTitle"
	 */
	String uriSyntaxTitle;

	private Feed createHeader(final UriInfo uriInfo) {
		final Feed f = Abdera.getInstance().getFactory().newFeed();
		final URI feedLink = uriInfo.getRequestUri();
		f.addLink(feedLink.toString(), "self");
		f.setTitle(this.title, Text.Type.HTML);
		f.setSubtitle(this.subTitle, Text.Type.HTML);
		f.addAuthor(this.defaultAuthor);
		f.addContributor(this.defaultContributor);
		return f;
	}

	public Feed createSyntax(final UriInfo uriInfo) {
		final Feed f = this.createHeader(uriInfo);
		final Entry entry = f.addEntry();
		entry.setTitle(this.title, Text.Type.HTML);
		entry.setContent(this.uriSyntaxContent, Type.HTML);
		entry.addAuthor(this.defaultAuthor);
		entry.addContributor(this.defaultContributor);
		entry.setEdited(new Date());
		entry.setPublished(new Date());
		entry.setUpdated(new Date());
		return f;
	}

	private Feed getFeed(final UriInfo uriInfo, final String item) {
		final Feed f = this.createHeader(uriInfo);
		final Map<String, Map<String, String>> locations = this.links.get(item);
		if (NullChecker.isNotEmpty(locations)) {
			for (final java.util.Map.Entry<String, Map<String, String>> entry : locations
					.entrySet()) {
				final String key = entry.getKey();
				final Map<String, String> valueSet = entry.getValue();
				final Entry atomEntry = f.addEntry();
				atomEntry.setTitle(valueSet.get("title"));
				atomEntry.addCategory(this.title);
				if (valueSet.containsKey("description")) {
					final String description = valueSet.get("description");
					atomEntry.setContent("<p>" + description + "</p>",
							Type.HTML);
				}
				final String path = uriInfo.getPath();
				if (path.endsWith("/")) {
					atomEntry.addLink(path + key);
				} else {
					atomEntry.addLink(path + "/" + key);
				}
				atomEntry.addAuthor(this.defaultAuthor);
				atomEntry.addContributor(this.defaultContributor);

				atomEntry.setEdited(new Date());
				atomEntry.setPublished(new Date());
				atomEntry.setUpdated(new Date());
			}
		} else {
			final Entry entry = f.addEntry();
			entry.setTitle(this.notSupportedTitle);
			entry.setContent(this.notSupportedContent, Type.HTML);
			entry.addAuthor(this.defaultAuthor);
			entry.addContributor(this.defaultContributor);
			entry.setEdited(new Date());
			entry.setPublished(new Date());
			entry.setUpdated(new Date());

			final URI feedLink = uriInfo.getRequestUri();
			f.addLink(feedLink.toString(), "self");
		}
		return f;
	}

	/**
	 * @param baseUri
	 * @uml.property  name="baseUri"
	 */
	@Required
	public void setBaseUri(final String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @param defaultAuthor
	 * @uml.property  name="defaultAuthor"
	 */
	@Required
	public void setDefaultAuthor(final String defaultAuthor) {
		this.defaultAuthor = defaultAuthor;
	}

	/**
	 * @param defaultContributor
	 * @uml.property  name="defaultContributor"
	 */
	@Required
	public void setDefaultContributor(final String defaultContributor) {
		this.defaultContributor = defaultContributor;
	}

	@Required
	public void setLinks(
			final Map<String, Map<String, Map<String, String>>> links) {
		this.links = links;
	}

	/**
	 * @param notSupportedContent
	 * @uml.property  name="notSupportedContent"
	 */
	@Required
	public void setNotSupportedContent(final String notSupportedContent) {
		this.notSupportedContent = notSupportedContent;
	}

	/**
	 * @param notSupportedTitle
	 * @uml.property  name="notSupportedTitle"
	 */
	@Required
	public void setNotSupportedTitle(final String notSupportedTitle) {
		this.notSupportedTitle = notSupportedTitle;
	}

	/**
	 * @param subTitle
	 * @uml.property  name="subTitle"
	 */
	@Required
	public void setSubTitle(final String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * @param title
	 * @uml.property  name="title"
	 */
	@Required
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @param uriSyntaxContent
	 * @uml.property  name="uriSyntaxContent"
	 */
	@Required
	public void setUriSyntaxContent(final String uriSyntaxContent) {
		this.uriSyntaxContent = uriSyntaxContent;
	}

	/**
	 * @param uriSyntaxTitle
	 * @uml.property  name="uriSyntaxTitle"
	 */
	@Required
	public void setUriSyntaxTitle(final String uriSyntaxTitle) {
		this.uriSyntaxTitle = uriSyntaxTitle;
	}

	@Override
	public Feed transform(final UriInfo uriInfo) throws TransformerException {
		final MultivaluedMap<String, String> valuedMap = uriInfo
				.getPathParameters();
		final List<String> patientId = valuedMap.get("pid");
		if (NullChecker.isEmpty(patientId)
				|| (NullChecker.isNotEmpty(patientId) && NullChecker
						.isEmpty(patientId.get(0)))) {
			final Feed f = this.createSyntax(uriInfo);
			return f;
		}

		final List<String> parents = valuedMap.get("parent");
		if (NullChecker.isEmpty(parents)
				|| (NullChecker.isNotEmpty(parents) && NullChecker
						.isEmpty(parents.get(0)))) {
			final Feed f = this.getFeed(uriInfo, "/");
			return f;
		}

		final List<String> grandChildren = valuedMap.get("grandchild");
		if (NullChecker.isNotEmpty(grandChildren)
				|| (NullChecker.isNotEmpty(grandChildren) && NullChecker
						.isEmpty(grandChildren.get(0)))) {
			final Feed f = this.getFeed(uriInfo, grandChildren.get(0));
			return f;
		}

		final List<String> children = valuedMap.get("child");
		if (NullChecker.isEmpty(children)
				|| (NullChecker.isNotEmpty(children) && NullChecker
						.isEmpty(children.get(0)))) {
			final Feed f = this.getFeed(uriInfo, parents.get(0));
			return f;
		} else {
			final Feed f = this.getFeed(uriInfo, children.get(0));
			return f;
		}
	}

}
