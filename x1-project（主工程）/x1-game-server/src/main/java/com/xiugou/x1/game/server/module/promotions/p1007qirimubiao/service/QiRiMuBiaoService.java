/**
 * 
 */
package com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.service;

import org.gaming.prefab.task.Task;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.QiRiMuBiaoTaskCache;
import com.xiugou.x1.design.module.QiRiMuBiaoTaskCache.QiRiMuBiaoTaskConfig;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.promotions.p1007qirimubiao.model.QiRiMuBiao;

/**
 * @author YY
 *
 */
@Service
public class QiRiMuBiaoService extends PlayerOneToOneResetableService<QiRiMuBiao> {

	@Autowired
	private QiRiMuBiaoTaskCache qiRiMuBiaoTaskCache;
	@Autowired
	private BattleConstCache battleConstCache;
	
	@Override
	protected QiRiMuBiao createWhenNull(long entityId) {
		QiRiMuBiao entity = new QiRiMuBiao();
		entity.setPid(entityId);
		entity.setDay(1);
		entity.setOpened(false);
		for(QiRiMuBiaoTaskConfig taskCfg : qiRiMuBiaoTaskCache.all()) {
			Task task = Task.create(taskCfg.getId());
			entity.getTaskMap().put(task.getId(), task);
		}
		entity.setEndTime(LocalDateTimeUtil.now().plusDays(battleConstCache.getQrmb_last_days()));
		return entity;
	}

	@Override
	protected void doDailyReset(QiRiMuBiao entity) {
		if(!entity.isOpened()) {
			return;
		}
		entity.setDay(entity.getDay() + 1);
	}

	@Override
	protected boolean doSpecialReset(QiRiMuBiao entity) {
		if(entity.getTaskMap().size() == qiRiMuBiaoTaskCache.all().size()) {
			return false;
		}
		for(QiRiMuBiaoTaskConfig taskCfg : qiRiMuBiaoTaskCache.all()) {
			if(entity.getTaskMap().containsKey(taskCfg.getId())) {
				continue;
			}
			Task task = Task.create(taskCfg.getId());
			entity.getTaskMap().put(task.getId(), task);
		}
		return true;
	}
	
	
}
