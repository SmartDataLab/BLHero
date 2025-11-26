package com.xiugou.x1.design.module;


import com.xiugou.x1.design.module.autogen.MainlineTreasureBoxAbstractCache;

@org.springframework.stereotype.Component
public class MainlineTreasureBoxCache extends MainlineTreasureBoxAbstractCache<MainlineTreasureBoxAbstractCache.MainlineTreasureBoxCfg> {
	
	
	public MainlineTreasureBoxCfg getTimeBox(int zoneId) {
		return this.findInTypeTypeArgIndex(MlTreasureBoxType.TIME.getValue(), zoneId);
	}
	
	public MainlineTreasureBoxCfg getBossBox(int bossId) {
		return this.findInTypeTypeArgIndex(MlTreasureBoxType.BOSS.getValue(), bossId);
	}
	
	public static enum MlTreasureBoxType {
		BOSS(1),
		TIME(2),
		;
		private final int value;
		private MlTreasureBoxType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
}