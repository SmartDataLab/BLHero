/**
 * 
 */
package org.gaming.simulator.netty;

import org.gaming.simulator.ui.base.ViewManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author YY
 *
 */
public abstract class AbsNettyHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ViewManager.addResult("================" + ctx.channel().localAddress() + "连接成功================");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ViewManager.addResult("================" + ctx.channel().localAddress() + "断开连接================");
	}
}
