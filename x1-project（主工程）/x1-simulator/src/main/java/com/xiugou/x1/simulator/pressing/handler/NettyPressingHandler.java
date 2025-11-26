/**
 * 
 */
package com.xiugou.x1.simulator.pressing.handler;

import com.xiugou.x1.simulator.pressing.PlayerContext;
import com.xiugou.x1.simulator.pressing.X1Pressing;
import com.xiugou.x1.simulator.pressing.message.OperationMessage;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandler.Sharable;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author YY
 *
 */
@Sharable
public class NettyPressingHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] byteMsg = (byte[]) msg;
        PlayerContext playerContext = ctx.channel().attr(X1Pressing.PLAYERCONTEXT).get();
        if (playerContext == null) {
            return;
        }
        MessageWrapper wrapper = MessageWrapper.parseFrom(byteMsg);
        OperationMessage operationMessage = new OperationMessage(playerContext, wrapper);
        playerContext.getActor().tell(operationMessage, ActorRef.noSender());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		PlayerContext playerContext = ctx.channel().attr(X1Pressing.PLAYERCONTEXT).get();
		if (playerContext == null) {
            return;
        }
//		System.out.println(playerContext.getId() + "连接成功");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		PlayerContext playerContext = ctx.channel().attr(X1Pressing.PLAYERCONTEXT).get();
		if (playerContext == null) {
            return;
        }
//		System.out.println(playerContext.getId() + "连接断开");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
