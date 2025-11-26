/**
 * 
 */
package org.gaming.simulator.netty.websocket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author YY
 * 二进制帧的编码器
 */
@Sharable
public class BinaryFrameEncoder extends MessageToMessageEncoder<byte[]> {

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
		ByteBuf byteBuf = ctx.alloc().buffer(msg.length);
		byteBuf.writeBytes(msg);
		out.add(new BinaryWebSocketFrame(byteBuf));
	}
}
