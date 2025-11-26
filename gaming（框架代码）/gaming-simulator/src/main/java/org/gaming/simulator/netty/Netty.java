/**
 * 
 */
package org.gaming.simulator.netty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.gaming.simulator.ui.slim.panel.LinkType;

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

/**
 * @author YY
 *
 */
public class Netty {
	public static final AttributeKey<Netty> NETTY = AttributeKey.newInstance("NETTY");
	
	private Bootstrap b;
	
	private Channel channel;
	
	private Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap = new HashMap<>();
	
	private URI uri;
	
	private EventLoopGroup workerGroup;
	
	private static Netty netty;
	
	public static Netty ins() {
		return netty;
	}
	
	public static void init(Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap) {
		netty = new Netty(channelInitializerMap);
	}
	
	public Netty(Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap) {
		this.channelInitializerMap = channelInitializerMap;
		this.workerGroup = new NioEventLoopGroup();
	}
	
	public Netty(EventLoopGroup workerGroup, Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap) {
		this.channelInitializerMap = channelInitializerMap;
		this.workerGroup = workerGroup;
	}
	
	public static void main(String[] args) {
		try {
//			InetAddress inetAddress = InetAddress.getByName("wss://www.xiugouchedui.cn/game?ip=127.0.0.1&port=11001");
			//如果连wss的nginx转发端口，那么需要在url上加上443端口
			URI uri = new URI("wss://www.xiugouchedui.cn:443/game?ip=127.0.0.1&port=11001");
			System.out.println(uri.getHost());
			System.out.println(uri.getPort());
			
//			InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(host, port)
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connect(String uri, LinkType linkType) {
		try {
			if(linkType == LinkType.Tcp) {
				this.uri = new URI("tcp://" + uri);
			} else if(linkType == LinkType.Ws) {
				this.uri = new URI("ws://" + uri);
			} else if(linkType == LinkType.Wss) {
				this.uri = new URI("wss://" + uri);
			}
			
			b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.option(ChannelOption.TCP_NODELAY, true);
			ChannelInitializer<Channel> channelInitializer = channelInitializerMap.get(linkType);
			b.handler(channelInitializer);
			ChannelFuture f = b.connect(this.uri.getHost(), this.uri.getPort());
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					future.channel().attr(NETTY).set(Netty.this);
				}
			});
			f.sync();
			this.channel = f.channel();
			
			System.out.println(this.uri + " " + this.channel.id().toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			workerGroup.shutdownGracefully();
		}
	}
	
	public void write(byte[] msg) {
		if (channel == null || msg == null || !channel.isActive()) {
			return;
		}
		channel.writeAndFlush(msg);
	}
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public boolean isActive() {
		return this.channel.isActive();
	}

	public URI getUri() {
		return uri;
	}
}
