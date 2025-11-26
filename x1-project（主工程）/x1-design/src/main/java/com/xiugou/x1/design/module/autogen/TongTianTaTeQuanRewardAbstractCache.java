package com.xiugou.x1.design.module.autogen;


public abstract class TongTianTaTeQuanRewardAbstractCache<T extends TongTianTaTeQuanRewardAbstractCache.TongTianTaTeQuanRewardCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1004特权系统通天塔层数奖励_TongTianTaTeQuanReward";
	}

	protected java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> towerTypeRoundCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引towerTypeRoundCollector
		java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>> towerTypeRoundCollector = new java.util.HashMap<Integer, java.util.HashMap<Integer, java.util.ArrayList<T>>>();
		for(T data : all()) {
			java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = towerTypeRoundCollector.get(data.getTowerType());
			if(layer1Map == null) {
				layer1Map = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
				towerTypeRoundCollector.put(data.getTowerType(), layer1Map);
			}
			java.util.ArrayList<T> collector = layer1Map.get(data.getRound());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				layer1Map.put(data.getRound(), collector);
			}
			collector.add(data);
		}
		this.towerTypeRoundCollector = towerTypeRoundCollector;
	}



	public final java.util.ArrayList<T> getInTowerTypeRoundCollector(int towerType, int round) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = towerTypeRoundCollector.get(towerType);
		if(layer1Map == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TongTianTaTeQuanRewardCache.getInTowerTypeRoundCollector", towerType, round);
		}
		java.util.ArrayList<T> ts = layer1Map.get(round);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("TongTianTaTeQuanRewardCache.getInTowerTypeRoundCollector", towerType, round);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTowerTypeRoundCollector(int towerType, int round) {
		java.util.HashMap<Integer, java.util.ArrayList<T>> layer1Map = towerTypeRoundCollector.get(towerType);
		if(layer1Map == null) {
			return null;
		}
		java.util.ArrayList<T> ts = layer1Map.get(round);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class TongTianTaTeQuanRewardCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 塔类型
		 */
		protected int towerType;
		/**
		 * 奖励期数
		 */
		protected int round;
		/**
		 * 奖励等级
		 */
		protected int rewardLv;
		/**
		 * 所需达到层数
		 */
		protected int layer;
		/**
		 * 普通挑战奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing normalReward;
		/**
		 * 高级挑战奖励
		 */
		protected com.xiugou.x1.design.struct.RewardThing highReward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getTowerType() {
			return towerType;
		}
		public int getRound() {
			return round;
		}
		public int getRewardLv() {
			return rewardLv;
		}
		public int getLayer() {
			return layer;
		}
		public com.xiugou.x1.design.struct.RewardThing getNormalReward() {
			return normalReward;
		}
		public com.xiugou.x1.design.struct.RewardThing getHighReward() {
			return highReward;
		}
	}

}