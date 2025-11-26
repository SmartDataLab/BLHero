package com.xiugou.x1.design.module.autogen;


public abstract class LanguageAutoAbstractCache<T extends LanguageAutoAbstractCache.LanguageAutoCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "YLanguageAuto_LanguageAuto";
	}
	protected java.util.HashMap<String, T> langIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引langIdIndex
		java.util.HashMap<String, T> langIdIndex = new java.util.HashMap<String, T>();
		for(T data : all()) {
			langIdIndex.put(data.getLangId(), data);
		}
		this.langIdIndex = langIdIndex;
	}

	public final T getInLangIdIndex(String langId) {
		T t = langIdIndex.get(langId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("LanguageAutoCache.getInLangIdIndex", langId);
		}
		return t;
	}

	public final T findInLangIdIndex(String langId) {
		T t = langIdIndex.get(langId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class LanguageAutoCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 语言ID
		 */
		protected String langId;
		/**
		 * 简体中文
		 */
		protected String cn;
		/**
		 * 英文
		 */
		protected String en;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getLangId() {
			return langId;
		}
		public String getCn() {
			return cn;
		}
		public String getEn() {
			return en;
		}
	}

}