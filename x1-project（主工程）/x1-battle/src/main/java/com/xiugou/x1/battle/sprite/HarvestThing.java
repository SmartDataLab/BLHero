/**
 * 
 */
package com.xiugou.x1.battle.sprite;

import pb.xiugou.x1.protobuf.battle.Battle.PbHarvest;

/**
 * @author YY
 *
 */
public class HarvestThing {
	private int id;
	private long rebornTime;
	private int identity;
	private byte ctype;
	private int cx;
	private int cy;
	private int fogArea;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PbHarvest build() {
		PbHarvest.Builder builder = PbHarvest.newBuilder();
		builder.setId(id);
		builder.setIdentity(identity);
		builder.setRebornTime(rebornTime);
		builder.setCtype(ctype);
		builder.setCx(cx);
		builder.setCy(cy);
		builder.setFogArea(fogArea);
		return builder.build();
	}
	public long getRebornTime() {
		return rebornTime;
	}
	public void setRebornTime(long rebornTime) {
		this.rebornTime = rebornTime;
	}
	public byte getCtype() {
		return ctype;
	}
	public void setCtype(byte ctype) {
		this.ctype = ctype;
	}
	public int getCx() {
		return cx;
	}
	public void setCx(int cx) {
		this.cx = cx;
	}
	public int getCy() {
		return cy;
	}
	public void setCy(int cy) {
		this.cy = cy;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public int getFogArea() {
		return fogArea;
	}
	public void setFogArea(int fogArea) {
		this.fogArea = fogArea;
	}
}
