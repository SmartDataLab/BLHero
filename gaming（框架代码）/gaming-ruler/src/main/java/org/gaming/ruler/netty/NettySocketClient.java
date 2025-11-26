/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @author YY
 *
 */
public class NettySocketClient {

	public final static AttributeKey<String> TAG = AttributeKey.valueOf("TAG");
	
	private static Logger logger = LoggerFactory.getLogger(NettySocketClient.class);
	
	private AtomicReference<Boolean> shutdown = new AtomicReference<>(false);
	/**
	 * 客户端连接标签
	 */
	private String tag;
	private String host;
	private int port;
	private Bootstrap bootstrap;
	
	private ChannelFutureListener reconnectListener;
	
	private final AtomicReference<Channel> channelRef = new AtomicReference<>();
	
	public static EventLoopGroup eventGroup = new NioEventLoopGroup(16, new DefaultThreadFactory("eventGroup"));
	
	public NettySocketClient(String host, int port, int eventThreads, ChannelInitializer<?> channelInitializer) {
		this.host = host;
		this.port = port;
		
		bootstrap = new Bootstrap();
		bootstrap.group(eventGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000);
		bootstrap.handler(channelInitializer);
	}
	
	public NettySocketClient(String host, int port, ChannelInitializer<?> channelInitializer) {
		this(host, port, 0, channelInitializer);
	}
	
	/**
	 * 关闭服务
	 */
	public void shutdown() {
		try {
			shutdown.set(true);
			eventGroup.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			logger.error("shutdown event group failed", e);
		}
	}
	
	/**
	 * 手动连接
	 * @param listeners
	 */
	//TODO 后续再进行实现
	public void handConnect() {
		
	}
	
	/**
	 * 普通连接，不会自动重连
	 * @return
	 */
	private ChannelFuture connect() {
		return bootstrap.connect(host, port);
	}
	
	public void syncConnect() {
		try {
			ChannelFuture f = bootstrap.connect(host, port).sync();
			setChannel(f.channel());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 进行自动连接，并在失败、断开连接后重连
	 */
	public void autoConnect() {
		if(reconnectListener == null) {
			reconnectListener = new ReconnectFutureListener();
		}
		reConnect();
	}
	
	/**
	 * 用于重连的监听器
	 * @author YY
	 *
	 */
	public class ReconnectFutureListener implements ChannelFutureListener {
		
		public void operationComplete(ChannelFuture f) throws Exception {
			try {
				boolean success = f.isSuccess();
				if (success) {
					logger.info("远程服务器{}:{}连接成功!", host, port);
					f.channel().attr(TAG).set(tag);
					setChannel(f.channel());
				} else {
					logger.debug("远程服务器{}:{}连接失败!，即将进行重连", host, port);
				}
			} finally {
				checkConnect();
			}
		}
	}
	
	
	/**
	 * 重连
	 */
	private void reConnect() {
		ChannelFuture future = connect();
		future.addListener(reconnectListener);
	}
	
	private void checkConnect() {
		if (this.shutdown.get()) {
			this.close();
			return;
		}
		eventGroup.schedule(() -> {
			if (isChannelActive()) {
				checkConnect();
			} else {
				reConnect();
			}
		}, 3000, TimeUnit.MILLISECONDS);
	}
	
	public boolean isChannelActive() {
		return channelRef.get() != null && channelRef.get().isActive();
	}
	
	private void setChannel(Channel channel) {
		close();
		channelRef.set(channel);
	}

	public void close() {
		Channel channel = channelRef.get();
		if (channel != null && channel.isActive()) {
			channel.close();
		}
	}
	
	public void write(byte[] content) {
		Channel channel = channelRef.get();
		if (channel != null && channel.isWritable()) {
			channel.writeAndFlush(content);
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
