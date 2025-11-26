/**
 * 
 */
package com.xiugou.x1.game.server.module.rank.model;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "ranker", comment = "排行榜表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class Ranker extends AbstractEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "唯一ID，排行榜类型_数据标识（玩家ID、公会ID等等）")
	private String id;
	@Column(name = "rank_type", comment = "排行榜类型", readonly = true)
	private int rankType;
	@Column(name = "rank_desc", comment = "排行榜类型描述", readonly = true)
	private String rankDesc;
	@Column(name = "entity_id", comment = "实体ID，不同排行榜，实体ID有不同语意", readonly = true)
	private long entityId;
	@Column(comment = "实体的名字，只是用来在数据库上看的", readonly = true)
	private String name;
	@Column(comment = "分数")
	private long score;
	@Column(name = "sub_score", comment = "二级分数")
	private long subScore;
	@Column(comment = "进榜时间")
	private long time;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRankType() {
		return rankType;
	}
	public void setRankType(int rankType) {
		this.rankType = rankType;
	}
	public long getEntityId() {
		return entityId;
	}
	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public long getSubScore() {
		return subScore;
	}
	public void setSubScore(long subScore) {
		this.subScore = subScore;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getRankDesc() {
		return rankDesc;
	}
	public void setRankDesc(String rankDesc) {
		this.rankDesc = rankDesc;
	}


}
