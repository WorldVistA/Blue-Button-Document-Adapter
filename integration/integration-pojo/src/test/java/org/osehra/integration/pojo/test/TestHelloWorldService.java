package org.osehra.integration.pojo.test;

import org.osehra.integration.core.component.Component;
import org.osehra.integration.core.component.ComponentException;
import org.osehra.integration.test.spring.CustomProfileValueSource;

import javax.annotation.Resource;

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
@ContextConfiguration(locations = { "classpath:org/osehra/integration/pojo/test/helloWorldComponent.xml" })
@TestExecutionListeners(value = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })
@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
@IfProfileValue(name = "test-groups", values = { "integration",  "component", "all" })
public class TestHelloWorldService {

	@Resource(name = "helloWorldComponent")
	Component<String, ?> component;

	@Test
	public void testHelloWorldService() throws ComponentException {
		final Object obj = this.component.processInbound("0");
		System.out.println("Result = " + obj);
	}

}