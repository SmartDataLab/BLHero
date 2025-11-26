/**
 * 
 */
package com.xiugou.x1.backstage.module.item.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "item_cfg", comment = "道具配置", dbAlias = "backstage")
public class ItemCfg extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "道具ID")
	private int id;
	@Column(comment = "道具名")
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
