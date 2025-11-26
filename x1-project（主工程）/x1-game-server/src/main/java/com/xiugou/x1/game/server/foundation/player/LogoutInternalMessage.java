/**
 * 
 */
package com.xiugou.x1.game.server.foundation.player;

import java.time.LocalDateTime;

/**
 * @author YY
 *
 */
public class LogoutInternalMessage implements PlayerInternalMessage {
	public PlayerContext playerContext;
	public LogoutType logoutType;
	public LocalDateTime forbidEndTime;
	public LocalDateTime lastLoginTime;

	@Override
	public long getPlayerId() {
		return playerContext.getId();
	}
}
