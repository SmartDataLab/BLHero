package com.xiugou.x1.design.module.autogen;


public abstract class MainlineTaskAbstractCache<T extends MainlineTaskAbstractCache.MainlineTaskCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Z主线场景任务表_MainlineTask";
	}
	protected java.util.HashMap<Integer, T> preTaskIndex;


	@Override
	protected final void loadAutoGenerate() {
		//构建索引preTaskIndex
		java.util.HashMap<Integer, T> preTaskIndex = new java.util.HashMap<Integer, T>();
		for(T data : all()) {
			preTaskIndex.put(data.getPreTask(), data);
		}
		this.preTaskIndex = preTaskIndex;
	}

	public final T getInPreTaskIndex(int preTask) {
		T t = preTaskIndex.get(preTask);
		if(t == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("MainlineTaskCache.getInPreTaskIndex", preTask);
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



	public static class MainlineTaskCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 任务ID
		 */
		protected int id;
		/**
		 * 主线ID
		 */
		protected int mainlineId;
		/**
		 * 前置任务
		 */
		protected int preTask;
		/**
		 * 任务描述
		 */
		protected String desc;
		/**
		 * 是否为并行任务
		 */
		protected int parallel;
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
		public int getMainlineId() {
			return mainlineId;
		}
		public int getPreTask() {
			return preTask;
		}
		public String getDesc() {
			return desc;
		}
		public int getParallel() {
			return parallel;
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