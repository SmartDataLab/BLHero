/**
 * 
 */
package com.xiugou.x1.backstage.module.gamecause.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "game_cause", comment = "游戏流水事件表", dbAlias = "backstage")
public class GameCause extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "事件ID")
	private long id;
	@Column(comment = "事件名")
	private String name;
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
}
