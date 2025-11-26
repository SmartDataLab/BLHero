package com.xiugou.x1.design.module.autogen;


public abstract class BubblingAbstractCache<T extends BubblingAbstractCache.BubblingCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M冒泡配置表_Bubbling";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class BubblingCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 冒泡ID
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