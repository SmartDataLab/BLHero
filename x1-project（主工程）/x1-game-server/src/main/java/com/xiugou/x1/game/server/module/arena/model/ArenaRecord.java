/**
 * 
 */
package com.xiugou.x1.game.server.module.arena.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 *
 */
@Repository
@JvmCache(relation = { "pid", "id" })
@Table(name = "arena_record", comment = "英雄信息表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }) })
public class ArenaRecord extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "other_pid", comment = "对方玩家ID")
	private long otherPid;
	@Column(name = "other_nick", comment = "昵称")
	private String otherNick;
	@Column(comment = "头像")
	private String head;
	@Column(name = "battle_time", comment = "战斗时间戳，毫秒")
	private long battleTime;
	@Column(comment = "1胜利，2失败")
	private int result;
	@Column(comment = "积分增减")
	private int score;
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
	public long getOtherPid() {
		return otherPid;
	}
	public void setOtherPid(long otherPid) {
		this.otherPid = otherPid;
	}
	public String getOtherNick() {
		return otherNick;
	}
	public void setOtherNick(String otherNick) {
		this.otherNick = otherNick;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public long getBattleTime() {
		return battleTime;
	}
	public void setBattleTime(long battleTime) {
		this.battleTime = battleTime;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
