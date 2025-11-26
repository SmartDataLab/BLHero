/**
 * 
 */
package com.xiugou.x1.simulator.pressing;

import org.gaming.simulator.netty.Netty;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;
import com.xiugou.x1.simulator.pressing.handler.MessageHandler;

import akka.actor.ActorRef;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 * @author YY
 *
 */
public class PlayerContext {
	
	private String openId;
	private int serverId;
	private Netty netty;
	private long id;
	private ActorRef actor;
	private MessageHandler messageHandler;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ActorRef getActor() {
		return actor;
	}
	public void setActor(ActorRef actor) {
		this.actor = actor;
	}
	public void write(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		netty.write(builder.build().toByteArray());
	}
	public Netty getNetty() {
		return netty;
	}
	public void setNetty(Netty netty) {
		this.netty = netty;
	}
	public MessageHandler getMessageHandler() {
		return messageHandler;
	}
	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}
}
