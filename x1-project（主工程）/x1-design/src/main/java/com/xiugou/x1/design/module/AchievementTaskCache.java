package com.xiugou.x1.design.module;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gaming.prefab.task.ITaskConfig;

import com.xiugou.x1.design.module.AchievementTaskCache.AchievementTaskConfig;
import com.xiugou.x1.design.module.autogen.AchievementTaskAbstractCache;

@org.springframework.stereotype.Component
public class AchievementTaskCache extends AchievementTaskAbstractCache<AchievementTaskConfig> {
	
	public static class AchievementTaskConfig extends AchievementTaskAbstractCache.AchievementTaskCfg implements ITaskConfig {

		@Override
		public String getIdentity() {
			return this.taskType;
		}

		@Override
		public List<Integer> getNeedConditions() {
			return this.taskParams;
		}

		@Override
		public long getTargetNum() {
			return this.taskTargetNum;
		}
	}
	
	public Map<String, ArrayList<AchievementTaskConfig>> getTaskTypeCollector() {
		return this.taskTypeCollector;
	}
}