/**
 * 
 */
package com.xiugou.x1.backstage.module.mailsetting.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "mail_setting", comment = "后台邮箱发送配置", dbAlias = "backstage")
public class MailSetting extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "ID")
	private long id;
	@Column(comment = "邮件协议")
	private String protocol;
	@Column(comment = "邮箱系统服务器地址")
	private String host;
	@Column(comment = "端口")
	private int port;
	@Column(comment = "发件人邮箱地址")
	private String account;
	@Column(name = "auth_code", comment = "发件人邮箱授权码")
	private String authCode;
	@Column(name = "client_master_mail", comment = "客户端管理员邮箱地址")
	private String clientMasterMail;
	@Column(name = "server_master_mail", comment = "服务端管理员邮箱地址")
	private String serverMasterMail;
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getClientMasterMail() {
		return clientMasterMail;
	}
	public void setClientMasterMail(String clientMasterMail) {
		this.clientMasterMail = clientMasterMail;
	}
	public String getServerMasterMail() {
		return serverMasterMail;
	}
	public void setServerMasterMail(String serverMasterMail) {
		this.serverMasterMail = serverMasterMail;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
