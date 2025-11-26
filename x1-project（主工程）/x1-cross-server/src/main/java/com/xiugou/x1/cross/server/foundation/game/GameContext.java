/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;


import java.util.HashSet;
import java.util.Set;

import org.gaming.fakecmd.side.cross.IGameContext;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

import akka.actor.ActorRef;
import io.netty.channel.Channel;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;
import pb.xiugou.x1.protobuf.cross.Cross.PlayerCrossWrapper;

/**
 * 
 * @author YY
 *
 */
public class GameContext implements IGameContext {
	private int id;
	
	private Set<Integer> mergeServerIds = new HashSet<>();
	
	private final Channel channel;
	
	private ActorRef actor;
	
	public GameContext(Channel channel) {
		this.channel = channel;
		this.actor = GameActorPool.getOneActor();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void tell(MessageWrapper wrapper) {
		ProtocolMessage message = new ProtocolMessage(this, wrapper);
		actor.tell(message, actor);
	}
	
	public void tell(GameInternalMessage message) {
		actor.tell(message, actor);
	}
	
	public void write(byte[] data) {
		if (isChannelActive()) {
			this.channel.writeAndFlush(data);
		}
	}
	
	public void write(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		write(proto.getNumber(), msg, 0);
	}
	
	protected void write(int protoId, GeneratedMessageV3 msg, int messageId) {
		if (isChannelActive()) {
			MessageWrapper.Builder builder = MessageWrapper.newBuilder();
			builder.setId(messageId);
			builder.setProtoId(protoId);
			builder.setData(msg.toByteString());
			this.channel.writeAndFlush(builder.build().toByteArray());
		} else {
			// TODO 记录日志
		}
	}
	
	protected void writeToPlayer(long playerId, int protoId, GeneratedMessageV3 msg, int messageId, long syncId) {
		PlayerCrossWrapper.Builder crossWrapper = PlayerCrossWrapper.newBuilder();
		crossWrapper.setPlayerId(playerId);
		crossWrapper.setProtoId(protoId);
		crossWrapper.setId(messageId);
		crossWrapper.setData(msg.toByteString());
		crossWrapper.setSyncId(syncId);
		this.write(PlayerCrossWrapper.Proto.ID_VALUE, crossWrapper.build(), 0);
	}
	
	protected void writeToPlayer(long playerId, ProtocolMessageEnum proto, GeneratedMessageV3 msg, int messageId, long syncId) {
		writeToPlayer(playerId, proto.getNumber(), msg, messageId, syncId);
	}
	
	public boolean isChannelActive() {
		if (channel != null && channel.isActive()) {
			return true;
		}
		return false;
	}

	public void closeChannel() {
		if (channel != null && channel.isActive()) {
			this.channel.close();
		}
	}

	public void setActor(ActorRef actor) {
		this.actor = actor;
	}

	public Channel getChannel() {
		return channel;
	}

	public Set<Integer> getMergeServerIds() {
		return mergeServerIds;
	}

}
