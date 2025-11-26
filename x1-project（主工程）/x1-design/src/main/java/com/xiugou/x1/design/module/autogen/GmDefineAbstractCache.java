package com.xiugou.x1.design.module.autogen;


public abstract class GmDefineAbstractCache<T extends GmDefineAbstractCache.GmDefineCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "GGm指令说明_GmDefine";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> funcIdxCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引funcIdxCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> funcIdxCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = funcIdxCollector.get(data.getFuncIdx());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				funcIdxCollector.put(data.getFuncIdx(), collector);
			}
			collector.add(data);
		}
		this.funcIdxCollector = funcIdxCollector;
	}



	public final java.util.ArrayList<T> getInFuncIdxCollector(int funcIdx) {
		java.util.ArrayList<T> ts = funcIdxCollector.get(funcIdx);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("GmDefineCache.getInFuncIdxCollector", funcIdx);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInFuncIdxCollector(int funcIdx) {
		java.util.ArrayList<T> ts = funcIdxCollector.get(funcIdx);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class GmDefineCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int idx;
		/**
		 * 功能序号
		 */
		protected int funcIdx;
		/**
		 * GM指令
		 */
		protected String gmName;
		/**
		 * 调用示例，注意参数之间有英文空格
		 */
		protected String example;
		@Override
		public int id() {
			return idx;
		}
		public int getIdx() {
			return idx;
		}
		public int getFuncIdx() {
			return funcIdx;
		}
		public String getGmName() {
			return gmName;
		}
		public String getExample() {
			return example;
		}
	}

}