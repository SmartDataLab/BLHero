/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.service;

import java.util.List;

import org.gaming.prefab.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.ActivityRewardsCache;
import com.xiugou.x1.design.module.ActivityRewardsCache.ActivityRewardsConfig;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1010huodongmubiao.model.HuoDongMuBiao;

/**
 * @author yy
 *
 */
@Service
public class HuoDongMuBiaoService extends OneToManyService<HuoDongMuBiao> {

	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ActivityRewardsCache activityRewardsCache;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	
	public HuoDongMuBiao getEntity(long playerId, int typeId) {
		HuoDongMuBiao entity = this.repository().getByKeys(playerId, typeId);
		if(entity == null) {
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(typeId);
			entity = new HuoDongMuBiao();
			entity.setPid(playerId);
			entity.setTypeId(typeId);
			entity.setTypeName(activeTemplateCfg.getName());
			this.insert(entity);
		}
		boolean needUpdate = false;
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(entity.getTurns() != promotionControl.getTurns()) {
			entity.setTurns(promotionControl.getTurns());
			entity.setRewardRound(1);
			entity.getTasks().clear();
			List<ActivityRewardsConfig> cfgList = activityRewardsCache.getInTemplateIdCollector(typeId);
			for(ActivityRewardsConfig cfg : cfgList) {
				entity.getTasks().add(Task.create(cfg.getId()));
			}
			needUpdate = true;
		}
		if(needUpdate) {
			this.update(entity);
		}
		return entity;
	}
}
