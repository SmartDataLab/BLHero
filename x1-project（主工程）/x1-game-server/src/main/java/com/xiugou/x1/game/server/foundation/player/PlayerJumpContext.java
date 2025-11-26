/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.game.IPlayerContext;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @author YY
 *
 */
public class PlayerJumpContext implements IPlayerContext {
	private long id;
	private int serverZone;
	private int currMsgId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCurrMsgId() {
		return currMsgId;
	}

	public void setCurrMsgId(int currMsgId) {
		this.currMsgId = currMsgId;
	}

	@Override
	public void write(int cmdId, GeneratedMessageV3 msg, int messageId) {
		//跳转玩家上下文是在玩家真正的上下文对象不在线的时候创建的，所以不需要把数据写回到网络层
	}

	@Override
	public boolean isLogin() {
		return false;
	}

	@Override
	public void write(int cmd, ByteString msg, int msgId) {
		//跳转玩家上下文是在玩家真正的上下文对象不在线的时候创建的，所以不需要把数据写回到网络层
	}

	public int getServerZone() {
		return serverZone;
	}

	public void setServerZone(int serverZone) {
		this.serverZone = serverZone;
	}
}
