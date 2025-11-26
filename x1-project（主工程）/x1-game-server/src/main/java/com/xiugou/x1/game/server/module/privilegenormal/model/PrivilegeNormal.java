/**
 * 
 */
package com.xiugou.x1.game.server.module.privilegenormal.model;

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
import org.gaming.tool.DateTimeUtil;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService.DailyResetEntity;
import com.xiugou.x1.game.server.module.privilegenormal.struct.PrivilegeNormalData;

/**
 * @author YY
 *
 */
@Repository
@JvmCache
@Table(name = "privilege_normal", comment = "普通特权表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class PrivilegeNormal extends AbstractEntity implements DailyResetEntity {
	@Id(strategy = Strategy.IDENTITY)
	@Column(comment = "玩家ID")
	private long pid;
	@Column(name = "daily_time", comment = "每日重置时间")
	private LocalDateTime dailyTime = LocalDateTime.now();
	@Column(name = "exp_pool", comment = "角色经验池")
	private long expPool;
	@Column(comment = "特权数据", length = 1000)
	private Map<Integer, PrivilegeNormalData> datas = new HashMap<>();

	//装备分解获得素材是否翻倍
	public boolean isEquipSalvageDouble() {
		return datas.get(3) != null;
	}
	
	//副本掉落是否翻倍
	public boolean isDungeonDropDouble() {
		return datas.get(3) != null;
	}
	
	//是否可领取经验池
	public boolean canGetExpPool() {
		return datas.get(3) != null;
	}
	
	//是否招募打折
	public boolean isRecruitDiscount() {
		PrivilegeNormalData data = datas.get(2);
		return data != null && data.getEndTime() > DateTimeUtil.currMillis();
	}
	
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

	public long getExpPool() {
		return expPool;
	}

	public void setExpPool(long expPool) {
		this.expPool = expPool;
	}

	public Map<Integer, PrivilegeNormalData> getDatas() {
		return datas;
	}

	public void setDatas(Map<Integer, PrivilegeNormalData> datas) {
		this.datas = datas;
	}
}
