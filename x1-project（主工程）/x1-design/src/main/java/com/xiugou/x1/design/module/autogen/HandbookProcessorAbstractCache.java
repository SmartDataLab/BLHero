package com.xiugou.x1.design.module.autogen;


public abstract class HandbookProcessorAbstractCache<T extends HandbookProcessorAbstractCache.HandbookProcessorCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T图鉴收集进度奖励表_HandbookProcessor";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HandbookProcessorCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 等级
		 */
		protected int level;
		/**
		 * 所需积分
		 */
		protected int needPoint;
		/**
		 * 奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return level;
		}
		public int getLevel() {
			return level;
		}
		public int getNeedPoint() {
			return needPoint;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}