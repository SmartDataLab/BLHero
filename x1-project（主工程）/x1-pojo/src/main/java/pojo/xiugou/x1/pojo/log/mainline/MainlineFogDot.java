/**
 * 
 */
package pojo.xiugou.x1.pojo.log.mainline;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "mainline_fog_dot", comment = "主线迷雾解锁打点表", dbAlias = "log", asyncType = AsyncType.INSERT, asyncDelay = 60)
public class MainlineFogDot extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
	private long id;
	@Column(name = "fog_id", comment = "迷雾ID")
	private int fogId;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(comment = "战斗力")
	private long fighting;
	@Column(comment = "等级")
	private int level;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getFogId() {
		return fogId;
	}
	public void setFogId(int fogId) {
		this.fogId = fogId;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getFighting() {
		return fighting;
	}
	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
