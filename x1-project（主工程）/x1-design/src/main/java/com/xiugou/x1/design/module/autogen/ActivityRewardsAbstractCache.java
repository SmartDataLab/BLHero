package com.xiugou.x1.design.module.autogen;


public abstract class ActivityRewardsAbstractCache<T extends ActivityRewardsAbstractCache.ActivityRewardsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "H活动-奖励配置表_ActivityRewards";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> templateIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引templateIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> templateIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = templateIdCollector.get(data.getTemplateId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				templateIdCollector.put(data.getTemplateId(), collector);
			}
			collector.add(data);
		}
		this.templateIdCollector = templateIdCollector;
	}



	public final java.util.ArrayList<T> getInTemplateIdCollector(int templateId) {
		java.util.ArrayList<T> ts = templateIdCollector.get(templateId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ActivityRewardsCache.getInTemplateIdCollector", templateId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInTemplateIdCollector(int templateId) {
		java.util.ArrayList<T> ts = templateIdCollector.get(templateId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ActivityRewardsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 任务ID
		 */
		protected int id;
		/**
		 * 活动ID
		 */
		protected int templateId;
		/**
		 * 显示顺序
		 */
		protected int index;
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
		 * 自选奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> selectRewards;
		/**
		 * 固定奖励
		 */
		protected java.util.List<com.xiugou.x1.design.struct.RewardThing> rewards;
		/**
		 * 描述
		 */
		protected String desc;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getTemplateId() {
			return templateId;
		}
		public int getIndex() {
			return index;
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
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getSelectRewards() {
			return selectRewards;
		}
		public java.util.List<com.xiugou.x1.design.struct.RewardThing> getRewards() {
			return rewards;
		}
		public String getDesc() {
			return desc;
		}
	}

}