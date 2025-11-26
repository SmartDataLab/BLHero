package com.xiugou.x1.design.module.autogen;


public abstract class GiftGroupAbstractCache<T extends GiftGroupAbstractCache.GiftGroupCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L礼品组_GiftGroup";
	}

	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> typeGroupIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeGroupIdCollector
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> typeGroupIdCollector = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = typeGroupIdCollector.get(data.getType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
				typeGroupIdCollector.put(data.getType(), layer1Map);
			}
			java.util.ArrayList<T> collector = layer1Map.get(data.getGroupId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				layer1Map.put(data.getGroupId(), collector);
			}
			collector.add(data);
		}
		this.typeGroupIdCollector = typeGroupIdCollector;
	}



	public final java.util.ArrayList<T> getInTypeGroupIdCollector(int type, int groupId) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = typeGroupIdCollector.get(type);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("GiftGroupCache.getInTypeGroupIdCollector", type, groupId);
		}
		java.util.ArrayList<T> ts = layer1Map.get(groupId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("GiftGroupCache.getInTypeGroupIdCollector", type, groupId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTypeGroupIdCollector(int type, int groupId) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = typeGroupIdCollector.get(type);
		if(layer1Map == null) {
			return null;
		}
		java.util.ArrayList<T> ts = layer1Map.get(groupId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class GiftGroupCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * id
		 */
		protected int id;
		/**
		 * 类型
		 */
		protected int type;
		/**
		 * 礼品组
		 */
		protected int groupId;
		/**
		 * 等级
		 */
		protected int level;
		/**
		 * 固定奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> fixationReward;
		/**
		 * 随机奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RandomItem> randomReward;
		/**
		 * 自选奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> optionReward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getType() {
			return type;
		}
		public int getGroupId() {
			return groupId;
		}
		public int getLevel() {
			return level;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getFixationReward() {
			return fixationReward;
		}
		public java.util.List<com.xiugou.x1.design.struct.RandomItem> getRandomReward() {
			return randomReward;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getOptionReward() {
			return optionReward;
		}
	}

}