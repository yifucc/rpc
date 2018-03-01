package edu.southeast.rpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.southeast.rpc.common.RpcDecoder;
import edu.southeast.rpc.common.RpcEncoder;
import edu.southeast.rpc.common.RpcRequest;
import edu.southeast.rpc.common.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 框架的RPC客户端（用于发送RPC请求）
 * @author ifcc
 * @date 2017年10月17日
 * @School SouthEast University
 * @version 1.0
 */
public class RPCClient extends SimpleChannelInboundHandler<RpcResponse>{
	private static final Logger LOGGER=LoggerFactory.getLogger(RPCClient.class);
	
	private String host;
	private int port;
	
	private RpcResponse response;
	
	private final Object obj = new Object();
	
	public RPCClient(String host, int port) {
		this.host=host;
		this.port=port;
	}
	
	/**
	 * 连接服务端，发送消息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public RpcResponse send(RpcRequest request)throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline()
						.addLast(new RpcEncoder(RpcRequest.class))  //OUT  --1
						.addLast(new RpcDecoder(RpcResponse.class)) //IN  --1
						.addLast(RPCClient.this);                   //IN  --2
					
				}
				
			}).option(ChannelOption.SO_KEEPALIVE, true);
			//连接
			ChannelFuture future = bootstrap.connect(host,port).sync();
			//将request对象写入outbundle处理后发出（即RpcEncoder编码器）
			future.channel().writeAndFlush(request).sync();
			// 用线程等待的方式决定是否关闭连接
			// 其意义是：先在此阻塞，等待获取到服务端的返回后，被唤醒，从而关闭网络连接
			synchronized (obj) {
				obj.wait();
			}
			if (response!=null) {
				future.channel().closeFuture().sync();
			}
			return response;
		} finally {
			group.shutdownGracefully();
		}
	}
	
	/**
	 * 读取服务端的返回结果
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
		this.response=response;
		synchronized (obj) {
			obj.notifyAll();
		}
		
	}
	
	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("client caught exception",cause);
		ctx.close();
	}
}
