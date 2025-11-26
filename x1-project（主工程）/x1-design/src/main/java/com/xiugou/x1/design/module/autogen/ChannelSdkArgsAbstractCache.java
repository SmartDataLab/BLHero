package com.xiugou.x1.design.module.autogen;


public abstract class ChannelSdkArgsAbstractCache<T extends ChannelSdkArgsAbstractCache.ChannelSdkArgsCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "0渠道对接参数_ChannelSdkArgs";
	}


	@Override
	protected final void loadAutoGenerate() {
		//构建常量
		java.util.HashMap<String, String> constMap = new java.util.HashMap<String, String>();
		for(T data : all()) {
			constMap.put(data.getKeyCol(), data.getValueCol());
		}
		this.loadConst(constMap);
	}





	public static class ChannelSdkArgsCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * 键名
		 */
		protected String keyCol;
		/**
		 * 值类型
		 */
		protected String typeCol;
		/**
		 * 值
		 */
		protected String valueCol;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getKeyCol() {
			return keyCol;
		}
		public String getTypeCol() {
			return typeCol;
		}
		public String getValueCol() {
			return valueCol;
		}
	}

	protected String yile_game_id;
	public String getYile_game_id() {
		return yile_game_id;
	}
	protected String yile_product_key;
	public String getYile_product_key() {
		return yile_product_key;
	}
	protected String yile_recharge_key;
	public String getYile_recharge_key() {
		return yile_recharge_key;
	}
	protected String yile_login_url;
	public String getYile_login_url() {
		return yile_login_url;
	}
	protected String wabo_game_id;
	public String getWabo_game_id() {
		return wabo_game_id;
	}
	protected String wabo_endpoint;
	public String getWabo_endpoint() {
		return wabo_endpoint;
	}
	protected String wabo_token_url;
	public String getWabo_token_url() {
		return wabo_token_url;
	}
	protected String wabo_orderquery_url;
	public String getWabo_orderquery_url() {
		return wabo_orderquery_url;
	}
	protected String wabo_callback_url;
	public String getWabo_callback_url() {
		return wabo_callback_url;
	}
	protected String hql_game_id;
	public String getHql_game_id() {
		return hql_game_id;
	}
	protected String hql_game_key;
	public String getHql_game_key() {
		return hql_game_key;
	}
	protected String hql_login_url;
	public String getHql_login_url() {
		return hql_login_url;
	}
}