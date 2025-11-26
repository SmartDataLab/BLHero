package com.xiugou.x1.design.module.autogen;


public abstract class SysMainTemplateAbstractCache<T extends SysMainTemplateAbstractCache.SysMainTemplateCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "F福利-类型配置表_SysMainTemplate";
	}
	protected java.util.HashMap<Integer, T> logicTypeIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引logicTypeIndex
		java.util.HashMap<Integer, T> logicTypeIndex = new java.util.HashMap<Integer, T>();
		for(T data : all()) {
			logicTypeIndex.put(data.getLogicType(), data);
		}
		this.logicTypeIndex = logicTypeIndex;
	}

	public final T getInLogicTypeIndex(int logicType) {
		T t = logicTypeIndex.get(logicType);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("SysMainTemplateCache.getInLogicTypeIndex", logicType);
		}
		return t;
	}

	public final T findInLogicTypeIndex(int logicType) {
		T t = logicTypeIndex.get(logicType);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class SysMainTemplateCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 系统ID（唯一）
		 */
		protected int id;
		/**
		 * 活动逻辑分类
		 */
		protected int logicType;
		/**
		 * 事件名称
		 */
		protected String name;
		/**
		 * 跨服类型
		 */
		protected int crossType;
		/**
		 * 是否隐藏按钮
		 */
		protected int active;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getLogicType() {
			return logicType;
		}
		public String getName() {
			return name;
		}
		public int getCrossType() {
			return crossType;
		}
		public int getActive() {
			return active;
		}
	}

}