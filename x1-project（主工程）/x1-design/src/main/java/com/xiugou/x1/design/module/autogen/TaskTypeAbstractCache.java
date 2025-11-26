package com.xiugou.x1.design.module.autogen;


public abstract class TaskTypeAbstractCache<T extends TaskTypeAbstractCache.TaskTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "R任务类型说明表_TaskType";
	}
	protected java.util.HashMap<String, T> typeIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeIndex
		java.util.HashMap<String, T> typeIndex = new java.util.HashMap<String, T>();
		for(T data : all()) {
			typeIndex.put(data.getType(), data);
		}
		this.typeIndex = typeIndex;
	}

	public final T getInTypeIndex(String type) {
		T t = typeIndex.get(type);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TaskTypeCache.getInTypeIndex", type);
		}
		return t;
	}

	public final T findInTypeIndex(String type) {
		T t = typeIndex.get(type);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class TaskTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 任务类型
		 */
		protected String type;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getType() {
			return type;
		}
	}

}