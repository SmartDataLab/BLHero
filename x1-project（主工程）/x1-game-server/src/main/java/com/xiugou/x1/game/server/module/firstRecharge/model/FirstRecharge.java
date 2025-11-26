package com.xiugou.x1.game.server.module.firstRecharge.model;

import java.time.LocalDateTime;
import java.util.HashMap;
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
import com.xiugou.x1.game.server.module.firstRecharge.struct.FirstRechargeData;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "first_recharge", comment = "首充表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class FirstRecharge extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "recharge_datas", comment = "首充数据", length = 5000)
	private Map<Integer, FirstRechargeData> rechargeDatas = new HashMap<>();

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public LocalDateTime getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(LocalDateTime dailyTime) {
		this.dailyTime = dailyTime;
	}

	public Map<Integer, FirstRechargeData> getRechargeDatas() {
		return rechargeDatas;
	}

	public void setRechargeDatas(Map<Integer, FirstRechargeData> rechargeDatas) {
		this.rechargeDatas = rechargeDatas;
	}
}
