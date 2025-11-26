/**
 * 
 */
package com.xiugou.x1.game.server.foundation.cross;

import org.gaming.fakecmd.side.game.CrossCmdRegister.ICrossCmdMessage;
import org.gaming.fakecmd.side.game.ICrossContext;

import com.google.protobuf.ByteString;

import pb.xiugou.x1.protobuf.Wrapper.MessageWrapper;

/**
 * @author YY
 *
 */
public class CrossMessage implements ICrossCmdMessage {
	private CrossContext context;
	private MessageWrapper wrapper;
	
	public CrossMessage(CrossContext context, MessageWrapper wrapper) {
		this.context = context;
		this.wrapper = wrapper;
	}

	public CrossContext getContext() {
		return context;
	}

	public MessageWrapper getWrapper() {
		return wrapper;
	}

	@Override
	public ICrossContext getCrossContext() {
		return context;
	}

	@Override
	public int getMessageId() {
		return wrapper.getId();
	}

	@Override
	public int getCmd() {
		return wrapper.getProtoId();
	}

	@Override
	public ByteString getData() {
		return wrapper.getData();
	}
	
}
