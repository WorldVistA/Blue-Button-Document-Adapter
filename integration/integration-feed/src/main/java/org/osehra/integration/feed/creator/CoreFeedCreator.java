package org.osehra.integration.feed.creator;

import org.osehra.integration.util.NullChecker;

import java.util.Date;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRISyntaxException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Text;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CoreFeedCreator {	
	
	private static final Log LOG = LogFactory.getLog(CoreFeedCreator.class);
		
	private String baseUri;	
	
	private String title;	
	
	private String subTitle;
	
	private String version;		
	
	private String author;
	
	private String contributor;

	private String atomNamespace;
	
	private String atomSchemaLocation;
	
	private String dasextensionNamespace;
	
	private String dasextensionPrefix;
	
	private String dasextensionSchemaLocation;
	
	
	public Feed createFeed() {
		return this.createFeed(null, null, null);
	}
	
	public Feed createFeed(String requestURIStr, String assigningAuthorityStr, String pidStr) {
		// instantiate the new Feed
		Feed f = Abdera.getInstance().getFactory().newFeed();
		// add the feed attributes
		f = this.addFeedAttributes(f);
		// add the header elements
		f = this.addFeedHeaderElements(f, requestURIStr, assigningAuthorityStr, pidStr);	
		return  f;
	}
	
	private Feed addFeedAttributes(final Feed f) {
		// set Feed attributes
//		try {
//			f.setBaseUri(this.baseUri);	
//		} catch(IRISyntaxException ex) {
//			LOG.error(ex.getMessage(), ex);
//		}
		f.declareNS("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		f.declareNS(this.dasextensionNamespace, this.dasextensionPrefix);
		f.setAttributeValue(
				  new QName("http://www.w3.org/2001/XMLSchema-instance", 
						  "schemaLocation", "xsi"), 
				  this.atomNamespace
				  +" "
				  +this.atomSchemaLocation
				  +" "
				  +this.dasextensionNamespace 
				  +" "
				  +this.dasextensionSchemaLocation);		
		return f;
	}
	
	private Feed addFeedHeaderElements(final Feed f, String requestURIStr, String assigningAuthorityStr, String pidStr) {			
		// add Feed header elements		
		f.setTitle(this.title, Text.Type.HTML);
		f.setSubtitle(this.subTitle, Text.Type.HTML);	
		f.addAuthor(this.author);
		f.addContributor(this.contributor);
		f.setUpdated(new Date());		
		try {
			f.setGenerator(this.baseUri, this.version, this.title);
			// NOTE: No spaces can be in the input String for 
			// setId(String) or an IRISyntaxException will be thrown. 			
			String idText = null;
			if(NullChecker.isNotEmpty(requestURIStr)) {
				idText = requestURIStr;
			} else {
				// NOTE: This id is set to the baseUri as the default value 
				// for the Navigation component Feeds, but the response 
				// Feeds will have this value updated in the Processes by 
				// XSL Transformations.
				idText = this.baseUri;
			}			
			idText = idText.trim().replaceAll(" ", "");					
			f.setId(idText); 
			// NOTE: This "self" link is set to the baseUri as the default value 
			// for the Navigation component Feeds, but the response 
			// Feeds will have this value updated in the Processes by 
			// XSL Transformations.
			if(NullChecker.isNotEmpty(requestURIStr)) {
				f.addLink(requestURIStr.concat("/"), "self"); 
			} else {
				f.addLink(this.baseUri, "self"); 
			}
			
			// if have both the assigningAuthority and pid values, 
			// then add them to the alternate link's URI
			if(NullChecker.isNotEmpty(assigningAuthorityStr) && NullChecker.isNotEmpty(pidStr)) {
				f.addLink(this.baseUri.concat(assigningAuthorityStr).concat("/").concat(pidStr).concat("/"), "alternate");
			} else {
				f.addLink(this.baseUri, "alternate");
			}
		} catch(IRISyntaxException ex) {
			LOG.error(ex.getMessage(), ex);
		}
		return f;
	}		
	
//	private String filterHtmlDisplayText(String text) {		
//		// Bug Fix: Feed when converted to String seems to be 
//		// automatically converting all "&"'s to "&amp;".  In 
//		// order to avoid existing "&amp;"'s from being 
//		// converted twice later on and displaying incorrectly, 
//		// the "&amp;"'s are being un-converted here. - JWM
//		String filteredText = text.replaceAll("&amp;", "&");
//		
//		return filteredText;
//	}

	public void setBaseUri(String baseUri) {
		// verify the baseUri ends with a /
		if(!baseUri.trim().endsWith("/")) {
			this.baseUri = baseUri.trim().concat("/");
		} else {
			this.baseUri = baseUri.trim();
		}		
	}

	public void setTitle(String title) {
		this.title = title.trim();
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle.trim();
	}

	public void setVersion(String version) {
		this.version = version.trim();
	}

	public void setAuthor(String author) {
		this.author = author.trim();
	}

	public void setContributor(String contributor) {
		this.contributor = contributor.trim();
	}

	public void setAtomNamespace(String atomNamespace) {
		this.atomNamespace = atomNamespace;
	}

	public void setAtomSchemaLocation(String atomSchemaLocation) {
		this.atomSchemaLocation = atomSchemaLocation;
	}

	public void setDasextensionNamespace(String dasextensionNamespace) {
		this.dasextensionNamespace = dasextensionNamespace;
	}
	
	public void setDasextensionPrefix(String dasextensionPrefix) {
		this.dasextensionPrefix = dasextensionPrefix;
	}	

	public void setDasextensionSchemaLocation(String dasextensionSchemaLocation) {
		this.dasextensionSchemaLocation = dasextensionSchemaLocation;
	}

}
