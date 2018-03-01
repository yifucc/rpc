package edu.southeast.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Rpc 编码器
 * @author ifcc
 * @date 2017年10月13日
 * @School SouthEast University
 * @version 1.0
 */
public class RpcEncoder extends MessageToByteEncoder{
	 private Class<?> genericClass;
	 
	 public RpcEncoder(Class<?> genericClass){
		 this.genericClass=genericClass;
	 }

	@Override
	public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		//序列化
		if (genericClass.isInstance(msg)) {
			byte[] data = SerializationUtil.serialize(msg);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
	 
	 
}
