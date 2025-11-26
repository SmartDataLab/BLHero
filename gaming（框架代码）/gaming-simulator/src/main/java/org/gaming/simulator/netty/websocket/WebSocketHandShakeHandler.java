/**
 * 
 */
package org.gaming.simulator.netty.websocket;

import org.gaming.simulator.netty.Netty;
import org.gaming.simulator.ui.base.ViewManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

/**
 * @author YY
 *
 */
public class WebSocketHandShakeHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise channelPromise;
    private boolean handshaked;
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.channelPromise = ctx.newPromise();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 握手协议返回，设置结束握手
        if (!this.handshaker.isHandshakeComplete() && msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse)msg;
            this.handshaker.finishHandshake(ctx.channel(), response);
            this.channelPromise.setSuccess();
            this.handshaked = true;
            
            ViewManager.addResult("\n================" + ctx.channel().localAddress() + "握手成功================");
            return;
        }
        if(msg instanceof byte[]) {
        	ctx.fireChannelRead(msg);
        } else {
        	System.out.println(msg.getClass().getSimpleName() + " " + ctx.channel().id().toString());
        }
    }
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Netty netty = ctx.channel().attr(Netty.NETTY).get();
		ViewManager.addResult("\n================" + netty.getUri() + "进行握手================");
		
		System.out.println(ctx.channel().id().toString() + " 进行握手");
		
		HttpHeaders httpHeaders = new DefaultHttpHeaders();
		handshaker = WebSocketClientHandshakerFactory.newHandshaker(netty.getUri(), WebSocketVersion.V13,
				(String) null, true, httpHeaders);
		handshaker.handshake(ctx.channel());
		
		ctx.fireChannelActive();
	}

	public boolean isHandshaked() {
		return handshaked;
	}
}
