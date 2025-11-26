/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.struct;

/**
 * @author YY
 *
 */
public class ArenaTop {
	//玩家ID
	private long playerId;
	//服务器ID
	private int serverId;
	//名次
	private int rank;
	//是不是机器人
	private boolean robot;
	//机器人昵称
	private String nick;
	//机器人形象
	private int image;
	//机器人战力
	private long fighting;
	
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public boolean isRobot() {
		return robot;
	}
	public void setRobot(boolean robot) {
		this.robot = robot;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
}
