/**
 * 
 */
package com.xiugou.x1.game.server.foundation.cross;

import org.gaming.fakecmd.side.game.ICrossContext;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

import akka.actor.ActorRef;
import io.netty.channel.Channel;
import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 * @author YY
 *
 */
public class CrossContext implements ICrossContext {
	
	private final Channel channel;
	
	private final ActorRef actor;
	
	public CrossContext(Channel channel) {
		this.channel = channel;
		this.actor = CrossActorPool.getActor();
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

	public void write(byte[] data) {
		if (isChannelActive()) {
			this.channel.writeAndFlush(data);
		}
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
	
	public void tell(MessageWrapper wrapper) {
		CrossMessage message = new CrossMessage(this, wrapper);
		actor.tell(message, actor);
	}

	@Override
	public int getId() {
		return 0;
	}
}
