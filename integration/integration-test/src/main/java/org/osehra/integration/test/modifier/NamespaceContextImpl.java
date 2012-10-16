package org.osehra.integration.test.modifier;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class NamespaceContextImpl implements NamespaceContext {

	private Map<String,String> namespaceMap;

	public NamespaceContextImpl(Map<String,String> map) {
		this.namespaceMap = map;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		String uri = namespaceMap.get(prefix);
		return uri;
	}

	@Override
	public String getPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

}
