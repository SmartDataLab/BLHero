package com.xiugou.x1.design.module.autogen;


public abstract class EquipattrStashAbstractCache<T extends EquipattrStashAbstractCache.EquipattrStashCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z装备附加属性库_EquipattrStash";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> stashIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引stashIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> stashIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = stashIdCollector.get(data.getStashId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				stashIdCollector.put(data.getStashId(), collector);
			}
			collector.add(data);
		}
		this.stashIdCollector = stashIdCollector;
	}



	public final java.util.ArrayList<T> getInStashIdCollector(int stashId) {
		java.util.ArrayList<T> ts = stashIdCollector.get(stashId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("EquipattrStashCache.getInStashIdCollector", stashId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInStashIdCollector(int stashId) {
		java.util.ArrayList<T> ts = stashIdCollector.get(stashId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class EquipattrStashCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 库id
		 */
		protected int stashId;
		/**
		 * 属性类型
		 */
		protected int type;
		/**
		 * Max属性值
		 */
		protected int max;
		/**
		 * 随机权重
		 */
		protected int weight;
		/**
		 * 评分
		 */
		protected int score;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getStashId() {
			return stashId;
		}
		public int getType() {
			return type;
		}
		public int getMax() {
			return max;
		}
		public int getWeight() {
			return weight;
		}
		public int getScore() {
			return score;
		}
	}

}