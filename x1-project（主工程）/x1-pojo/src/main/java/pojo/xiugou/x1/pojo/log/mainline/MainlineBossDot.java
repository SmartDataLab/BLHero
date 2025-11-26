/**
 * 
 */
package pojo.xiugou.x1.pojo.log.mainline;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.Index;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author YY
 *
 */
@Repository
@Table(name = "mainline_boss_dot", comment = "主线BOSS打点表", dbAlias = "log", asyncType = AsyncType.INSERT, asyncDelay = 60, indexs = {
		@Index(name = "boss_id", columns = { "boss_id" }) })
public class MainlineBossDot extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
	private long id;
	@Column(name = "boss_id", comment = "领主ID")
	private int bossId;
	@Column(name = "boss_name", comment = "领主名称")
	private String bossName;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(comment = "战斗力")
	private long fighting;
	@Column(comment = "时机")
	private int timing;
	@Column(comment = "领主等级")
	private int level;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public String getBossName() {
		return bossName;
	}
	public void setBossName(String bossName) {
		this.bossName = bossName;
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
	public int getTiming() {
		return timing;
	}
	public void setTiming(int timing) {
		this.timing = timing;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
