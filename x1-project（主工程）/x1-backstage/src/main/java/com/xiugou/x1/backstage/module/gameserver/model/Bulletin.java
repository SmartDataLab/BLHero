/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver.model;

import org.gaming.backstage.service.OneToManyRedisHashEntity;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Table;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 * 公告
 */
@Repository
@Table(name = "bulletin", comment = "游戏公告表", dbAlias = "backstage")
public class Bulletin extends OneToManyRedisHashEntity {
	@Id(strategy = Strategy.AUTO, autoBase = 1)
	@Column(comment = "公告ID")
	private long id;
	@Column(comment = "公告标题")
	private String title;
	@Column(comment = "公告内容", extra = "text")
	private String content;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public Long redisOwnerKey() {
		return 0L;
	}
	@Override
	public Long redisHashKey() {
		return id;
	}
}
