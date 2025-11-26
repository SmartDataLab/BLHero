package com.xiugou.x1.design.module.autogen;


public abstract class BattleAttrAbstractCache<T extends BattleAttrAbstractCache.BattleAttrCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "4战斗属性表_BattleAttr";
	}
	protected java.util.HashMap<String, T> attrNameIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引attrNameIndex
		java.util.HashMap<String, T> attrNameIndex = new java.util.HashMap<String, T>();
		for(T data : all()) {
			attrNameIndex.put(data.getAttrName(), data);
		}
		this.attrNameIndex = attrNameIndex;
	}

	public final T getInAttrNameIndex(String attrName) {
		T t = attrNameIndex.get(attrName);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("BattleAttrCache.getInAttrNameIndex", attrName);
		}
		return t;
	}

	public final T findInAttrNameIndex(String attrName) {
		T t = attrNameIndex.get(attrName);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class BattleAttrCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 属性ID
		 */
		protected int id;
		/**
		 * 属性字段名
		 */
		protected String attrName;
		/**
		 * 运算时/xx
		 */
		protected int type;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getAttrName() {
			return attrName;
		}
		public int getType() {
			return type;
		}
	}

}