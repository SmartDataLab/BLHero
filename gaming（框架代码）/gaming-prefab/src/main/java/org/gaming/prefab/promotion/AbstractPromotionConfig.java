/**
 * 
 */
package org.gaming.prefab.promotion;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.MappedSuperclass;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.MappedSuperclass.Priority;
import org.gaming.db.orm.AbstractEntity;

/**
 * @author YY
 *
 */
//@Repository
//@Table(name = "promotion_config", comment = "活动配置数据表", dbAlias = "game")
@MappedSuperclass(sort = Priority._2)
public class AbstractPromotionConfig extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(name = "control_id", comment = "活动控制ID", readonly = true)
	private long controlId;
	@Column(comment = "配置的名字", readonly = true)
	private String name;
	@Column(comment = "配置的内容", extra = "text")
	private String content;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getControlId() {
		return controlId;
	}
	public void setControlId(long controlId) {
		this.controlId = controlId;
	}
}
