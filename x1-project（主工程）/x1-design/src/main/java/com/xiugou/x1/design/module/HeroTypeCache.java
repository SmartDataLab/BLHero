package com.xiugou.x1.design.module;


import com.xiugou.x1.battle.config.IHeroTypeConfig;
import com.xiugou.x1.battle.config.IHeroTypeConfigCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;

@org.springframework.stereotype.Component
public class HeroTypeCache extends HeroTypeAbstractCache<HeroTypeConfig> implements IHeroTypeConfigCache {
	
	public static class HeroTypeConfig extends HeroTypeCfg implements IHeroTypeConfig {
		
	}

	@Override
	public IHeroTypeConfig getConfig(int identity) {
		return this.getOrThrow(identity);
	}
}