package com.xiugou.x1.design.module.autogen;


public abstract class ModelHeightAbstractCache<T extends ModelHeightAbstractCache.ModelHeightCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M模型高度表_ModelHeight";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class ModelHeightCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 模型id
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