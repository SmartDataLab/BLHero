package com.xiugou.x1.design.module;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.autogen.DungeonAbstractCache;
import com.xiugou.x1.design.module.autogen.SceneZoneAbstractCache.SceneZoneCfg;

@org.springframework.stereotype.Component
public class DungeonCache extends DungeonAbstractCache<DungeonAbstractCache.DungeonCfg> {

	@Autowired
	private SceneZoneCache sceneZoneCache;
	@Autowired
	private DungeonMonsterCache dungeonMonsterCache;
	
	@Override
	protected boolean check() {
		boolean correct = true;
		for(DungeonCfg cfg : this.all()) {
			List<SceneZoneCfg> list = sceneZoneCache.getInSceneIdCollector(cfg.getSceneId());
			if(list == null) {
				logger.error("{}未找到场景配置{}", fileName(), cfg.getSceneId());
				correct = false;
			}
			for(int i = 0; i < list.size(); i++) {
				dungeonMonsterCache.findInSceneIdZoneIdRefreshIdIndex(cfg.getId(), i + 1, 1);
			}
			
		}
		return correct;
	}
}