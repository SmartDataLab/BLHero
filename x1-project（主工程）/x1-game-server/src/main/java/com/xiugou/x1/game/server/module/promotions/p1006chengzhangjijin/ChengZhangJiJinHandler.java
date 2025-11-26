/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ChengZhangJiJinCache;
import com.xiugou.x1.design.module.autogen.ChengZhangJiJinAbstractCache.ChengZhangJiJinCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;
import com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.model.ChengZhangJiJin;
import com.xiugou.x1.game.server.module.promotions.p1006chengzhangjijin.service.ChengZhangJiJinService;

import pb.xiugou.x1.protobuf.promotion.P1006ChengZhangJiJin.CZJJTakeRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1006ChengZhangJiJin.CZJJTakeRewardResponse;
import pb.xiugou.x1.protobuf.promotion.P1006ChengZhangJiJin.ChengZhangJiJinRequest;
import pb.xiugou.x1.protobuf.promotion.P1006ChengZhangJiJin.ChengZhangJiJinResponse;

/**
 * @author YY
 *
 */
@Controller
public class ChengZhangJiJinHandler {

	@Autowired
	private ChengZhangJiJinService chengZhangJiJinService;
	@Autowired
	private ThingService thingService;
	@Autowired
	private ChengZhangJiJinCache chengZhangJiJinCache;
	@Autowired
	private PlayerService playerService;
	
	
	@PlayerCmd
	public ChengZhangJiJinResponse info(PlayerContext playerContext, ChengZhangJiJinRequest request) {
		ChengZhangJiJin entity = chengZhangJiJinService.getEntity(playerContext.getId());
		
		ChengZhangJiJinResponse.Builder response = ChengZhangJiJinResponse.newBuilder();
		response.setRound(entity.getRound());
		response.setOpen(entity.isOpen());
		response.addAllRewardIds(entity.getRewardIds());
		return response.build();
	}
	
	@PlayerCmd
	public CZJJTakeRewardResponse takeReward(PlayerContext playerContext, CZJJTakeRewardRequest request) {
		ChengZhangJiJin entity = chengZhangJiJinService.getEntity(playerContext.getId());
		Asserts.isTrue(!entity.getRewardIds().contains(request.getRewardId()), TipsCode.CZJJ_TOOK_REWARD);
		
		Player player = playerService.getEntity(playerContext.getId());
		
		ChengZhangJiJinCfg chengZhangJiJinCfg = chengZhangJiJinCache.getInRoundRewardIdIndex(entity.getRound(), request.getRewardId());
		Asserts.isTrue(player.getLevel() >= chengZhangJiJinCfg.getLevel(), TipsCode.CZJJ_LEVEL_LACK);
		
		entity.getRewardIds().add(chengZhangJiJinCfg.getRewardId());
		if(entity.getRewardIds().size() >= chengZhangJiJinCache.getInRoundCollector(entity.getRound()).size()) {
			entity.setRound(entity.getRound() + 1);
			entity.getRewardIds().clear();
			entity.setOpen(false);
		}
		chengZhangJiJinService.update(entity);
		
		thingService.add(playerContext.getId(), chengZhangJiJinCfg.getReward(), GameCause.CZJJ_REWARD);
		
		CZJJTakeRewardResponse.Builder response = CZJJTakeRewardResponse.newBuilder();
		response.setRound(entity.getRound());
		response.setOpen(entity.isOpen());
		response.setRewardId(chengZhangJiJinCfg.getId());
		return response.build();
	}
}
