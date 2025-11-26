/**
 * 
 */
package com.xiugou.x1.game.server.module.arena.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.battle.template.TemplateFighter;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;
import com.xiugou.x1.game.server.module.arena.struct.ArenaCompetitor;

/**
 * @author hyy
 *
 */
@Repository
@JvmCache
@Table(name = "arena_player", comment = "玩家竞技场数据表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class ArenaPlayer extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
	@Column(name = "challenge_num", comment = "已挑战次数")
	private int challengeNum;
	@Column(name = "buy_num", comment = "已购买挑战次数")
	private int buyNum;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "auto_refresh_time", comment = "自动刷新时间")
	private long autoRefreshTime;
	@Column(name = "hand_refresh_time", comment = "手动刷新时间")
	private long handRefreshTime;
	@Column(comment = "待挑战者", length = 5000)
	private List<ArenaCompetitor> competitors = new ArrayList<>();
	@Column(name = "template_fighter", comment = "将要挑战的对方阵容数据", extra = "text")
	private TemplateFighter templateFighter;
	@Column(comment = "积分")
	private long score;

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getChallengeNum() {
		return challengeNum;
	}

	public void setChallengeNum(int challengeNum) {
		this.challengeNum = challengeNum;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}

	public long getAutoRefreshTime() {
		return autoRefreshTime;
	}

	public void setAutoRefreshTime(long autoRefreshTime) {
		this.autoRefreshTime = autoRefreshTime;
	}

	public long getHandRefreshTime() {
		return handRefreshTime;
	}

	public void setHandRefreshTime(long handRefreshTime) {
		this.handRefreshTime = handRefreshTime;
	}

	public List<ArenaCompetitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<ArenaCompetitor> competitors) {
		this.competitors = competitors;
	}

	public TemplateFighter getTemplateFighter() {
		return templateFighter;
	}

	public void setTemplateFighter(TemplateFighter templateFighter) {
		this.templateFighter = templateFighter;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
}
