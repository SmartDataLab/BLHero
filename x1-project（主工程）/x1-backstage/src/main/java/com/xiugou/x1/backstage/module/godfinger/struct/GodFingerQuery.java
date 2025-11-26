/**
 * 
 */
package com.xiugou.x1.backstage.module.godfinger.struct;

import org.gaming.backstage.PageQuery;

/**
 * @author YY
 *
 */
public class GodFingerQuery extends PageQuery {
	private String openId;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
