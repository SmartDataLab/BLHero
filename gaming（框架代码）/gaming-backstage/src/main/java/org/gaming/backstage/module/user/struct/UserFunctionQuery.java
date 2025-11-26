/**
 * 
 */
package org.gaming.backstage.module.user.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class UserFunctionQuery extends PageQuery {
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
