package edu.southeast.rpc.praser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.southeast.rpc.client.RPCProxy;

public class DiscoveryPraser extends AbstractSimpleBeanDefinitionParser{
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		return RPCProxy.class;
	}
	
	@Override
	protected void doParse(Element element, ParserContext context, BeanDefinitionBuilder builder) {
		String address = element.getAttribute("address");
		String protocol = element.getAttribute("protocol");
		element.setAttribute("id", "RpcProxy");//确保后面可以引用到这个id的bean对象
		builder.addConstructorArgValue(address);
	}
}
