package edu.southeast.rpc.common;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * RPC 解码器
 * @author ifcc
 * @date 2017年10月13日
 * @School SouthEast University
 * @version 1.0
 */
public class RpcDecoder extends ByteToMessageDecoder{
	
	private Class<?> genericClass;
	
	public RpcDecoder(Class<?> genericClass){
		this.genericClass=genericClass;
	}
	
	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes()<4) {
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (dataLength<0) {
			ctx.close();
		}
		if (in.readableBytes()<dataLength) {
			in.resetReaderIndex();
		}
		//将bytebuf转化成byte[]
		byte[] data=new byte[dataLength];
		in.readBytes(data);
		Object obj = SerializationUtil.deserialize(data, genericClass);
		out.add(obj);
	}

}
