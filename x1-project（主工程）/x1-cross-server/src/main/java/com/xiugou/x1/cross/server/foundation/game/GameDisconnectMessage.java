/**
 * 
 */
package com.xiugou.x1.cross.server.foundation.game;

import com.xiugou.x1.design.TipsHelper;

/**
 * @author YY
 *
 */
public class GameDisconnectMessage implements GameInternalMessage {
	private GameContext gameContext;

	@Override
	public long getServerId() {
		return gameContext.getId();
	}

	public GameContext getGameContext() {
		return gameContext;
	}

	public void setGameContext(GameContext gameContext) {
		this.gameContext = gameContext;
	}

	@Override
	public void onException(Exception e) {
		TipsHelper.onException(e);
	}
}
