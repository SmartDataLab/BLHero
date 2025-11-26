/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.struct;

import pb.xiugou.x1.protobuf.promotion.P1004TongTianTaTeQuan.PbTTTTQDetail;

/**
 * @author YY
 * 通天塔特权详情
 */
public class TTTTQDetail {
	private int towerType;
	private int round;
	private boolean openHigh;
	private int normalRewardLv;
	private int highRewardLv;
	public int getTowerType() {
		return towerType;
	}
	public void setTowerType(int towerType) {
		this.towerType = towerType;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public boolean isOpenHigh() {
		return openHigh;
	}
	public void setOpenHigh(boolean openHigh) {
		this.openHigh = openHigh;
	}
	public int getNormalRewardLv() {
		return normalRewardLv;
	}
	public void setNormalRewardLv(int normalRewardLv) {
		this.normalRewardLv = normalRewardLv;
	}
	public int getHighRewardLv() {
		return highRewardLv;
	}
	public void setHighRewardLv(int highRewardLv) {
		this.highRewardLv = highRewardLv;
	}
	
	public PbTTTTQDetail build() {
		PbTTTTQDetail.Builder builder = PbTTTTQDetail.newBuilder();
		builder.setTowerType(this.getTowerType());
		builder.setRound(this.getRound());
		builder.setOpenHigh(this.isOpenHigh());
		builder.setNormalRewardLv(this.getNormalRewardLv());
		builder.setHighRewardLv(this.getHighRewardLv());
		return builder.build();
	}
}
