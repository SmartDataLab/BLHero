/**
 *
 */
package pojo.xiugou.x1.pojo.module.player.form;

import java.time.LocalDateTime;

/**
 * @author YY
 */
public class PlayerDetailForm {
	private long id;
	private String openId;
	private int serverId;
	private String nick;
	private String head;
	private int sex;
	private int level;
	private long exp;
	private long gold;
	private long diamond;
	private long nowFighting;
	private long hisMaxFighting;
	private int changeNameNum;
	private boolean online;
	private LocalDateTime lastLoginTime;
	private LocalDateTime lastLogoutTime;
	private LocalDateTime forbidEndTime;
	private long dailyOnline;
	private long realTotalPay;
	private String loginIp;
	private String loginDeviceType;
	private String loginDeviceId;
	private String loginVersion;
	private String createIp;
	// 创号客户端类型，ANDROID，IOS，H5
	private String createDeviceType;
	private String createDeviceId;
	private long createChannel;
	private boolean oldPlayer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
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

	public int getChangeNameNum() {
		return changeNameNum;
	}

	public void setChangeNameNum(int changeNameNum) {
		this.changeNameNum = changeNameNum;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public LocalDateTime getForbidEndTime() {
		return forbidEndTime;
	}

	public void setForbidEndTime(LocalDateTime forbidEndTime) {
		this.forbidEndTime = forbidEndTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginDeviceType() {
		return loginDeviceType;
	}

	public void setLoginDeviceType(String loginDeviceType) {
		this.loginDeviceType = loginDeviceType;
	}

	public String getLoginDeviceId() {
		return loginDeviceId;
	}

	public void setLoginDeviceId(String loginDeviceId) {
		this.loginDeviceId = loginDeviceId;
	}

	public String getLoginVersion() {
		return loginVersion;
	}

	public void setLoginVersion(String loginVersion) {
		this.loginVersion = loginVersion;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getCreateDeviceType() {
		return createDeviceType;
	}

	public void setCreateDeviceType(String createDeviceType) {
		this.createDeviceType = createDeviceType;
	}

	public String getCreateDeviceId() {
		return createDeviceId;
	}

	public void setCreateDeviceId(String createDeviceId) {
		this.createDeviceId = createDeviceId;
	}

	public long getCreateChannel() {
		return createChannel;
	}

	public void setCreateChannel(long createChannel) {
		this.createChannel = createChannel;
	}

	public long getRealTotalPay() {
		return realTotalPay;
	}

	public void setRealTotalPay(long realTotalPay) {
		this.realTotalPay = realTotalPay;
	}

	public long getNowFighting() {
		return nowFighting;
	}

	public void setNowFighting(long nowFighting) {
		this.nowFighting = nowFighting;
	}

	public long getHisMaxFighting() {
		return hisMaxFighting;
	}

	public void setHisMaxFighting(long hisMaxFighting) {
		this.hisMaxFighting = hisMaxFighting;
	}

	public long getDailyOnline() {
		return dailyOnline;
	}

	public void setDailyOnline(long dailyOnline) {
		this.dailyOnline = dailyOnline;
	}

	public LocalDateTime getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(LocalDateTime lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public boolean isOldPlayer() {
		return oldPlayer;
	}

	public void setOldPlayer(boolean oldPlayer) {
		this.oldPlayer = oldPlayer;
	}
}
