package com.xiugou.x1.design.module.autogen;


public abstract class PlotAbstractCache<T extends PlotAbstractCache.PlotCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J剧情配置表_Plot";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PlotCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 对话ID
		 */
		protected int id;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
	}

}