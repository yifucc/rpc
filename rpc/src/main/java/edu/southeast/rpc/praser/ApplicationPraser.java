package edu.southeast.rpc.praser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApplicationPraser extends AbstractSimpleBeanDefinitionParser{
	@Override
	protected Class<?> getBeanClass(Element element) {
		// TODO Auto-generated method stub
		return Object.class;
	}
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		NodeList nodeList = element.getElementsByTagName("rpc:registry");
		System.out.println(nodeList.item(0).getNodeName());
		Element registry=(Element)nodeList.item(0);
		System.out.println(registry.getAttribute("address"));
	}
}
