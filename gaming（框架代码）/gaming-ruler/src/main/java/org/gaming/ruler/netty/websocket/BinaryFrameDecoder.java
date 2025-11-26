/**
 * 
 */
package org.gaming.ruler.netty.websocket;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author YY
 * 二进制帧的解码器
 */
@Sharable
public class BinaryFrameDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {
	
    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame obj, List<Object> out) throws Exception {
        ByteBuf byteBuf = ((BinaryWebSocketFrame)obj).content();
        byte[] byteMsg = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byteMsg);
		out.add(byteMsg);
    }
}
