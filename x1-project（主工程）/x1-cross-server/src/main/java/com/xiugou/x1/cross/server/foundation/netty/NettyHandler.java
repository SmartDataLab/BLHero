/**
 *
 */
package com.xiugou.x1.cross.server.foundation.netty;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.cross.server.foundation.game.GameContext;
import com.xiugou.x1.cross.server.foundation.game.GameDisconnectMessage;

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
	
    private static final AttributeKey<GameContext> GAMECONTEXT = AttributeKey.newInstance("GAMECONTEXT");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	GameContext gameContext = new GameContext(ctx.channel());
        ctx.channel().attr(GAMECONTEXT).set(gameContext);
        logger.info("发现游戏服连接，{}", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	GameContext gameContext = ctx.channel().attr(GAMECONTEXT).get();
        if (gameContext != null) {
        	GameDisconnectMessage disconMessage = new GameDisconnectMessage();
        	disconMessage.setGameContext(gameContext);
        	gameContext.tell(disconMessage);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] byteMsg = (byte[]) msg;
        GameContext gameContext = ctx.channel().attr(GAMECONTEXT).get();
        if (gameContext == null) {
            return;
        }
        MessageWrapper wrapper = MessageWrapper.parseFrom(byteMsg);
        gameContext.tell(wrapper);
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
        GameContext gameContext = ctx.channel().attr(GAMECONTEXT).get();
        if (gameContext != null) {
        	GameDisconnectMessage disconMessage = new GameDisconnectMessage();
        	disconMessage.setGameContext(gameContext);
        	gameContext.tell(disconMessage);
            logger.error(exceptionId + " handle from player [" + gameContext.getId() + "] close channel!");
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
            	GameContext gameContext = ctx.channel().attr(GAMECONTEXT).get();
                if (gameContext != null) {
					logger.info("游戏服连接空闲超时: id={}， remoteAddress={}，游戏服ID={}", ctx.channel().id(),
							ctx.channel().remoteAddress(), gameContext.getId());
					GameDisconnectMessage disconMessage = new GameDisconnectMessage();
		        	disconMessage.setGameContext(gameContext);
		        	gameContext.tell(disconMessage);
                } else {
                	logger.info("幽灵连接空闲超时: id={}， remoteAddress={}", ctx.channel().id(), ctx.channel().remoteAddress());
                	ctx.channel().close();
                }
            }
        }
    }
}
