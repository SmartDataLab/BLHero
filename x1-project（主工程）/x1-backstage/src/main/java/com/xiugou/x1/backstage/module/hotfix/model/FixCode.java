/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.model;

import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "fix_code", comment = "热更代码记录表", dbAlias = "backstage")
public class FixCode extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "server_uids", comment = "热更的服务器唯一ID集合")
	private List<Integer> serverUids = new ArrayList<>();
	@Column(name = "file_names", comment = "配置文件相对路径", extra = "text")
	private List<String> fileNames = new ArrayList<>();
	@Column(name = "user_id", comment = "操作员ID")
	private long userId;
	@Column(name = "user_name", comment = "操作员名称")
	private String userName;
	@Column(name = "fix_time", comment = "热更时间批号")
	private String fixTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Integer> getServerUids() {
		return serverUids;
	}
	public void setServerUids(List<Integer> serverUids) {
		this.serverUids = serverUids;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
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
	public String getFixTime() {
		return fixTime;
	}
	public void setFixTime(String fixTime) {
		this.fixTime = fixTime;
	}
}
