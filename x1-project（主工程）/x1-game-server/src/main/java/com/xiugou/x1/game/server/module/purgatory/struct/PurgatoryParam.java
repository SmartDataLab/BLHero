/**
 * 
 */
package com.xiugou.x1.game.server.module.purgatory.struct;

/**
 * @author YY
 *
 */
public class PurgatoryParam {
	//玩家是否使用加成次数
	private int plusFlag;
	//布阵加成系数
	private int formationPlus;
	//开战时的轮数
	private int round;
	public int getPlusFlag() {
		return plusFlag;
	}
	public void setPlusFlag(int plusFlag) {
		this.plusFlag = plusFlag;
	}
	public int getFormationPlus() {
		return formationPlus;
	}
	public void setFormationPlus(int formationPlus) {
		this.formationPlus = formationPlus;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
}
