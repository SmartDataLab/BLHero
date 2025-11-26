package com.xiugou.x1.design.module.autogen;


public abstract class PurgatorySeasonAbstractCache<T extends PurgatorySeasonAbstractCache.PurgatorySeasonCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L炼狱阵容推荐_PurgatorySeason";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PurgatorySeasonCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 赛季
		 */
		protected int id;
		/**
		 * 推荐英雄
		 */
		protected java.util.List<Integer> suggest;
		/**
		 * 提升道具id
		 */
		protected int drop;
		/**
		 * 品质加成
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> Quality;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public java.util.List<Integer> getSuggest() {
			return suggest;
		}
		public int getDrop() {
			return drop;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getQuality() {
			return Quality;
		}
	}

}