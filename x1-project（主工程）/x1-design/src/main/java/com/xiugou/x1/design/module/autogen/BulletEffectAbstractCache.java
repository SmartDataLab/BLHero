package com.xiugou.x1.design.module.autogen;


public abstract class BulletEffectAbstractCache<T extends BulletEffectAbstractCache.BulletEffectCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "2子弹表_BulletEffect";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class BulletEffectCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 子弹ID
		 */
		protected int id;
		/**
		 * 作用范围
		 */
		protected String rangeWay;
		/**
		 * 附加buff
		 */
		protected String buffs;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getRangeWay() {
			return rangeWay;
		}
		public String getBuffs() {
			return buffs;
		}
	}

}