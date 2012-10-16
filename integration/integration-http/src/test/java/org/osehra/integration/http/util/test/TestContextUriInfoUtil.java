package org.osehra.integration.http.util.test;


import org.osehra.integration.http.util.ContextUriInfoUtil;
import org.osehra.integration.test.spring.CustomProfileValueSource;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {})
@TestExecutionListeners(value = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
@IfProfileValue(name = "test-groups", values = { "integration",  "http", "all" })
public class TestContextUriInfoUtil {
	
	@Test
	public void testContextUriInfoUtil1stPassPathparamsOnly() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";
						
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");	

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri,				
				pathParams);
		
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());	
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil1stPassPathparamsPathPOnly() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";

		String pathP = "/2.16.840.1.113883.4.349/1012581676V377802/benefits/integratedCare/careCoordinatorProfiles/";
		
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");	

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri,	
				pathP,
				pathParams);
		
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());	
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil1stPassPathParamsQueryP() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";
						
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");
		
		String queryP = "?zzzzzz=3333333&xxxxx=111111&yyyy=22222";
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("zzzzzz", "3333333");
		queryParams.put("xxxxx", "111111");
		queryParams.put("yyyy", "22222");

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri, 
				null, 
				pathParams, 
				queryP,
				queryParams);
				
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil1stPassPathParamsQueryParams() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";
						
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("queryParamName1", "queryParamValue1");
		queryParams.put("queryParamName2", "queryParamValue2");
		queryParams.put("queryParamName3", "queryParamValue3");

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri,				
				pathParams, 
				queryParams);
		
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil2ndPassPathparamsOnly() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";
						
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");
		pathParams.put("homeCommunityId", "2.16.840.1.113883.4.349.3");		
		pathParams.put("remoteRepositoryId", "3.33.333.3.333333.3.333");		
		pathParams.put("documentUniqueId", "1001");
		pathParams.put("fileExtension", "xml");

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri,				
				pathParams);
		
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());	
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil2ndPassPathparamsPathPOnly() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";

		String pathP = "/2.16.840.1.113883.4.349/1012581676V377802/benefits/integratedCare/careCoordinatorProfiles/2.16.840.1.113883.4.349.3_3.33.333.3.333333.3.333_1001.xml";
		
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");
		pathParams.put("homeCommunityId", "2.16.840.1.113883.4.349.3");		
		pathParams.put("remoteRepositoryId", "3.33.333.3.333333.3.333");		
		pathParams.put("documentUniqueId", "1001");
		pathParams.put("fileExtension", "xml");

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri,	
				pathP,
				pathParams);
		
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());	
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil2ndPassPathParamsQueryP() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";
						
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");
		pathParams.put("homeCommunityId", "2.16.840.1.113883.4.349.3");		
		pathParams.put("remoteRepositoryId", "3.33.333.3.333333.3.333");		
		pathParams.put("documentUniqueId", "1001");
		pathParams.put("fileExtension", "xml");
		
		String queryP = "?zzzzzz=3333333&xxxxx=111111&yyyy=22222";
		
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("zzzzzz", "3333333");
		queryParams.put("xxxxx", "111111");
		queryParams.put("yyyy", "22222");

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri, 
				null, 
				pathParams, 
				queryP,
				queryParams);
				
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());
		System.out.println("");
	}
	
	@Test
	public void testContextUriInfoUtil2nd2ndPassPathParamsQueryParams() throws URISyntaxException {
		
		String dasBaseUri = "http://localhost:port/";
						
		Map<String, String> pathParams = new HashMap<String, String>();
		pathParams.put("aa", "2.16.840.1.113883.4.349");
		pathParams.put("pid", "1012581676V377802");		
		pathParams.put("profile", "benefits");
		pathParams.put("domain", "integratedCare");
		pathParams.put("speciality", "careCoordinatorProfiles");
		pathParams.put("homeCommunityId", "2.16.840.1.113883.4.349.3");		
		pathParams.put("remoteRepositoryId", "3.33.333.3.333333.3.333");		
		pathParams.put("documentUniqueId", "1001");
		pathParams.put("fileExtension", "xml");

		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("queryParamName1", "queryParamValue1");
		queryParams.put("queryParamName2", "queryParamValue2");
		queryParams.put("queryParamName3", "queryParamValue3");

		javax.ws.rs.core.UriInfo contextUriInfo = ContextUriInfoUtil.createUriInfo(
				dasBaseUri,				
				pathParams, 
				queryParams);
		
		System.out.println("baseUri: "+contextUriInfo.getBaseUri());
		System.out.println("path: "+contextUriInfo.getPath());
		System.out.println("absolutePath: "+contextUriInfo.getAbsolutePath());
		System.out.println("requestUri: "+contextUriInfo.getRequestUri());
		System.out.println("");
	}
	
}

