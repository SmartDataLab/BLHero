package com.xiugou.x1.game.server.module.shop.struct;

/**
 * @author yh
 * @date 2023/7/28
 * @apiNote
 */
public class ProductDetailData {
	private int productId; //商品ID
	private int buyNum; //本轮购买的数量
	private int freeNum; //免费领取次数
	private int advNum; //广告次数

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public int getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}

	public int getAdvNum() {
		return advNum;
	}

	public void setAdvNum(int advNum) {
		this.advNum = advNum;
	}
}
