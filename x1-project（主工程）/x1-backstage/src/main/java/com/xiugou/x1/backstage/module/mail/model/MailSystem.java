/**
 * 
 */
package com.xiugou.x1.backstage.module.mail.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import pojo.xiugou.x1.pojo.module.mail.MailAttachment;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "mail_system", comment = "系统邮件表", dbAlias = "backstage")
public class MailSystem extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据ID")
	private long id;
	@Column(name = "user_id", comment = "创建邮件的用户", readonly = true)
	private long userId;
	@Column(name = "user_name", comment = "创建邮件的用户", readonly = true)
	private String userName;
	@Column(comment = "标题")
	private String title;
	@Column(comment = "内容", extra = "text")
	private String content;
	@Column(comment = "奖励", length = 5000)
	private List<MailAttachment> rewards = new ArrayList<>();
	@Column(comment = "邮件类型，1全服邮件，2玩家邮件")
	private int type;
	@Column(name = "server_uids", comment = "服务器唯一ID集合", extra = "text")
	private Set<Integer> serverUids = new HashSet<>();
	@Column(name = "send_server_uids", comment = "已发送的服务器唯一ID集合", extra = "text")
	private Set<Integer> sendServerUids = new HashSet<>();
	@Column(name = "delete_server_uids", comment = "已发送删除的服务器唯一ID集合", extra = "text")
	private Set<Integer> deleteServerUids = new HashSet<>();
	@Column(comment = "玩家ID集合，分号分隔", extra = "text")
	private String pids;
	@Column(name = "player_level", comment = "收到邮件的玩家等级限制")
	private int playerLevel;
	@Column(comment = "邮件审核状态，0未审核，1审核通过，2审核不通过，3通知删除")
	private int status;
	@Column(name = "check_user_id", comment = "审核用户ID")
	private long checkUserId;
	@Column(name = "check_user_name", comment = "审核用户")
	private String checkUserName;
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
	public Set<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(Set<Integer> serverUids) {
		this.serverUids = serverUids;
	}
	public Set<Integer> getSendServerUids() {
		return sendServerUids;
	}
	public void setSendServerUids(Set<Integer> sendServerUids) {
		this.sendServerUids = sendServerUids;
	}
	public Set<Integer> getDeleteServerUids() {
		return deleteServerUids;
	}
	public void setDeleteServerUids(Set<Integer> deleteServerUids) {
		this.deleteServerUids = deleteServerUids;
	}
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
}
