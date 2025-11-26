package com.xiugou.x1.design.module;


import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.TrainingExpertCampCache.TrainingExpertCampConfig;
import com.xiugou.x1.design.module.autogen.DungeonAbstractCache.DungeonCfg;
import com.xiugou.x1.design.module.autogen.FunctionOpenAbstractCache;
import com.xiugou.x1.design.module.autogen.MainlineSceneAbstractCache.MainlineSceneCfg;
import com.xiugou.x1.design.module.autogen.MainlineTaskAbstractCache.MainlineTaskCfg;
import com.xiugou.x1.design.module.autogen.PlayerLevelAbstractCache.PlayerLevelCfg;
import com.xiugou.x1.design.struct.Keyv;

@org.springframework.stereotype.Component
public class FunctionOpenCache extends FunctionOpenAbstractCache<FunctionOpenAbstractCache.FunctionOpenCfg> {

	@Autowired
	private PlayerLevelCache playerLevelCache;
	@Autowired
	private MainlineSceneCache mainlineSceneCache;
	@Autowired
	private TrainingExpertCampCache trainingExpertCampCache;
	@Autowired
	private DungeonCache dungeonCache;
	@Autowired
	private MainlineTaskCache mainlineTaskCache;
	
	@Override
	protected boolean check() {
		
//		1 角色等级：       1#角色等级
//		2 主线场景ID：     2#场景ID
//		3 高级训练营等级： 3#等级
//		4 开服天数：       4#开服天数
//		5 通关副本：       5#副本ID
//		6 接取指定任务：   6#任务ID
		boolean hasError = false;
		for(FunctionOpenCfg cfg : this.all()) {
			for(Keyv keyv : cfg.getOpenCondition()) {
				if(keyv.getKey() == 1) {
					PlayerLevelCfg config = playerLevelCache.getOrNull(keyv.getValue());
					if(config == null) {
//						logger.error("功能开启配置ID={}，未找到{}中等级为{}的配置", cfg.getId(), playerLevelCache.fileName(), keyv.getValue());
//						hasError = true;
					}
				} else if(keyv.getKey() == 2) {
					MainlineSceneCfg config = mainlineSceneCache.getOrNull(keyv.getValue());
					if(config == null) {
						logger.error("功能开启配置ID={}，未找到{}中ID为{}的配置", cfg.getId(), mainlineSceneCache.fileName(), keyv.getValue());
						hasError = true;
					}
				} else if(keyv.getKey() == 3) {
					TrainingExpertCampConfig config = trainingExpertCampCache.getOrNull(keyv.getValue());
					if(config == null) {
						logger.error("功能开启配置ID={}，未找到{}中ID为{}的配置", cfg.getId(), trainingExpertCampCache.fileName(), keyv.getValue());
						hasError = true;
					}
				} else if(keyv.getKey() == 4) {
					if(keyv.getValue() < 0) {
						logger.error("功能开启配置ID={}，开服天数参数不能小于0", cfg.getId());
						hasError = true;
					}
				} else if(keyv.getKey() == 5) {
					DungeonCfg config = dungeonCache.getOrNull(keyv.getValue());
					if(config == null) {
						logger.error("功能开启配置ID={}，未找到{}中ID为{}的配置", cfg.getId(), dungeonCache.fileName(), keyv.getValue());
						hasError = true;
					}
				} else if(keyv.getKey() == 6) {
					MainlineTaskCfg config = mainlineTaskCache.getOrNull(keyv.getValue());
					if(config == null) {
						logger.error("功能开启配置ID={}，未找到{}中ID为{}的配置", cfg.getId(), mainlineTaskCache.fileName(), keyv.getValue());
						hasError = true;
					}
				} else {
					logger.error("功能开启配置ID={}，这是什么类型{}", cfg.getId(), keyv.getKey());
					hasError = true;
				}
			}
		}
		return !hasError;
	}
	
	
}