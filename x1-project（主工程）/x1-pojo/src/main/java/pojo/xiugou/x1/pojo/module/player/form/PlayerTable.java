/**
 * 
 */
package pojo.xiugou.x1.pojo.module.player.form;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YY
 *
 */
public class PlayerTable {
	private int serverId;
	private List<PlayerData> datas = new ArrayList<>();
	
	public static class PlayerData {
		private long platformId;
		private long channelId;
		private long playerId;
		private String openId;
		private int serverId;
		private String nick;
		private String head;
		private int sex;
		private int level;
		private long gold;
		private long diamond;
		private boolean online;
		private long dailyOnline;
		private long bornTime;
		private long lastLoginTime;
		private long fighting;
		private long recharge;
		public long getChannelId() {
			return channelId;
		}
		public void setChannelId(long channelId) {
			this.channelId = channelId;
		}
		public long getPlayerId() {
			return playerId;
		}
		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}
		public String getOpenId() {
			return openId;
		}
		public void setOpenId(String openId) {
			this.openId = openId;
		}
		public int getServerId() {
			return serverId;
		}
		public void setServerId(int serverId) {
			this.serverId = serverId;
		}
		public String getNick() {
			return nick;
		}
		public void setNick(String nick) {
			this.nick = nick;
		}
		public String getHead() {
			return head;
		}
		public void setHead(String head) {
			this.head = head;
		}
		public int getSex() {
			return sex;
		}
		public void setSex(int sex) {
			this.sex = sex;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public long getGold() {
			return gold;
		}
		public void setGold(long gold) {
			this.gold = gold;
		}
		public long getDiamond() {
			return diamond;
		}
		public void setDiamond(long diamond) {
			this.diamond = diamond;
		}
		public boolean isOnline() {
			return online;
		}
		public void setOnline(boolean online) {
			this.online = online;
		}
		public long getBornTime() {
			return bornTime;
		}
		public void setBornTime(long bornTime) {
			this.bornTime = bornTime;
		}
		public long getLastLoginTime() {
			return lastLoginTime;
		}
		public void setLastLoginTime(long lastLoginTime) {
			this.lastLoginTime = lastLoginTime;
		}
		public long getDailyOnline() {
			return dailyOnline;
		}
		public void setDailyOnline(long dailyOnline) {
			this.dailyOnline = dailyOnline;
		}
		public long getFighting() {
			return fighting;
		}
		public void setFighting(long fighting) {
			this.fighting = fighting;
		}
		public long getRecharge() {
			return recharge;
		}
		public void setRecharge(long recharge) {
			this.recharge = recharge;
		}
		public long getPlatformId() {
			return platformId;
		}
		public void setPlatformId(long platformId) {
			this.platformId = platformId;
		}
	}

	public List<PlayerData> getDatas() {
		return datas;
	}

	public void setDatas(List<PlayerData> datas) {
		this.datas = datas;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
}
