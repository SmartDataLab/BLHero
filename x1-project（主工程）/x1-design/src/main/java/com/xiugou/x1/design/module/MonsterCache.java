package com.xiugou.x1.design.module;


import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.attr.IBattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.battle.config.IMonsterConfigCache;
import com.xiugou.x1.design.module.autogen.MonsterAbstractCache;

@org.springframework.stereotype.Component
public class MonsterCache extends MonsterAbstractCache<MonsterCache.MonsterConfig> implements IMonsterConfigCache {

	@Autowired
	private ItemRandomPackCache itemRandomPackCache;
	
	public static class MonsterConfig extends MonsterAbstractCache.MonsterCfg implements IMonsterConfig, IBattleAttr {

		private BattleAttr battleAttr;
		@Override
		public int getConfigId() {
			return id;
		}

		@Override
		public IBattleAttr getPanelAttr() {
			if(battleAttr == null) {
				BattleAttr tempAttr = new BattleAttr();
				for(Attr attr : this.getAttribute()) {
					tempAttr.addById(attr.getAttrId(), attr.getValue());
				}
				battleAttr = tempAttr;
			}
			return battleAttr;
		}
	}
	
	@Override
	public IMonsterConfig getConfig(int id) {
		return this.getOrNull(id);
	}

	@Override
	protected void loadAfterAllReady() {
		for(MonsterConfig monsterConfig : this.all()) {
			for(int randomGroup : monsterConfig.getRandomProduce()) {
				try {
					itemRandomPackCache.randomReward(randomGroup);
				} catch (Exception e) {
					logger.error("怪物配置{}的随机掉落组{}有问题", monsterConfig.getId(), randomGroup);
				}
			}
		}
	}
	
	@Override
	protected boolean check() {
		for(MonsterConfig monsterConfig : this.all()) {
			if(monsterConfig.getAttribute() == null) {
				logger.error("怪物配置{}的属性有问题", monsterConfig.getId());
			}
		}
		return true;
	}
}