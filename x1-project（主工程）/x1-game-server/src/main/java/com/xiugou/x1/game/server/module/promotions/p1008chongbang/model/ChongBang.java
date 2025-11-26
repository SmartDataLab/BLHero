/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1008chongbang.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(relation = { "pid", "typeId" })
@Table(name = "p1008_chong_bang", comment = "冲榜活动玩家数据", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_typeid", columns = { "pid", "type_id" }, type = IndexType.UNIQUE) })
public class ChongBang extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "数据唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(name = "type_id", comment = "活动类型ID", readonly = true)
	private int typeId;
	@Column(name = "type_name", comment = "活动名字（仅用于看）", readonly = true)
	private String typeName;
	@Column(comment = "活动轮数")
	private int turns;
	@Column(comment = "分数")
	private long score;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
}
