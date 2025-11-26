/**
 * 
 */
package com.xiugou.x1.game.server.module.handbook.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.annotation.enuma.IndexType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.handbook.struct.BookDetail;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(relation = { "pid", "identity" })
@Table(name = "hand_book", comment = "图鉴表", dbAlias = "game", asyncType = AsyncType.UPDATE, indexs = {
		@Index(name = "pid", columns = { "pid" }),
		@Index(name = "pid_identity", columns = { "pid", "identity" }, type = IndexType.UNIQUE) })
public class Handbook extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
	@Column(comment = "唯一ID")
	private long id;
	@Column(comment = "玩家ID", readonly = true)
	private long pid;
	@Column(comment = "图鉴标识（图鉴类型），总览的积分记录在类型0的数据中", readonly = true)
	private int identity;
	@Column(comment = "图鉴名称", readonly = true)
	private String name;
	@Column(comment = "进度积分")
	private int point;
	@Column(name = "book_details", comment = "图鉴详情，记录等级", extra = "text")
	private Map<Integer, BookDetail> bookDetails = new HashMap<>();
	@Column(name = "reward_lv", comment = "已经领取到的进度奖励等级")
	private int rewardLv;
	@Column(comment = "已经激活的套装")
	private Set<Integer> suits = new HashSet<>();
	
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
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Map<Integer, BookDetail> getBookDetails() {
		return bookDetails;
	}
	public void setBookDetails(Map<Integer, BookDetail> bookDetails) {
		this.bookDetails = bookDetails;
	}
	public int getRewardLv() {
		return rewardLv;
	}
	public void setRewardLv(int rewardLv) {
		this.rewardLv = rewardLv;
	}
	public Set<Integer> getSuits() {
		return suits;
	}
	public void setSuits(Set<Integer> suits) {
		this.suits = suits;
	}
}
