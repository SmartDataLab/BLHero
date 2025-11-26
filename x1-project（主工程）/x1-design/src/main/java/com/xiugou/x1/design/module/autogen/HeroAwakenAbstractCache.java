package com.xiugou.x1.design.module.autogen;


public abstract class HeroAwakenAbstractCache<T extends HeroAwakenAbstractCache.HeroAwakenCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y英雄觉醒表_HeroAwaken";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> qualityLevelIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引qualityLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> qualityLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = qualityLevelIndex.get(data.getQuality());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				qualityLevelIndex.put(data.getQuality(), layer1Map);
			}
			layer1Map.put(data.getLevel(), data);
		}
		this.qualityLevelIndex = qualityLevelIndex;
	}

	public final T getInQualityLevelIndex(int quality, int level) {
		java.util.HashMap<Integer, T> layer1Map = qualityLevelIndex.get(quality);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HeroAwakenCache.getInQualityLevelIndex", quality, level);
		}
		T t = layer1Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HeroAwakenCache.getInQualityLevelIndex", quality, level);
		}
		return t;
	}

	public final T findInQualityLevelIndex(int quality, int level) {
		java.util.HashMap<Integer, T> layer1Map = qualityLevelIndex.get(quality);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class HeroAwakenCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 英雄品质
		 */
		protected int quality;
		/**
		 * 觉醒等级
		 */
		protected int level;
		/**
		 * 消耗材料
		 */
		protected java.util.List<com.xiugou.x1.design.struct.CostThing> cost;
		/**
		 * 消耗本体数量
		 */
		protected int needBody;
		/**
		 * 指定英雄碎片品质
		 */
		protected int otherQuality;
		/**
		 * 数量
		 */
		protected int otherBody;
		/**
		 * 属性加成
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attr;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getQuality() {
			return quality;
		}
		public int getLevel() {
			return level;
		}
		public java.util.List<com.xiugou.x1.design.struct.CostThing> getCost() {
			return cost;
		}
		public int getNeedBody() {
			return needBody;
		}
		public int getOtherQuality() {
			return otherQuality;
		}
		public int getOtherBody() {
			return otherBody;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttr() {
			return attr;
		}
	}

}