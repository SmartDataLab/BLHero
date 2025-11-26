/**
 * 
 */
package pojo.xiugou.x1.pojo.log.function;

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
@Table(name = "guide_dot", comment = "引导打点表", dbAlias = "log", asyncType = AsyncType.INSERT, asyncDelay = 60)
public class GuideDot extends AbstractEntity {
	@Id(strategy = Strategy.AUTO)
    @Column(comment = "日志ID")
	private long id;
	@Column(name = "guide_id", comment = "指引ID")
	private int guideId;
	@Column(name = "guide_name", comment = "指引名称")
	private String guideName;
	@Column(name = "player_id", comment = "玩家ID")
	private long playerId;
	@Column(name = "step", comment = "步骤")
	private int step;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getGuideId() {
		return guideId;
	}
	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}
	public String getGuideName() {
		return guideName;
	}
	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
}
