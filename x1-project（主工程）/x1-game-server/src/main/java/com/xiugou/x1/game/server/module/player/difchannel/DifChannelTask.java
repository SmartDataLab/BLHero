/**
 * 
 */
package com.xiugou.x1.game.server.module.player.difchannel;

import com.google.protobuf.ByteString;
import com.xiugou.x1.game.server.module.player.model.Player;

/**
 * @author YY
 *
 */
public class DifChannelTask {
	private final Player player;
	private final ByteString channelData;
	private final boolean create;
	public DifChannelTask(Player player, ByteString channelData, boolean create) {
		this.player = player;
		this.channelData = channelData;
		this.create = create;
	}
	public Player getPlayer() {
		return player;
	}
	public ByteString getChannelData() {
		return channelData;
	}
	public boolean isCreate() {
		return create;
	}
}
