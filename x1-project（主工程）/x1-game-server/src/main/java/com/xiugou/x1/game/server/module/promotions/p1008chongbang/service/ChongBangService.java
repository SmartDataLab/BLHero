/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1008chongbang.service;

import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ActiveTemplateCache;
import com.xiugou.x1.design.module.autogen.ActiveTemplateAbstractCache.ActiveTemplateCfg;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1008chongbang.event.HeroRecruitRankEvent;
import com.xiugou.x1.game.server.module.promotions.p1008chongbang.model.ChongBang;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.recruit.event.RecruitNumEvent;

/**
 * @author YY
 *
 */
@Service
public class ChongBangService extends OneToManyService<ChongBang> {

	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ActiveTemplateCache activeTemplateCache;
	
	public ChongBang getEntity(long playerId, int typeId) {
		ChongBang entity = this.repository().getByKeys(playerId, typeId);
		if(entity == null) {
			ActiveTemplateCfg activeTemplateCfg = activeTemplateCache.getOrThrow(typeId);
			entity = new ChongBang();
			entity.setPid(playerId);
			entity.setTypeId(typeId);
			entity.setTypeName(activeTemplateCfg.getName());
			this.insert(entity);
		}
		boolean needUpdate = false;
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(entity.getTurns() != promotionControl.getTurns()) {
			entity.setTurns(promotionControl.getTurns());
			entity.setScore(0);
			needUpdate = true;
		}
		if(needUpdate) {
			this.update(entity);
		}
		return entity;
	}
	
	@Subscribe
	private void listen(RecruitNumEvent event) {
		int typeId = RankType.HERO_RECRUIT.getActivityId();
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(!promotionControl.isRunning()) {
			return;
		}
		ChongBang entity = this.getEntity(event.getPid(), typeId);
		entity.setScore(entity.getScore() + 1);
		this.update(entity);
		
		EventBus.post(HeroRecruitRankEvent.of(event.getPid(), typeId));
	}
}
