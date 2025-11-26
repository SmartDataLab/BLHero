/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.event;

import java.util.List;

import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
public class GetNewHeroEvent {
	private long playerId;
	private List<Hero> newHeros;
	
	public static GetNewHeroEvent of(long playerId, List<Hero> newHeros) {
		GetNewHeroEvent event = new GetNewHeroEvent();
		event.playerId = playerId;
		event.newHeros = newHeros;
		return event;
	}

	public long getPlayerId() {
		return playerId;
	}

	public List<Hero> getNewHeros() {
		return newHeros;
	}
}
