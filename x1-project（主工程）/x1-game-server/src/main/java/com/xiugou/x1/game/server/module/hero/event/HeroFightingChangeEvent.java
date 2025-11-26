/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.event;

import org.gaming.prefab.IGameCause;

import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
public class HeroFightingChangeEvent {
	private Hero hero;
	private long heroOldFighting;
	private boolean changeTroopAttr;
	private IGameCause gameCause;
	
	public static HeroFightingChangeEvent of(Hero hero, long heroOldFighting, boolean changeTroopAttr, IGameCause gameCause) {
		HeroFightingChangeEvent event = new HeroFightingChangeEvent();
		event.hero = hero;
		event.heroOldFighting = heroOldFighting;
		event.changeTroopAttr = changeTroopAttr;
		event.gameCause = gameCause;
		return event;
	}

	public IGameCause getGameCause() {
		return gameCause;
	}

	public long getHeroOldFighting() {
		return heroOldFighting;
	}

	public boolean isChangeTroopAttr() {
		return changeTroopAttr;
	}

	public Hero getHero() {
		return hero;
	}
}
