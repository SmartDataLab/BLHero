package com.xiugou.x1.backstage.module.player.struct;

import pojo.xiugou.x1.pojo.module.hero.HeroForm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yh
 * @date 2023/7/18
 * @apiNote
 */
public class PlayerHeroData {
	private List<HeroForm> heroFormList =new ArrayList<>();

	public List<HeroForm> getHeroFormList() {
		return heroFormList;
	}

	public void setHeroFormList(List<HeroForm> heroFormList) {
		this.heroFormList = heroFormList;
	}
}
