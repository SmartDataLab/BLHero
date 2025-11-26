package com.xiugou.x1.design.module;


import java.util.Set;

import com.xiugou.x1.design.module.autogen.HandbookCatalogAbstractCache;

@org.springframework.stereotype.Component
public class HandbookCatalogCache extends HandbookCatalogAbstractCache<HandbookCatalogAbstractCache.HandbookCatalogCfg> {
	
	public Set<Integer> getTypes() {
		return this.typeCollector.keySet();
	}
	
}