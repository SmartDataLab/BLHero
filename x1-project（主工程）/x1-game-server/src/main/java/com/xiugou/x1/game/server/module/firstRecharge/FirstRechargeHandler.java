package com.xiugou.x1.game.server.module.firstRecharge;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.FirstRechargeCache;
import com.xiugou.x1.design.module.autogen.FirstRechargeAbstractCache.FirstRechargeCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.firstRecharge.model.FirstRecharge;
import com.xiugou.x1.game.server.module.firstRecharge.service.FirstRechargeService;
import com.xiugou.x1.game.server.module.firstRecharge.struct.FirstRechargeData;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;

import pb.xiugou.x1.protobuf.firstrecharge.FirstRecharge.FirstRechargeInfoResponse;
import pb.xiugou.x1.protobuf.firstrecharge.FirstRecharge.FirstRechargeRewardRequest;
import pb.xiugou.x1.protobuf.firstrecharge.FirstRecharge.FirstRechargeRewardResponse;

/**
 * @author yh
 * @date 2023/8/21
 * @apiNote
 */
@Controller
public class FirstRechargeHandler extends AbstractModuleHandler {
	@Autowired
	private FirstRechargeService firstRechargeService;
	@Autowired
	private FirstRechargeCache firstRechargeCache;
	@Autowired
	private ThingService thingService;

	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public boolean needDailyPush() {
		return true;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		FirstRecharge firstRecharge = firstRechargeService.getEntity(playerId);
		
		FirstRechargeInfoResponse.Builder response = FirstRechargeInfoResponse.newBuilder();
		response.setDailyTime(LocalDateTimeUtil.toEpochMilli(firstRecharge.getDailyTime()));
		for(FirstRechargeData rechargeData : firstRecharge.getRechargeDatas().values()) {
			response.addRechargeData(firstRechargeService.build(rechargeData));
		}
		playerContextManager.push(playerId, FirstRechargeInfoResponse.Proto.ID, response.build());
	}

	@PlayerCmd
	public FirstRechargeRewardResponse reward(PlayerContext playerContext, FirstRechargeRewardRequest request) {
		FirstRecharge firstRecharge = firstRechargeService.getEntity(playerContext.getId());
		FirstRechargeData rechargeData = firstRecharge.getRechargeDatas().get(request.getRechargeId());
		Asserts.isTrue(rechargeData != null, TipsCode.FIRST_RECHARGE_NO_BUY);
		Asserts.isTrue(rechargeData.getCanTakeDay() > rechargeData.getRewardDay(), TipsCode.FIRST_RECHARGE_RECEIVED_TODAY_REWARD);

		int rewardDay = rechargeData.getRewardDay() + 1;
		rechargeData.setRewardDay(rewardDay);
		firstRechargeService.update(firstRecharge);
		
		FirstRechargeCfg rechargeCfg = firstRechargeCache.getInRechargeIdDayIndex(rechargeData.getRechargeId(), rewardDay);
		thingService.add(playerContext.getId(), rechargeCfg.getReward(), GameCause.FIRST_RECHARGE_REWARD);
		
		FirstRechargeRewardResponse.Builder response = FirstRechargeRewardResponse.newBuilder();
		response.setRechargeData(firstRechargeService.build(rechargeData));
		return response.build();
	}

}
