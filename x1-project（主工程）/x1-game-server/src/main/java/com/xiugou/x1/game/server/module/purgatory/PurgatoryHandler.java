package com.xiugou.x1.game.server.module.purgatory;

import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.PurgatoryCache;
import com.xiugou.x1.design.module.PurgatoryRefineCache;
import com.xiugou.x1.design.module.autogen.PurgatoryAbstractCache.PurgatoryCfg;
import com.xiugou.x1.design.module.autogen.PurgatoryRefineAbstractCache.PurgatoryRefineCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;
import com.xiugou.x1.game.server.module.purgatory.model.PurgatorySystem;
import com.xiugou.x1.game.server.module.purgatory.service.PurgatoryService;
import com.xiugou.x1.game.server.module.purgatory.service.PurgatorySystemService;

import pb.xiugou.x1.protobuf.purgatory.Purgatory.PbLevelAttr;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryBuyPlusTimesRequest;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryBuyPlusTimesResponse;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryBuyRefineItemRequest;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryBuyRefineItemResponse;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryInfoResponse;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryRefineRequest;
import pb.xiugou.x1.protobuf.purgatory.Purgatory.PurgatoryRefineResponse;

/**
 * @author yh
 * @date 2023/8/9
 * @apiNote
 */
@Controller
public class PurgatoryHandler extends AbstractModuleHandler {
	@Autowired
	private PurgatoryService purgatoryService;
	@Autowired
	private PurgatorySystemService purgatorySystemService;
	@Autowired
	private BattleConstCache battleConstCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private PurgatoryRefineCache purgatoryRefineCache;
	@Autowired
	private PurgatoryCache purgatoryCache;

	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		Purgatory purgatory = purgatoryService.getEntity(playerId);
		PurgatorySystem purgatorySystem = purgatorySystemService.getEntity();

		PurgatoryInfoResponse.Builder response = PurgatoryInfoResponse.newBuilder();
		Map<Integer, Attr> attrs = purgatory.getAttrs();
		for (Map.Entry<Integer, Attr> entry : attrs.entrySet()) {
			PbLevelAttr.Builder pbLevelAttr = PbLevelAttr.newBuilder();
			pbLevelAttr.setLevel(entry.getKey());
			pbLevelAttr.setPbAttr(PbHelper.build(entry.getValue()));
			response.addAttrs(pbLevelAttr);
		}
		response.setLevel(purgatory.getLevel());
		response.setFreeTimes(purgatory.getFreeTimes());
		response.setNextResetTime(LocalDateTimeUtil.toEpochMilli(purgatorySystem.getNextResetTime()));
		playerContextManager.push(playerId, PurgatoryInfoResponse.Proto.ID, response.build());
	}

	/**
	 * 购买洗练道具
	 */
	@PlayerCmd
	public PurgatoryBuyRefineItemResponse buyRefineItem(PlayerContext playerContext,
			PurgatoryBuyRefineItemRequest request) {
		thingService.cost(playerContext.getId(), battleConstCache.getPurgatory_buy_refine_item(),
				GameCause.PURGATORY_BUY_REFINE);

		PurgatoryRefineCfg purgatoryRefineCfg = purgatoryRefineCache.getOrThrow(1);

		CostThing cost = purgatoryRefineCfg.getCost();
		RewardThing thing = RewardThing.of(cost.getThingId(), 1);

		thingService.add(playerContext.getId(), thing, GameCause.PURGATORY_BUY_REFINE, NoticeType.TIPS);

		PurgatoryBuyRefineItemResponse.Builder response = PurgatoryBuyRefineItemResponse.newBuilder();
		response.setRewards(PbHelper.build(thing));
		return response.build();

	}

	/**
	 * 购买加成次数
	 */
	@PlayerCmd
	public PurgatoryBuyPlusTimesResponse buyPlusTimes(PlayerContext playerContext, PurgatoryBuyPlusTimesRequest request) {
		Purgatory purgatory = purgatoryService.getEntity(playerContext.getId());
		Asserts.isTrue(purgatory.getFreeTimes() >= 2, TipsCode.PURGATORY_HAVE_FREE_TIMES);

		CostThing costThing = battleConstCache.getPurgatory_buy_plus_times();
		thingService.cost(playerContext.getId(), costThing, GameCause.PURGATORY_BUY_PLUS_TIMES);

		purgatory.setPlusTimes(purgatory.getPlusTimes() + 1);
		purgatoryService.update(purgatory);

		PurgatoryBuyPlusTimesResponse.Builder response = PurgatoryBuyPlusTimesResponse.newBuilder();
		response.setTimes(purgatory.getPlusTimes());
		return response.build();
	}

	/**
	 * 洗练
	 */
	@PlayerCmd
	public PurgatoryRefineResponse refine(PlayerContext playerContext, PurgatoryRefineRequest request) {
		Purgatory purgatory = purgatoryService.getEntity(playerContext.getId());
		int level = purgatory.getLevel();
		Asserts.isTrue(request.getLevel() <= level, TipsCode.PURGATORY_LEVEL_NOT_COMPLETE);

		PurgatoryRefineCfg purgatoryRefineCfg = purgatoryRefineCache.getOrThrow(purgatory.getRefineTimes() + 1);
		CostThing cost = null;
		if (request.getStatus() == 1) {
			cost = purgatoryRefineCfg.getPassCost();
		} else {
			cost = purgatoryRefineCfg.getCost();
		}
		thingService.cost(playerContext.getId(), cost, GameCause.PURGATORY_REFINE);
		
		if(request.getStatus() == 1) {
			purgatory.setRefineTimes(purgatory.getRefineTimes() + 1);
		}

		PurgatoryCfg purgatoryCfg = purgatoryCache.getOrThrow(request.getLevel());
		Attr attr = purgatoryService.getAttrByStashId(purgatoryCfg.getAttrStash());
		Map<Integer, Attr> attrs = purgatory.getAttrs();
		attrs.put(request.getLevel(), attr);
		purgatoryService.update(purgatory);

		PurgatoryRefineResponse.Builder response = PurgatoryRefineResponse.newBuilder();
		response.setAttr(PbHelper.build(attr));
		return response.build();
	}

}
