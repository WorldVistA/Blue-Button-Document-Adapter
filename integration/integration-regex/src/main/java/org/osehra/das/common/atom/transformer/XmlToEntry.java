package org.osehra.das.common.atom.transformer;

import org.osehra.das.common.transformer.TransformerException;
import org.osehra.das.common.transformer.xsl.XMLtoString;
import org.osehra.das.common.validation.NullChecker;
import org.osehra.das.common.xpath.XPathUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.Text;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;

public class XmlToEntry {

	/**
	 * @uml.property  name="authorExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression authorExpression;
	/**
	 * @uml.property  name="categoryExpressions"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="javax.xml.xpath.XPathExpression"
	 */
	List<XPathExpression> categoryExpressions;
	/**
	 * @uml.property  name="commentExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression commentExpression;
	/**
	 * @uml.property  name="contributorExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression contributorExpression;
	/**
	 * @uml.property  name="dateFormat"
	 */
	String dateFormat;
	/**
	 * @uml.property  name="editedExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression editedExpression;
	/**
	 * @uml.property  name="extensionExpressions"
	 * @uml.associationEnd  qualifier="key:javax.xml.namespace.QName javax.xml.xpath.XPathExpression"
	 */
	Map<QName, XPathExpression> extensionExpressions;
	/**
	 * @uml.property  name="idExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression idExpression;
	/**
	 * @uml.property  name="publishedExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression publishedExpression;
	/**
	 * @uml.property  name="rightsExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression rightsExpression;
	/**
	 * @uml.property  name="subTitleExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression subTitleExpression;
	/**
	 * @uml.property  name="summaryTransformer"
	 * @uml.associationEnd  
	 */
	XMLtoString summaryTransformer;
	/**
	 * @uml.property  name="titleExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression titleExpression;
	/**
	 * @uml.property  name="updatedExpression"
	 * @uml.associationEnd  
	 */
	XPathExpression updatedExpression;

	/**
	 * @uml.property  name="xmlToString"
	 * @uml.associationEnd  
	 */
	org.osehra.das.common.transformer.xml.XMLToString xmlToString;

	public String evaluteExpression(final Document document,
			final XPathExpression expression) {
		if (NullChecker.isNotEmpty(expression)) {
			try {
				return expression.evaluate(document);
			} catch (final XPathExpressionException ex) {
				throw new RuntimeException(ex);
			}
		}
		return null;
	}

	private List<String> evaluteExpressions(final Document document,
			final List<XPathExpression> expressions) {
		final List<String> results = new ArrayList<String>();
		if (NullChecker.isNotEmpty(expressions)) {
			try {
				for (final XPathExpression expression : expressions) {
					results.add(expression.evaluate(document));
				}
			} catch (final XPathExpressionException ex) {
				throw new RuntimeException(ex);
			}
		}
		return results;
	}

	public String getAuthor(final Document document) {
		return this.evaluteExpression(document, this.authorExpression);
	}

	public List<String> getCategories(final Document document) {
		return this.evaluteExpressions(document, this.categoryExpressions);
	}

	public String getComment(final Document document) {
		return this.evaluteExpression(document, this.commentExpression);
	}

	public String getContributor(final Document document) {
		return this.evaluteExpression(document, this.contributorExpression);
	}

	public String getEditedDate(final Document document) {
		return this.evaluteExpression(document, this.editedExpression);
	}

	public Map<QName, String> getExtensions(final Document document) {
		final Map<QName, String> extensions = new LinkedHashMap<QName, String>();
		if (NullChecker.isNotEmpty(this.extensionExpressions)) {
			try {
				for (final Entry<QName, XPathExpression> entry : this.extensionExpressions
						.entrySet()) {
					extensions.put(entry.getKey(),
							entry.getValue().evaluate(document));
				}
			} catch (final XPathExpressionException ex) {
				throw new RuntimeException(ex);
			}
		}
		return extensions;
	}

	public String getId(final Document document) {
		return this.evaluteExpression(document, this.idExpression);
	}

	public String getPublished(final Document document) {
		return this.evaluteExpression(document, this.publishedExpression);
	}

	public String getRights(final Document document) {
		return this.evaluteExpression(document, this.rightsExpression);
	}

	public String getSubTitle(final Document document) {
		return this.evaluteExpression(document, this.subTitleExpression);
	}

	public String getSummary(final Document document) {
		try {
			if (NullChecker.isNotEmpty(this.summaryTransformer)) {
				final String summary = this.summaryTransformer
						.transform(document);
				return summary;
			}
			return null;
		} catch (final TransformerException ex) {
			throw new RuntimeException(ex);
		}
	}

	public String getTitle(final Document document) {
		return this.evaluteExpression(document, this.titleExpression);
	}

	public String getUpdated(final Document document) {
		return this.evaluteExpression(document, this.updatedExpression);
	}

	public Date parseDate(final String dateString) {
		try {
			final SimpleDateFormat ofd = new SimpleDateFormat(this.dateFormat);
			final Date date = ofd.parse(dateString);
			return date;
		} catch (final ParseException ex) {
			throw new RuntimeException(ex);
		}

	}

	public void setAuthorExpression(final String authorExpression) {
		this.authorExpression = XPathUtil.compileExpression(authorExpression);
	}

	@Required
	public void setCategoryExpressions(final List<String> categoryExpressions) {
		this.categoryExpressions = new ArrayList<XPathExpression>();
		for (final String expression : categoryExpressions) {
			final XPathExpression xExpression = XPathUtil
					.compileExpression(expression);
			this.categoryExpressions.add(xExpression);
		}
	}

	public void setCommentExpression(final String commentExpression) {
		this.commentExpression = XPathUtil.compileExpression(commentExpression);
	}

	public void setContributorExpression(final String contributorExpression) {
		this.contributorExpression = XPathUtil
				.compileExpression(contributorExpression);
	}

	/**
	 * @param dateFormat
	 * @uml.property  name="dateFormat"
	 */
	@Required
	public void setDateFormat(final String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setEditedExpression(final String editedExpression) {
		this.editedExpression = XPathUtil.compileExpression(editedExpression);
	}

	public void setExtensionExpressions(
			final Map<QName, String> extensionExpressions) {
		this.extensionExpressions = new LinkedHashMap<QName, XPathExpression>();
		for (final Entry<QName, String> expression : extensionExpressions
				.entrySet()) {
			final QName key = expression.getKey();
			final XPathExpression xExpression = XPathUtil
					.compileExpression(expression.getValue());
			this.extensionExpressions.put(key, xExpression);
		}
	}

	public void setIdExpression(final String idExpression) {
		this.idExpression = XPathUtil.compileExpression(idExpression);
	}

	public void setPublishedExpression(final String publishedExpression) {
		this.publishedExpression = XPathUtil
				.compileExpression(publishedExpression);
	}

	public void setRightsExpression(final String rightsExpression) {
		this.rightsExpression = XPathUtil.compileExpression(rightsExpression);
	}

	public void setSubTitleExpression(final String subTitleExpression) {
		this.subTitleExpression = XPathUtil
				.compileExpression(subTitleExpression);
	}

	/**
	 * @param summaryTransformer
	 * @uml.property  name="summaryTransformer"
	 */
	public void setSummaryTransformer(final XMLtoString summaryTransformer) {
		this.summaryTransformer = summaryTransformer;
	}

	@Required
	public void setTitleExpression(final String titleExpression) {
		this.titleExpression = XPathUtil.compileExpression(titleExpression);
	}

	public void setUpdatedExpression(final String updatedExpression) {
		this.updatedExpression = XPathUtil.compileExpression(updatedExpression);
	}

	/**
	 * @param xmlToString
	 * @uml.property  name="xmlToString"
	 */
	@Required
	public void setXmlToString(
			final org.osehra.das.common.transformer.xml.XMLToString xmlToString) {
		this.xmlToString = xmlToString;
	}

	public org.apache.abdera.model.Entry transform(final Document document,
			final org.apache.abdera.model.Entry entry)
			throws TransformerException {
		final List<String> categories = this.getCategories(document);
		for (final String category : categories) {
			entry.addCategory(category);
		}
		final String title = this.getTitle(document);
		if (NullChecker.isNotEmpty(title)) {
			entry.setTitle(title, Text.Type.TEXT);
		}
		final String editedDate = this.getEditedDate(document);
		if (NullChecker.isNotEmpty(editedDate)) {
			final Date date = this.parseDate(editedDate);
			entry.setEdited(date);
		}

		final String id = this.getId(document);
		if (NullChecker.isNotEmpty(id)) {
			entry.setId(id);
		}

		final String published = this.getPublished(document);
		if (NullChecker.isNotEmpty(published)) {
			final Date date = this.parseDate(published);
			entry.setPublished(date);
		}

		final String rights = this.getRights(document);
		if (NullChecker.isNotEmpty(rights)) {
			entry.setRights(rights);
		}

		final String summary = this.getSummary(document);
		if (NullChecker.isNotEmpty(summary)) {
			entry.setSummaryAsHtml(summary);
		}

		final String updated = this.getUpdated(document);
		if (NullChecker.isNotEmpty(updated)) {
			final Date date = this.parseDate(updated);
			entry.setUpdated(date);
		}

		final String author = this.getAuthor(document);
		if (NullChecker.isNotEmpty(author)) {
			entry.addAuthor(author);
		}

		final String comment = this.getComment(document);
		if (NullChecker.isNotEmpty(comment)) {
			entry.addComment(comment);
		}

		final String contributor = this.getContributor(document);
		if (NullChecker.isNotEmpty(contributor)) {
			entry.addContributor(contributor);
		}

		final Map<QName, String> extensions = this.getExtensions(document);
		for (final java.util.Map.Entry<QName, String> extension : extensions
				.entrySet()) {
			final QName extensionQName = extension.getKey();
			final String text = extension.getValue();
			final Element element = entry.addExtension(extensionQName);
			element.setText(text);
		}
		final String content = this.xmlToString.transform(document);
		entry.setContent(content, MediaType.APPLICATION_XML);

		return entry;
	}
}
