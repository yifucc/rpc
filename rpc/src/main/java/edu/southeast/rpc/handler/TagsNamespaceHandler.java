package edu.southeast.rpc.handler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import edu.southeast.rpc.praser.DiscoveryPraser;
import edu.southeast.rpc.praser.ProtocolPraser;
import edu.southeast.rpc.praser.ReferencePraser;
import edu.southeast.rpc.praser.RegistryPraser;
import edu.southeast.rpc.praser.ServicePraser;

public class TagsNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		//registerBeanDefinitionParser("application", new ApplicationPraser());
		registerBeanDefinitionParser("registry", new RegistryPraser());
		registerBeanDefinitionParser("discovery", new DiscoveryPraser());
		registerBeanDefinitionParser("protocol", new ProtocolPraser());
		registerBeanDefinitionParser("reference", new ReferencePraser());
		registerBeanDefinitionParser("service", new ServicePraser());
	}

}
