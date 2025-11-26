package com.xiugou.x1.design.module.autogen;


public abstract class HandbookLevelAbstractCache<T extends HandbookLevelAbstractCache.HandbookLevelCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "T图鉴等级表_HandbookLevel";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> qualityUpTypeLevelIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引qualityUpTypeLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>> qualityUpTypeLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.HashMap<Integer, T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = qualityUpTypeLevelIndex.get(data.getQuality());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
				qualityUpTypeLevelIndex.put(data.getQuality(), layer1Map);
			}
			java.util.HashMap<Integer, T> layer2Map = layer1Map.get(data.getUpType());
			if(layer2Map == null) {
				layer2Map = new java.util.HashMap<Integer, T>();
				layer1Map.put(data.getUpType(), layer2Map);
			}
			layer2Map.put(data.getLevel(), data);
		}
		this.qualityUpTypeLevelIndex = qualityUpTypeLevelIndex;
	}

	public final T getInQualityUpTypeLevelIndex(int quality, int upType, int level) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = qualityUpTypeLevelIndex.get(quality);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HandbookLevelCache.getInQualityUpTypeLevelIndex", quality, upType, level);
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(upType);
		if(layer2Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HandbookLevelCache.getInQualityUpTypeLevelIndex", quality, upType, level);
		}
		T t = layer2Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HandbookLevelCache.getInQualityUpTypeLevelIndex", quality, upType, level);
		}
		return t;
	}

	public final T findInQualityUpTypeLevelIndex(int quality, int upType, int level) {
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> layer1Map = qualityUpTypeLevelIndex.get(quality);
		if(layer1Map == null) {
			return null;
		}
		java.util.HashMap<Integer, T> layer2Map = layer1Map.get(upType);
		if(layer2Map == null) {
			return null;
		}
		T t = layer2Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}



	public static class HandbookLevelCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 图鉴品质
		 */
		protected int quality;
		/**
		 * 提升方式
		 */
		protected int upType;
		/**
		 * 图鉴等级
		 */
		protected int level;
		/**
		 * 需要等级
		 */
		protected int needLevel;
		/**
		 * 消耗道具数量
		 */
		protected int needCost;
		/**
		 * 增长积分
		 */
		protected int point;
		/**
		 * 提升属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attrs;
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
		public int getUpType() {
			return upType;
		}
		public int getLevel() {
			return level;
		}
		public int getNeedLevel() {
			return needLevel;
		}
		public int getNeedCost() {
			return needCost;
		}
		public int getPoint() {
			return point;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttrs() {
			return attrs;
		}
	}

}