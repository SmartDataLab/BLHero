/**
 * 
 */
package com.xiugou.x1.simulator.pressing;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.gaming.ruler.akka.AkkaContext;
import org.gaming.simulator.netty.Netty;
import org.gaming.simulator.netty.datagram.ByteDatagramClientChannelInitializer;
import org.gaming.simulator.netty.websocket.WebSocketClientChannelInitializer;
import org.gaming.simulator.ui.slim.panel.LinkType;
import org.gaming.tool.DateTimeUtil;

import com.xiugou.x1.simulator.pressing.handler.MessageHandler;
import com.xiugou.x1.simulator.pressing.handler.NettyPressingHandler;
import com.xiugou.x1.simulator.pressing.message.ConnectToServerMessage;

import akka.actor.ActorRef;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.AttributeKey;

/**
 * @author YY
 *
 */
public class X1Pressing {
	
	public static final AttributeKey<PlayerContext> PLAYERCONTEXT = AttributeKey.newInstance("PLAYERCONTEXT");
	
	private static ActorRef[] ACTOR_POOL;
	private static Map<Long, PlayerContext> playerContextMap = new HashMap<>();
	private static EventLoopGroup workerGroup = new NioEventLoopGroup(10);
	
	public static void main(String[] args) {
		int actorNum = AkkaContext.PARALLELISM_MAX * 2;
		ACTOR_POOL = new ActorRef[actorNum];
		for (int i = 0; i < actorNum; i++) {
			ActorRef actorRef = AkkaContext.createActor(PlayerActor.class);
			ACTOR_POOL[i] = actorRef;
		}
		
		Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap = new HashMap<>();
		channelInitializerMap.put(LinkType.Tcp, new ByteDatagramClientChannelInitializer(new NettyPressingHandler()));
		channelInitializerMap.put(LinkType.Ws, new WebSocketClientChannelInitializer(new NettyPressingHandler()));
		try {
			SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			channelInitializerMap.put(LinkType.Wss, new WebSocketClientChannelInitializer(sslContext, new NettyPressingHandler()));
		} catch (SSLException e) {
			e.printStackTrace();
		}
		
		MessageHandler messageHandler = new MessageHandler();
		
		for(int i = 0; i < 3000; i++) {
			PlayerContext playerContext = new PlayerContext();
			playerContext.setId(i + 1);
			playerContext.setOpenId("press-" + DateTimeUtil.currSecond() + "-" + (i + 1));
			playerContext.setMessageHandler(messageHandler);
			playerContext.setActor(ACTOR_POOL[(int)(playerContext.getId() % ACTOR_POOL.length)]);
			playerContext.setNetty(new Netty(workerGroup, channelInitializerMap));
			
			playerContextMap.put(playerContext.getId(), playerContext);
			playerContext.setServerId(1);
			playerContext.getActor().tell(new ConnectToServerMessage(playerContext, "www.xiugouchedui.cn:10001"), ActorRef.noSender());
			
//			playerContext.setServerId(4);
//			playerContext.getActor().tell(new ConnectToServerMessage(playerContext, "127.0.0.1:10004"), ActorRef.noSender());
		}
	}
}
