/**
 * 
 */
package com.xiugou.x1.game.server.module.tower.service;

import java.util.HashMap;
import java.util.Map;

import org.gaming.prefab.mail.MailArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.TowerCache;
import com.xiugou.x1.design.module.autogen.TowerAbstractCache.TowerCfg;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.mail.module.Mail;
import com.xiugou.x1.game.server.module.mail.service.MailService;
import com.xiugou.x1.game.server.module.tower.constant.TowerType;
import com.xiugou.x1.game.server.module.tower.model.Tower;

/**
 * @author YY
 *
 */
@Service
public class TowerService extends PlayerOneToOneResetableService<Tower> {

	@Autowired
	private MailService mailService;
	@Autowired
	private TowerCache towerCache;
	
	private static Map<Integer, BaseTowerBattleProcessor> towerProcessorMap = new HashMap<>();
	
	protected static void register(BaseTowerBattleProcessor processor) {
		towerProcessorMap.put(processor.towerType().getValue(), processor);
	}
	public static BaseTowerBattleProcessor getProcessor(int towerType) {
		return towerProcessorMap.get(towerType);
	}
	
	@Override
	protected Tower createWhenNull(long entityId) {
		Tower entity = new Tower();
		entity.setPid(entityId);
		return entity;
	}
	@Override
	protected void doDailyReset(Tower entity) {
		TowerCfg towerCfg = towerCache.findInTypeLayerIndex(TowerType.NORMAL.getValue(), entity.getNormalLayer());
		if(towerCfg == null || towerCfg.getDailyRewards().isEmpty()) {
			return;
		}
		Mail mail = mailService.newMail(entity.getPid(), MailTemplate.TOWER_REWARD,
				MailArgs.build(entity.getNormalLayer()), towerCfg.getDailyRewards(), GameCause.TOWER_REWARD);
		mailService.insert(mail);
	}
}
