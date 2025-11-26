/**
 * 
 */
package com.xiugou.x1.game.server.module.home.struct;

/**
 * @author YY
 *
 */
public class HomeProducer {
	private int id;
	//产出等级
	private int level;
	//开始产出的时间
	private long startTime;
	//已产出的数量
	private long produce;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getProduce() {
		return produce;
	}
	public void setProduce(long produce) {
		this.produce = produce;
	}
}
