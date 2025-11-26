package com.xiugou.x1.design.module;


import java.util.List;

import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.config.IBattleConstCache;
import com.xiugou.x1.design.module.autogen.BattleConstAbstractCache;

@org.springframework.stereotype.Component
public class BattleConstCache extends BattleConstAbstractCache<BattleConstAbstractCache.BattleConstCfg> implements IBattleConstCache {

	@Override
	public int getFinalNormalFactor() {
		return this.final_normal_factor;
	}

	@Override
	public List<Integer> getFinalSkillFactor() {
		return this.final_skill_factor;
	}

	@Override
	public List<Integer> getHitRange() {
		return this.hit_range;
	}

	@Override
	public List<Integer> getCritRange() {
		return this.crit_range;
	}

	@Override
	public int getDmgFloat() {
		return this.dmg_float;
	}

	@Override
	public List<Attr> getMonsterInitAttr() {
		return this.monster_init_attr;
	}
}