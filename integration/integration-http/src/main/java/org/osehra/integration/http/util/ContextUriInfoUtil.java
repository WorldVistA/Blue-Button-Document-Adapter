package org.osehra.integration.http.util;

import org.osehra.integration.util.NullChecker;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ContextUriInfoUtil {

	/**
	 * Takes information from inputs and fills in UriInfo values as:
	 * baseUri: http://hostname:port/
	 * path: /pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * absolutePath: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * requestUri: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * 
	 * @param baseUri
	 * @param pathParams
	 * @return
	 * @throws URISyntaxException
	 */
	public static UriInfo createUriInfo(String baseUri,
			final Map<String, String> pathParams)
			throws URISyntaxException {
		
		return ContextUriInfoUtil.createUriInfo(baseUri, null, pathParams, null, null);
	}
	
	/**
	 * Takes information from inputs and fills in UriInfo values as:
	 * baseUri: http://hostname:port/
	 * path: /pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * absolutePath: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * requestUri: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 *  
	 * @param baseUri
	 * @param path
	 * @param pathParams
	 * @return
	 * @throws URISyntaxException
	 */
	public static UriInfo createUriInfo(String baseUri,
			final String path,
			final Map<String, String> pathParams)			
			throws URISyntaxException {
		return ContextUriInfoUtil.createUriInfo(baseUri, path, pathParams, null, null);
	}
	
	/**
	 * Takes information from inputs and fills in UriInfo values as:
	 * baseUri: http://hostname:port/
	 * path: /pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * absolutePath: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * requestUri: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3[/optionalFilename.ext]?queryParamName1=queryParamValue1+queryParamName2=queryParamValue2+queryParamName3=queryParamValue3
	 * 
	 * @param baseUri
	 * @param pathParams
	 * @param queryParams
	 * @return
	 * @throws URISyntaxException
	 */
	public static UriInfo createUriInfo(String baseUri,
			final Map<String, String> pathParams, 
			final Map<String, String> queryParams)
			throws URISyntaxException {
		return ContextUriInfoUtil.createUriInfo(baseUri, null, pathParams, null, queryParams);
	}
	
	/**
	 * Takes information from inputs and fills in UriInfo values as:
	 * baseUri: http://hostname:port/
	 * path: /pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * absolutePath: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * requestUri: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3[/optionalFilename.ext]?queryParamName1=queryParamValue1+queryParamName2=queryParamValue2+queryParamName3=queryParamValue3
	 * 
	 * @param baseUri
	 * @param path
	 * @param pathParams
	 * @param queryParams
	 * @return
	 * @throws URISyntaxException
	 */
	public static UriInfo createUriInfo(String baseUri,
			final String path,
			final Map<String, String> pathParams, 
			final Map<String, String> queryParams)
			throws URISyntaxException {
		return ContextUriInfoUtil.createUriInfo(baseUri, path, pathParams, null, queryParams);
	}
	
	/**
	 * Takes information from inputs and fills in UriInfo values as:
	 * baseUri: http://hostname:port/
	 * path: /pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * absolutePath: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3/[optionalFilename.ext]
	 * requestUri: http://hostname:port/pathParamValue1/pathParamValue2/pathParamValue3[/optionalFilename.ext]?queryParamName1=queryParamValue1+queryParamName2=queryParamValue2+queryParamName3=queryParamValue3
	 * 
	 * @param baseUri
	 * @param pathP
	 * @param pathParams
	 * @param queryP
	 * @param queryParams
	 * @return
	 * @throws URISyntaxException
	 */
	public static UriInfo createUriInfo(String baseUri,
			final String pathP,
			final Map<String, String> pathParams, 
			final String queryP,
			final Map<String, String> queryParams)
			throws URISyntaxException {
		
		final org.osehra.integration.http.uri.UriInfo uriInfo 
			= new org.osehra.integration.http.uri.UriInfo();
		
		// set baseUri
		String baseUriLocal = baseUri;
		if (!baseUriLocal.endsWith("/")) {
			baseUriLocal = baseUriLocal + "/";
		}	
		final URI uri = new URI(baseUriLocal);
		uriInfo.setBaseUri(uri);		

		// transform the pathParams values into an MultivaluedMap type 
		// and a StringBuilder type, and then set into uriInfo as 
		// pathParameters and path respectively 	
		StringBuilder pathSb = new StringBuilder();
		if(NullChecker.isNotEmpty(pathParams)) {			
			MultivaluedMap<String, String> pathParametersMvm = new MultivaluedMapImpl();		
			for (final Entry<String, String> entry : pathParams.entrySet()) {							
				pathParametersMvm.add(entry.getKey(), entry.getValue());				
			}		
			uriInfo.setPathParameters(pathParametersMvm);	
			// construct the path
			if(pathParams.containsKey("aa")) {				
				pathSb.append(pathParams.get("aa"));
				if(pathParams.containsKey("pid")) {					
					pathSb.append("/"+pathParams.get("pid"));
					if(pathParams.containsKey("profile")) {
						pathSb.append("/"+pathParams.get("profile"));
						if(pathParams.containsKey("domain")) {
							pathSb.append("/"+pathParams.get("domain"));	
							if(pathParams.containsKey("speciality")) {
								if(pathParams.containsKey("childDomain")) {
									pathSb.append("/"+pathParams.get("childDomain"));
								}
								pathSb.append("/"+pathParams.get("speciality"));
							}	
						}
						if(pathParams.containsKey("documentUniqueId")) {
							pathSb.append("/");
							boolean haveLongerFilename = false;
							if(pathParams.containsKey("homeCommunityId") && pathParams.containsKey("remoteRepositoryId")) {
								pathSb.append(pathParams.get("homeCommunityId"));							
								pathSb.append("_"+pathParams.get("remoteRepositoryId"));								
								haveLongerFilename = true;
							}
							if(haveLongerFilename) {
								pathSb.append("_");
							}
							pathSb.append(pathParams.get("documentUniqueId"));
							if(pathParams.containsKey("fileExtension")) {
								pathSb.append("."+pathParams.get("fileExtension"));
							}	
						}		
					}										
				}				
			}			
		}	
		String finalPath = null;		
		if(NullChecker.isNotEmpty(pathP)) {
			finalPath = pathP;
			if(finalPath.startsWith("/")) {
				finalPath = finalPath.replaceFirst("/", "");
			}
			if(!ContextUriInfoUtil.containsFilename(finalPath)) {
				if(finalPath.endsWith("/")) {					
					finalPath = finalPath.substring(0, finalPath.length()-1); 
				}	
			}			
		} else {
			finalPath = pathSb.toString();
		}		
		// set path
		String pathToSetAsPath = null;
		if(ContextUriInfoUtil.containsFilename(finalPath)) {
			pathToSetAsPath = finalPath;
		} else {
			pathToSetAsPath = finalPath + "/";
		}		
		uriInfo.setPath("/" + pathToSetAsPath);
		
		// set absolutePath
		String absolutePath = "";
		if(ContextUriInfoUtil.containsFilename(finalPath)) {
			absolutePath = baseUriLocal + finalPath;
		} else {
			if(NullChecker.isNotEmpty(finalPath)) {
				absolutePath = baseUriLocal + finalPath + "/";
			} else {
				absolutePath = baseUriLocal;
			}
			
		}
		uriInfo.setAbsolutePath(new URI(absolutePath));		
		
		// transform the queryParams values into an MultivaluedMap type 
		// and a StringBuilder type, setting the MultivaluedMap into 
		// uriInfo as queryParameters, and setting the StringBuilder 
		// up to be used in requestUri  
		StringBuilder querySb = null;		
		if(NullChecker.isNotEmpty(queryParams)) {
			querySb = new StringBuilder();
			final int numberOfQueryParams = queryParams.size();
			int currentQueryParamPosition = 0;			
			MultivaluedMap<String, String> queryParametersMvm = new MultivaluedMapImpl();
			for (final Entry<String, String> entry : queryParams.entrySet()) {
				String queryKey = entry.getKey();
				String queryValue = entry.getValue();				
				queryParametersMvm.add(queryKey, queryValue);
				querySb.append(queryKey+"="+queryValue);
				if(currentQueryParamPosition!=(numberOfQueryParams-1)) {
					querySb.append("&");
				}
				currentQueryParamPosition++;
			}		
			uriInfo.setQueryParameters(queryParametersMvm);
		}
		
		// set requestUri
		String finalQuery = null;
		if(NullChecker.isNotEmpty(queryP)) {		
			finalQuery = queryP;			
			if(!finalQuery.startsWith("?")) {
				finalQuery = "?" + finalQuery;
			}			
		} else {
			if(NullChecker.isNotEmpty(querySb)) {
				finalQuery = "?" + querySb.toString();
			}
		}		
		URI requestUri = null;
		if(NullChecker.isEmpty(finalQuery)) {
			if(ContextUriInfoUtil.containsFilename(finalPath)) {
				requestUri = new URI(baseUriLocal + finalPath);
			} else {
				if(NullChecker.isNotEmpty(finalPath)) {
					requestUri = new URI(baseUriLocal + finalPath + "/");
				} else {
					requestUri = new URI(baseUriLocal);
				}
			}
		} else {
			requestUri = new URI(baseUriLocal + finalPath + finalQuery);
		}
		uriInfo.setRequestUri(requestUri);	
				
		return uriInfo;
	}
	
	private static boolean containsFilename(String str) {
		boolean answer = false;
	    int i = str.lastIndexOf(".");
	    if ((i != -1) && (i != (str.length() - 1))  && (i != (str.length() - 2)) && (i != (str.length() - 3)) && (i == (str.length() - 4))) {
	    	answer = true;
	    }
	    return answer;
	}

}
