package pojo.xiugou.x1.pojo.log.player;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/7/20
 * @apiNote
 */
@Repository
@Table(name = "player_scatter_log", comment = "玩家在线分布日志表", dbAlias = "log", asyncType = AsyncType.INSERT)
public class PlayerScatterLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "日志ID")
	private long id;
	@Column(comment = "年月日")
	private int date;
	@Column(name = "time_period", comment = "时间段")
	private int timePeriod;
	@Column(name = "time_period_text", comment = "时间段文本")
	private String timePeriodText;
	@Column(name = "online_num", comment = "时间段人数")
	private int onlineNum;
	@Column(name = "online_base", comment = "时间段基数")
	private int onlineBase;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}
	public String getTimePeriodText() {
		return timePeriodText;
	}
	public void setTimePeriodText(String timePeriodText) {
		this.timePeriodText = timePeriodText;
	}
	public int getOnlineBase() {
		return onlineBase;
	}
	public void setOnlineBase(int onlineBase) {
		this.onlineBase = onlineBase;
	}
	public int getOnlineNum() {
		return onlineNum;
	}
	public void setOnlineNum(int onlineNum) {
		this.onlineNum = onlineNum;
	}
}
