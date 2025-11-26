package org.gaming.simulator.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContext;

/**
 * WebSocket
 */
public class WebSocketClientChannelInitializer extends ChannelInitializer<Channel> {
    private ChannelHandler[] handlers;
    private SslContext sslContext;

    public WebSocketClientChannelInitializer(ChannelHandler... handlers) {
        this.handlers = handlers;
    }
    
    public WebSocketClientChannelInitializer(SslContext sslContext, ChannelHandler... handlers) {
    	this.sslContext = sslContext;
        this.handlers = handlers;
    }

    @Override
    public void initChannel(Channel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if(sslContext != null) {
        	pipeline.addLast(sslContext.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        
        pipeline.addLast(new BinaryFrameDecoder());
        pipeline.addLast(new BinaryFrameEncoder());
        pipeline.addLast(new WebSocketHandShakeHandler());
        pipeline.addLast(handlers);
    }
}
