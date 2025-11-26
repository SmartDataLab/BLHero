/**
 * 
 */
package pojo.xiugou.x1.pojo.log.player;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "player_online_log", comment = "每5分钟打点的在线人数表", dbAlias = "log", asyncType = AsyncType.INSERT)
public class PlayerOnlineLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "日志ID")
	private long id;
	@Column(name = "date_str", comment = "日期时间")
	private String dateStr;
	@Column(name = "time_period", comment = "时间段")
	private int timePeriod;
	@Column(name = "online_num", comment = "在线人数")
	private int onlineNum;
	@Column(name = "new_online_num", comment = "在线新人数")
	private int newOnlineNum;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}
	public int getNewOnlineNum() {
		return newOnlineNum;
	}
	public void setNewOnlineNum(int newOnlineNum) {
		this.newOnlineNum = newOnlineNum;
	}
	public int getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
}
