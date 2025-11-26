package com.xiugou.x1.design.module.autogen;


public abstract class SensitiveWordsAbstractCache<T extends SensitiveWordsAbstractCache.SensitiveWordsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "M敏感词库表统计_SensitiveWords";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class SensitiveWordsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * ID
		 */
		protected int id;
		/**
		 * 敏感词汇
		 */
		protected String word;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getWord() {
			return word;
		}
	}

}