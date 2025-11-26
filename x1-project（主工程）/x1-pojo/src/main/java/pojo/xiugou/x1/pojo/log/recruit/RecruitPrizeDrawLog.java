package pojo.xiugou.x1.pojo.log.recruit;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.LogTable;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/6/25
 * @apiNote
 */

@Repository
@LogTable(name = "recruit_prize_draw_log", comment = "招募抽取次数日志表", dbAlias = "log", asyncType = AsyncType.INSERT, byColumn = "time")
public class RecruitPrizeDrawLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一Id")
	private long id;
	@Column(comment = "玩家ID")
	private long pid;
	@Column(comment = "招募的倍数")
	private int multiple;
	@Column(comment = "发生时间")
	private LocalDateTime time = LocalDateTime.now();
	@Column(name = "date_str", comment = "日期")
	private String dateStr;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
}
