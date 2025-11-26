/**
 * 
 */
package org.gaming.backstage.interceptor;

/**
 * @author YY
 *
 */
public class RoleContext {
	private IRoleType roleType;
	private long id;
	private String name;
	private String token;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public IRoleType getRoleType() {
		return roleType;
	}
	public void setRoleType(IRoleType roleType) {
		this.roleType = roleType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
