/**
 * 
 */
package org.gaming.backstage.module.user.form;

/**
 * @author YY
 *
 */
public class UserForm {
	private long id;
	private String name;
	private String password;
	private boolean usable;
	private boolean superUser;
	private String phone;
	private String mailAddress;
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
	public boolean isCanReadSensitive() {
		return canReadSensitive;
	}
	public void setCanReadSensitive(boolean canReadSensitive) {
		this.canReadSensitive = canReadSensitive;
	}
}
