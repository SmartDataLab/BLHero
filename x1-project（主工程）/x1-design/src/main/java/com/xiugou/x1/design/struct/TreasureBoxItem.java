/**
 * 
 */
package com.xiugou.x1.design.struct;

/**
 * @author YY
 *
 */
public class TreasureBoxItem {
	//奖励ID
	private int itemId;
	//宝箱奖励数量
	private long num;
	//升级奖励ID
	private int upItemId;
	//升级奖励数量
	private long upNum;
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public int getUpItemId() {
		return upItemId;
	}
	public void setUpItemId(int upItemId) {
		this.upItemId = upItemId;
	}
	public long getUpNum() {
		return upNum;
	}
	public void setUpNum(long upNum) {
		this.upNum = upNum;
	}
}
