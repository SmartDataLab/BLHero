package com.xiugou.x1.design.module.autogen;


public abstract class ItemRandomPackAbstractCache<T extends ItemRandomPackAbstractCache.ItemRandomPackCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "D掉落表_ItemRandomPack";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> groupCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引groupCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> groupCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = groupCollector.get(data.getGroup());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				groupCollector.put(data.getGroup(), collector);
			}
			collector.add(data);
		}
		this.groupCollector = groupCollector;
	}



	public final java.util.ArrayList<T> getInGroupCollector(int group) {
		java.util.ArrayList<T> ts = groupCollector.get(group);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ItemRandomPackCache.getInGroupCollector", group);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInGroupCollector(int group) {
		java.util.ArrayList<T> ts = groupCollector.get(group);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ItemRandomPackCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 掉落组ID
		 */
		protected int group;
		/**
		 *  权重
		 */
		protected int weight;
		/**
		 * 掉落奖励(概率#道具id#数量|)
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RandomItem> reward;
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
		public int getWeight() {
			return weight;
		}
		public java.util.List<com.xiugou.x1.design.struct.RandomItem> getReward() {
			return reward;
		}
	}

}