/**
 * 
 */
package com.xiugou.x1.game.server.module.player.message;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerInternalMessage;

/**
 * @author YY
 *
 */
public interface PlayerMessage {
	
	static class PlayerLoginMessage implements PlayerInternalMessage {
		private final PlayerContext playerContext;
		//是否创号
		private final boolean create;
		public PlayerLoginMessage(PlayerContext playerContext, boolean create) {
			this.playerContext = playerContext;
			this.create = create;
		}
		public PlayerContext getPlayerContext() {
			return playerContext;
		}
		@Override
		public long getPlayerId() {
			return playerContext.getId();
		}
		public boolean isCreate() {
			return create;
		}
	}
	
	static class PlayerForbidMessage implements PlayerInternalMessage {
		private final long pid;
		private final long forbidEndTime;

		public PlayerForbidMessage(long pid, long forbidEndTime) {
			this.pid = pid;
			this.forbidEndTime = forbidEndTime;
		}

		@Override
		public long getPlayerId() {
			return pid;
		}

		public long getForbidEndTime() {
			return forbidEndTime;
		}
	}
	
	/**
	 * 封禁账号
	 * @author YY
	 *
	 */
	static class PlayerForbidListMessage implements PlayerInternalMessage {
		private final long pid;

		public PlayerForbidListMessage(long pid) {
			this.pid = pid;
		}

		@Override
		public long getPlayerId() {
			return pid;
		}
	}
	
	static class PlayerDailyResetMessage implements PlayerInternalMessage {
		private final PlayerContext playerContext;
		public PlayerDailyResetMessage(PlayerContext playerContext) {
			this.playerContext = playerContext;
		}

		@Override
		public long getPlayerId() {
			return playerContext.getId();
		}

		public PlayerContext getPlayerContext() {
			return playerContext;
		}
	}
}
