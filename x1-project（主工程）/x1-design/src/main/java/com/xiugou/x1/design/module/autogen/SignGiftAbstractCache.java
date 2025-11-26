package com.xiugou.x1.design.module.autogen;


public abstract class SignGiftAbstractCache<T extends SignGiftAbstractCache.SignGiftCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Q签到表_SignGift";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class SignGiftCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 天数
		 */
		protected int id;
		/**
		 * 奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing reward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public com.xiugou.x1.design.struct.RewardThing getReward() {
			return reward;
		}
	}

}