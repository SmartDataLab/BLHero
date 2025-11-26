package com.xiugou.x1.design.module;


import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.battle.config.IMonsterConfig;
import com.xiugou.x1.design.module.autogen.DungeonMonsterAbstractCache;

@org.springframework.stereotype.Component
public class DungeonMonsterCache extends DungeonMonsterAbstractCache<DungeonMonsterAbstractCache.DungeonMonsterCfg> {

	@Autowired
	private MonsterCache monsterCache;

	@Override
	protected boolean check() {
		boolean correct = true;
		for(DungeonMonsterCfg cfg : this.all()) {
			IMonsterConfig monsterConfig = monsterCache.getConfig(cfg.getMonsterId());
			if(monsterConfig == null) {
				logger.error("{}未找到怪物ID为{}的怪物配置", fileName(), cfg.getMonsterId());
				correct = false;
			}
		}
		return correct;
	}
	
	
}