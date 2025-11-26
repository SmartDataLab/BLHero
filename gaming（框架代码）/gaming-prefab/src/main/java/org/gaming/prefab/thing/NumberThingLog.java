/**
 * 
 */
package org.gaming.prefab.thing;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.MappedSuperclass;
import org.gaming.db.orm.AbstractEntity;
import org.gaming.prefab.IGameCause;

/**
 * @author YY
 *
 */
@MappedSuperclass
public abstract class NumberThingLog extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "日志ID")
	private long id;
	@Column(name = "owner_id", comment = "持有者ID")
	private long ownerId;
	@Column(name = "owner_name", comment = "持有者名字")
	private String ownerName;
	@Column(name = "thing_id", comment = "物品标识")
	private int thingId;
	@Column(name = "thing_name", comment = "物品名称")
	private String thingName;
	@Column(comment = "变化量")
	private long delta;
	@Column(comment = "变化后的当前量")
	private long curr;
	@Column(name = "game_cause", comment = "游戏事件")
	private int gameCause;
	@Column(name = "game_cause_text", comment = "游戏事件")
	private String gameCauseText;
	@Column(comment = "发生时间")
	private LocalDateTime time = LocalDateTime.now();
	@Column(name = "remark", comment = "备注信息")
	private String remark;
	
	
	public void setGameCause(IGameCause gameCause) {
		this.gameCause = gameCause.getCode();
		this.gameCauseText = gameCause.getDesc();
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setThingId(int thingId) {
		this.thingId = thingId;
	}

	public void setThingName(String thingName) {
		this.thingName = thingName;
	}

	public void setDelta(long delta) {
		this.delta = delta;
	}

	public void setCurr(long curr) {
		this.curr = curr;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getGameCause() {
		return gameCause;
	}

	public void setGameCause(int gameCause) {
		this.gameCause = gameCause;
	}

	public String getGameCauseText() {
		return gameCauseText;
	}

	public void setGameCauseText(String gameCauseText) {
		this.gameCauseText = gameCauseText;
	}

	public long getId() {
		return id;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public int getThingId() {
		return thingId;
	}

	public String getThingName() {
		return thingName;
	}

	public long getDelta() {
		return delta;
	}

	public long getCurr() {
		return curr;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public String getRemark() {
		return remark;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}
