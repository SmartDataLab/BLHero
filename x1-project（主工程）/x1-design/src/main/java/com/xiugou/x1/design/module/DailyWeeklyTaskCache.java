package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.prefab.task.ITaskConfig;

import com.xiugou.x1.design.module.autogen.DailyWeeklyTaskAbstractCache;

@org.springframework.stereotype.Component
public class DailyWeeklyTaskCache extends DailyWeeklyTaskAbstractCache<DailyWeeklyTaskCache.DailyWeeklyTaskConfig> {
    public static class DailyWeeklyTaskConfig extends DailyWeeklyTaskAbstractCache.DailyWeeklyTaskCfg implements ITaskConfig {

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