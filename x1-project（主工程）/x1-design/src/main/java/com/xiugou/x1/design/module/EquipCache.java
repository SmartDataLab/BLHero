package com.xiugou.x1.design.module;


import org.springframework.beans.factory.annotation.Autowired;

import com.xiugou.x1.design.module.autogen.EquipAbstractCache;
import com.xiugou.x1.design.module.autogen.ItemAbstractCache.ItemCfg;

@org.springframework.stereotype.Component
public class EquipCache extends EquipAbstractCache<EquipAbstractCache.EquipCfg> {

	@Autowired
	private ItemCache itemCache;
	
	@Override
	protected boolean check() {
		boolean hasError = false;
		for(EquipCfg equipCfg : this.all()) {
			ItemCfg itemCfg = itemCache.getOrNull(equipCfg.getEquipId());
			if(itemCfg == null) {
				logger.error("{}中未找到id为{}的装备配置", itemCache.fileName(), equipCfg.getEquipId());
//				hasError = true;
			}
		}
		return !hasError;
	}
}