/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.LanguageAutoCache;
import com.xiugou.x1.design.module.MeiRiChongZhiCache;
import com.xiugou.x1.design.module.MeiRiChongZhiCache.MeiRiChongZhiConfig;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.MeiRiChongZhiAbstractCache.MeiRiChongZhiCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.promotion.constant.PromotionLogicType;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.model.MeiRiChongZhi;
import com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.service.MeiRiChongZhiPromotionService;
import com.xiugou.x1.game.server.module.promotions.p1011meirichongzhi.service.MeiRiChongZhiService;

import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiCfgRequest;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiCfgResponse;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiInfoRequest;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiInfoResponse;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiTakeRewardRequest;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.MeiRiChongZhiTakeRewardResponse;
import pb.xiugou.x1.protobuf.promotion.P1011MeiRiChongZhi.PbMeiRiChongZhiCfg;
import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionRedPointMessage;

/**
 * @author yy
 * 前端非要把这玩意包装成跟P1010一样的结构，唉、、、
 */
@Controller
public class MeiRiChongZhiHandler {

	@Autowired
	private MeiRiChongZhiService meiRiChongZhiService;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private MeiRiChongZhiCache meiRiChongZhiCache;
	@Autowired
	private ThingService thingService;
	@Autowired
	private PlayerContextManager playerContextManager;
	@Autowired
	private MeiRiChongZhiPromotionService meiRiChongZhiPromotionService;
	@Autowired
	private LanguageAutoCache languageAutoCache;
	
	
	@PlayerCmd
	public MeiRiChongZhiInfoResponse info(PlayerContext playerContext, MeiRiChongZhiInfoRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.MEI_RI_CHONG_ZHI.getValue(), TipsCode.ERROR_PARAM);
		
		MeiRiChongZhi entity = meiRiChongZhiService.getEntity(playerContext.getId(), request.getTypeId());
		
		MeiRiChongZhiInfoResponse.Builder response = MeiRiChongZhiInfoResponse.newBuilder();
		response.setTypeId(entity.getTypeId());
		for(MeiRiChongZhiCfg cfg : meiRiChongZhiCache.getInActivityIdCollector(entity.getTypeId())) {
			response.addTasks(meiRiChongZhiService.buildToTask(entity, cfg));
		}
		return response.build();
	}
	
	
	@PlayerCmd
	public MeiRiChongZhiTakeRewardResponse takeReward(PlayerContext playerContext, MeiRiChongZhiTakeRewardRequest request) {
		ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(request.getTypeId());
		Asserts.isTrue(activeTemplateCfg.getLogicType() == PromotionLogicType.MEI_RI_CHONG_ZHI.getValue(), TipsCode.ERROR_PARAM);
		
		promotionControlService.assertRunning(applicationSettings.getGameServerId(), request.getTypeId());
		
		MeiRiChongZhi entity = meiRiChongZhiService.getEntity(playerContext.getId(), request.getTypeId());
		Asserts.isTrue(entity.getCanRewardId() >= request.getTaskId(), TipsCode.MRCZ_REWARD_LOCK);
		Asserts.isTrue(!entity.getTakeRewards().contains(request.getTaskId()), TipsCode.MRCZ_REWARD_TOOK);
		
		MeiRiChongZhiCfg meiRiChongZhiCfg = meiRiChongZhiCache.getInActivityIdRewardIdIndex(entity.getTypeId(), request.getTaskId());
		List<RewardThing> rewardList = new ArrayList<>();
		rewardList.addAll(meiRiChongZhiCfg.getRewards());
		for(RewardThing select : meiRiChongZhiCfg.getSelectRewards()) {
			if(select.getThingId() == request.getSelectItem()) {
				rewardList.add(select);
				break;
			}
		}
		entity.getTakeRewards().add(request.getTaskId());
		meiRiChongZhiService.update(entity);
		
		thingService.add(playerContext.getId(), rewardList, GameCause.MRCZ_REWARD);
		
		PromotionRedPointMessage.Builder redMessage = PromotionRedPointMessage.newBuilder();
		redMessage.setTypeId(request.getTypeId());
		redMessage.setRedPoint(meiRiChongZhiPromotionService.showLoginRedPoint(playerContext.getId(), request.getTypeId()));
		playerContextManager.push(playerContext.getId(), PromotionRedPointMessage.Proto.ID, redMessage.build());
		
		MeiRiChongZhiTakeRewardResponse.Builder response = MeiRiChongZhiTakeRewardResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		response.setTasks(meiRiChongZhiService.buildToTask(entity, meiRiChongZhiCfg));
		return response.build();
	}
	
	@PlayerCmd
	public MeiRiChongZhiCfgResponse cfgs(PlayerContext playerContext, MeiRiChongZhiCfgRequest request) {
		List<MeiRiChongZhiConfig> cfgs = meiRiChongZhiCache.findInActivityIdCollector(request.getTypeId());
		if(cfgs == null) {
			return MeiRiChongZhiCfgResponse.getDefaultInstance();
		}
		
		MeiRiChongZhiCfgResponse.Builder response = MeiRiChongZhiCfgResponse.newBuilder();
		response.setTypeId(request.getTypeId());
		for(MeiRiChongZhiConfig cfg : cfgs) {
			PbMeiRiChongZhiCfg.Builder data = PbMeiRiChongZhiCfg.newBuilder();
			data.setId(cfg.getRewardId());
			data.setIndex(cfg.getRewardId());
			data.setTaskTargetNum(cfg.getRewardId());
			data.addAllSelectRewards(PbHelper.buildReward(cfg.getSelectRewards()));
			data.addAllRewards(PbHelper.buildReward(cfg.getRewards()));
			data.setDesc(languageAutoCache.getLang(playerContext.getLangType(), cfg.getDescLang()));
			response.addCfgs(data.build());
		}
		return response.build();
	} 
}
