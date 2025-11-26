/**
 * 
 */
package pojo.xiugou.x1.pojo.log.mainline;

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
@Table(name = "mainline_task_dot", comment = "主线任务打点表", dbAlias = "log", asyncType = AsyncType.INSERT, asyncDelay = 60)
public class MainlineTaskDot extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
	private long id;
	@Column(name = "task_id", comment = "任务ID")
	private int taskId;
	@Column(name = "task_name", comment = "任务名称")
	private String taskName;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(comment = "打点时机")
	private int timing;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTiming() {
		return timing;
	}
	public void setTiming(int timing) {
		this.timing = timing;
	}
}
