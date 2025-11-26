package com.xiugou.x1.design.module;


import org.springframework.stereotype.Component;

import com.xiugou.x1.design.module.MeiRiChongZhiCache.MeiRiChongZhiConfig;
import com.xiugou.x1.design.module.autogen.MeiRiChongZhiAbstractCache;

@Component
public class MeiRiChongZhiCache extends MeiRiChongZhiAbstractCache<MeiRiChongZhiConfig> {
	
	public static class MeiRiChongZhiConfig extends MeiRiChongZhiAbstractCache.MeiRiChongZhiCfg {
		
		public String getDescLang() {
			return "MeiRiChongZhi_Desc_" + id;
		}
	}
}