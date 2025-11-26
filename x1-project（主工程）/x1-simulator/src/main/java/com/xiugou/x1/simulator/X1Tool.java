/**
 * 
 */
package com.xiugou.x1.simulator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.gaming.protobuf3.simulator.SimulatorMain;
import org.gaming.protobuf3.simulator.netty.NettyHandler;
import org.gaming.protobuf3.simulator.protocol.ProtocolPrinter;
import org.gaming.simulator.netty.datagram.ByteDatagramClientChannelInitializer;
import org.gaming.simulator.netty.websocket.WebSocketClientChannelInitializer;
import org.gaming.simulator.ui.slim.panel.ConnectNorthPanel;
import org.gaming.simulator.ui.slim.panel.LinkType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.battleattr.BattleAttr.PbBattleAttr;
import pb.xiugou.x1.protobuf.ministruct.MiniStruct.PbThing;
import pb.xiugou.x1.protobuf.player.Player.HeartBeatResponse;

/**
 * @author YY
 *
 */
public class X1Tool {
	
	public static void main(String[] args) {
//		www.xiugouchedui.cn:443/game?ip=127.0.0.1&port=10004
		
		ProtocolPrinter.classPrinterMap.put(PbThing.class, new PbThingPrinter());
		ProtocolPrinter.classPrinterMap.put(PbBattleAttr.class, new PbBattleAttrPrinter());
		
		System.out.println(MessageWrapper.class.getResource("."));
		String path = MessageWrapper.class.getResource(".").getPath();
		
		ProtoFileReader.readProtoFiles(path);
		
		Map<LinkType, ChannelInitializer<Channel>> channelInitializerMap = new HashMap<>();
		channelInitializerMap.put(LinkType.Tcp, new ByteDatagramClientChannelInitializer(new NettyHandler()));
		channelInitializerMap.put(LinkType.Ws, new WebSocketClientChannelInitializer(new NettyHandler()));
		try {
			SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			channelInitializerMap.put(LinkType.Wss, new WebSocketClientChannelInitializer(sslContext, new NettyHandler()));
		} catch (SSLException e) {
			e.printStackTrace();
		}
		
		NettyHandler.addIgnoreClass(HeartBeatResponse.class);
		
		String projectDir = System.getProperty("user.dir");
		String useLogPath = projectDir + File.separator + "config\\silmuselog";
		ConnectNorthPanel.DEFAULT_LINK_TYPE = LinkType.Ws;
		SimulatorMain.start("游戏服连接器", new X1Logic(), channelInitializerMap, useLogPath);
	}
}
