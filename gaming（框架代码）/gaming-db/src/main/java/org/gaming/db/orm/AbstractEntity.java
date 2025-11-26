/**
 * 
 */
package org.gaming.db.orm;

import java.time.LocalDateTime;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.MappedSuperclass;
import org.gaming.db.annotation.MappedSuperclass.Priority;

/**
 * @author YY
 *
 */
@MappedSuperclass(sort = Priority.FIRST)
public abstract class AbstractEntity {
	@Column(name = "insert_time", comment = "插入时间", readonly = true)
	private LocalDateTime insertTime;
	@Column(name = "update_time", comment = "更新时间")
	private LocalDateTime updateTime;
	
	public final LocalDateTime getInsertTime() {
		return insertTime;
	}
	public final void setInsertTime(LocalDateTime insertTime) {
		this.insertTime = insertTime;
	}
	public final LocalDateTime getUpdateTime() {
		return updateTime;
	}
	public final void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}
	//ID生成器
	public long idGenerator() {
		return 0;
	}
}
