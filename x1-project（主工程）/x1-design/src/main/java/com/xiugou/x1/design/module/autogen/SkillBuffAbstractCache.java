package com.xiugou.x1.design.module.autogen;


public abstract class SkillBuffAbstractCache<T extends SkillBuffAbstractCache.SkillBuffCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "Buff表_SkillBuff";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class SkillBuffCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * buffID
		 */
		protected int id;
		/**
		 * buff名称
		 */
		protected String name;
		/**
		 * buff功能ID：是标识时不同的功能，

		 */
		protected int buffEnum;
		/**
		 * 属性参数
		 */
		protected java.util.List<com.xiugou.x1.battle.config.Attr> attrParams;
		/**
		 * 状态系列：
不同类型的buff统一都是共同作用
		 */
		protected int series;
		/**
		 * buff执行优先级
		 */
		protected int priority;
		/**
		 * 叠加方式
1、覆盖：同类型buff，优先级高的覆盖优先级低的
2、叠加：同类型buff，共存叠加层数
3、共存，buff共同各自作用

		 */
		protected int stackWay;
		/**
		 * 最大叠加层数：
		 */
		protected int maxStack;
		/**
		 * 层数叠满触发的buff
		 */
		protected java.util.List<Integer> maxStackTrigger;
		/**
		 * BUFF机制参数
		 */
		protected java.util.List<Integer> intParams;
		/**
		 * 作用范围
		 */
		protected String rangeWay;
		/**
		 * 作用对象
		 */
		protected String targetType;
		/**
		 * 目标数量上限
		 */
		protected String targetLimit;
		/**
		 * 持续时间毫秒
		 */
		protected int lastTime;
		/**
		 * 增减益
		 */
		protected int gain_or_loss;
		/**
		 * 驱散方式
		 */
		protected int remove_type;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public int getBuffEnum() {
			return buffEnum;
		}
		public java.util.List<com.xiugou.x1.battle.config.Attr> getAttrParams() {
			return attrParams;
		}
		public int getSeries() {
			return series;
		}
		public int getPriority() {
			return priority;
		}
		public int getStackWay() {
			return stackWay;
		}
		public int getMaxStack() {
			return maxStack;
		}
		public java.util.List<Integer> getMaxStackTrigger() {
			return maxStackTrigger;
		}
		public java.util.List<Integer> getIntParams() {
			return intParams;
		}
		public String getRangeWay() {
			return rangeWay;
		}
		public String getTargetType() {
			return targetType;
		}
		public String getTargetLimit() {
			return targetLimit;
		}
		public int getLastTime() {
			return lastTime;
		}
		public int getGain_or_loss() {
			return gain_or_loss;
		}
		public int getRemove_type() {
			return remove_type;
		}
	}

}