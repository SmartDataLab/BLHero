/**
 * 
 */
package com.xiugou.x1.cross.server.module.arena.service;

import org.gaming.tool.DateTimeUtil;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.cross.server.foundation.service.SystemOneToOneService;
import com.xiugou.x1.cross.server.module.TimeSetting;
import com.xiugou.x1.cross.server.module.arena.model.ArenaSystem;

/**
 * @author YY
 *
 */
@Service
public class ArenaSystemService extends SystemOneToOneService<ArenaSystem> {

	@Autowired
	private TimeSetting timeSetting;
	@Autowired
	private ArenaSeasonService arenaSeasonService;
	
	@Override
	public ArenaSystem getEntity() {
		ArenaSystem entity = this.repository().getByMainKey(1L);
		if(entity == null) {
			synchronized (this) {
				entity = this.repository().getByMainKey(1L);
				if(entity == null) {
					entity = new ArenaSystem();
					entity.setId(1L);
					entity.setSettleTime(LocalDateTimeUtil.toEpochMilli(timeSetting.nextWeekMondayOTime()));
					entity.setDailyTime(LocalDateTimeUtil.toEpochMilli(timeSetting.nextDayOTime()));
					this.insert(entity);
				}
			}
		}
		return entity;
	}

	protected void runInSchedule() {
		ArenaSystem arenaSystem = this.getEntity();
		long now = DateTimeUtil.currMillis();
		if(now > arenaSystem.getDailyTime()) {
			arenaSystem.setDailyTime(LocalDateTimeUtil.toEpochMilli(timeSetting.nextDayOTime()));
			this.update(arenaSystem);
			//结算每日奖励
			arenaSeasonService.settleDaily();
		}
		
		if(now > arenaSystem.getSettleTime()) {
			arenaSystem.setSettleTime(LocalDateTimeUtil.toEpochMilli(timeSetting.nextWeekMondayOTime()));
			this.update(arenaSystem);
			//结算赛季奖励
			arenaSeasonService.settleSeason();
		}
	}
}
