/**
 * 
 */
package com.xiugou.x1.game.server.module.evil.struct;

import java.util.ArrayList;
import java.util.List;

import com.xiugou.x1.design.struct.CostThing;

/**
 * @author YY
 *
 */
public class EvilSpeedUpCost {
	private long costMinute;
	private List<CostThing> costList = new ArrayList<>();
	public List<CostThing> getCostList() {
		return costList;
	}
	public void setCostList(List<CostThing> costList) {
		this.costList = costList;
	}
	public long getCostMinute() {
		return costMinute;
	}
	public void setCostMinute(long costMinute) {
		this.costMinute = costMinute;
	}
}
