package com.xiugou.x1.design.module.autogen;


public abstract class GameHelpAbstractCache<T extends GameHelpAbstractCache.GameHelpCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "B帮助说明_GameHelp";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class GameHelpCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 属性ID
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