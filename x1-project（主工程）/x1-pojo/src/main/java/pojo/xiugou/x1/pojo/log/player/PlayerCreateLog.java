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
@Table(name = "player_create_log", comment = "玩家创号日志表", dbAlias = "log", asyncType = AsyncType.INSERT)
public class PlayerCreateLog extends AbstractEntity {
    @Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
    private long id;
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "open_id", comment = "账户ID")
    private String openId;
    @Column(comment = "玩家名称")
    private String nick;
    @Column(name = "device_type", comment = "客户端类型，ANDROID，IOS，H5", length = 10)
    private String deviceType;
    @Column(name = "born_time", comment = "创号时间")
    private LocalDateTime bornTime;
    @Column(comment = "创号IP")
    private String ip;
    @Column(name = "channel_id", comment = "渠道ID")
    private int channelId;

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

    public LocalDateTime getBornTime() {
        return bornTime;
    }

    public void setBornTime(LocalDateTime bornTime) {
        this.bornTime = bornTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
}
