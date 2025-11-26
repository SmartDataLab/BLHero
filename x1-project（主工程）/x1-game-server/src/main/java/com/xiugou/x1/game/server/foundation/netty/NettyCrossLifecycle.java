/**
 * 
 */
package com.xiugou.x1.game.server.foundation.netty;

import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.ruler.lifecycle.LifecycleInfo;
import org.gaming.ruler.lifecycle.Ordinal;
import org.gaming.ruler.lifecycle.Priority;
import org.gaming.ruler.netty.NettySocketClient;
import org.gaming.ruler.netty.datagram.ByteDatagramChannelInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

/**
 * @author YY
 *
 */
@Component
public class NettyCrossLifecycle implements Lifecycle {
	
	private static Logger logger = LoggerFactory.getLogger(NettyCrossLifecycle.class);

	@Autowired
	private ApplicationSettings applicationSettings;
	
	private NettySocketClient netty;

	@Override
	public LifecycleInfo getInfo() {
		return LifecycleInfo.valueOf(this.getClass().getSimpleName(), Priority.LOW, Ordinal.MAX);
	}
	
	@Override
	public void start() throws Exception {
		// 启动Netty
		String ip = applicationSettings.getCrossServerIp();
		int port = applicationSettings.getCrossServerPort();
		logger.info("服务器正在尝试跨服连接，目标IP{}，目标端口{}", ip, port);
		netty = new NettySocketClient(ip, port, new ByteDatagramChannelInitializer(new NettyCrossHandler()));
		netty.autoConnect();
	}

	@Override
	public void stop() throws Exception {
		if (netty != null) {
			netty.shutdown();
		}
	}
}
