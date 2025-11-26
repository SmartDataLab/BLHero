package com.xiugou.x1.backstage.module.player.struct;

import pojo.xiugou.x1.pojo.module.bag.BagForm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yh
 * @date 2023/7/18
 * @apiNote
 */
public class PlayerBagData {
	private List<BagForm> bagFormList = new ArrayList<>();

	public List<BagForm> getBagFormList() {
		return bagFormList;
	}

	public void setBagFormList(List<BagForm> bagFormList) {
		this.bagFormList = bagFormList;
	}
}
