/**
 * 
 */
package org.gaming.prefab.thing;

import java.util.List;

/**
 * @author YY
 *
 */
public class ExpRewardReceipt extends RewardReceipt {
	private int oldLevel;
	private int currLevel;
	
	public ExpRewardReceipt() {}
	public ExpRewardReceipt(List<? extends IRewardThing> rewardThings, int oldLevel, int currLevel) {
		super(rewardThings);
		this.oldLevel = oldLevel;
		this.currLevel = currLevel;
	}
	
	public int getOldLevel() {
		return oldLevel;
	}
	public void setOldLevel(int oldLevel) {
		this.oldLevel = oldLevel;
	}
	public int getCurrLevel() {
		return currLevel;
	}
	public void setCurrLevel(int currLevel) {
		this.currLevel = currLevel;
	}
}
