package com.xiugou.x1.design.module.autogen;


public abstract class FairylandAbstractCache<T extends FairylandAbstractCache.FairylandCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "X仙境守卫(村庄)_Fairyland";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class FairylandCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 仙境关卡Id
		 */
		protected int id;
		/**
		 * 通关奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing reward;
		/**
		 * 神像血量
		 */
		protected int godHP;
		/**
		 * 栅栏血量
		 */
		protected int fenceHP;
		/**
		 * 栅栏消耗
		 */
		protected com.xiugou.x1.design.struct.CostThing fenceBuild;
		/**
		 * 随机奖励
		 */
		protected java.util.List<Integer> randomReward;
		/**
		 * 展示奖励
		 */
		protected java.util.List<Integer> showRewrad;
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
		public int getGodHP() {
			return godHP;
		}
		public int getFenceHP() {
			return fenceHP;
		}
		public com.xiugou.x1.design.struct.CostThing getFenceBuild() {
			return fenceBuild;
		}
		public java.util.List<Integer> getRandomReward() {
			return randomReward;
		}
		public java.util.List<Integer> getShowRewrad() {
			return showRewrad;
		}
	}

}