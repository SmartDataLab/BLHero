package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.prefab.task.ITaskConfig;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.ZhanLingGoalCache.ZhanLingGoalConfig;
import com.xiugou.x1.design.module.autogen.ZhanLingGoalAbstractCache;

@Component
public class ZhanLingGoalCache extends ZhanLingGoalAbstractCache<ZhanLingGoalConfig> {
	
	public static class ZhanLingGoalConfig extends ZhanLingGoalAbstractCache.ZhanLingGoalCfg implements ITaskConfig {

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
}