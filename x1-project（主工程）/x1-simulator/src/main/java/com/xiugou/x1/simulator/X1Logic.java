/**
 * 
 */
package com.xiugou.x1.simulator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.gaming.protobuf3.simulator.plugin.PluginLogic;
import org.gaming.protobuf3.simulator.protocol.ProtocolMessage;
import org.gaming.protobuf3.simulator.protocol.parser.ProtocolType;
import org.gaming.simulator.netty.Netty;
import org.gaming.simulator.netty.websocket.WebSocketHandShakeHandler;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.InvalidProtocolBufferException;

import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.player.Player.HeartBeatRequest;
import pb.xiugou.x1.protobuf.player.Player.LoginRequest;
import pb.xiugou.x1.protobuf.player.Player.LoginResponse;
import pb.xiugou.x1.protobuf.player.Player.LoginStatus;

/**
 * @author YY
 *
 */
public class X1Logic implements PluginLogic {

	@Override
	public AbstractMessage login(String openId, int areaId) {
		WebSocketHandShakeHandler wsHandler = Netty.ins().getChannel().pipeline().get(WebSocketHandShakeHandler.class);
		if(wsHandler != null) {
			int count = 0;
			while(!wsHandler.isHandshaked() && count < 10) {
				try {
					System.out.println("Handshaked");
					Thread.sleep(500);
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		LoginRequest.Builder builder = LoginRequest.newBuilder();
		builder.setOpenId(openId);
		builder.setServerId(areaId);
		builder.setDeviceType("IOS");
		builder.setDeviceType("IOS-TEST");
		builder.setChannelId(1);
		return builder.build();
	}

	@Override
	public AbstractMessage create(String openId, int areaId) {
		return null;
	}

	@Override
	public int getProtocolId(String protocolName) {
		Integer cmd = ProtoFileReader.reqProtocolIdMap.get(protocolName);
		if(cmd != null) {
			return cmd;
		}
		cmd = ProtoFileReader.resProtocolIdMap.get(protocolName);
		if(cmd != null) {
			return cmd;
		}
		return 0;
	}

	@Override
	public ProtocolType getProtocolType(String protocolName) {
		if(ProtoFileReader.resProtocolIdMap.containsKey(protocolName)) {
			return ProtocolType.RESPONSE;
		} else {
			return ProtocolType.REQUEST;
		}
	}

	@Override
	public ProtocolMessage encode(String protocolName, AbstractMessage msg) {
		Integer cmd = ProtoFileReader.reqProtocolIdMap.get(protocolName);
		if(cmd == null) {
			return null;
		}
		
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
		builder.setProtoId(cmd);
		builder.setData(msg.toByteString());
		MessageWrapper wrapper = builder.build();
		
		ProtocolMessage message = new ProtocolMessage();
		message.setProtocolId(wrapper.getProtoId());
		message.setContent(wrapper.toByteArray());
		return message;
	}

	@Override
	public ProtocolMessage decode(byte[] data) {
		try {
			MessageWrapper wrapper = MessageWrapper.parseFrom(data);
			ProtocolMessage message = new ProtocolMessage();
			message.setProtocolId(wrapper.getProtoId());
			message.setContent(wrapper.getData().toByteArray());
			return message;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onDisconnect() {
		//停止心跳定时器
		if(scheduledFuture != null && !scheduledFuture.isCancelled()) {
			scheduledFuture.cancel(false);
		}
	}

	@Override
	public void onConnect() {
	}

	private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> scheduledFuture = null;
			
	@Override
	public void onRead(AbstractMessage object) {
		if(object instanceof LoginResponse) {
			LoginResponse loginResp = (LoginResponse)object;
			if(loginResp.getStatus() == LoginStatus.NORMAL) {
				if(scheduledFuture != null && !scheduledFuture.isCancelled()) {
					scheduledFuture.cancel(false);
				}
				//开启心跳定时器
				scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {
					
					@Override
					public void run() {
						MessageWrapper.Builder builder = MessageWrapper.newBuilder();
						builder.setProtoId(HeartBeatRequest.Proto.ID_VALUE);
						builder.setData(HeartBeatRequest.getDefaultInstance().toByteString());
						MessageWrapper wrapper = builder.build();
						Netty.ins().write(wrapper.toByteArray());
					}
				}, 0, 30, TimeUnit.SECONDS);
			}
		}
	}

}
