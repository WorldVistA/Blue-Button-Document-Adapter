package org.osehra.integration.pojo.test;

public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello(final String name) {
		return "Hello " + name;
	}
}
