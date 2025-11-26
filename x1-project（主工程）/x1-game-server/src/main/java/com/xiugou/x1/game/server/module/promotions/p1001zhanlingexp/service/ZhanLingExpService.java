/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ZhanLingExpCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.design.module.autogen.ZhanLingExpAbstractCache.ZhanLingExpCfg;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1001zhanlingexp.model.ZhanLingExp;

import pb.xiugou.x1.protobuf.promotion.Promotion.PromotionRedPointMessage;

/**
 * @author hyy
 *
 */
@Service
public class ZhanLingExpService extends OneToManyService<ZhanLingExp> {

	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ZhanLingExpCache zhanLingExpCache;
	@Autowired
	private PlayerContextManager playerContextManager;
	
	public ZhanLingExp getEntity(long playerId, int typeId) {
		ZhanLingExp entity = this.repository().getByKeys(playerId, typeId);
		if(entity == null) {
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(typeId);
			entity = new ZhanLingExp();
			entity.setPid(playerId);
			entity.setTypeId(typeId);
			entity.setTypeName(activeTemplateCfg.getName());
			this.insert(entity);
		}
		boolean needUpdate = false;
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(entity.getTurns() != promotionControl.getTurns()) {
			entity.setTurns(promotionControl.getTurns());
			entity.setLevel(0);
			entity.setExp(0);
			entity.setFreeRewardLv(0);
			entity.setPremiumRewardLv(0);
			entity.setBuyPremium(false);
			needUpdate = true;
		}
		if(needUpdate) {
			this.update(entity);
		}
		return entity;
	}
	
	public List<RewardThing> takeRewardsAndMark(ZhanLingExp entity) {
		List<RewardThing> rewardList = new ArrayList<>();
		//免费奖励
		for(int i = entity.getFreeRewardLv() + 1; i <= entity.getLevel(); i++) {
			ZhanLingExpCfg cfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), i);
			if(cfg == null) {
				continue;
			}
			rewardList.addAll(cfg.getFreeReward());
		}
		entity.setFreeRewardLv(entity.getLevel());
		//高级奖励
		if(entity.isBuyPremium()) {
			for(int i = entity.getPremiumRewardLv() + 1; i <= entity.getLevel(); i++) {
				ZhanLingExpCfg cfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), i);
				if(cfg == null) {
					continue;
				}
				rewardList.addAll(cfg.getPremiumReward());
			}
			entity.setPremiumRewardLv(entity.getLevel());
		}
		return rewardList;
	}
	
	public void addExp(long playerId, long exp) {
		List<ZhanLingExp> entityList = this.getEntities(playerId);
		for(ZhanLingExp entity : entityList) {
			if(!promotionControlService.isRunning(applicationSettings.getGameServerId(), entity.getTypeId())) {
				continue;
			}
			
			entity.setExp(entity.getExp() + exp);
			
			boolean levelUp = false;
	        ZhanLingExpCfg zhanLingExpCfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), entity.getLevel() + 1);
	        while(zhanLingExpCfg != null && entity.getExp() >= zhanLingExpCfg.getNeedExp()) {
	        	entity.setExp(entity.getExp() - zhanLingExpCfg.getNeedExp());
	        	entity.setLevel(entity.getLevel() + 1);
	        	
	        	zhanLingExpCfg = zhanLingExpCache.findInActivityIdLevelIndex(entity.getTypeId(), entity.getLevel() + 1);
	        	levelUp = true;
	        }
	        this.update(entity);
	        
	        if(levelUp) {
	        	PromotionRedPointMessage.Builder message = PromotionRedPointMessage.newBuilder();
		        message.setTypeId(entity.getTypeId());
		        message.setRedPoint(true);
		        playerContextManager.push(playerId, PromotionRedPointMessage.Proto.ID, message.build());
	        }
		}
	}
}
