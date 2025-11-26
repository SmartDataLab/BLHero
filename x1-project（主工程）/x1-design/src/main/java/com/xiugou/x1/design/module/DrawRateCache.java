package com.xiugou.x1.design.module;


import org.gaming.ruler.util.DropUtil.IDrop;

import com.xiugou.x1.design.module.DrawRateCache.DrawRateConfig;
import com.xiugou.x1.design.module.autogen.DrawRateAbstractCache;

@org.springframework.stereotype.Component
public class DrawRateCache extends DrawRateAbstractCache<DrawRateConfig> {
	
	public static class DrawRateConfig extends DrawRateAbstractCache.DrawRateCfg implements IDrop {

		@Override
		public int getDropId() {
			return this.drawId;
		}

		@Override
		public int getDropRate() {
			return this.weight;
		}
	}
}