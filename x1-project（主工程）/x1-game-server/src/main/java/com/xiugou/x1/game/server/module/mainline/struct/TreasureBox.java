/**
 * 
 */
package com.xiugou.x1.game.server.module.mainline.struct;

/**
 * @author YY
 *
 */
public class TreasureBox {
	private int boxType;
	//消失时间
	private long disappearTime;
	//箱子参数
	private int boxArg;
	
	public long getDisappearTime() {
		return disappearTime;
	}
	public void setDisappearTime(long disappearTime) {
		this.disappearTime = disappearTime;
	}
	public int getBoxType() {
		return boxType;
	}
	public void setBoxType(int boxType) {
		this.boxType = boxType;
	}
	public int getBoxArg() {
		return boxArg;
	}
	public void setBoxArg(int boxArg) {
		this.boxArg = boxArg;
	}
}
