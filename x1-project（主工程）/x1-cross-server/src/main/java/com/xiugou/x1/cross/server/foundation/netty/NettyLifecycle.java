/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.netty;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.netty.NettySocketServer;
import org.gaming.ruler.netty.datagram.ByteDatagramChannelInitializer;
import org.gaming.ruler.netty.websocket.WebSocketChannelInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.cross.server.foundation.starting.ApplicationSettings;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author YY
 *
 */
@Component
public class NettyLifecycle implements Lifecycle {

	private static Logger logger = LoggerFactory.getLogger(NettyLifecycle.class);
	
	@Autowired
	private ApplicationSettings applicationSettings;
	
	private NettySocketServer netty;
	
	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MAX);
	}

	@Override
	public void start() throws Exception {
		ChannelInitializer<Channel> channelInitializer = new ByteDatagramChannelInitializer(new NettyHandler());
		//启动Netty
		netty = new NettySocketServer(2, 10);
		netty.startServer(applicationSettings.getCorssServerPort(), channelInitializer);
		if(channelInitializer instanceof ByteDatagramChannelInitializer) {
			logger.info("服务器启动成功，TCP连接端口：{}", applicationSettings.getCorssServerPort());
		} else if(channelInitializer instanceof WebSocketChannelInitializer) {
			logger.info("服务器启动成功，WS连接端口：{}", applicationSettings.getCorssServerPort());
		}
	}

	@Override
	public void stop() throws Exception {
		if(netty != null) {
			netty.shutdown();
		}
	}
}
