package com.xiugou.x1.game.server.module.rank;

import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.RankRewardCache;
import com.xiugou.x1.design.module.autogen.RankRewardAbstractCache.RankRewardCfg;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.rank.model.RankPlayer;
import com.xiugou.x1.game.server.module.rank.model.RankReward;
import com.xiugou.x1.game.server.module.rank.service.PlayerRankService;
import com.xiugou.x1.game.server.module.rank.service.RankPlayerService;
import com.xiugou.x1.game.server.module.rank.service.RankRewardService;
import com.xiugou.x1.game.server.module.rank.struct.RankFirstReach;
import com.xiugou.x1.game.server.module.rank.struct.RankRewardData;

import pb.xiugou.x1.protobuf.rank.Rank.PbRankInfo;
import pb.xiugou.x1.protobuf.rank.Rank.PbRanker;
import pb.xiugou.x1.protobuf.rank.Rank.RankInfoResponse;
import pb.xiugou.x1.protobuf.rank.Rank.RankRequest;
import pb.xiugou.x1.protobuf.rank.Rank.RankResponse;
import pb.xiugou.x1.protobuf.rank.Rank.RankRewardRequest;
import pb.xiugou.x1.protobuf.rank.Rank.RankRewardResponse;

/**
 * @author yh
 * @date 2023/7/24
 * @apiNote
 */
@Controller
public class RankHandler extends AbstractModuleHandler {
	@Autowired
	private RankRewardService rankRewardService;
	@Autowired
	private RankRewardCache rankRewardCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private RankPlayerService rankPlayerService;

	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}

	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		RankPlayer rankPlayer = rankPlayerService.getEntity(playerId);
		
		RankInfoResponse.Builder response = RankInfoResponse.newBuilder();
		for(RankType rankType : RankType.values()) {
			RankReward rankReward = rankRewardService.getEntity(rankType.getValue());
			
			RankRewardData rankRewardData = rankPlayer.getTypeRewards().get(rankType.getValue());
			
			PbRankInfo.Builder builder = PbRankInfo.newBuilder();
			builder.setRankType(rankType.getValue());
			if(rankRewardData != null) {
				builder.addAllRewardId(rankRewardData.getRewards());
			}
			builder.setAchieveId(rankReward.getRewardId());
			response.addRankInfos(builder.build());
		}
		playerContextManager.push(playerId, RankInfoResponse.Proto.ID, response.build());
	}
	

	@PlayerCmd
	public RankResponse info(PlayerContext playerContext, RankRequest request) {
		int rankType = request.getRankType();
		PlayerRankService playerRankService = PlayerRankService.getService(RankType.getRankType(rankType));
		
		PbRanker selfRanker = playerRankService.buildSelf(playerContext.getId());
		List<PbRanker> rankers = playerRankService.buildList(request.getPage(), 50);

		RankReward rankReward = rankRewardService.getEntity(rankType);
		RankPlayer rankPlayer = rankPlayerService.getEntity(playerContext.getId());
		
		RankResponse.Builder response = RankResponse.newBuilder();
		response.setRankType(rankType);
		response.setPage(request.getPage());
		RankRewardData rankRewardData = rankPlayer.getTypeRewards().get(rankType);
		if(rankRewardData != null) {
			response.addAllRewardId(rankRewardData.getRewards());
		}
		response.addAllRankList(rankers);
		response.setAchieveId(rankReward.getRewardId());
		response.setSelfRank(selfRanker);
		for(RankFirstReach rankFirstReach : rankReward.getFirstReachs().values()) {
			response.addFirstReaches(rankFirstReach.build());
		}
		return response.build();
	}

	@PlayerCmd
	public RankRewardResponse receiveReward(PlayerContext playerContext, RankRewardRequest request) {
		int rewardId = request.getRewardId();
		RankType rankType = RankType.getRankType(request.getRankType());
		Asserts.isTrue(rankType != null, TipsCode.RANK_TYPE_MISS);
		RankReward rankReward = rankRewardService.getEntity(request.getRankType());
		int maxReward = rankReward.getRewardId(); //全服最高可领的奖励ID
		Asserts.isTrue(rewardId <= maxReward, TipsCode.RANK_NO_RECEIVED_REWARD);

		RankPlayer rankPlayer = rankPlayerService.getEntity(playerContext.getId());
		//奖励ID不能大于全服可领奖励ID，奖励不能重复领取
		RankRewardData rankRewardData = rankPlayer.getTypeRewards().get(rankType.getValue());
		if(rankRewardData == null) {
			rankRewardData = new RankRewardData();
			rankRewardData.setRankType(rankType.getValue());
			rankPlayer.getTypeRewards().put(rankRewardData.getRankType(), rankRewardData);
		}
		
		Asserts.isTrue(!rankRewardData.getRewards().contains(rewardId), TipsCode.RANK_RECEIVED_REWARD);
		
		RankRewardCfg rewardCfg = rankRewardCache.getInTypeRewardIdIndex(request.getRankType(), rewardId);

		rankRewardData.getRewards().add(rewardId);
		rankPlayerService.update(rankPlayer);

		thingService.add(playerContext.getId(), rewardCfg.getReward(), GameCause.RANK_REWARD, NoticeType.TIPS);

		RankRewardResponse.Builder response = RankRewardResponse.newBuilder();
		response.setRankType(request.getRankType());
		response.addAllRewardId(rankRewardData.getRewards());
		return response.build();
	}
}
