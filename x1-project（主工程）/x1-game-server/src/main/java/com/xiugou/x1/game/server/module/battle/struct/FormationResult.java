/**
 * 
 */
package com.xiugou.x1.game.server.module.battle.struct;

import java.util.ArrayList;
import java.util.List;

import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
public class FormationResult {

	private int mainHero;
	private List<HeroAndPos> list = new ArrayList<>();
	
	public static class HeroAndPos {
		private Hero hero;
		private int pos;
		public Hero getHero() {
			return hero;
		}
		public void setHero(Hero hero) {
			this.hero = hero;
		}
		public int getPos() {
			return pos;
		}
		public void setPos(int pos) {
			this.pos = pos;
		}
	}

	public int getMainHero() {
		return mainHero;
	}

	public void setMainHero(int mainHero) {
		this.mainHero = mainHero;
	}

	public List<HeroAndPos> getList() {
		return list;
	}

	public void setList(List<HeroAndPos> list) {
		this.list = list;
	}
}
