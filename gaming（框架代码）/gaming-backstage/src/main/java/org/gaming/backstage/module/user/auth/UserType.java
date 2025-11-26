/**
 * 
 */
package org.gaming.backstage.module.user.auth;

import org.gaming.backstage.interceptor.IRoleType;

/**
 * @author YY
 *
 */
public enum UserType implements IRoleType {

	USER1(1, "User1版本，layui版本的user对象"),
//	USER2(2, "User2版本，vue版本的user对象"),
	
	;
	private final int value;
	private UserType(int value, String desc) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
