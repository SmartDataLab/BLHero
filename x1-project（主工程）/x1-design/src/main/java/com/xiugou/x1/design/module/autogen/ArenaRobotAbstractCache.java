package com.xiugou.x1.design.module.autogen;


public abstract class ArenaRobotAbstractCache<T extends ArenaRobotAbstractCache.ArenaRobotCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "J竞技场机器人表_ArenaRobot";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> seasonCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引seasonCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> seasonCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = seasonCollector.get(data.getSeason());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				seasonCollector.put(data.getSeason(), collector);
			}
			collector.add(data);
		}
		this.seasonCollector = seasonCollector;
	}



	public final java.util.ArrayList<T> getInSeasonCollector(int season) {
		java.util.ArrayList<T> ts = seasonCollector.get(season);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ArenaRobotCache.getInSeasonCollector", season);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInSeasonCollector(int season) {
		java.util.ArrayList<T> ts = seasonCollector.get(season);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ArenaRobotCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 赛季
		 */
		protected int season;
		/**
		 * 积分小
		 */
		protected int scoreMin;
		/**
		 * 积分大
		 */
		protected int scoreMax;
		/**
		 * 排名上
		 */
		protected int rankUp;
		/**
		 * 排名下
		 */
		protected int rankDown;
		/**
		 * 等级
		 */
		protected int level;
		/**
		 * 怪物数量
		 */
		protected int monsterNum;
		/**
		 * 怪物ID
		 */
		protected java.util.List<Integer> monsterIds;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getSeason() {
			return season;
		}
		public int getScoreMin() {
			return scoreMin;
		}
		public int getScoreMax() {
			return scoreMax;
		}
		public int getRankUp() {
			return rankUp;
		}
		public int getRankDown() {
			return rankDown;
		}
		public int getLevel() {
			return level;
		}
		public int getMonsterNum() {
			return monsterNum;
		}
		public java.util.List<Integer> getMonsterIds() {
			return monsterIds;
		}
	}

}