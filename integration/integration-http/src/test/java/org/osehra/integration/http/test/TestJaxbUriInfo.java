package org.osehra.integration.http.test;

import org.osehra.integration.core.transformer.TransformerException;
import org.osehra.integration.http.uri.MultiValuedMap;
import org.osehra.integration.http.uri.MultiValuedMap.Entry;
import org.osehra.integration.http.uri.UriInfo;
import org.osehra.integration.test.spring.CustomProfileValueSource;
import org.osehra.integration.util.JaxbUtil;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {})
@TestExecutionListeners(value = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
@IfProfileValue(name = "test-groups", values = { "integration",  "marshal", "all" })
public class TestJaxbUriInfo {

	@Resource(name = "jaxbUriTransformer")
	JaxbUtil jaxbUtil;

	@Test
	public void testJaxbGeneration() throws JAXBException, TransformerException {
		UriInfo uriInfo = new UriInfo();
		uriInfo.setAbsolutePathExt("someAbsolutePath/anotherPath");
		uriInfo.setBaseUriExt("http://localhost:7171");
		uriInfo.setPathExt("http://localhost:7171/path/123");
		uriInfo.setRequestUriExt("http://localhost:7171/path/123");
		final Entry e1 = new Entry();
		e1.setKey("pathParameterKey1");
		e1.getValue().add("Value1");
		e1.getValue().add("Value2");
		final Entry e2 = new Entry();
		e2.setKey("pathParameterKey2");
		e2.getValue().add("Value1");
		e2.getValue().add("Value2");
		final MultiValuedMap pathParameters = new MultiValuedMap();
		pathParameters.getEntry().add(e1);
		pathParameters.getEntry().add(e2);
		uriInfo.setPathParametersExt(pathParameters);

		final Entry e3 = new Entry();
		e3.setKey("queryParameterKey1");
		e3.getValue().add("Value1");
		e3.getValue().add("Value2");
		final Entry e4 = new Entry();
		e4.setKey("queryParameterKey2");
		e4.getValue().add("Value1");
		e4.getValue().add("Value2");
		final MultiValuedMap queryParameters = new MultiValuedMap();
		pathParameters.getEntry().add(e3);
		pathParameters.getEntry().add(e4);
		uriInfo.setQueryParametersExt(queryParameters);

		final Document doc = this.jaxbUtil.marshal(uriInfo);
		// System.out.println(new XMLToString().transform(doc));
		uriInfo = (UriInfo) this.jaxbUtil.unmarshal(doc);
	}
}
