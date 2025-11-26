/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class UserChannelQuery extends PageQuery {
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
