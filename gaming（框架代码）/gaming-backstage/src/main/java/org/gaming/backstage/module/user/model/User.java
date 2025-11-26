/**
 * 
 */
package org.gaming.backstage.module.user.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "user", comment = "用户表", dbAlias = "backstage", indexs = {@Index(name = "name_index", columns = {"name"}, type = IndexType.UNIQUE)})
public class User extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "用户名")
	private String name;
	@Column(comment = "密码")
	private String password;
	@Column(comment = "用户级别，高级别用户创建低级别用户，数字越小级别越高", readonly = true)
	private int level;
	@Column(name = "super_user", comment = "是否超级管理员，0不是，1是")
	private boolean superUser;
	@Column(name = "up_user_id", comment = "上级用户ID", readonly = true)
	private long upUserId;
	@Column(comment = "手机号码")
	private String phone;
	@Column(name = "mail_address", comment = "邮箱地址")
	private String mailAddress;
	@Column(comment = "是否可用")
	private boolean usable;
	@Column(name = "can_read_sensitive", comment = "是否可查看敏感信息")
	private boolean canReadSensitive;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getUpUserId() {
		return upUserId;
	}
	public void setUpUserId(long upUserId) {
		this.upUserId = upUserId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public boolean isUsable() {
		return usable;
	}
	public void setUsable(boolean usable) {
		this.usable = usable;
	}
	public boolean isSuperUser() {
		return superUser;
	}
	public void setSuperUser(boolean superUser) {
		this.superUser = superUser;
	}
	public boolean isCanReadSensitive() {
		return canReadSensitive;
	}
	public void setCanReadSensitive(boolean canReadSensitive) {
		this.canReadSensitive = canReadSensitive;
	}
}
