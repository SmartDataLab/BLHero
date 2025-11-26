package com.xiugou.x1.design.module;


import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.config.IHeroLevelConfig;
import com.xiugou.x1.battle.config.IHeroLevelConfigCache;
import com.xiugou.x1.design.module.autogen.HeroLevelAbstractCache;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;
import com.xiugou.x1.design.module.autogen.SkillLevelAbstractCache.SkillLevelCfg;
import com.xiugou.x1.design.struct.Keyv;

@org.springframework.stereotype.Component
public class HeroLevelCache extends HeroLevelAbstractCache<HeroLevelCache.HeroLevelConfig> implements IHeroLevelConfigCache {

	@Autowired
	private HeroTypeCache heroTypeCache;
	@Autowired
	private SkillLevelCache skillLevelCache;
	
	public static class HeroLevelConfig extends HeroLevelAbstractCache.HeroLevelCfg implements IHeroLevelConfig {
		private BattleAttr troopAttr;

		public BattleAttr getTroopAttr() {
			return troopAttr;
		}
	}
	
	@Override
	public IHeroLevelConfig getConfig(int identity, int level) {
		return this.getInHeroIdentityLevelIndex(identity, level);
	}

	@Override
	protected void loadAfterReady() {
		for(ArrayList<HeroLevelConfig> levelConfigs : this.heroIdentityCollector.values()) {
			for(HeroLevelConfig levelConfig : levelConfigs) {
				BattleAttr troopAttr = new BattleAttr();
				for(HeroLevelConfig temp : levelConfigs) {
					if(levelConfig.getLevel() < temp.getLevel()) {
						continue;
					}
					for(Attr attr : temp.getGlobalAttribute()) {
						troopAttr.addById(attr.getAttrId(), attr.getValue());
					}
				}
				levelConfig.troopAttr = troopAttr;
			}
		}
	}
	
	@Override
	protected void loadAfterAllReady() {
		for(HeroTypeCfg cfg : heroTypeCache.all()) {
			Map<Integer, HeroLevelConfig> map = this.heroIdentityLevelIndex.get(cfg.getId());
			if(map == null) {
//				logger.error("未找到英雄{}-{}的等级配置", cfg.getId(), cfg.getName());
				continue;
			}
		}
		for(HeroLevelConfig cfg : this.all()) {
			for(Keyv skill : cfg.getSkills()) {
				if(skill.getLevel() <= 0) {
					continue;
				}
				SkillLevelCfg skillLevelCfg = skillLevelCache.findInSkillIdLevelIndex(skill.getKey(), skill.getValue());
				if(skillLevelCfg == null) {
//					logger.error("未找到技能{}-{}的等级配置", skill.getKey(), skill.getValue());
				}
			}
		}
		super.loadAfterAllReady();
	}
}