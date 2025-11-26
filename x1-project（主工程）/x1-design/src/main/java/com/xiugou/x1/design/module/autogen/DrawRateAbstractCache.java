package com.xiugou.x1.design.module.autogen;


public abstract class DrawRateAbstractCache<T extends DrawRateAbstractCache.DrawRateCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "C抽奖概率表_DrawRate";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> drawTypeCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引drawTypeCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> drawTypeCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = drawTypeCollector.get(data.getDrawType());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				drawTypeCollector.put(data.getDrawType(), collector);
			}
			collector.add(data);
		}
		this.drawTypeCollector = drawTypeCollector;
	}



	public final java.util.ArrayList<T> getInDrawTypeCollector(int drawType) {
		java.util.ArrayList<T> ts = drawTypeCollector.get(drawType);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("DrawRateCache.getInDrawTypeCollector", drawType);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInDrawTypeCollector(int drawType) {
		java.util.ArrayList<T> ts = drawTypeCollector.get(drawType);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class DrawRateCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 抽奖类型
		 */
		protected int drawType;
		/**
		 * 奖励组
		 */
		protected int group;
		/**
		 * 奖项ID
		 */
		protected int drawId;
		/**
		 * 数量上限
		 */
		protected int num;
		/**
		 * 抽取权重
		 */
		protected int weight;
		/**
		 * 抽取下限
		 */
		protected int drawMayme;
		/**
		 * 抽取上限
		 */
		protected int drawMust;
		/**
		 * 要求开服天数
		 */
		protected int openDay;
		/**
		 * 广播ID
		 */
		protected int marqueeId;
		/**
		 * 抽中是否特殊展示
		 */
		protected int needShow;
		/**
		 * 纳入条件
		 */
		protected int precondition;
		/**
		 * 每日抽取次数下限
		 */
		protected int drawNumMin;
		/**
		 * 每日抽取次数上限
		 */
		protected int drawNumMax;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getDrawType() {
			return drawType;
		}
		public int getGroup() {
			return group;
		}
		public int getDrawId() {
			return drawId;
		}
		public int getNum() {
			return num;
		}
		public int getWeight() {
			return weight;
		}
		public int getDrawMayme() {
			return drawMayme;
		}
		public int getDrawMust() {
			return drawMust;
		}
		public int getOpenDay() {
			return openDay;
		}
		public int getMarqueeId() {
			return marqueeId;
		}
		public int getNeedShow() {
			return needShow;
		}
		public int getPrecondition() {
			return precondition;
		}
		public int getDrawNumMin() {
			return drawNumMin;
		}
		public int getDrawNumMax() {
			return drawNumMax;
		}
	}

}