/**
 * 
 */
package com.xiugou.x1.design.struct;

import org.gaming.prefab.thing.IRewardThing;

/**
 * @author YY
 *
 */
public class RewardThing implements IRewardThing {
	private int item;
	private long num;
	
	public static RewardThing of(int item, long num) {
		RewardThing thing = new RewardThing();
		thing.item = item;
		thing.num = num;
		return thing;
	}
	@Override
	public int getThingId() {
		return item;
	}
	@Override
	public long getNum() {
		return num;
	}
}
