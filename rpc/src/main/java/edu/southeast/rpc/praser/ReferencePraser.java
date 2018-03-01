package edu.southeast.rpc.praser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.southeast.rpc.registry.ServiceDiscovery;


public class ReferencePraser extends AbstractSimpleBeanDefinitionParser{
	private static String registryAddress;
	public static void setRegistryAddress(String registryAddress) {
		ReferencePraser.registryAddress = registryAddress;
	}
	public static String getRegistryAddress() {
		return registryAddress;
	}
	@Override
	protected Class<?> getBeanClass(Element element) {
		Class<?> className=null;
		try {
			String interfaceName = element.getAttribute("interface");
			className = Class.forName(interfaceName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return className;
	}
	
	@Override
	protected void doParse(Element element, ParserContext context, BeanDefinitionBuilder builder) {
		
		String	interfaceName = element.getAttribute("interface");
		
		//ServiceDiscovery serviceDiscovery = new ServiceDiscovery(registryAddress, interfaceName);
		builder.setFactoryMethod("create");
		//builder.getBeanDefinition().setDependsOn("RpcProxy");
		builder.getBeanDefinition().setFactoryBeanName("RpcProxy");
		builder.addConstructorArgValue(interfaceName);
	}
}
