/**
 * 
 */
package com.xiugou.x1.game.server.module.achievement.service;

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
import com.xiugou.x1.game.server.module.achievement.model.Achievement;
import com.xiugou.x1.game.server.module.achievement.model.AchievementPointLog;

/**
 * @author hyy
 *
 */
@Component
public class AchievementPointStorer extends NumberThingStorer<AchievementPointLog> {

	@Autowired
	private AchievementService achievementService;
	
	@Override
	protected AchievementPointLog newLog() {
		return new AchievementPointLog();
	}

	@Override
	protected BaseRepository<AchievementPointLog> initRepository() {
		return SlimDao.getRepository(AchievementPointLog.class);
	}

	@Override
	protected IThingType thingType() {
		return ItemType.ACHIEVEMENT_POINT;
	}

	@Override
	protected ITipCause lackCode() {
		throw new UnsupportedOperationException("不应该被调用的函数");
	}

	@Override
	public long getCount(long entityId, int thingId) {
		return achievementService.getEntity(entityId).getPoint();
	}

	@Override
	protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause,
			String remark) {
		long total = 0;
        for (IRewardThing rewardThing : rewardThings) {
            total += rewardThing.getNum();
        }
        
        Achievement achievement = achievementService.getEntity(entityId);
        achievement.setPoint(achievement.getPoint() + total);
        achievementService.update(achievement);
        
        return new RewardReceipt(rewardThings);
	}

	@Override
	protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause,
			String remark) {
		throw new UnsupportedOperationException("不应该被调用的函数");
	}

	@Override
	protected String getOwnerName(long entityId) {
		return "";
	}

}
