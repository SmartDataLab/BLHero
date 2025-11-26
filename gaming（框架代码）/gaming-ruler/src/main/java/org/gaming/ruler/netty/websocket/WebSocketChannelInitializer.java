package org.gaming.ruler.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * WebSocket
 */
public class WebSocketChannelInitializer extends ChannelInitializer<Channel> {
    private SslContext sslCtx;
    private ChannelHandler[] handlers;
    //空闲秒数
    private int idle;

    public WebSocketChannelInitializer(ChannelInboundHandlerAdapter... handlers) {
		this(0, handlers);
    }
    
    public WebSocketChannelInitializer(int idle, ChannelHandler... handlers) {
        this(null, idle, handlers);
    }

    public WebSocketChannelInitializer(SslContext sslCtx, int idle, ChannelHandler... handlers) {
    	this.sslCtx = sslCtx;
        this.idle = idle;
        this.handlers = handlers;
    }

    @Override
    public void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(65536));
        //idle 心跳监测
        pipeline.addLast(new IdleStateHandler(idle, 0, 0));
        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
        pipeline.addLast(new BinaryFrameDecoder());
        pipeline.addLast(new BinaryFrameEncoder());
        pipeline.addLast(handlers);
    }
}
