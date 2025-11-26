/**
 *
 */
package com.xiugou.x1.game.server.module.player.model;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.ruler.spring.Spring;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;

import pojo.xiugou.x1.pojo.module.player.model.IPlayerEntity;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(cacheTime = 0, loadAllOnStart = true)
@Table(name = "player", comment = "玩家信息表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "openid_serverid", columns = { "open_id", "server_id" }, type = IndexType.UNIQUE) })
public class Player extends AbstractEntity implements DailyResetEntity, IPlayerEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "玩家ID")
	private long id;
	@Column(name = "open_id", comment = "账户ID", readonly = true)
	private String openId;
	@Column(name = "server_id", comment = "服务器ID", readonly = true)
	private int serverId;
	@Column(comment = "昵称")
	private String nick;
	@Column(comment = "头像")
	private String head;
	@Column(comment = "形象")
	private int image;
	@Column(comment = "性别，0无性别，1男，2女")
	private int sex;
	@Column(comment = "等级")
	private int level;
	@Column(comment = "经验")
	private long exp;
	@Column(name = "vip_level", comment = "vip等级")
	private int vipLevel;
	@Column(name = "vip_exp", comment = "vip经验")
	private long vipExp;
	@Column(comment = "金币")
	private long gold;
	@Column(comment = "钻石")
	private long diamond;
	@Column(comment = "战斗力")
	private long fighting;
	@Column(name = "change_name_num", comment = "修改名字的次数")
	private int changeNameNum;
	@Column(comment = "是否在线")
	private boolean online;
	@Column(name = "last_login_time", comment = "最后登录时间")
	private LocalDateTime lastLoginTime = null;
	@Column(name = "last_logout_time", comment = "最后登出时间")
	private LocalDateTime lastLogoutTime = LocalDateTime.now();
	@Column(name = "forbid_end_time", comment = "封号结束时间")
	private LocalDateTime forbidEndTime = LocalDateTime.now();
	@Column(name = "buy_privilege", comment = "是否已购买特权")
	private boolean buyPrivilege;

	@Column(name = "daily_online", comment = "今日在线时长")
	private int dailyOnline;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();

	@Column(name = "login_ip", comment = "登录IP")
	private String loginIp;
	@Column(name = "login_device_type", comment = "登录客户端类型，ANDROID，IOS，H5", length = 10)
	private String loginDeviceType;
	@Column(name = "login_device_id", comment = "登录设备ID")
	private String loginDeviceId;
	@Column(name = "login_version", comment = "登录游戏版本号")
	private String loginVersion;

	@Column(name = "create_ip", comment = "创号IP", readonly = true)
	private String createIp;
	@Column(name = "create_device_type", comment = "创号客户端类型，ANDROID，IOS，H5", readonly = true, length = 10)
	private String createDeviceType;
	@Column(name = "create_device_id", comment = "创号设备ID", readonly = true)
	private String createDeviceId;
	@Column(name = "create_channel", comment = "注册渠道", readonly = true)
	private int createChannel;
	@Column(name = "create_pay", comment = "是否创号当天有充值")
	private boolean createPay;
	
	@Column(name = "lang_type", comment = "语言类型")
	private String langType;

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

	public long getVipExp() {
		return vipExp;
	}

	public void setVipExp(long vipExp) {
		this.vipExp = vipExp;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
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

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
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

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCreateDeviceId() {
		return createDeviceId;
	}

	public void setCreateDeviceId(String createDeviceId) {
		this.createDeviceId = createDeviceId;
	}

	public String getCreateDeviceType() {
		return createDeviceType;
	}

	public void setCreateDeviceType(String createDeviceType) {
		this.createDeviceType = createDeviceType;
	}

	public String getLoginVersion() {
		return loginVersion;
	}

	public void setLoginVersion(String loginVersion) {
		this.loginVersion = loginVersion;
	}

	public int getCreateChannel() {
		return createChannel;
	}

	public void setCreateChannel(int createChannel) {
		this.createChannel = createChannel;
	}

	public int getChangeNameNum() {
		return changeNameNum;
	}

	public void setChangeNameNum(int changeNameNum) {
		this.changeNameNum = changeNameNum;
	}

	public LocalDateTime getForbidEndTime() {
		return forbidEndTime;
	}

	public void setForbidEndTime(LocalDateTime forbidEndTime) {
		this.forbidEndTime = forbidEndTime;
	}

	public int getDailyOnline() {
		return dailyOnline;
	}

	public void setDailyOnline(int dailyOnline) {
		this.dailyOnline = dailyOnline;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public boolean isCreatePay() {
		return createPay;
	}

	public void setCreatePay(boolean createPay) {
		this.createPay = createPay;
	}

	public LocalDateTime getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(LocalDateTime lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public boolean isBuyPrivilege() {
		return buyPrivilege;
	}

	public void setBuyPrivilege(boolean buyPrivilege) {
		this.buyPrivilege = buyPrivilege;
	}

	public long getFighting() {
		return fighting;
	}

	public void setFighting(long fighting) {
		this.fighting = fighting;
	}

	@Override
	public long idGenerator() {
		ApplicationSettings applicationSettings = Spring.getBean(ApplicationSettings.class);
		long idBase = applicationSettings.getGameServerPlatformid() * 10000 + applicationSettings.getGameServerId();
		//js精确的最大整数是2的53次方，因此玩家ID偏移不能太大
		return idBase << 25;
	}
	
	public static void main(String[] args) {
		long preId11 = 10010001L << 25;
		System.out.println(preId11);
		long preId12 = 10010002L << 25;
		System.out.println(preId12);
		long preId14 = 10010004L << 25;	//335879998537728
		System.out.println(preId14);
		
		long preId101 = 10010101L << 25;//335883253317632
		System.out.println(preId101);
		
		long preId9999 = 10019999L << 25;//335883253317632
		System.out.println(preId9999);
		
		
		long preId1 = 99999998L << 25;
		System.out.println(preId1);
		long preId2 = 99999999L << 25;
		System.out.println(preId2);
		System.out.println(preId2 - preId1);
		
		System.out.println((long)Math.pow(2, 53));
		System.out.println(Long.MAX_VALUE);
	}

	public String getLangType() {
		return langType;
	}

	public void setLangType(String langType) {
		this.langType = langType;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}
}
