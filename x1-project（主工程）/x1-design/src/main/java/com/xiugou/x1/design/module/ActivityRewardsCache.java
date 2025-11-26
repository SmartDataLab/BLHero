package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.prefab.task.ITaskConfig;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.ActivityRewardsCache.ActivityRewardsConfig;
import com.xiugou.x1.design.module.autogen.ActivityRewardsAbstractCache;

@Component
public class ActivityRewardsCache extends ActivityRewardsAbstractCache<ActivityRewardsConfig> {
	
	public static class ActivityRewardsConfig extends ActivityRewardsAbstractCache.ActivityRewardsCfg implements ITaskConfig {

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