/**
 * 
 */
package org.gaming.prefab.promotion;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.MappedSuperclass;
import org.gaming.db.annotation.MappedSuperclass.Priority;
import org.gaming.db.orm.AbstractEntity;

/**
 * @author YY
 *
 */
@MappedSuperclass(sort = Priority._2)
public abstract class AbstractPromotionControl extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "logic_type", comment = "活动逻辑类型", readonly = true)
	private int logicType;
	@Column(name = "type_id", comment = "活动类型ID", readonly = true)
	private int typeId;
	@Column(comment = "活动名字", readonly = true)
	private String name;
	@Column(comment = "活动所在阶段")
	private PromotionStage stage = PromotionStage.IDLE;
	@Column(name = "start_time", comment = "开始时间")
	private LocalDateTime startTime;
	@Column(name = "still_time", comment = "沉寂时间")
	private LocalDateTime stillTime;
	@Column(name = "end_time", comment = "结束时间")
	private LocalDateTime endTime;
	@Column(name = "idle_time", comment = "空闲时间")
	private LocalDateTime nextTime;
	@Column(comment = "是否已结算")
	private boolean settled;
	
	public boolean isRunning() {
		return stage == PromotionStage.RUNNING;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public PromotionStage getStage() {
		return stage;
	}
	public void setStage(PromotionStage stage) {
		this.stage = stage;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getStillTime() {
		return stillTime;
	}
	public void setStillTime(LocalDateTime stillTime) {
		this.stillTime = stillTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public boolean isSettled() {
		return settled;
	}
	public void setSettled(boolean settled) {
		this.settled = settled;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLogicType() {
		return logicType;
	}
	public void setLogicType(int logicType) {
		this.logicType = logicType;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public LocalDateTime getNextTime() {
		return nextTime;
	}
	public void setNextTime(LocalDateTime nextTime) {
		this.nextTime = nextTime;
	}
}
