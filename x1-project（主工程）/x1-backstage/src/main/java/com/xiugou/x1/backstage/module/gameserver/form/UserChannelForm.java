/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.form;

import java.util.List;

/**
 * @author YY
 *
 */
public class UserChannelForm {
	private long userId;
	private List<Long> channel;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public List<Long> getChannel() {
		return channel;
	}
	public void setChannel(List<Long> channel) {
		this.channel = channel;
	}
}
