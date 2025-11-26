package com.xiugou.x1.design.module.autogen;


public abstract class QiRiMuBiaoTaskAbstractCache<T extends QiRiMuBiaoTaskAbstractCache.QiRiMuBiaoTaskCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1007七日目标_QiRiMuBiaoTask";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> dayCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引dayCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> dayCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = dayCollector.get(data.getDay());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				dayCollector.put(data.getDay(), collector);
			}
			collector.add(data);
		}
		this.dayCollector = dayCollector;
	}



	public final java.util.ArrayList<T> getInDayCollector(int day) {
		java.util.ArrayList<T> ts = dayCollector.get(day);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("QiRiMuBiaoTaskCache.getInDayCollector", day);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInDayCollector(int day) {
		java.util.ArrayList<T> ts = dayCollector.get(day);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class QiRiMuBiaoTaskCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 任务ID
		 */
		protected int id;
		/**
		 * 天数
		 */
		protected int day;
		/**
		 * 任务类型
		 */
		protected String taskType;
		/**
		 * 任务参数
		 */
		protected java.util.List<Integer> taskParams;
		/**
		 * 任务完成目标数量
		 */
		protected long taskTargetNum;
		/**
		 * 完成积分
		 */
		protected int score;
		/**
		 * 任务奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getDay() {
			return day;
		}
		public String getTaskType() {
			return taskType;
		}
		public java.util.List<Integer> getTaskParams() {
			return taskParams;
		}
		public long getTaskTargetNum() {
			return taskTargetNum;
		}
		public int getScore() {
			return score;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}