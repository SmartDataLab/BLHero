/**
 * 
 */
package com.xiugou.x1.game.server.module.dailyWeekly.service;

import java.util.List;

import org.gaming.db.repository.BaseRepository;
import org.gaming.db.usecase.SlimDao;
import org.gaming.prefab.IGameCause;
import org.gaming.prefab.ITipCause;
import org.gaming.prefab.thing.CostReceipt;
import org.gaming.prefab.thing.ICostThing;
import org.gaming.prefab.thing.IRewardThing;
import org.gaming.prefab.thing.IThingType;
import org.gaming.prefab.thing.NumberThingStorer;
import org.gaming.prefab.thing.RewardReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.design.constant.ItemType;
import com.xiugou.x1.game.server.module.dailyWeekly.model.DailyTask;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service.ZhanLingExpService;

import pojo.xiugou.x1.pojo.log.dailyweekly.DayPointLog;

/**
 * @author hyy
 *
 */
@Component
public class DayPointStorer extends NumberThingStorer<DayPointLog> {

	@Autowired
	private DailyTaskService dailyTaskService;
	@Autowired
	private ZhanLingExpService zhanLingExpService;
	
	@Override
	protected DayPointLog newLog() {
		return new DayPointLog();
	}

	@Override
	protected BaseRepository<DayPointLog> initRepository() {
		return SlimDao.getRepository(DayPointLog.class);
	}

	@Override
	protected IThingType thingType() {
		return ItemType.DAY_POINT;
	}

	@Override
	protected ITipCause lackCode() {
		throw new UnsupportedOperationException("不应该被调用的函数");
	}

	@Override
	public long getCount(long entityId, int thingId) {
		return dailyTaskService.getEntity(entityId).getDayPoints();
	}

	@Override
	protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause,
			String remark) {
		long total = 0;
		for (IRewardThing rewardThing : rewardThings) {
            total += rewardThing.getNum();
        }
		DailyTask entity = dailyTaskService.getEntity(entityId);
		entity.setDayPoints(entity.getDayPoints() + total);
		dailyTaskService.update(entity);
		//添加战令活跃度
		zhanLingExpService.addExp(entityId, total);
		
		return new RewardReceipt(rewardThings);
	}

	@Override
	protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause,
			String remark) {
		throw new UnsupportedOperationException("day point " + thingType().getDesc() + " can't cost");
	}

	@Override
	protected String getOwnerName(long entityId) {
		return "";
	}
}
