/**
 * 
 */
package org.gaming.ruler.netty;

import org.gaming.ruler.netty.datagram.ByteDatagramChannelInitializer;

/**
 * @author YY
 *
 */
public class TestServer {

	public static void main(String[] args) {
		ByteDatagramChannelInitializer channelInitializer = new ByteDatagramChannelInitializer(new NettyHandler());
		
		NettySocketServer server = new NettySocketServer(1, 16);
		server.startServer(TestConfig.startPort, TestConfig.endPort, channelInitializer);
	}
}
