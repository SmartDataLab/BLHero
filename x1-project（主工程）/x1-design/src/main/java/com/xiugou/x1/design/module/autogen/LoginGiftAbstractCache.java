package com.xiugou.x1.design.module.autogen;


public abstract class LoginGiftAbstractCache<T extends LoginGiftAbstractCache.LoginGiftCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "D登录豪礼表_LoginGift";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class LoginGiftCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 天数
		 */
		protected int id;
		/**
		 * 奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing reward;
		/**
		 * 是否大奖
		 */
		protected int bigGift;
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
		public int getBigGift() {
			return bigGift;
		}
	}

}