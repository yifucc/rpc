package edu.southeast.rpc.praser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.southeast.rpc.registry.ServiceRegistry;

public class RegistryPraser extends AbstractSimpleBeanDefinitionParser{
	@Override
	protected Class<?> getBeanClass(Element element) {
		
		return ServiceRegistry.class;
	}
	
	@Override
	protected void doParse(Element element, ParserContext context, BeanDefinitionBuilder builder) {
		String protocol = element.getAttribute("protocol");
		String registryAddress = element.getAttribute("address");
		element.setAttribute("id", "serviceRegistry");//确保RPCServer可以引用到这个id的bean
		builder.addConstructorArgValue(registryAddress);
	}

}
