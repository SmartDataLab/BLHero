/**
 * 
 */
package com.xiugou.x1.game.server.module.hero.service;

import java.util.Map;

import org.gaming.prefab.thing.RewardReceipt;

import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
public class HeroReceipt extends RewardReceipt {

	private Map<Integer, Hero> heroes;
	
	public HeroReceipt(Map<Integer, Hero> heroes) {
		this.heroes = heroes;
	}

	public Map<Integer, Hero> getHeroes() {
		return heroes;
	}
}
