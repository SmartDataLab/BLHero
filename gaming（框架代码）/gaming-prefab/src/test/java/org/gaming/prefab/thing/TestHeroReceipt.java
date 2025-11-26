/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.List;

/**
 * @author YY
 *
 */
public class TestHeroReceipt extends RewardReceipt {

	private List<TestHero> heroes;
	
	public TestHeroReceipt(List<TestHero> heroes) {
		this.heroes = heroes;
	}
	public List<TestHero> getHeroes() {
		return heroes;
	}
}
