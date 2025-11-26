/**
 * 
 */
package com.xiugou.x1.design.struct;

import org.gaming.prefab.thing.ICostThing;

import com.xiugou.x1.design.constant.ItemType;

/**
 * @author YY
 *
 */
public class CostThing implements ICostThing {
	private int item;
	private long num;
	
	public static CostThing of(int item, long num) {
		CostThing thing = new CostThing();
		thing.item = item;
		thing.num = num;
		return thing;
	}
	
	public static CostThing of(ItemType itemType, long num) {
		CostThing thing = new CostThing();
		thing.item = itemType.getThingId();
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
