package com.xiugou.x1.design.module.autogen;


public abstract class ActiveTemplateAbstractCache<T extends ActiveTemplateAbstractCache.ActiveTemplateCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "H活动-类型配置表_ActiveTemplate";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> logicTypeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引logicTypeCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> logicTypeCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = logicTypeCollector.get(data.getLogicType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				logicTypeCollector.put(data.getLogicType(), collector);
			}
			collector.add(data);
		}
		this.logicTypeCollector = logicTypeCollector;
	}



	public final java.util.ArrayList<T> getInLogicTypeCollector(int logicType) {
		java.util.ArrayList<T> ts = logicTypeCollector.get(logicType);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("ActiveTemplateCache.getInLogicTypeCollector", logicType);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInLogicTypeCollector(int logicType) {
		java.util.ArrayList<T> ts = logicTypeCollector.get(logicType);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class ActiveTemplateCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 活动ID（唯一）
		 */
		protected int id;
		/**
		 * 活动逻辑分类
		 */
		protected int logicType;
		/**
		 * 活动名称
		 */
		protected String name;
		/**
		 * 跨服类型
		 */
		protected int crossType;
		/**
		 * 是否开启
		 */
		protected int status;
		/**
		 * 开始时间
		 */
		protected String startTime;
		/**
		 * 结束时间
		 */
		protected String endTime;
		/**
		 * 活动类型
		 */
		protected int opType;
		/**
		 * 活动参数
		 */
		protected java.util.List<Integer> openParams;
		/**
		 * 持续天数
		 */
		protected int openDuration;
		/**
		 * 开启周期
		 */
		protected java.util.List<Integer> openPeriod;
		/**
		 * 开服天数 
		 */
		protected int openDay;
		/**
		 * 拓展字段2
		 */
		protected String content2;
		/**
		 * 渠道ID
		 */
		protected String channelId;
		/**
		 * 最小等级
		 */
		protected int minLevel;
		/**
		 * 屏蔽充值
		 */
		protected int hide;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getLogicType() {
			return logicType;
		}
		public String getName() {
			return name;
		}
		public int getCrossType() {
			return crossType;
		}
		public int getStatus() {
			return status;
		}
		public String getStartTime() {
			return startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public int getOpType() {
			return opType;
		}
		public java.util.List<Integer> getOpenParams() {
			return openParams;
		}
		public int getOpenDuration() {
			return openDuration;
		}
		public java.util.List<Integer> getOpenPeriod() {
			return openPeriod;
		}
		public int getOpenDay() {
			return openDay;
		}
		public String getContent2() {
			return content2;
		}
		public String getChannelId() {
			return channelId;
		}
		public int getMinLevel() {
			return minLevel;
		}
		public int getHide() {
			return hide;
		}
	}

}