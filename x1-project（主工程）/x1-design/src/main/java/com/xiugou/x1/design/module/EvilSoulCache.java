package com.xiugou.x1.design.module;

import java.util.ArrayList;
import java.util.List;

import org.gaming.tool.SortUtil;

import com.xiugou.x1.design.module.autogen.EvilSoulAbstractCache;

@org.springframework.stereotype.Component
public class EvilSoulCache extends EvilSoulAbstractCache<EvilSoulAbstractCache.EvilSoulCfg> {
	private List<EvilSoulCfg> sortList;

	@Override
	protected void loadAfterReady() {
		sortList = new ArrayList<>(all());
		SortUtil.sortIntDesc(sortList, EvilSoulCfg::getQuality);
	}

	public List<EvilSoulCfg> getSortList() {
		return sortList;
	}
}