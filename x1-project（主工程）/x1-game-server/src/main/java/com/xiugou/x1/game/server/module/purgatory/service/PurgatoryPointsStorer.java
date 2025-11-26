package com.xiugou.x1.game.server.module.purgatory.service;

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
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;

import pojo.xiugou.x1.pojo.log.shop.PurgatoryPointsLog;

/**
 * @author yh
 * @date 2023/7/27
 * @apiNote
 */
@Component
public class PurgatoryPointsStorer extends NumberThingStorer<PurgatoryPointsLog> {
	
	@Autowired
	private PurgatoryService purgatoryService;
	@Autowired
	private PlayerService playerService;
	
	@Override
	protected PurgatoryPointsLog newLog() {
		return new PurgatoryPointsLog();
	}

	@Override
	protected BaseRepository<PurgatoryPointsLog> initRepository() {
		return SlimDao.getRepository(PurgatoryPointsLog.class);
	}

	@Override
	protected IThingType thingType() {
		return ItemType.PURGATORY_POINTS;
	}

	@Override
	protected ITipCause lackCode() {
		return TipsCode.PURGATORY_POINTS_LACK;
	}

	@Override
	public long getCount(long entityId, int thingId) {
		return purgatoryService.getEntity(entityId).getPoints();
	}

	@Override
	protected RewardReceipt doAdd(long entityId, List<? extends IRewardThing> rewardThings, IGameCause cause, String remark) {
		Purgatory purgatory = purgatoryService.getEntity(entityId);
		long sum = 0;
		for (IRewardThing rewardThing : rewardThings) {
			sum += rewardThing.getNum();
		}
		purgatory.setPoints(purgatory.getPoints() + sum);
		purgatoryService.update(purgatory);

		return new RewardReceipt(rewardThings);
	}

	@Override
	protected CostReceipt doCost(long entityId, List<? extends ICostThing> costThings, IGameCause cause, String remark) {
		Purgatory purgatory = purgatoryService.getEntity(entityId);
		long sum = 0;
		for (ICostThing costThing : costThings) {
			sum += costThing.getNum();
		}
		purgatory.setPoints(purgatory.getPoints() - sum);
		purgatoryService.update(purgatory);

		return new CostReceipt(costThings);
	}
	@Override
	protected String getOwnerName(long entityId) {
		Player player = playerService.getEntity(entityId);
		return player.getNick();
	}
}
