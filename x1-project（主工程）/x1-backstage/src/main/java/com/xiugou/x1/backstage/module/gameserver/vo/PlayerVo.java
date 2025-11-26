/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

/**
 * @author YY
 *
 */
public class PlayerVo {
	private int serverId;
	private String nick;
	private int level;
	private String head;
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
}
