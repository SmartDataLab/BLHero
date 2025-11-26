package com.xiugou.x1.design.module.autogen;


public abstract class DrawTypeAbstractCache<T extends DrawTypeAbstractCache.DrawTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C抽奖类型表_DrawType";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class DrawTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 抽奖类型
		 */
		protected int id;
		/**
		 * 免费次数
		 */
		protected int freeNum;
		/**
		 * 广告次数
		 */
		protected int advNum;
		/**
		 * 单抽消耗道具
		 */
		protected com.xiugou.x1.design.struct.CostThing oneCostItem;
		/**
		 * 单抽消耗仙玉
		 */
		protected com.xiugou.x1.design.struct.CostThing oneCostDiamond;
		/**
		 * 十抽仙玉折扣
		 */
		protected int tenDiscount;
		/**
		 * 积分奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing point;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getFreeNum() {
			return freeNum;
		}
		public int getAdvNum() {
			return advNum;
		}
		public com.xiugou.x1.design.struct.CostThing getOneCostItem() {
			return oneCostItem;
		}
		public com.xiugou.x1.design.struct.CostThing getOneCostDiamond() {
			return oneCostDiamond;
		}
		public int getTenDiscount() {
			return tenDiscount;
		}
		public com.xiugou.x1.design.struct.RewardThing getPoint() {
			return point;
		}
	}

}