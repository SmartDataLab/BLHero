package com.xiugou.x1.design.module;


import com.xiugou.x1.design.module.autogen.ExpLimitAbstractCache;

@org.springframework.stereotype.Component
public class ExpLimitCache extends ExpLimitAbstractCache<ExpLimitAbstractCache.ExpLimitCfg> {
	
	public float getExpRate(int killNum) {
		for(ExpLimitCfg cfg : this.all()) {
			if(cfg.getLimitMin() <= killNum && (killNum <= cfg.getLimitMax() || cfg.getLimitMax() == 0)) {
				return cfg.getExpAdd();
			}
		}
		return 1;
	}
}