package com.xiugou.x1.design.module.autogen;


public abstract class PrivilegeBoostTypeAbstractCache<T extends PrivilegeBoostTypeAbstractCache.PrivilegeBoostTypeCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "VIP特权加成类型表_PrivilegeBoostType";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PrivilegeBoostTypeCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 特权类型
		 */
		protected int type;
		/**
		 * 加成途径
		 */
		protected java.util.List<Integer> path;
		@Override
		public int id() {
			return type;
		}
		public int getType() {
			return type;
		}
		public java.util.List<Integer> getPath() {
			return path;
		}
	}

}