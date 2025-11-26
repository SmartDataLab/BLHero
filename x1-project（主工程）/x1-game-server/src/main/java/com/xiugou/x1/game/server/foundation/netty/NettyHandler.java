/**
 *
 */
package com.xiugou.x1.game.server.foundation.netty;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.game.server.foundation.player.LogoutInternalMessage;
import com.xiugou.x1.game.server.foundation.player.LogoutType;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 * @author YY
 *
 */
@Sharable
public class NettyHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(NettyHandler.class);
	
	private static AtomicLong exceptionIdGen = new AtomicLong(0);
	
    private static final AttributeKey<PlayerContext> PLAYERCONTEXT = AttributeKey.newInstance("PLAYERCONTEXT");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PlayerContext playerContext = new PlayerContext(ctx.channel());
        ctx.channel().attr(PLAYERCONTEXT).set(playerContext);
        logger.info("发现连接，{}", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	PlayerContext playerContext = ctx.channel().attr(PLAYERCONTEXT).get();
        if (playerContext != null && playerContext.getActor() != null && playerContext.getLogoutType() == null) {
        	LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
    		logoutMessage.playerContext = playerContext;
    		logoutMessage.logoutType = LogoutType.CONNECT_BREAK;
    		playerContext.tell(logoutMessage);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] byteMsg = (byte[]) msg;
        PlayerContext playerContext = ctx.channel().attr(PLAYERCONTEXT).get();
        if (playerContext == null) {
            return;
        }
        MessageWrapper wrapper = MessageWrapper.parseFrom(byteMsg);
        playerContext.tell(wrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	long exceptionId = exceptionIdGen.incrementAndGet();
        if("Connection reset by peer".equals(cause.getMessage())) {
        	logger.error(exceptionId + " handle from [" + ctx.channel().id() + "] failed! Connection reset by peer");
        } else if("远程主机强迫关闭了一个现有的连接。".equals(cause.getMessage())) {
        	logger.error(exceptionId + " handle from [" + ctx.channel().id() + "] failed! 远程主机强迫关闭了一个现有的连接。");
        } else {
        	logger.error(exceptionId + " handle from [" + ctx.channel().id() + "] failed!", cause);
        }
        PlayerContext playerContext = ctx.channel().attr(PLAYERCONTEXT).get();
        if (playerContext != null && playerContext.getActor() != null && playerContext.getLogoutType() == null) {
        	LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
    		logoutMessage.playerContext = playerContext;
    		logoutMessage.logoutType = LogoutType.CONNECT_ERROR;
    		playerContext.tell(logoutMessage);
            logger.error(exceptionId + " handle from player [" + playerContext.getId() + "] close channel!");
        } else {
        	logger.error(exceptionId + " connection[" + ctx.channel().id() + "] not found in manager");
            ctx.channel().close();
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                PlayerContext playerContext = ctx.channel().attr(PLAYERCONTEXT).get();
                if (playerContext != null && playerContext.getActor() != null) {
					logger.info("玩家连接空闲超时: id={}， remoteAddress={}，玩家ID={}", ctx.channel().id(),
							ctx.channel().remoteAddress(), playerContext.getId());
                	LogoutInternalMessage logoutMessage = new LogoutInternalMessage();
            		logoutMessage.playerContext = playerContext;
            		logoutMessage.logoutType = LogoutType.IDLE_TOO_LONG;
            		playerContext.tell(logoutMessage);
                } else {
                	logger.info("幽灵连接空闲超时: id={}， remoteAddress={}", ctx.channel().id(), ctx.channel().remoteAddress());
                	ctx.channel().close();
                }
            }
        }
    }
}
