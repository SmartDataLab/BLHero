/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.vo;

/**
 * @author YY
 *
 */
public class UserVo {
	private long id;
	private String name;
	private String phone;
	private String mailAddress;
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
}
