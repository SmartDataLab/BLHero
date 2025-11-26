package com.xiugou.x1.game.server.module.recruit.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.Id.Strategy;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;
import com.xiugou.x1.game.server.module.recruit.struct.RecruitData;

/**
 * @author yh
 * @date 2023/6/19
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "recruit", comment = "招募表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Recruit extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(comment = "积分")
	private long points;
	@Column(name = "recruit_num", comment = "抽取次数")
	private int recruitNum;
	@Column(comment = "阶段")
	private int stage;
	@Column(name = "stage_num", comment = "当前阶段召唤的次数")
	private int stageNum;
	@Column(name = "stage_up_weight", comment = "当前阶段提升权重的机会是否已经使用")
	private boolean stageUpWeight;
	@Column(name = "recruit_data", comment = "抽卡列表", length = 600)
	private List<RecruitData> recruitData = new ArrayList<>();
	@Column(name = "discount_num", comment = "今日已使用折扣次数")
	private int discountNum;
	@Column(name = "ad_free_num", comment = "已观看广告次数")
	private int adFreeNum;
	@Column(comment = "当前使用的倍数")
	private int multiple;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "last_refresh_time", comment = "上次刷新时间")
	private long lastRefreshTime;
	@Column(name = "draw_nums", comment = "抽奖项已抽奖次数")
	private Map<Integer, Integer> drawNums = new HashMap<>();
	@Column(name = "today_recruit", comment = "今天已抽奖次数")
	private int todayRecruit;
    @Column(name = "hand_refresh", comment = "手动刷新次数")
    private int handRefresh;

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getStageNum() {
		return stageNum;
	}

	public void setStageNum(int stageNum) {
		this.stageNum = stageNum;
	}

	public int getDiscountNum() {
		return discountNum;
	}

	public void setDiscountNum(int discountNum) {
		this.discountNum = discountNum;
	}

	public int getAdFreeNum() {
		return adFreeNum;
	}

	public void setAdFreeNum(int adFreeNum) {
		this.adFreeNum = adFreeNum;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public List<RecruitData> getRecruitData() {
		return recruitData;
	}

	public void setRecruitData(List<RecruitData> recruitData) {
		this.recruitData = recruitData;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public int getRecruitNum() {
		return recruitNum;
	}

	public void setRecruitNum(int recruitNum) {
		this.recruitNum = recruitNum;
	}

	@Override
	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	@Override
	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public boolean isStageUpWeight() {
		return stageUpWeight;
	}

	public void setStageUpWeight(boolean stageUpWeight) {
		this.stageUpWeight = stageUpWeight;
	}

	public Map<Integer, Integer> getDrawNums() {
		return drawNums;
	}

	public void setDrawNums(Map<Integer, Integer> drawNums) {
		this.drawNums = drawNums;
	}

	public int getTodayRecruit() {
		return todayRecruit;
	}

	public void setTodayRecruit(int todayRecruit) {
		this.todayRecruit = todayRecruit;
	}

	public int getHandRefresh() {
		return handRefresh;
	}

	public void setHandRefresh(int handRefresh) {
		this.handRefresh = handRefresh;
	}
}
