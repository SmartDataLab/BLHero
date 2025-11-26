/**
 * 
 */
package pojo.xiugou.x1.pojo.log.player;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "player_recharge_log", comment = "玩家充值日志表，用于统计LTV", dbAlias = "log", asyncType = AsyncType.INSERT)
public class PlayerRechargeLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
    private long id;
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "open_id", comment = "账户ID")
    private String openId;
    @Column(comment = "玩家名称")
    private String nick;
    @Column(name = "level", comment = "等级")
    private int level;
    @Column(name = "recharge_time", comment = "充值时间")
    private LocalDateTime rechargeTime;
    @Column(name = "born_time", comment = "创号时间")
    private LocalDateTime bornTime;
    @Column(name = "channel_id", comment = "渠道ID")
    private int channelId;
    @Column(name = "money", comment = "真实支付金额")
    private long money;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public LocalDateTime getRechargeTime() {
		return rechargeTime;
	}
	public void setRechargeTime(LocalDateTime rechargeTime) {
		this.rechargeTime = rechargeTime;
	}
	public LocalDateTime getBornTime() {
		return bornTime;
	}
	public void setBornTime(LocalDateTime bornTime) {
		this.bornTime = bornTime;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
}
