package com.xiugou.x1.design.module.autogen;


public abstract class OutputPathAbstractCache<T extends OutputPathAbstractCache.OutputPathCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J奖励产出途径表_OutputPath";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class OutputPathCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * id
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