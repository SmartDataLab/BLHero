package com.xiugou.x1.design.module;


import org.gaming.ruler.util.DropUtil.IDrop;

import com.xiugou.x1.design.module.EquipRateCache.EquipRateConfig;
import com.xiugou.x1.design.module.autogen.EquipRateAbstractCache;

@org.springframework.stereotype.Component
public class EquipRateCache extends EquipRateAbstractCache<EquipRateConfig> {
	
	public static class EquipRateConfig extends EquipRateAbstractCache.EquipRateCfg implements IDrop {

		@Override
		public int getDropId() {
			return this.id;
		}

		@Override
		public int getDropRate() {
			return this.weight;
		}
		
	}
}