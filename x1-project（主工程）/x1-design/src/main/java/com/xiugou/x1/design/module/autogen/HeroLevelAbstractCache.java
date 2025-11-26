package com.xiugou.x1.design.module.autogen;


public abstract class HeroLevelAbstractCache<T extends HeroLevelAbstractCache.HeroLevelCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Y英雄等级表_HeroLevel";
	}
	protected java.util.HashMap<Integer, java.util.HashMap<Integer, T>> heroIdentityLevelIndex;

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> heroIdentityCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引heroIdentityLevelIndex
		java.util.HashMap<Integer, java.util.HashMap<Integer, T>> heroIdentityLevelIndex = new java.util.HashMap<Integer, java.util.HashMap<Integer, T>>();
		for(T data : all()) {
			java.util.HashMap<Integer, T> layer1Map = heroIdentityLevelIndex.get(data.getHeroIdentity());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, T>();
				heroIdentityLevelIndex.put(data.getHeroIdentity(), layer1Map);
			}
			layer1Map.put(data.getLevel(), data);
		}
		this.heroIdentityLevelIndex = heroIdentityLevelIndex;
		//构建索引heroIdentityCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> heroIdentityCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = heroIdentityCollector.get(data.getHeroIdentity());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				heroIdentityCollector.put(data.getHeroIdentity(), collector);
			}
			collector.add(data);
		}
		this.heroIdentityCollector = heroIdentityCollector;
	}

	public final T getInHeroIdentityLevelIndex(int heroIdentity, int level) {
		java.util.HashMap<Integer, T> layer1Map = heroIdentityLevelIndex.get(heroIdentity);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HeroLevelCache.getInHeroIdentityLevelIndex", heroIdentity, level);
		}
		T t = layer1Map.get(level);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HeroLevelCache.getInHeroIdentityLevelIndex", heroIdentity, level);
		}
		return t;
	}

	public final T findInHeroIdentityLevelIndex(int heroIdentity, int level) {
		java.util.HashMap<Integer, T> layer1Map = heroIdentityLevelIndex.get(heroIdentity);
		if(layer1Map == null) {
			return null;
		}
		T t = layer1Map.get(level);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInHeroIdentityCollector(int heroIdentity) {
		java.util.ArrayList<T> ts = heroIdentityCollector.get(heroIdentity);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("HeroLevelCache.getInHeroIdentityCollector", heroIdentity);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInHeroIdentityCollector(int heroIdentity) {
		java.util.ArrayList<T> ts = heroIdentityCollector.get(heroIdentity);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class HeroLevelCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 英雄标识
		 */
		protected int heroIdentity;
		/**
		 * 等级
		 */
		protected int level;
		/**
		 * 下一级序号
		 */
		protected int nextId;
		/**
		 * 消耗本体数量
		 */
		protected int costBody;
		/**
		 * 除本体外的消耗
		 */
		protected java.util.List<com.xiugou.x1.design.struct.CostThing> costMaterial;
		/**
		 * 是不是突破升级类型
		 */
		protected int breakLevel;
		/**
		 * 技能
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> skills;
		/**
		 * 全局属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> globalAttribute;
		/**
		 * 升级属性
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> levelAttribute;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getHeroIdentity() {
			return heroIdentity;
		}
		public int getLevel() {
			return level;
		}
		public int getNextId() {
			return nextId;
		}
		public int getCostBody() {
			return costBody;
		}
		public java.util.List<com.xiugou.x1.design.struct.CostThing> getCostMaterial() {
			return costMaterial;
		}
		public int getBreakLevel() {
			return breakLevel;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getSkills() {
			return skills;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getGlobalAttribute() {
			return globalAttribute;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getLevelAttribute() {
			return levelAttribute;
		}
	}

}