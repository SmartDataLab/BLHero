/**
 * 
 */
package pojo.xiugou.x1.pojo.apiutil;

import java.util.ArrayList;
import java.util.List;

import pojo.xiugou.x1.pojo.module.mail.MailAttachment;

/**
 * @author YY
 *
 */
public class SanQiHuYuSendGift {
	private long playerId;
	private List<MailAttachment> rewards = new ArrayList<>();
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public List<MailAttachment> getRewards() {
		return rewards;
	}
	public void setRewards(List<MailAttachment> rewards) {
		this.rewards = rewards;
	}
}
