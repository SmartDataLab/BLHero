/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import org.gaming.fakecmd.side.game.IPlayerContext;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @author hyy
 *
 */
public class PlayerContextPhantom implements IPlayerContext {
	private long playerId;

	public PlayerContextPhantom(long playerId) {
		this.playerId = playerId;
	}
	
	@Override
	public long getId() {
		return playerId;
	}

	@Override
	public int getServerZone() {
		return 0;
	}
	
	@Override
	public int getCurrMsgId() {
		return 0;
	}

	@Override
	public void setCurrMsgId(int msgId) {
		
	}

	@Override
	public void write(int cmdId, GeneratedMessageV3 msg, int messageId) {
		//幽灵玩家上下文代表的是不在线的玩家，所以不需要把数据写到网络层
	}

	@Override
	public boolean isLogin() {
		return false;
	}

	@Override
	public void write(int cmd, ByteString msg, int msgId) {
		//幽灵玩家上下文代表的是不在线的玩家，所以不需要把数据写到网络层
	}
}
