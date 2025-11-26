package com.xiugou.x1.design.module.autogen;


public abstract class VipAbstractCache<T extends VipAbstractCache.VipCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "VIP配置表_Vip";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class VipCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * vip等级
		 */
		protected int level;
		/**
		 * 贵族经验奖励
		 */
		protected long needExp;
		/**
		 * vip特权
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> privilege;
		/**
		 * vip奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		/**
		 * 现价
		 */
		protected int currentPrice;
		@Override
		public int id() {
			return level;
		}
		public int getLevel() {
			return level;
		}
		public long getNeedExp() {
			return needExp;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getPrivilege() {
			return privilege;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
		public int getCurrentPrice() {
			return currentPrice;
		}
	}

}