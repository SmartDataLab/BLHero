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
import com.xiugou.x1.game.server.module.dailyWeekly.model.WeeklyTask;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service.ZhanLingExpService;

import pojo.xiugou.x1.pojo.log.dailyweekly.WeekPointLog;

/**
 * @author hyy
 *
 */
@Component
public class WeekPointStorer extends NumberThingStorer<WeekPointLog> {

	@Autowired
	private WeeklyTaskService weeklyTaskService;
	@Autowired
	private ZhanLingExpService zhanLingExpService;
	
	@Override
	protected WeekPointLog newLog() {
		return new WeekPointLog();
	}

	@Override
	protected BaseRepository<WeekPointLog> initRepository() {
		return SlimDao.getRepository(WeekPointLog.class);
	}

	@Override
	protected IThingType thingType() {
		return ItemType.WEEK_POINT;
	}

	@Override
	protected ITipCause lackCode() {
		throw new UnsupportedOperationException("不应该被调用的函数");
	}

	@Override
	public long getCount(long entityId, int thingId) {
		return weeklyTaskService.getEntity(entityId).getWeekPoints();
	}

	@Override
	protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause,
			String remark) {
		long total = 0;
		for (IRewardThing rewardThing : rewardThings) {
            total += rewardThing.getNum();
        }
		WeeklyTask entity = weeklyTaskService.getEntity(entityId);
		entity.setWeekPoints(entity.getWeekPoints() + total);
		weeklyTaskService.update(entity);
		
		//添加战令活跃度
		zhanLingExpService.addExp(entityId, total);
		
		return new RewardReceipt(rewardThings);
	}

	@Override
	protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause,
			String remark) {
		throw new UnsupportedOperationException("week point " + thingType().getDesc() + " can't cost");
	}

	@Override
	protected String getOwnerName(long entityId) {
		return "";
	}
}
