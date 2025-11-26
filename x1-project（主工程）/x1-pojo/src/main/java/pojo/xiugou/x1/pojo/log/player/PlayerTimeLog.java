/**
 * 
 */
package pojo.xiugou.x1.pojo.log.player;

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
@Table(name = "player_time_log", comment = "分时注册与在线统计表", dbAlias = "log", asyncType = AsyncType.INSERT)
public class PlayerTimeLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
    private long id;
	@Column(name = "date_str", comment = "日期时间")
	private String dateStr;
	@Column(name = "time_period", comment = "时间段")
	private String timePeriod;
	@Column(name = "min_online", comment = "最低在线人数")
	private int minOnline;
	@Column(name = "max_online", comment = "最高在线人数")
	private int maxOnline;
	@Column(name = "create_num", comment = "创建账号数")
	private int createNum;
	@Column(name = "login_num", comment = "登录账号数")
	private int loginNum;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getMinOnline() {
		return minOnline;
	}
	public void setMinOnline(int minOnline) {
		this.minOnline = minOnline;
	}
	public int getMaxOnline() {
		return maxOnline;
	}
	public void setMaxOnline(int maxOnline) {
		this.maxOnline = maxOnline;
	}
	public int getCreateNum() {
		return createNum;
	}
	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}
	public int getLoginNum() {
		return loginNum;
	}
	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getTimePeriod() {
		return timePeriod;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
}
