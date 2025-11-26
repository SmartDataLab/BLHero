/**
 * 
 */
package com.xiugou.x1.backstage.module.player.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class WhiteListQuery extends PageQuery {
	private String openId;
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
