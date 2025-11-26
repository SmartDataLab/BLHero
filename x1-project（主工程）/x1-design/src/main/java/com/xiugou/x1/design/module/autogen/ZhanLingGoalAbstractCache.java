package com.xiugou.x1.design.module.autogen;


public abstract class ZhanLingGoalAbstractCache<T extends ZhanLingGoalAbstractCache.ZhanLingGoalCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "P1002目标类战令奖励_ZhanLingGoal";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> activityIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引activityIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> activityIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = activityIdCollector.get(data.getActivityId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				activityIdCollector.put(data.getActivityId(), collector);
			}
			collector.add(data);
		}
		this.activityIdCollector = activityIdCollector;
	}



	public final java.util.ArrayList<T> getInActivityIdCollector(int activityId) {
		java.util.ArrayList<T> ts = activityIdCollector.get(activityId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ZhanLingGoalCache.getInActivityIdCollector", activityId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInActivityIdCollector(int activityId) {
		java.util.ArrayList<T> ts = activityIdCollector.get(activityId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ZhanLingGoalCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 任务ID
		 */
		protected int id;
		/**
		 * 活动类型ID
		 */
		protected int activityId;
		/**
		 * 排序编号
		 */
		protected int sortIndex;
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
		 * 免费奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> freeReward;
		/**
		 * 高级奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> premiumReward;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getActivityId() {
			return activityId;
		}
		public int getSortIndex() {
			return sortIndex;
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
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getFreeReward() {
			return freeReward;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getPremiumReward() {
			return premiumReward;
		}
	}

}