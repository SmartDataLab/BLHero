/**
 * 
 */
package com.xiugou.x1.simulator.pressing.message;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;
import com.xiugou.x1.simulator.pressing.PlayerContext;

import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 * @author YY
 *
 */
public class OperationMessage {
	private PlayerContext playerContext;
	private MessageWrapper wrapper;
	public PlayerContext getPlayerContext() {
		return playerContext;
	}
	public void setPlayerContext(PlayerContext playerContext) {
		this.playerContext = playerContext;
	}
	public MessageWrapper getWrapper() {
		return wrapper;
	}
	public void setWrapper(MessageWrapper wrapper) {
		this.wrapper = wrapper;
	}
	public void setProtocol(ProtocolMessageEnum proto, GeneratedMessageV3 msg) {
		MessageWrapper.Builder builder = MessageWrapper.newBuilder();
		builder.setProtoId(proto.getNumber());
		builder.setData(msg.toByteString());
		wrapper = builder.build();
	}
	public OperationMessage(PlayerContext playerContext, MessageWrapper wrapper) {
		this.playerContext = playerContext;
		this.wrapper = wrapper;
	}
	
}
