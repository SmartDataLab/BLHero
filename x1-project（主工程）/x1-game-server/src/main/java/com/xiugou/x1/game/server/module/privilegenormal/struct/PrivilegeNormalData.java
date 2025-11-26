/**
 * 
 */
package com.xiugou.x1.game.server.module.privilegenormal.struct;

import pb.xiugou.x1.protobuf.privilegenormal.PrivilegeNormal.PbPrivilegeNormalData;

/**
 * @author YY
 *
 */
public class PrivilegeNormalData {
	private int id;
	//每日奖励是否领取，true表示已领取
	private boolean rewarded;
	//特权失效时间，毫秒，0为永久
	private long endTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isRewarded() {
		return rewarded;
	}
	public void setRewarded(boolean rewarded) {
		this.rewarded = rewarded;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public PbPrivilegeNormalData build() {
		PbPrivilegeNormalData.Builder builder = PbPrivilegeNormalData.newBuilder();
		builder.setId(this.getId());
		builder.setRewarded(this.isRewarded());
		builder.setEndTime(this.getEndTime());
		return builder.build();
	}
}
