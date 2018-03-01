package edu.southeast.rpc.server;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.southeast.rpc.common.RpcRequest;
import edu.southeast.rpc.common.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理具体的业务调用
 * 通过构造时传入的“业务接口及实现”handlerMap，来调用客户端所请求的业务方法
 * 并将业务方法返回值封装成response对象写入下一个handler（即编码handler——RpcEncoder）
 * @author ifcc
 * @date 2017年10月17日
 * @School SouthEast University
 * @version 1.0
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(RpcHandler.class);
	
	private final Map<String,Object> handlerMap;
	
	public RpcHandler(Map<String, Object> handlerMap) {
		this.handlerMap=handlerMap;
	}
	
	/**
	 *  接收消息，处理消息，返回结果
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
		RpcResponse response = new RpcResponse();
		response.setRequestId(request.getRequestId());
		try {
			//根据request来处理具体的业务调用
			Object result = handle(request);
			response.setResult(result);
		} catch (Exception e) {
			response.setError(e);
		}
		//写入outbundle（即RPCEncoder）进行下一步处理（即编码）后发送到channel中给客户端
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * 根据request来处理具体的业务调用
	 * 调用是通过反射的方式来完成
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Object handle(RpcRequest request) throws Exception{
		String className = request.getClassName();
		//得到实现类对象
		Object serviceBean = handlerMap.get(className);
		//得到要调用的方法、参数类型和参数值
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		
		//得到接口类
		Class<?> forName = Class.forName(className);
		//调用实现类对象的指定方法并返回结果
		Method method = forName.getMethod(methodName, parameterTypes);
		return method.invoke(serviceBean, parameters);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("server caught exception",cause);
		ctx.close();
	}
}
