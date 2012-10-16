package org.osehra.das.common.test.bus;

public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
