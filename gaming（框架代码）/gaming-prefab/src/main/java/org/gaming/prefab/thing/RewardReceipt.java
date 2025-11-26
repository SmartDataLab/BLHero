/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class RewardReceipt {

	public static final RewardReceipt EMPTY = new RewardReceipt();
	
	private List<RewardDetail> details = new ArrayList<>();
	//备注
	private String remark = "";
	
	public RewardReceipt() {}
	
	public RewardReceipt(List<? extends IRewardThing> rewardThings) {
		for(IRewardThing thing : rewardThings) {
			RewardDetail detail = new RewardDetail(thing.getThingId(), thing.getNum());
			details.add(detail);
		}
	}
	
	public RewardReceipt append(RewardDetail detail) {
		details.add(detail);
		return this;
	}
	
	public void merge(RewardReceipt rewardReceipt) {
		this.details.addAll(rewardReceipt.details);
	}
	
	public List<RewardDetail> getDetails() {
		return details;
	}
	
	public static class RewardDetail {
		private int thingId;
		private long num;
		private Object extra;
		
		public RewardDetail(int thingId, long num, Object extra) {
			this.thingId = thingId;
			this.num = num;
			this.extra = extra;
		}
		public RewardDetail(int thingId, long num) {
			this(thingId, num, null);
		}
		
		public int getThingId() {
			return thingId;
		}
		public long getNum() {
			return num;
		}
		public Object getExtra() {
			return extra;
		}
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
