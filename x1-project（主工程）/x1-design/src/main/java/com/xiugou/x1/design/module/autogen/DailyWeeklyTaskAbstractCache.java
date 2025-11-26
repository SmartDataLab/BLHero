package com.xiugou.x1.design.module.autogen;


public abstract class DailyWeeklyTaskAbstractCache<T extends DailyWeeklyTaskAbstractCache.DailyWeeklyTaskCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "R日常周常任务表_DailyWeeklyTask";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引typeCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> typeCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = typeCollector.get(data.getType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				typeCollector.put(data.getType(), collector);
			}
			collector.add(data);
		}
		this.typeCollector = typeCollector;
	}



	public final java.util.ArrayList<T> getInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DailyWeeklyTaskCache.getInTypeCollector", type);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTypeCollector(int type) {
		java.util.ArrayList<T> ts = typeCollector.get(type);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class DailyWeeklyTaskCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 任务ID
		 */
		protected int id;
		/**
		 * 日常周常
		 */
		protected int type;
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
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewardPoints;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getType() {
			return type;
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
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewardPoints() {
			return rewardPoints;
		}
	}

}