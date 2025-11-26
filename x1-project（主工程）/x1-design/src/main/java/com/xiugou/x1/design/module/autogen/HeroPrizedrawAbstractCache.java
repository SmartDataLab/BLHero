package com.xiugou.x1.design.module.autogen;


public abstract class HeroPrizedrawAbstractCache<T extends HeroPrizedrawAbstractCache.HeroPrizedrawCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y英雄抽取表_HeroPrizedraw";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class HeroPrizedrawCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 品质
		 */
		protected int quality;
		/**
		 * 抽取权重
		 */
		protected int weight;
		/**
		 * 碎片
		 */
		protected java.util.List<Integer> fragment;
		/**
		 * 积分
		 */
		protected long points;
		/**
		 * 抽奖多少次后必定中
		 */
		protected int gottaNum;
		/**
		 * 提升阶段变化数量
		 */
		protected int upStageNum;
		@Override
		public int id() {
			return quality;
		}
		public int getQuality() {
			return quality;
		}
		public int getWeight() {
			return weight;
		}
		public java.util.List<Integer> getFragment() {
			return fragment;
		}
		public long getPoints() {
			return points;
		}
		public int getGottaNum() {
			return gottaNum;
		}
		public int getUpStageNum() {
			return upStageNum;
		}
	}

}