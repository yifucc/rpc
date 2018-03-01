package edu.southeast.rpc.praser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import edu.southeast.rpc.server.RPCServer;

public class ServicePraser implements BeanDefinitionParser{

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String interfaceName = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		RPCServer.SERVICE_INFO_MAP.put(interfaceName, ref);
		return null;
	}

}
