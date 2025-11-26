/**
 * 
 */
package org.gaming.simulator.netty.datagram;

import java.nio.ByteOrder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author YY
 *
 */
public class ByteDatagramClientChannelInitializer extends ChannelInitializer<Channel> {

	private static final int MAX_FRAME_LENGTH = 64 * 1024 * 1024;
	
	//端序
	private ByteOrder byteOrder;
	
	private ChannelInboundHandlerAdapter handler;

	
	public ByteDatagramClientChannelInitializer(ChannelInboundHandlerAdapter handler) {
		this(ByteOrder.BIG_ENDIAN, handler);
    }
    
    public ByteDatagramClientChannelInitializer(ByteOrder byteOrder, ChannelInboundHandlerAdapter handler) {
    	this.byteOrder = byteOrder;
        this.handler = handler;
    }
    
    @Override
	protected final void initChannel(Channel channel) throws Exception {
		// 添加消息处理器
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new LengthFieldBasedFrameDecoder(byteOrder, MAX_FRAME_LENGTH, 0, 4, 0, 4, true));
		pipeline.addLast(new ByteDatagramDecoder());
		pipeline.addLast(new LengthFieldPrepender(byteOrder, 4, 0, false));
		pipeline.addLast(new ByteDatagramEncoder());
		pipeline.addLast(handler);
	}
}
