package com.xiugou.x1.design.module.autogen;


public abstract class AchievementTaskAbstractCache<T extends AchievementTaskAbstractCache.AchievementTaskCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C成就任务表_AchievementTask";
	}
	protected java.util.HashMap<Integer, T> preTaskIndex;

	protected java.util.HashMap<String, java.util.ArrayList<T>> taskTypeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引preTaskIndex
		java.util.HashMap<Integer, T> preTaskIndex = new java.util.HashMap<Integer, T>();
		for(T data : all()) {
			preTaskIndex.put(data.getPreTask(), data);
		}
		this.preTaskIndex = preTaskIndex;
		//构建索引taskTypeCollector
		java.util.HashMap<String, java.util.ArrayList<T>> taskTypeCollector = new java.util.HashMap<String, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = taskTypeCollector.get(data.getTaskType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				taskTypeCollector.put(data.getTaskType(), collector);
			}
			collector.add(data);
		}
		this.taskTypeCollector = taskTypeCollector;
	}

	public final T getInPreTaskIndex(int preTask) {
		T t = preTaskIndex.get(preTask);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("AchievementTaskCache.getInPreTaskIndex", preTask);
		}
		return t;
	}

	public final T findInPreTaskIndex(int preTask) {
		T t = preTaskIndex.get(preTask);
		if(t == null) {
			return null;
		}
		return t;
	}

	public final java.util.ArrayList<T> getInTaskTypeCollector(String taskType) {
		java.util.ArrayList<T> ts = taskTypeCollector.get(taskType);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("AchievementTaskCache.getInTaskTypeCollector", taskType);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTaskTypeCollector(String taskType) {
		java.util.ArrayList<T> ts = taskTypeCollector.get(taskType);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class AchievementTaskCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 任务ID
		 */
		protected int id;
		/**
		 * 前置任务
		 */
		protected int preTask;
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
		 * 奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getPreTask() {
			return preTask;
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
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
	}

}