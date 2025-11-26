/**
 *
 */
package com.xiugou.x1.game.server.module.goldenpig.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.goldenpig.model.GoldenPig;
import com.xiugou.x1.game.server.module.vip.constant.VipFuncType;
import com.xiugou.x1.game.server.module.vip.service.VipService;

/**
 * @author YY
 */
@Service
public class GoldenPigService extends PlayerOneToOneResetableService<GoldenPig> {
	@Autowired
	private ThingService thingService;
	@Autowired
	private BattleTypeCache battleTypeCache;
	@Autowired
	private VipService privilegeBoostService;

	@Override
	protected GoldenPig createWhenNull(long entityId) {
		GoldenPig entity = new GoldenPig();
		entity.setPid(entityId);
		return entity;
	}

	@Override
	protected void doDailyReset(GoldenPig entity) {
		int challengeNum = entity.getChallengeNum();
		// 发放免费门票
		if (challengeNum == 0) {
			return;
		}
		entity.setChallengeNum(0);
		BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(BattleType.GOLDEN_PIG.getValue());
		RewardThing freeTicket = battleTypeCfg.getFreeTicket();

		List<RewardThing> tickets = new ArrayList<>();
		tickets.add(freeTicket);
		challengeNum -= 1;

		if (challengeNum > 0) {
			int times = privilegeBoostService.boostTimes(entity.getPid(), VipFuncType.GOLD_PIG_TIME);
			tickets.add(ThingUtil.multiplyReward(freeTicket, Math.min(times, challengeNum)));
		}
		thingService.add(entity.getPid(), tickets, GameCause.GOLDENPIG_TICKET, NoticeType.SLIENT);
	}
}
