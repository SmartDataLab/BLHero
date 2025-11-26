/**
 * 
 */
package org.gaming.ruler.netty.datagram;

import java.nio.ByteOrder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author YY
 *
 */
public class ByteDatagramChannelInitializer extends ChannelInitializer<Channel> {

	private static final int MAX_FRAME_LENGTH = 64 * 1024 * 1024;
	
	//端序
	private ByteOrder byteOrder;
	
	private ChannelInboundHandlerAdapter handler;
    /**
     * 空闲检测时间
     */
	private int idle;

	
	public ByteDatagramChannelInitializer(ChannelInboundHandlerAdapter handler) {
		this(ByteOrder.BIG_ENDIAN, handler, 0);
    }
	
    public ByteDatagramChannelInitializer(ChannelInboundHandlerAdapter handler, int idle) {
       this(ByteOrder.BIG_ENDIAN, handler, idle);
    }
    
    public ByteDatagramChannelInitializer(ByteOrder byteOrder, ChannelInboundHandlerAdapter handler) {
       this(byteOrder, handler, 0);
    }
    
    public ByteDatagramChannelInitializer(ByteOrder byteOrder, ChannelInboundHandlerAdapter handler, int idle) {
        this.byteOrder = byteOrder;
        this.handler = handler;
        this.idle = idle;
    }
    
    
    @Override
	protected final void initChannel(Channel channel) throws Exception {
		// 添加消息处理器
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new IdleStateHandler(idle, 0, 0));
		pipeline.addLast(new LengthFieldBasedFrameDecoder(byteOrder, MAX_FRAME_LENGTH, 0, 4, 0, 4, true));
		pipeline.addLast(new ByteDatagramDecoder());
		pipeline.addLast(new LengthFieldPrepender(byteOrder, 4, 0, false));
		pipeline.addLast(new ByteDatagramEncoder());
		pipeline.addLast(handler);
	}
}
