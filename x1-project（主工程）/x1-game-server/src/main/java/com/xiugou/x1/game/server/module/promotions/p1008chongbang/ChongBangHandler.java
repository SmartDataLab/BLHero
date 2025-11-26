/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1008chongbang;

import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.module.ActiveRankRewardsCache;
import com.xiugou.x1.design.module.autogen.ActiveRankRewardsAbstractCache.ActiveRankRewardsCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;

import pb.xiugou.x1.protobuf.promotion.P1008ChongBang.ChongBangRewardPreviewRequest;
import pb.xiugou.x1.protobuf.promotion.P1008ChongBang.ChongBangRewardPreviewResponse;
import pb.xiugou.x1.protobuf.promotion.P1008ChongBang.PbChongBangReward;

/**
 * @author YY
 *
 */
@Controller
public class ChongBangHandler {

	@Autowired
	private ActiveRankRewardsCache activeRankRewardsCache;
	
	@PlayerCmd
	public ChongBangRewardPreviewResponse rewardPreview(PlayerContext playerContext, ChongBangRewardPreviewRequest request) {
		List<ActiveRankRewardsCfg> cfgList = activeRankRewardsCache.findInActiveIdCollector(request.getTypeId());
		
		ChongBangRewardPreviewResponse.Builder response = ChongBangRewardPreviewResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		for(ActiveRankRewardsCfg cfg : cfgList) {
			PbChongBangReward.Builder data = PbChongBangReward.newBuilder();
			data.setRankUp(cfg.getRankUp());
			data.setRankDown(cfg.getRankDown());
			data.addAllItems(PbHelper.buildReward(cfg.getRewards()));
			response.addRewards(data.build());
		}
		return response.build();
	}
}
