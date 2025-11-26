package com.xiugou.x1.design.module.autogen;


public abstract class GuideAbstractCache<T extends GuideAbstractCache.GuideCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "X引导配置表_Guide";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> groupSubIdIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引groupSubIdIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> groupSubIdIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = groupSubIdIndex.get(data.getGroup());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				groupSubIdIndex.put(data.getGroup(), layer1Map);
			}
			layer1Map.put(data.getSubId(), data);
		}
		this.groupSubIdIndex = groupSubIdIndex;
	}

	public final T getInGroupSubIdIndex(int group, int subId) {
		java.util.HashMap<Integer, T> layer1Map = groupSubIdIndex.get(group);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("GuideCache.getInGroupSubIdIndex", group, subId);
		}
		T t = layer1Map.get(subId);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("GuideCache.getInGroupSubIdIndex", group, subId);
		}
		return t;
	}

	public final T findInGroupSubIdIndex(int group, int subId) {
		java.util.HashMap<Integer, T> layer1Map = groupSubIdIndex.get(group);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(subId);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class GuideCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 引导组ID
		 */
		protected int group;
		/**
		 * 步骤
		 */
		protected int subId;
		/**
		 * 引导类型
		 */
		protected int type;
		/**
		 * 备注
		 */
		protected String beizhu;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getGroup() {
			return group;
		}
		public int getSubId() {
			return subId;
		}
		public int getType() {
			return type;
		}
		public String getBeizhu() {
			return beizhu;
		}
	}

}