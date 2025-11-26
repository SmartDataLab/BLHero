/**
 * 
 */
package pojo.xiugou.x1.pojo.module.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class MailToGameServer {
	private long id;
	private String title;
	private String content;
	private List<MailAttachment> rewards = new ArrayList<>();
	private int type;
	private String pids;
	private int playerLevel;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<MailAttachment> getRewards() {
		return rewards;
	}
	public void setRewards(List<MailAttachment> rewards) {
		this.rewards = rewards;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public String getPids() {
		return pids;
	}
	public void setPids(String pids) {
		this.pids = pids;
	}
}
