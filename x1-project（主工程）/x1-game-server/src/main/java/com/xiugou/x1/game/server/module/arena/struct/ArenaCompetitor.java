/**
 * 
 */
package com.xiugou.x1.game.server.module.arena.struct;

import org.gaming.fakecmd.side.game.IPlayer;

/**
 * @author YY
 *
 */
public class ArenaCompetitor implements IPlayer {
	//玩家ID或机器人ID
	private long id;
	//为0表示机器人
	private int serverZone;
	private String nick;
	private long score;
	private long fighting;
	private String head;
	private int image;
	private int rank;
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getServerZone() {
		return serverZone;
	}
	public void setServerZone(int serverZone) {
		this.serverZone = serverZone;
	}
}
