package org.osehra.integration.feed.handler;

import org.osehra.integration.util.GregorianDateUtil;
import org.osehra.integration.util.NullChecker;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.model.Feed;
import org.springframework.beans.factory.annotation.Required;

/**
 *  A class which handles all creation and manipulation 
 *  involving the feed::dasextension element. The 
 *  dasextension element is formed in accord with the 
 *  dasextension.xsd XML schema.
 *  
 * @author John W. May
 *
 */
public class DasExtensionUtil {
	
	/**
	 * The DAS dasextension elements' definition namespace 
	 */
	private String dasextensionNamespace;	
	
	/**
	 * Creates a default DAS dasextention Atom Feed with the site and error 
	 * values from an Http-related Exception. 
	 * 
	 * @param clientRequestUriInfo
	 * @param producerUri
	 * @param siteHttpStatus
	 * @param errorMessage
	 * @param componentName
	 * @param componentId
	 * @return
	 * @throws MalformedURLException
	 */
	public Feed getDefaultFeedForHttpException(Feed defaultFeed, final org.osehra.integration.http.uri.UriInfo clientRequestUriInfo, 
			final URI producerUri, final int siteHttpStatus, final String errorMessage, 
			final String componentName, final String componentId)  
					throws MalformedURLException {
				
		String producerUrl = "";			
		if (NullChecker.isNotEmpty(producerUri)) {			
			producerUrl = producerUri.toURL().toString();
		}
		
		this.addErrorDasExtension(defaultFeed, componentName, componentId, siteHttpStatus, 
				clientRequestUriInfo, errorMessage, "", new Integer(siteHttpStatus).toString(), 
				errorMessage, producerUrl);
		
		return defaultFeed;		
	}
	
	public Feed getDefaultFeedForInternalException(Feed defaultFeed, final String errorMessage, final String componentName, final String componentId) {
		
		int siteHttpStatus = 500;
		
		this.addErrorDasExtension(defaultFeed, componentName, componentId, siteHttpStatus, 
				null, errorMessage, "", new Integer(siteHttpStatus).toString(), 
				errorMessage, componentName);
		
		return defaultFeed;
	}
	
	/**
	 * Adds a new dasextension element in the given feed parameter, 
	 * which contains the given siteName, siteId, and an HTTP 
	 * success code of "200" for this site in the 
	 * feed::dasextension::query::sites::site Element.
	 * 
	 * @param feed - mandatory - must contain a valid Atom feed
	 * @param siteName - mandatory
	 * @param siteId - optional
	 * @param requestUriInfo - optional
	 * @return
	 */
	public Feed addSuccessDasExtension(Feed feed, final String siteName, final String siteId, 
			final javax.ws.rs.core.UriInfo requestUriInfo) {				
		if(NullChecker.isNotEmpty(feed)) {		
			// add on the base dasextension
			feed = this.addEmptyDasExtension(feed);
			
			// add on the path parameters
			feed = this.addPathParameters(feed, requestUriInfo);
			
			// add on the query parameters
			feed = this.addQueryParameters(feed, requestUriInfo);
			
			// add on the site element
			final int successHttpStatus = 200; // SUCCESS Http Status code
			feed = this.addSite(feed, siteName, siteId, 
					successHttpStatus, "", "", "");		
		}		
		return feed;
	}
	
	/**
	 * Adds a new feed::dasextension element into the given feed 
	 * parameter, which contains the given parameter 
	 * values within the dasextension Element, and 
	 * which includes the optional 
	 * feed::dasextension::query::errors::error Element.	
	 * 
	 * @param feed - mandatory - must contain a valid Atom feed
	 * @param siteName - mandatory
	 * @param siteId - optional	  
	 * @param siteHttpStatus - mandatory
	 * @param requestUriInfo - optional
	 * @param siteErrorMessage - optional
	 * @param errorSeverity - optional
	 * @param errorCode - optional
	 * @param errorValue - optional
	 * @param errorLocation - optional
	 * @return
	 */
	public Feed addErrorDasExtension(Feed feed, final String siteName, final String siteId, 
			final int siteHttpStatus, final javax.ws.rs.core.UriInfo requestUriInfo, 
			final String siteErrorMessage, final String errorSeverity, final String errorCode, 
			final String errorValue, final String errorLocation) {		
		if(NullChecker.isNotEmpty(feed)) { 		
			// add on the base dasextension
			feed = this.addEmptyDasExtension(feed);
			
			// add on the path parameters
			feed = this.addPathParameters(feed, requestUriInfo);
			
			// add on the query parameters
			feed = this.addQueryParameters(feed, requestUriInfo);
			
			// add on the site element
			feed = this.addSite(feed, siteName, siteId, 
					siteHttpStatus, "", "", siteErrorMessage);
			
			// add on the error element
			feed = this.addError(feed, errorSeverity, errorCode, errorValue, errorLocation);
		}		
		return feed;
	}	
	
	/**
	 * Places the org.osehra.integration.http.uri.UriInfo uriInfo instance's path 
	 * parameter values into the DAS dasextension Atom Feed
	 * at feed::dasextension::path::parameters.
	 * Notes: The feed parameter must contain a constructed DAS dasextension 
	 * Atom Feed.
	 * This method will remove any existing 
	 * feed::dasextension::path::parameters::entry elements before adding 
	 * the new entries from uriInfo.
	 * 
	 * @param feed - mandatory - must contain a valid feed::dasextension Element
	 * @param uriInfo - mandatory
	 * @return
	 */
	public Feed addPathParameters(Feed feed, final javax.ws.rs.core.UriInfo uriInfo) {		
		if(NullChecker.isNotEmpty(feed)) {			
			// get reference to the dasextension element in feed	
			ExtensibleElement dasExt = feed.getExtension(
					new QName(this.dasextensionNamespace, "dasextension"));			
			if (NullChecker.isNotEmpty(dasExt)) {				
				// check for a UriInfo to input
				if (NullChecker.isNotEmpty(uriInfo) && !(uriInfo.getPathParameters().isEmpty()) ) {
					
					ExtensibleElement parameters = this.getPathParameters(dasExt);
					
					// discard any existing path dasextension::path::parameter::entry elements					
					List<Element> pathParameterEntries = parameters.getElements();
					if(NullChecker.isNotEmpty(pathParameterEntries)) {
						for(Element curPathParameterEntry: pathParameterEntries) {
							curPathParameterEntry.discard();
						}
					}
					
					// add the dasextension::path::parameter::entry elements from uriInfo
					QName entryQname = new QName(this.dasextensionNamespace, "entry");
					QName keyQname = new QName(this.dasextensionNamespace, "key");
					QName valueQname = new QName(this.dasextensionNamespace, "value");
					
					MultivaluedMap<String, String> pathParametersExt = uriInfo.getPathParameters();
					Set<Map.Entry<String, List<String>>> pathParamsEntries 
						= pathParametersExt.entrySet();
					for (Map.Entry<String, List<String>> pathParameterEntry: pathParamsEntries) {
						ExtensibleElement entry = parameters.addExtension(entryQname);
						ExtensibleElement key = entry.addExtension(keyQname);	
						key.setText(pathParameterEntry.getKey());
						for(String entryValue: pathParameterEntry.getValue()) {
							ExtensibleElement value = entry.addExtension(valueQname);
							value.setText(entryValue);
						}
					}
				}
			}
		}		
		return feed;
	}
	
	/**
	 * Adds the given dasextension::path::parameters Element into 
	 * the given feed.
	 * Notes: This method will merge the given pathParameters with 
	 * any existing path::parameters in the given feed.
	 * 
	 * @param feed - mandatory - must contain a valid feed::dasextension Element 
	 * @param pathParameters - mandatory - must contain a valid and non-empty 
	 * 		dasextension::path::parameters Element.
	 * @return
	 */
	private Feed addPathParameters(Feed feed, final ExtensibleElement pathParameters) {		
		if(NullChecker.isNotEmpty(feed)) {
			if(NullChecker.isNotEmpty(pathParameters)) {
				// get reference to the dasextension element in feed	
				ExtensibleElement dasExt = feed.getExtension(
						new QName(this.dasextensionNamespace, "dasextension"));		
				if (NullChecker.isNotEmpty(dasExt)) {					
					List<Element> pathParametersEntries = pathParameters.getElements();
					if(NullChecker.isNotEmpty(pathParametersEntries)) {
						// get or create the new dasextension::path::parameters Element
						ExtensibleElement pathParametersFeed = this.getPathParameters(dasExt);
						// add in the dasextension::path::parameters::entry Elements 
						for(Element curPathParameterEntry: pathParametersEntries) {
							pathParametersFeed.addExtension(curPathParameterEntry);
						}
					}
				}
			}
		}
		return feed;
	}
	
	/**
	 * Places the org.osehra.integration.http.uri.UriInfo uriInfo instance's query 
	 * parameter values into the DAS dasextension Atom Feed
	 *  at feed::dasextension::query::parameters.
	 * Notes: The feed parameter must contain a constructed DAS dasextension 
	 * Atom Feed.
	 * This method will remove any existing 
	 * feed::dasextension::query::parameters::entry elements before adding 
	 * the new entries from uriInfo.
	 * 
	 * @param feed -mandatory - must contain a valid feed::dasextension Element
	 * @param uriInfo - mandatory
	 * @return
	 */
	public Feed addQueryParameters(Feed feed, final javax.ws.rs.core.UriInfo uriInfo) {		
		if(NullChecker.isNotEmpty(feed)) {			
			// get reference to the dasextension element in feed		
			ExtensibleElement dasExt = feed.getExtension(
					new QName(this.dasextensionNamespace, "dasextension"));		
			if (NullChecker.isNotEmpty(dasExt)) {				
				// check for a UriInfo to input
				if (NullChecker.isNotEmpty(uriInfo) && !(uriInfo.getQueryParameters().isEmpty()) ) {
					// get or create reference to the dasextension::query::parameters element in feed
					ExtensibleElement parameters = this.getQueryParameters(dasExt);
					
					// discard any existing path dasextension::path::parameter::entry elements					
					List<Element> queryParameterEntries = parameters.getElements();
					if(NullChecker.isNotEmpty(queryParameterEntries)) {
						for(Element curQueryParameterEntry: queryParameterEntries) {
							curQueryParameterEntry.discard();
						}
					}
					
					// add the dasextension::query::parameters::entry elements from uriInfo
					QName entryQname = new QName(this.dasextensionNamespace, "entry");
					QName keyQname = new QName(this.dasextensionNamespace, "key");
					QName valueQname = new QName(this.dasextensionNamespace, "value");
					
					MultivaluedMap<String, String> queryParametersExt = uriInfo.getQueryParameters();
					Set<Map.Entry<String, List<String>>> queryParamsEntries 
						= queryParametersExt.entrySet();
					for (Map.Entry<String, List<String>> queryParameterEntry: queryParamsEntries) {
						ExtensibleElement entry = parameters.addExtension(entryQname);
						ExtensibleElement key = entry.addExtension(keyQname);
						key.setText(queryParameterEntry.getKey());
						for(String entryValue: queryParameterEntry.getValue()) {
							ExtensibleElement value = entry.addExtension(valueQname);
							value.setText(entryValue);
						}
					}
				}
			}
		}		
		return feed;
	}
	
	/**
	 * Adds the given dasextension::query::parameters Element into 
	 * the given feed.
	 * Note: This method will merge the given queryParameters with 
	 * any existing query::parameters in the given feed.
	 * 
	 * @param feed - mandatory - must contain a valid feed::dasextension Element 
	 * @param queryParameters - mandatory - must contain a valid and non-empty 
	 * 		dasextension::query::parameters Element.
	 * @return
	 */
	private Feed addQueryParameters(Feed feed, final ExtensibleElement queryParameters) {		
		if(NullChecker.isNotEmpty(feed)) {
			if(NullChecker.isNotEmpty(queryParameters)) {
				// get reference to the dasextension element in feed	
				ExtensibleElement dasExt = feed.getExtension(
						new QName(this.dasextensionNamespace, "dasextension"));		
				if (NullChecker.isNotEmpty(dasExt)) {				
					List<Element> queryParametersEntries = queryParameters.getElements();
					if(NullChecker.isNotEmpty(queryParametersEntries)) {
						// get or create the new dasextension::path::parameters Element
						ExtensibleElement queryParametersFeed = this.getQueryParameters(dasExt);
						// add in the dasextension::path::parameters::entry Elements 
						for(Element curQueryParameterEntry: queryParametersEntries) {
							queryParametersFeed.addExtension(curQueryParameterEntry);
						}
					}
				}
			}
		}
		return feed;
	}
	
	/**
	 * Adds a single site element into the DAS dasextension Atom Feed.
	 *  i.e. adds one of the following inside of feed::dasextension::query::sites:
	 * 		<site>
	 *			<id></id>
	 *			<name></name>
	 *			<expectedCount></expectedCount>
	 *			<retrievedCount></retrievedCount>
	 *			<status></status>
	 *			<error></error>
	 *		</site>
	 *  Notes: The feed parameter must contain a constructed DAS dasextension 
	 *   Atom Feed.
	 *  An existing site element with the  same site::name value as the given 
	 *   siteName value will be removed before the new site element is added. 
	 *  
	 * @param feed - mandatory - must contain a valid feed::dasextension Element	 
	 * @param siteName - mandatory
	 * @param siteId - optional
	 * @param siteHttpStatus - mandatory
	 * @param expectedCount - optional
	 * @param retrievedCount - optional	 
	 * @param siteErrorMessage - optional
	 * @return	 
	 */
	public Feed addSite(Feed feed, final String siteName, final String siteId, 
			final int siteHttpStatus, final String expectedCount, 
			final String retrievedCount, final String siteErrorMessage) {		
		if(NullChecker.isNotEmpty(feed)) {			
			// get reference to the dasextension element in feed
			ExtensibleElement dasExt = feed.getExtension(
					new QName(this.dasextensionNamespace, "dasextension"));		
			if (NullChecker.isNotEmpty(dasExt)) {				
				// get reference to the dasextension::query::sites element in feed
				ExtensibleElement sites = this.getSites(dasExt);
				
				// search for any existing site children by site::name value with 
				// the new siteName, and if one is found, remove it
				List<Element> siteElements = sites.getElements();
				for(Element curSiteElement: siteElements) {
					List<Element> siteChildElements = curSiteElement.getElements();
					for(Element curSiteChildElement: siteChildElements) {
						if("name".equals(curSiteChildElement.getQName().getLocalPart())) {
							if(siteName.equals(curSiteChildElement.getText())) {
								curSiteElement.discard();
							}
						}
					}
				}				
				
				// add the dasextension::query::sites::site element
				QName siteQname = new QName(this.dasextensionNamespace, "site");
				ExtensibleElement site = sites.addExtension(siteQname);
				if(NullChecker.isNotEmpty(siteId)) {
					QName idQname = new QName(this.dasextensionNamespace, "id");
					ExtensibleElement idElement = site.addExtension(idQname);
					idElement.setText(siteId); 
				}
				QName nameQname = new QName(this.dasextensionNamespace, "name");
				ExtensibleElement nameElement = site.addExtension(nameQname);
				nameElement.setText(siteName); 
				if(NullChecker.isNotEmpty(expectedCount)) {
					QName expectedCountQname = new QName(this.dasextensionNamespace, "expectedCount");
					ExtensibleElement expectedCountElement = site.addExtension(expectedCountQname); 
					expectedCountElement.setText(expectedCount);
				}
				if(NullChecker.isNotEmpty(retrievedCount)) {
					QName retrievedCountQname = new QName(this.dasextensionNamespace, "retrievedCount");
					ExtensibleElement retrievedCountElement = site.addExtension(retrievedCountQname);
					retrievedCountElement.setText(retrievedCount);
				}
				QName statusQname = new QName(this.dasextensionNamespace, "status");
				ExtensibleElement statusElement = site.addExtension(statusQname);
				String siteStatus = new Integer(siteHttpStatus).toString();
				statusElement.setText(siteStatus);
				if(NullChecker.isNotEmpty(siteErrorMessage)) {
					QName errorQname = new QName(this.dasextensionNamespace, "error");
					ExtensibleElement siteErrorElement = site.addExtension(errorQname);
					siteErrorElement.setText(siteErrorMessage); 	
				}
			}
		}		
		return feed;
	}
	
	/**
	 * Adds a single site element into the given DAS dasextension Atom Feed
	 * at feed::dasextension::query::sites.
	 * 
	 * @param feed
	 * @param site - mandatory - must contain a valid 
	 * 		dasextension::query::sites::site Element
	 * @return
	 */
	private Feed addSite(Feed feed, final ExtensibleElement site) {		
		if(NullChecker.isNotEmpty(feed)) {		
			// get reference to the dasextension element in feed
			ExtensibleElement dasExt = feed.getExtension(
					new QName(this.dasextensionNamespace, "dasextension"));		
			if (NullChecker.isNotEmpty(dasExt)) {			
				// get feed::dasextension::query::sites and add the new 
				//	site into it in the feed
				ExtensibleElement sites = this.getSites(dasExt);			
				sites.addExtension(site);		
			}
		}		
		return feed;
	}
	
	/**
	 * Adds a single error element into the DAS dasextension Atom Feed,
	 *  i.e. adds one of the following inside of feed::dasextension::query::errors: 
 	 *		<error>
	 *			<errorSeverity></errorSeverity>
	 *			<errorCode></errorCode>
	 *			<errorValue></errorValue>
	 *			<errorLocation></errorLocation>
	 *		</error>
	 * Note: The feed parameter must contain a constructed DAS dasextension Atom Feed.
	 * 
	 * @param feed - mandatory - must contain a valid feed::dasextension Element 
	 * @param errorSeverity - optional
	 * @param errorCode - optional 
	 * @param errorValue - optional
	 * @param errorLocation - optional
	 * @return	 
	 */
	public Feed addError(Feed feed, final String errorSeverity, final String errorCode, 
			final String errorValue, final String errorLocation) {		
		if(NullChecker.isNotEmpty(feed)) {			
			// get reference to the dasextension element in feed
			ExtensibleElement dasExt = feed.getExtension(
					new QName(this.dasextensionNamespace, "dasextension"));		
			if (NullChecker.isNotEmpty(dasExt)) {	
				// get reference to the dasextension::query::errors element in feed
				// -- if errors doesn't exist, this method will add it
				ExtensibleElement errors = this.getErrors(dasExt);
				
				// add the dasextension::query::errors::error element
				QName errorQname = new QName(this.dasextensionNamespace, "error");
				ExtensibleElement errorsError = errors.addExtension(errorQname); 
				if(NullChecker.isNotEmpty(errorSeverity)) {
					QName errorSeverityQname = new QName(
							this.dasextensionNamespace, "errorSeverity");
					ExtensibleElement errorSeverityElement 
						= errorsError.addExtension(errorSeverityQname);
					errorSeverityElement.setText(errorSeverity);
				}
				if(NullChecker.isNotEmpty(errorCode)) {
					QName errorCodeQname = new QName(
							this.dasextensionNamespace, "errorCode");
					ExtensibleElement errorCodeElement 
						= errorsError.addExtension(errorCodeQname);
					errorCodeElement.setText(errorCode);
				}
				if(NullChecker.isNotEmpty(errorValue)) {
					QName errorValueQname = new QName(
							this.dasextensionNamespace, "errorValue");
					ExtensibleElement errorValueElement 
						= errorsError.addExtension(errorValueQname);
					errorValueElement.setText(errorValue); 
				}
				if(NullChecker.isNotEmpty(errorLocation)) {
					QName errorLocationQname = new QName(
							this.dasextensionNamespace, "errorLocation");
					ExtensibleElement errorLocationElement 
						= errorsError.addExtension(errorLocationQname);
					errorLocationElement.setText(errorLocation);
				}
			}
		}		
		return feed;
	}	
	
	/**
	 * Adds a single error element into the DAS dasextension Atom Feed
	 * at feed::dasextension::query::errors.
	 * 
	 * @param feed - mandatory - must contain a valid feed::dasextension Element
	 * @param errorsError - mandatory - must contain a valid 
	 * 		dasextension::query::errors::error Element
	 * @return
	 */
	private Feed addError(Feed feed, final ExtensibleElement errorsError) {		
		if(NullChecker.isNotEmpty(feed)) {		
			// get reference to the dasextension element in feed
			ExtensibleElement dasExt = feed.getExtension(
					new QName(this.dasextensionNamespace, "dasextension"));		
			if (NullChecker.isNotEmpty(dasExt)) {			
				// get feed::dasextension::query::errors and add the new 
				//   error into it in the feed
				ExtensibleElement errors = this.getErrors(dasExt);			
				errors.addExtension(errorsError);		
			}
		}		
		return feed;
	}
	
	/**
	 * Merge the elements in the given dasextensionToMerge Element into 
	 * the feed::dasextension element in the given feed.
	 * Note: The dasextension::path::parameters and 
	 * dasextension::query::parameters values in the given dasextensionToMerge 
	 * Element will only be merged into the given feed if the 
	 * given feed does not already have these Elements. 
	 * 
	 * @param feed - mandatory - must contain a valid feed::dasextension Element
	 * @param extensionToMerge - mandatory - must contain a valid dasextension Element
	 * @return
	 */
	public Feed mergeDasExtension(Feed feed, final Element dasextensionToMerge) {
		
		if(NullChecker.isNotEmpty(feed)) {		
			if(NullChecker.isNotEmpty(dasextensionToMerge)) {
				
				QName mergeExtQname = dasextensionToMerge.getQName();
				String mergeExtTagName = mergeExtQname.getLocalPart();			
				
				// if dasextensionToMerge isn't a dasextension, ignore it	
				if("dasextension".equals(mergeExtTagName)) {					
					ExtensibleElement feedDasExt = feed.getExtension(
							new QName(this.dasextensionNamespace, "dasextension"));	
					
					if(NullChecker.isNotEmpty(feedDasExt)) {
						// feed contains an existing dasextension, so merge other dasextensionToMerge with it:										
						
						// merge dasextension::path::parameters if none are already in feed					
						boolean feedPathParametersExists = this.pathParametersExists(feedDasExt);
						boolean mergePathParametersExists 
							= this.pathParametersExists((ExtensibleElement)dasextensionToMerge);
						if(false == feedPathParametersExists) {
							if(mergePathParametersExists) {								
								ExtensibleElement mergePathParameters 
									= this.getPathParameters((ExtensibleElement)dasextensionToMerge);
								feed = this.addPathParameters(feed, mergePathParameters);								
							}							
						}
						
						// merge dasextension::query::parameters if none are already in feed					
						boolean feedQueryParametersExists = this.queryParametersExists(feedDasExt);
						boolean mergeQueryParametersExists 
							= this.queryParametersExists((ExtensibleElement)dasextensionToMerge);
						if(false == feedQueryParametersExists) {
							if(mergeQueryParametersExists) {								
								ExtensibleElement mergeQueryParameters 
									= this.getQueryParameters((ExtensibleElement)dasextensionToMerge);
								feed = this.addQueryParameters(feed, mergeQueryParameters);								
							}							
						}
						
						// collect all of the current dasextension sites::site elements
						List<Element> siteElements = new ArrayList<Element>();
						ExtensibleElement mergeSites 
							= this.getSites((ExtensibleElement)dasextensionToMerge);
						if(NullChecker.isNotEmpty(mergeSites)) {
							siteElements.addAll(mergeSites.getElements()); 
						}
						
						// add in the site elements into the feed's dasextension
						for(final Element siteElement : siteElements) {				
							feed = this.addSite(feed, (ExtensibleElement)siteElement);
						}
						
						// collect all of the merge dasextension errors::error elements 
						List<Element> errorElements = new ArrayList<Element>();
						ExtensibleElement mergeErrors 
							= this.getErrors((ExtensibleElement)dasextensionToMerge);					
						if(NullChecker.isNotEmpty(mergeErrors)) {
							errorElements.addAll(mergeErrors.getElements()); 
						}
						
						// add in the error elements into the feed's dasextension
						for(final Element errorElement : errorElements) {				
							feed = this.addError(feed, (ExtensibleElement)errorElement);
						}							
						
					} else {						
						// feed has no existing dasextension element, so add in dasextensionToMerge
						feed.addExtension(dasextensionToMerge);						
					}
				}
			}
		}		
		return feed;	
	}

	/**
	 * Add an new empty feed::dasextension element with only 
	 * a completed management element to the given Feed.
	 * Sets the responseTime value to now.
	 * Note: A proper sites::site element must be added to 
	 * the new feed::dasextension element for this new 
	 * feed::dasextension element to be valid.
	 * 
	 * @param feed - mandatory
	 * @return
	 */
	private Feed addEmptyDasExtension(Feed feed) {
		
		// check to make sure there's a dasextension
		QName dasextensionQname = new QName(this.dasextensionNamespace, "dasextension");
		ExtensibleElement dasExt = feed.getExtension(dasextensionQname);		
		if (NullChecker.isEmpty(dasExt)) {
			
			dasExt = feed.addExtension(dasextensionQname);		
		
			// add the dasextension::management element
			QName managementQname = new QName(this.dasextensionNamespace, "management");
			ExtensibleElement management = dasExt.getExtension(managementQname);
			if (NullChecker.isEmpty(management)) {			
				management = dasExt.addExtension(managementQname);
			}
			
			QName responseTimeQname = new QName(this.dasextensionNamespace, "responseTime");
			ExtensibleElement responseTime = management.getExtension(responseTimeQname);
			if (NullChecker.isEmpty(responseTime)) {		
				responseTime = management.addExtension(responseTimeQname);
			}
			
			// set/reset the response time to now
			XMLGregorianCalendar xmlGregCalResponseTime 
				= GregorianDateUtil.getGregorianCalendarByDate(new Date());
			responseTime.setText(xmlGregCalResponseTime.toString()); // TODO: Verify date format
		}		
		return feed;		
	}
	
	/**
	 * Find the current dasextension::path element or create a new one in the 
	 * proper place in the given dasextension element. 
	 * 
	 * @param dasExt - must be a valid dasextension Element
	 * @return
	 */
	private ExtensibleElement getPath(ExtensibleElement dasExt) {
		
		// get or create a reference to the dasextension::path element in feed	
		QName pathQname = new QName(this.dasextensionNamespace, "path");		
		ExtensibleElement path = null;		
		ExtensibleElement firstChild = (ExtensibleElement)dasExt.getFirstChild();					
		if (NullChecker.isNotEmpty(firstChild)) {				
			QName fCQname = firstChild.getQName();
			if ("path".equals(fCQname.getLocalPart())) {
				// path is the firstChild
				path = firstChild;
			} else {				
				// no path, so add it before firstChild
				path = dasExt.addExtension(pathQname, fCQname);
			}					
		} else {
			// no children, so create a "path" element
			path = dasExt.addExtension(pathQname);
		}
		
		return path;
	}
	
	/**
	 * Find the current dasextension::query element or create a new one in the 
	 * proper place in the given dasextension element.
	 *  
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	private ExtensibleElement getQuery(ExtensibleElement dasExt) {
		
		QName queryQname = new QName(this.dasextensionNamespace, "query");	
		// attempt to get query element
		ExtensibleElement query = dasExt.getExtension(queryQname);			
		if (NullChecker.isEmpty(query)) {
			// there is no query element, so create one in the proper place						
			ExtensibleElement firstChild = dasExt.getFirstChild();
			if (NullChecker.isEmpty(firstChild)) {
				// no children, so merely add query
				query = dasExt.addExtension(queryQname);					
			} else {
				QName fCQname = firstChild.getQName();
				if ("path".equals(fCQname.getLocalPart())) {						
					// path is the firstChild, so get the second child					
					ExtensibleElement secondChild = firstChild.getNextSibling();
					if (NullChecker.isEmpty(secondChild)) {
						// no second child, so add query after path
						query = dasExt.addExtension(queryQname);
					} else {
						QName sCQname = secondChild.getQName();					
						// have a second child, so add query after 
						// path element and before second child
						query = dasExt.addExtension(queryQname, sCQname);
					}						
				} else {
					// add query before the first child element
					query = dasExt.addExtension(queryQname, fCQname);
				}					
			}
		}	
		return query;
	}
	
	/**
	 * Find the current dasextension::path::parameters element 
	 * or create a new one in the proper place in the given 
	 * dasextension element.
	 *  
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	private ExtensibleElement getPathParameters(ExtensibleElement dasExt) {
		
		QName parametersQname = new QName(this.dasextensionNamespace, "parameters");
		ExtensibleElement parameters = null;
		
		ExtensibleElement path = this.getPath(dasExt);		
		ExtensibleElement pathFirstChild = path.getFirstChild();			
		if (NullChecker.isNotEmpty(pathFirstChild)) {
			// already have a parameters 
			parameters = pathFirstChild;				
		} else {
			// no parameters, so add one
			parameters = path.addExtension(parametersQname);
		}		
		
		return parameters;
	}
	
	/**
	 * Find the current dasextension::query::parameters element 
	 * or create a new one in the proper place in the given 
	 * dasextension element.
	 *  
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	private ExtensibleElement getQueryParameters(ExtensibleElement dasExt) {
				
		QName parametersQname = new QName(this.dasextensionNamespace, "parameters");
		ExtensibleElement parameters = null;
		
		ExtensibleElement query = this.getQuery(dasExt);
		ExtensibleElement queryFirstChild = query.getFirstChild();		
		if (NullChecker.isEmpty(queryFirstChild)) {			
			// no children in query, so just query::parameters
			parameters = query.addExtension(parametersQname);
						
		} else {			
			
			QName fCQname = queryFirstChild.getQName();
			if ("parameters".equals(fCQname.getLocalPart())) {
				// already have a query::parameters 
				parameters = queryFirstChild;	
			} else {
				// firstQueryChild is not parameters, so add query::parameters before it
				parameters = query.addExtension(parametersQname, fCQname);
			}			
		}		
		return parameters;
	}
	
	/**
	 * Returns true if the the given dasExt contains a 
	 * feed::dasextension::path::parameters element, 
	 * else false.
	 *  
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	private boolean pathParametersExists(ExtensibleElement dasExt) {
		
		boolean pathParametersExists = false;
		
		if (NullChecker.isNotEmpty(dasExt)) {
			QName pathQname = new QName(this.dasextensionNamespace, "path");
			ExtensibleElement path = dasExt.getExtension(pathQname);
			if (NullChecker.isNotEmpty(path)) {								
				QName parametersQname = new QName(this.dasextensionNamespace, "parameters");
				ExtensibleElement parameters = path.getExtension(parametersQname);	
				if (NullChecker.isNotEmpty(parameters)) {
					pathParametersExists = true;						
				}				
			}		
		}		
		return pathParametersExists;
	}
	
	/**
	 * Returns true if the the given dasExt contains a 
	 * feed::dasextension::query::parameters element, 
	 * else false.
	 *  
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	private boolean queryParametersExists(ExtensibleElement dasExt) {
		
		boolean queryParametersExists = false;
		
		if (NullChecker.isNotEmpty(dasExt)) {
			QName queryQname = new QName(this.dasextensionNamespace, "query");
			ExtensibleElement query = dasExt.getExtension(queryQname);
			if (NullChecker.isNotEmpty(query)) {								
				QName parametersQname = new QName(this.dasextensionNamespace, "parameters");
				ExtensibleElement parameters = query.getExtension(parametersQname);	
				if (NullChecker.isNotEmpty(parameters)) {
					queryParametersExists = true;						
				}				
			}		
		}		
		return queryParametersExists;
	}	
	
	/**
	 * Find the current dasextension::query::sites element 
	 * or create a new one in the proper place in the given 
	 * dasextension element.
	 *  
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	public ExtensibleElement getSites(ExtensibleElement dasExt) {
		
		// get reference to the dasextension::query element in feed	
		ExtensibleElement query = this.getQuery(dasExt);
		
		// get reference to the dasextension::query::sites element in feed
		QName sitesQname = new QName(this.dasextensionNamespace, "sites");
		ExtensibleElement sites = query.getExtension(sitesQname); 
		if (NullChecker.isEmpty(sites))	{
			ExtensibleElement queryFirstChild = query.getFirstChild();
			if (NullChecker.isEmpty(queryFirstChild)) {
				// no children in query, so merely add sites element
				sites = query.addExtension(sitesQname);
			} else {
				QName fCQname = queryFirstChild.getQName();
				if ("parameters".equals(fCQname.getLocalPart())) {						
					ExtensibleElement querySecondChild = query.getNextSibling();
					if (NullChecker.isEmpty(querySecondChild)) {
						// no second child in query, so add sites after parameters
						sites = query.addExtension(sitesQname);
					} else {
						// add sites after parameters, and before the second child
						QName sCQname = querySecondChild.getQName();							
						sites = query.addExtension(sitesQname, sCQname);
					}					
				} else {
					// another element besides parameters is first child, 
					//  so add sites before this
					sites = dasExt.addExtension(sitesQname, fCQname);
				}
			}
		}		
		return sites;
	}
	
	/**
	 * Find the current dasextension::query::errors element 
	 * or create a new one in the proper place in the given 
	 * dasextension element.
	 *   
	 * @param dasExt - mandatory - must be a valid dasextension Element
	 * @return
	 */
	public ExtensibleElement getErrors(ExtensibleElement dasExt) {
		
		// get reference to the dasextension::query element in feed				
		ExtensibleElement query = this.getQuery(dasExt);			
		
		// get reference to the dasextension::query::errors element in feed	
		QName errorsQname = new QName(this.dasextensionNamespace, "errors");
		ExtensibleElement errors = query.getExtension(errorsQname);
		if(NullChecker.isEmpty(errors)) {				
			List<Element> queryChildren = query.getElements();
			if (NullChecker.isEmpty(queryChildren)) {
				// no children in query, so merely add errors element
				errors = query.addExtension(errorsQname);
			} else {
				// add errors after the last child							
				errors = query.addExtension(errorsQname);					
			}
		}		
		return errors;
	}

	public String getDasextensionNamespace() {
		return dasextensionNamespace;
	}

	@Required
	public void setDasextensionNamespace(String dasextensionNamespace) {
		this.dasextensionNamespace = dasextensionNamespace;		
	}

}
