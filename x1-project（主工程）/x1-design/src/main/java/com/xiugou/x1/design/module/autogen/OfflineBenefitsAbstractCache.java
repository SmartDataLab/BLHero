package com.xiugou.x1.design.module.autogen;


public abstract class OfflineBenefitsAbstractCache<T extends OfflineBenefitsAbstractCache.OfflineBenefitsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "L离线收益表_OfflineBenefits";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> monsterCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引monsterCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> monsterCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = monsterCollector.get(data.getMonster());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				monsterCollector.put(data.getMonster(), collector);
			}
			collector.add(data);
		}
		this.monsterCollector = monsterCollector;
	}



	public final java.util.ArrayList<T> getInMonsterCollector(int monster) {
		java.util.ArrayList<T> ts = monsterCollector.get(monster);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("OfflineBenefitsCache.getInMonsterCollector", monster);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInMonsterCollector(int monster) {
		java.util.ArrayList<T> ts = monsterCollector.get(monster);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class OfflineBenefitsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 怪物id
		 */
		protected int monster;
		/**
		 * 道具id
		 */
		protected int item;
		/**
		 * 最小数量
		 */
		protected float min;
		/**
		 * 最大数量
		 */
		protected float max;
		/**
		 * 基础时间（单位：分）
		 */
		protected int time;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getMonster() {
			return monster;
		}
		public int getItem() {
			return item;
		}
		public float getMin() {
			return min;
		}
		public float getMax() {
			return max;
		}
		public int getTime() {
			return time;
		}
	}

}