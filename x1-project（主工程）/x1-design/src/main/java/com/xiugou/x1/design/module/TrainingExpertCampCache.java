package com.xiugou.x1.design.module;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.design.module.TrainingExpertCampCache.TrainingExpertCampConfig;
import com.xiugou.x1.design.module.autogen.BattleAttrAbstractCache.BattleAttrCfg;
import com.xiugou.x1.design.module.autogen.TrainingExpertCampAbstractCache;

@org.springframework.stereotype.Component
public class TrainingExpertCampCache extends TrainingExpertCampAbstractCache<TrainingExpertCampConfig> {
	@Autowired
	BattleAttrCache battleAttrCache;

	public static class TrainingExpertCampConfig extends TrainingExpertCampAbstractCache.TrainingExpertCampCfg {
		private BattleAttr levelAttr;

		public BattleAttr getLevelAttr() {
			return levelAttr;
		}
	}

	@Override
	protected void loadAfterAllReady() {
		for (TrainingExpertCampConfig config1 : this.all()) {
			if (config1.getType() != 1) {
				continue;
			}
			BattleAttr levelAttr = new BattleAttr();
			for (TrainingExpertCampConfig config2 : this.all()) {
				if (config2.getType() != 1) {
					continue;
				}
				if (config2.getId() <= config1.getId()) {
					// 累加
					BattleAttrCfg battleAttrCfg = battleAttrCache.getOrNull(config2.getBuff());
					if (battleAttrCfg == null) {
						continue;
					}
					levelAttr.add(battleAttrCfg.getAttrName(), config2.getNum());
				}
			}
			config1.levelAttr = levelAttr;
		}
	}
}