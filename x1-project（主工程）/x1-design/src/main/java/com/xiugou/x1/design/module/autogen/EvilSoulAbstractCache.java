package com.xiugou.x1.design.module.autogen;


public abstract class EvilSoulAbstractCache<T extends EvilSoulAbstractCache.EvilSoulCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y妖魂_EvilSoul";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class EvilSoulCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 妖魂ID
		 */
		protected int id;
		/**
		 * 名称
		 */
		protected String name;
		/**
		 * 品质
		 */
		protected int quality;
		/**
		 * 炼化时长/时
		 */
		protected int refineTime;
		/**
		 * 固定奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> reward;
		/**
		 * 权重奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RandomItem> wightReward;
		/**
		 * 随机奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RandomItem> randomReward;
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
		public int getQuality() {
			return quality;
		}
		public int getRefineTime() {
			return refineTime;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getReward() {
			return reward;
		}
		public java.util.List<com.xiugou.x1.design.struct.RandomItem> getWightReward() {
			return wightReward;
		}
		public java.util.List<com.xiugou.x1.design.struct.RandomItem> getRandomReward() {
			return randomReward;
		}
	}

}