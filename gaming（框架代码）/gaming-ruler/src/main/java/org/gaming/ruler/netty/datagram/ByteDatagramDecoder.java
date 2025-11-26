/**
 * 
 */
package org.gaming.ruler.netty.datagram;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author YY
 *
 */
public class ByteDatagramDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte[] content = new byte[in.readableBytes()];
		in.readBytes(content);
		out.add(content);
	}
}
