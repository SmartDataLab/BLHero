package com.xiugou.x1.design.module.autogen;


public abstract class PrivilegeBoostAbstractCache<T extends PrivilegeBoostAbstractCache.PrivilegeBoostCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "VIP特权表_PrivilegeBoost";
	}


	@Override
	protected final void loadAutoGenerate() {
	}





	public static class PrivilegeBoostCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 特权id
		 */
		protected int type;
		/**
		 * 名称
		 */
		protected String name;
		/**
		 * 特权类型
		 */
		protected java.util.List<com.xiugou.x1.design.struct.Keyv> privilege;
		@Override
		public int id() {
			return type;
		}
		public int getType() {
			return type;
		}
		public String getName() {
			return name;
		}
		public java.util.List<com.xiugou.x1.design.struct.Keyv> getPrivilege() {
			return privilege;
		}
	}

}