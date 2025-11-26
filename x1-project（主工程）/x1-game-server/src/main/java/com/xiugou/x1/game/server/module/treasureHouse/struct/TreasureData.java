package com.xiugou.x1.game.server.module.treasureHouse.struct;

import org.gaming.tool.GsonUtil;

/**
 * @author yh
 * @date 2023/8/24
 * @apiNote
 */
public class TreasureData {
    private int shopId;  		//商店ID
    private int chooseReward;	//自选道具ID

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

	public int getChooseReward() {
		return chooseReward;
	}

	public void setChooseReward(int chooseReward) {
		this.chooseReward = chooseReward;
	}
	
	public static void main(String[] args) {
		//{"shopId":0,"chooseReward":0}
		System.out.println(GsonUtil.toJson(new TreasureData()));
	}
}
