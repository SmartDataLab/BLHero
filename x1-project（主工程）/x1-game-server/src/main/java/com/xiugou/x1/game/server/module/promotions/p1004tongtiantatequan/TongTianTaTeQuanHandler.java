/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.TongTianTaTeQuanRewardCache;
import com.xiugou.x1.design.module.autogen.TongTianTaTeQuanRewardAbstractCache.TongTianTaTeQuanRewardCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.model.TongTianTaTeQuan;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.service.TongTianTaTeQuanService;
import com.xiugou.x1.game.server.module.promotions.p1004tongtiantatequan.struct.TTTTQDetail;
import com.xiugou.x1.game.server.module.tower.model.Tower;
import com.xiugou.x1.game.server.module.tower.service.BaseTowerBattleProcessor;
import com.xiugou.x1.game.server.module.tower.service.TowerService;

import pb.xiugou.x1.protobuf.promotion.P1004TongTianTaTeQuan.TTTTQTakeRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1004TongTianTaTeQuan.TTTTQTakeRewardResponse;
import pb.xiugou.x1.protobuf.promotion.P1004TongTianTaTeQuan.TongTianTaTeQuanRequest;
import pb.xiugou.x1.protobuf.promotion.P1004TongTianTaTeQuan.TongTianTaTeQuanResponse;

/**
 * @author YY
 *
 */
@Controller
public class TongTianTaTeQuanHandler {

	@Autowired
	private TongTianTaTeQuanService tongTianTaTeQuanService;
	@Autowired
	private TongTianTaTeQuanRewardCache tongTianTaTeQuanRewardCache;
	@Autowired
	private TowerService towerService;
	@Autowired
	private ThingService thingService;
	
	@PlayerCmd
	public TongTianTaTeQuanResponse info(PlayerContext playerContext, TongTianTaTeQuanRequest request) {
		TongTianTaTeQuan entity = tongTianTaTeQuanService.getEntity(playerContext.getId());
		
		TongTianTaTeQuanResponse.Builder response = TongTianTaTeQuanResponse.newBuilder();
		for(TTTTQDetail detail : entity.getTowerDetails().values()) {
			response.addDetails(detail.build());
		}
		return response.build();
	}
	
	@PlayerCmd
	public TTTTQTakeRewardResponse takeReward(PlayerContext playerContext, TTTTQTakeRewardRequest request) {
		TongTianTaTeQuan entity = tongTianTaTeQuanService.getEntity(playerContext.getId());
		TTTTQDetail detail = entity.getTowerDetails().get(request.getTowerType());
		Asserts.isTrue(detail != null, TipsCode.TTTTQ_ERROR_PARAM, request.getTowerType());
		
		Tower tower = towerService.getEntity(playerContext.getId());
		BaseTowerBattleProcessor towerProcessor = TowerService.getProcessor(detail.getTowerType());
		int layer = towerProcessor.getTowerLayer(tower);
		
		List<TongTianTaTeQuanRewardCfg> rewardConfigs = tongTianTaTeQuanRewardCache.getInTowerTypeRoundCollector(detail.getTowerType(), detail.getRound());
		
		List<RewardThing> rewardList = new ArrayList<>();
		int normalMaxLv = 0;
		int highMaxLv = 0;
		for(TongTianTaTeQuanRewardCfg rewardCfg : rewardConfigs) {
			if(rewardCfg.getLayer() > layer) {
				continue;
			}
			if(rewardCfg.getRewardLv() > detail.getNormalRewardLv()) {
				rewardList.add(rewardCfg.getNormalReward());
				if(rewardCfg.getRewardLv() > normalMaxLv) {
					normalMaxLv = rewardCfg.getRewardLv();
				}
			}
			if(detail.isOpenHigh() && rewardCfg.getRewardLv() > detail.getHighRewardLv()) {
				rewardList.add(rewardCfg.getHighReward());
				if(rewardCfg.getRewardLv() > highMaxLv) {
					highMaxLv = rewardCfg.getRewardLv();
				}
			}
		}
		Asserts.isTrue(!rewardList.isEmpty(), TipsCode.TTTTQ_NO_REWARD);
		
		detail.setNormalRewardLv(normalMaxLv);
		detail.setHighRewardLv(highMaxLv);
		if(detail.getHighRewardLv() >= rewardConfigs.size()) {
			detail.setRound(detail.getRound() + 1);
			detail.setOpenHigh(false);
			detail.setNormalRewardLv(0);
			detail.setHighRewardLv(0);
		}
		tongTianTaTeQuanService.update(entity);
		
		thingService.add(playerContext.getId(), rewardList, GameCause.TTTTQ_REWARD);
		
		TTTTQTakeRewardResponse.Builder response = TTTTQTakeRewardResponse.newBuilder();
		response.setDetail(detail.build());
		return response.build();
	}
}
