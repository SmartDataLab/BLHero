/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.form;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YY
 *
 */
public class MailSystemForm {
	private long id;
	private String title;
	private String content;
	private String rewards;
	private int type;
	private Set<Integer> serverUids = new HashSet<>();
	//分号分隔
	private String pids;
	private int playerLevel;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
	public String getRewards() {
		return rewards;
	}
	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	public Set<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(Set<Integer> serverUids) {
		this.serverUids = serverUids;
	}
}
