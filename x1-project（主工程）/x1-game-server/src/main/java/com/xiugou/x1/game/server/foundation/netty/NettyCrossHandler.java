/**
 * 
 */
package com.xiugou.x1.game.server.foundation.netty;

import java.util.concurrent.atomic.AtomicLong;

import org.gaming.ruler.spring.Spring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiugou.x1.game.server.foundation.cross.CrossContext;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.GameConnectConfirmRequest;

/**
 * @author YY
 *
 */
@Sharable
public class NettyCrossHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = LoggerFactory.getLogger(NettyCrossHandler.class);
	
	private static final AttributeKey<CrossContext> CROSSCONTEXT = AttributeKey.newInstance("CROSSCONTEXT");
	
	private static AtomicLong exceptionIdGen = new AtomicLong(0);
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		CrossContext crossContext = new CrossContext(ctx.channel());
        ctx.channel().attr(CROSSCONTEXT).set(crossContext);
        logger.info("已经和跨服进行连接，{}", ctx.channel().id());
        
        ApplicationSettings applicationSettings = Spring.getBean(ApplicationSettings.class);
        
        GameConnectConfirmRequest.Builder request = GameConnectConfirmRequest.newBuilder();
        request.setMainServerId(applicationSettings.getGameServerId());
        request.addAllMergeServerIds(applicationSettings.getSubServerIds());
        crossContext.write(GameConnectConfirmRequest.Proto.ID, request.build());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.warn("和跨服的连接断开，{}", ctx.channel().id());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] byteMsg = (byte[]) msg;
		CrossContext crossContext = ctx.channel().attr(CROSSCONTEXT).get();
        if (crossContext == null) {
            return;
        }
        MessageWrapper wrapper = MessageWrapper.parseFrom(byteMsg);
        crossContext.tell(wrapper);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
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
	}
}
