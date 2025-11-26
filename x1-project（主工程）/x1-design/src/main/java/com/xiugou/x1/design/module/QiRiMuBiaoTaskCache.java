package com.xiugou.x1.design.module;


import java.util.List;

import org.gaming.prefab.task.ITaskConfig;

import com.xiugou.x1.design.module.autogen.QiRiMuBiaoTaskAbstractCache;

@org.springframework.stereotype.Component
public class QiRiMuBiaoTaskCache extends QiRiMuBiaoTaskAbstractCache<QiRiMuBiaoTaskCache.QiRiMuBiaoTaskConfig> {
	
	public static class QiRiMuBiaoTaskConfig extends QiRiMuBiaoTaskAbstractCache.QiRiMuBiaoTaskCfg implements ITaskConfig {

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