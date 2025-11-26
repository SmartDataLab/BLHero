package com.xiugou.x1.design.module.autogen;


public abstract class NpcNatterAbstractCache<T extends NpcNatterAbstractCache.NpcNatterCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "NNpc闲聊表_NpcNatter";
	}

	protected java.util.HashMap<Integer, java.util.ArrayList<T>> npcIdCollector;

	@Override
	protected final void loadAutoGenerate() {
		//构建索引npcIdCollector
		java.util.HashMap<Integer, java.util.ArrayList<T>> npcIdCollector = new java.util.HashMap<Integer, java.util.ArrayList<T>>();
		for(T data : all()) {
			java.util.ArrayList<T> collector = npcIdCollector.get(data.getNpcId());
			if(collector == null) {
				collector = new java.util.ArrayList<>();
				npcIdCollector.put(data.getNpcId(), collector);
			}
			collector.add(data);
		}
		this.npcIdCollector = npcIdCollector;
	}



	public final java.util.ArrayList<T> getInNpcIdCollector(int npcId) {
		java.util.ArrayList<T> ts = npcIdCollector.get(npcId);
		if(ts == null) {
			throw new org.gaming.design.exception.DesignNotFoundException("NpcNatterCache.getInNpcIdCollector", npcId);
		}
		return ts;
	}

	public final java.util.ArrayList<T> findInNpcIdCollector(int npcId) {
		java.util.ArrayList<T> ts = npcIdCollector.get(npcId);
		if(ts == null) {
			return null;
		}
		return ts;
	}

	public static class NpcNatterCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 序号
		 */
		protected int id;
		/**
		 * npcId
		 */
		protected int npcId;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public int getNpcId() {
			return npcId;
		}
	}

}