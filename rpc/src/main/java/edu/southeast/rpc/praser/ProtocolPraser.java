package edu.southeast.rpc.praser;

import java.net.InetAddress;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.southeast.rpc.server.RPCServer;

public class ProtocolPraser extends AbstractSimpleBeanDefinitionParser{
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		return RPCServer.class;
	}
	
	@Override
	protected void doParse(Element element, ParserContext context, BeanDefinitionBuilder builder) {
		try {
			String name = element.getAttribute("name");
			String port = element.getAttribute("port");
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			String serverAddress=hostAddress+":"+port;
			builder.addConstructorArgValue(serverAddress);
			builder.addConstructorArgReference("serviceRegistry");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
