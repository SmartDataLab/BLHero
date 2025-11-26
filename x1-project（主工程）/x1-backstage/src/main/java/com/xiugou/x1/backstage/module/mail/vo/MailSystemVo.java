/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.vo;

import java.util.List;

import pojo.xiugou.x1.pojo.module.mail.MailAttachment;

/**
 * @author YY
 *
 */
public class MailSystemVo {
	private long id;
	private long userId;
	private String userName;
	private String title;
	private String content;
	private List<MailAttachment> rewards;
	private int type;
	private String pids;
	private int playerLevel;
	private int status;
	private long checkUserId;
	private String checkUserName;
	private List<Integer> serverUids;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(long checkUserId) {
		this.checkUserId = checkUserId;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public List<MailAttachment> getRewards() {
		return rewards;
	}
	public void setRewards(List<MailAttachment> rewards) {
		this.rewards = rewards;
	}
	public List<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(List<Integer> serverUids) {
		this.serverUids = serverUids;
	}
}
