/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1009zhigou.service;

import java.time.LocalDateTime;

import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.ZhiGouLiBaoCache;
import com.xiugou.x1.design.module.autogen.ZhiGouLiBaoAbstractCache.ZhiGouLiBaoCfg;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.promotion.model.PromotionControl;
import com.xiugou.x1.game.server.module.promotion.service.PromotionControlService;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.model.ZhiGou;
import com.xiugou.x1.game.server.module.promotions.p1009zhigou.struct.ZhiGouLimit;

/**
 * @author yy
 *
 */
@Service
public class ZhiGouService extends OneToManyService<ZhiGou> {

	@Autowired
	private PromotionControlService promotionControlService;
	@Autowired
	private ApplicationSettings applicationSettings;
	@Autowired
	private ZhiGouLiBaoCache zhiGouLiBaoCache;
	@Autowired
	private TimeSetting timeSetting;
	
	public ZhiGou getEntity(long playerId, int typeId) {
		ZhiGou entity = this.repository().getByKeys(playerId, typeId);
		if(entity == null) {
			entity = new ZhiGou();
			entity.setPid(playerId);
			entity.setTypeId(typeId);
			this.insert(entity);
		}
		PromotionControl promotionControl = promotionControlService.getControl(applicationSettings.getGameServerId(), typeId);
		if(entity.getTurns() != promotionControl.getTurns()) {
			entity.setTurns(promotionControl.getTurns());
			entity.getLimitBuys().clear();
			this.update(entity);
		} else {
			boolean needUpdate = false;
			LocalDateTime now = LocalDateTimeUtil.now();
			if(now.isAfter(entity.getMonthlyTime())) {
				entity.setMonthlyTime(timeSetting.nextMonthOTime());
				for(ZhiGouLimit zhiGouLimit : entity.getLimitBuys().values()) {
					ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(zhiGouLimit.getId());
					if(zhiGouLiBaoCfg.getLimitType() != 3) {
						continue;
					}
					zhiGouLimit.setNum(0);
				}
				needUpdate = true;
			}
			if(now.isAfter(entity.getWeeklyTime())) {
				entity.setWeeklyTime(timeSetting.nextWeekMondayOTime());
				for(ZhiGouLimit zhiGouLimit : entity.getLimitBuys().values()) {
					ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(zhiGouLimit.getId());
					if(zhiGouLiBaoCfg.getLimitType() != 2) {
						continue;
					}
					zhiGouLimit.setNum(0);
				}
				needUpdate = true;
			}
			if(now.isAfter(entity.getDailyTime())) {
				entity.setDailyTime(timeSetting.nextDayOTime());
				for(ZhiGouLimit zhiGouLimit : entity.getLimitBuys().values()) {
					ZhiGouLiBaoCfg zhiGouLiBaoCfg = zhiGouLiBaoCache.getOrThrow(zhiGouLimit.getId());
					if(zhiGouLiBaoCfg.getLimitType() != 1) {
						continue;
					}
					zhiGouLimit.setNum(0);
				}
				needUpdate = true;
			}
			if(needUpdate) {
				this.update(entity);
			}
		}
		return entity;
	}
}
