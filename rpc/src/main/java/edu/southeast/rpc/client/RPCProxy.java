package edu.southeast.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import edu.southeast.rpc.common.RpcRequest;
import edu.southeast.rpc.common.RpcResponse;
import edu.southeast.rpc.registry.ServiceDiscovery;

/**
 * RPC代理
 * @author ifcc
 * @date 2017年10月17日
 * @School SouthEast University
 * @version 1.0
 */
public class RPCProxy {
	
	private String serverAddress;
	//private ServiceDiscovery serviceDiscovery;
	
	private String registryAddress;
	public RPCProxy(String registryAddress) {
		this.registryAddress=registryAddress;
	}
	
	public RPCProxy() {
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T create(String interfaceName) throws Exception{
		final ServiceDiscovery serviceDiscovery = new ServiceDiscovery(registryAddress, interfaceName);
		Class<?> interfaceClass = Class.forName(interfaceName);
		return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class<?>[] {interfaceClass}, new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						//创建RPCRequest，封装被代理类的属性
						RpcRequest request = new RpcRequest();
						request.setRequestId(UUID.randomUUID().toString());
						//得到声明这个方法的业务接口名称
						request.setClassName(method.getDeclaringClass().getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);
						//查找业务
						if (serviceDiscovery!=null) {
							serverAddress=serviceDiscovery.discover();
						}
						//获取服务的地址
						String[] array=serverAddress.split(":");
						String host=array[0];
						int port=Integer.parseInt(array[1]);
						//创建netty实现的RPCclient，连接服务端
						RPCClient client = new RPCClient(host, port);
						//通过netty向服务端发送请求
						RpcResponse response = client.send(request);
						//返回信息
						if (response.isError()) {
							throw response.getError();
						}else{
							return response.getResult();
						}
					}
				});
	}
}
