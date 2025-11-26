/**
 * 
 */
package com.xiugou.x1.game.server.module.rank.struct;

import pb.xiugou.x1.protobuf.rank.Rank.PbRankFirstReach;

/**
 * @author hyy
 *
 */
public class RankFirstReach {
	private int id;
	private long playerId;
	private String nick;
	private long time;
	private String head;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}
	
	public PbRankFirstReach build() {
		PbRankFirstReach.Builder builder = PbRankFirstReach.newBuilder();
		builder.setId(id);
		builder.setPlayerId(playerId);
		builder.setNick(nick);
		builder.setHead(head);
		builder.setTime(time);
		return builder.build();
	}
}
