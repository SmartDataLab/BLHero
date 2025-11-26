package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.prefab.task.ITaskConfig;

import com.xiugou.x1.design.module.autogen.MainlineTaskAbstractCache;

@org.springframework.stereotype.Component
public class MainlineTaskCache extends MainlineTaskAbstractCache<MainlineTaskCache.MainlineTaskConfig> {

    public static class MainlineTaskConfig extends MainlineTaskAbstractCache.MainlineTaskCfg implements ITaskConfig {

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