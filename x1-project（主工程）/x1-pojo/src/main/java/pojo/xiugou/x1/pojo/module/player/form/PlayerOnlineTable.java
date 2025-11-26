/**
 * 
 */
package pojo.xiugou.x1.pojo.module.player.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class PlayerOnlineTable {
	
	private List<PlayerOnlineData> datas = new ArrayList<>();
	
	public static class PlayerOnlineData {
		private long id;
		private String nick;
		private int level;
		private long diamond;
		private long gold;
		private LocalDateTime lastLoginTime;
		private int dailyOnline;
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public long getDiamond() {
			return diamond;
		}
		public void setDiamond(long diamond) {
			this.diamond = diamond;
		}
		public long getGold() {
			return gold;
		}
		public void setGold(long gold) {
			this.gold = gold;
		}
		public LocalDateTime getLastLoginTime() {
			return lastLoginTime;
		}
		public void setLastLoginTime(LocalDateTime lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
		}
		public int getDailyOnline() {
			return dailyOnline;
		}
		public void setDailyOnline(int dailyOnline) {
			this.dailyOnline = dailyOnline;
		}
		public String getNick() {
			return nick;
		}
		public void setNick(String nick) {
			this.nick = nick;
		}
	}

	public List<PlayerOnlineData> getDatas() {
		return datas;
	}

	public void setDatas(List<PlayerOnlineData> datas) {
		this.datas = datas;
	}
}
