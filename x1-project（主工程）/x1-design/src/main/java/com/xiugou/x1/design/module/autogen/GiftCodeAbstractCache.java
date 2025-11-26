package com.xiugou.x1.design.module.autogen;


public abstract class GiftCodeAbstractCache<T extends GiftCodeAbstractCache.GiftCodeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "D兑换码礼包表_GiftCode";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class GiftCodeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 兑换码礼包ID
		 */
		protected int id;
		/**
		 * 名字
		 */
		protected String name;
		/**
		 * 礼包可用开始时间
		 */
		protected String startTime;
		/**
		 * 礼包可用结束时间
		 */
		protected String endTime;
		/**
		 * 奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public String getStartTime() {
			return startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}