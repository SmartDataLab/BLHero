/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.event;

import java.util.Map;

import org.gaming.prefab.IGameCause;

import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
public class SomeHeroFightingChangeEvent {
	private long pid;
	private Map<Hero, Long> heroOldFightingMap;
	private IGameCause gameCause;
	private String remark;
	
	
	public static SomeHeroFightingChangeEvent of(long pid, Map<Hero, Long> heroOldFightingMap, IGameCause gameCause, String remark) {
		SomeHeroFightingChangeEvent event = new SomeHeroFightingChangeEvent();
		event.pid = pid;
		event.heroOldFightingMap = heroOldFightingMap;
		event.gameCause = gameCause;
		event.remark = remark;
		return event;
	}

	public long getPid() {
		return pid;
	}

	public Map<Hero, Long> getHeroOldFightingMap() {
		return heroOldFightingMap;
	}

	public IGameCause getGameCause() {
		return gameCause;
	}

	public String getRemark() {
		return remark;
	}
}
