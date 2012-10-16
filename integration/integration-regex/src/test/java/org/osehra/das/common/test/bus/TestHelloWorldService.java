
package org.osehra.das.common.test.bus;

import java.util.logging.Level;

import org.osehra.das.common.component.Component;
import org.osehra.das.common.component.ComponentException;
import org.osehra.das.common.component.ComponentImpl;
import org.osehra.das.common.interceptor.ConsoleInterceptor;
import org.osehra.das.common.interceptor.Interceptor;
import org.osehra.das.common.service.ServiceInvoker;
import org.osehra.das.common.service.pojo.PojoServiceInvoker;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.util.MethodInvoker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:org/osehra/das/common/test/bus/helloWorldComponent.xml"
})
@TestExecutionListeners(value = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class })

public class TestHelloWorldService {

	@Resource(name="helloWorldComponent")
	Component<String, ?> component;


	@Test
	public void testHelloWorldService() throws ComponentException {
		ConsoleInterceptor interceptor = new ConsoleInterceptor();
		interceptor.setLevel(Level.INFO);
		ComponentImpl traditionalComponent = new ComponentImpl();
		traditionalComponent.setInboundInterceptor(interceptor);

		HelloWorldService service = new HelloWorldServiceImpl();
		PojoServiceInvoker serviceInvoker = new PojoServiceInvoker();
		MethodInvoker methodInvoker = new MethodInvoker();
		methodInvoker.setTargetObject(service);
		methodInvoker.setTargetMethod("sayHello");
		serviceInvoker.setMethodInvoker(methodInvoker);
		traditionalComponent.setServiceInvoker(serviceInvoker);

		Object objTraditional = traditionalComponent.processInbound("Test0");
		System.out.println("Result = " + objTraditional);

		Object obj = this.component.processInbound("0");
		System.out.println("Result = " + obj);
	}

}