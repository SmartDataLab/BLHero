package com.xiugou.x1.game.server.module.trainingCamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.design.module.TrainingExpertCampCache;
import com.xiugou.x1.design.module.TrainingExpertCampCache.TrainingExpertCampConfig;
import com.xiugou.x1.design.module.TrainingGeneralCampCache;
import com.xiugou.x1.design.module.TrainingGeneralCampCache.TrainingGeneralCampConfig;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.AbstractHeroFightingSystem;
import com.xiugou.x1.game.server.module.hero.service.FightingScope;
import com.xiugou.x1.game.server.module.trainingCamp.model.TrainingCamp;

/**
 * @author yh
 * @date 2023/6/5
 * @apiNote
 */
@Service
public class TrainingCampAttrService extends AbstractHeroFightingSystem {
	
	@Autowired
	private TrainingCampService trainingCampService;
	@Autowired
	private TrainingGeneralCampCache trainingGeneralCampCache;
	@Autowired
	private TrainingExpertCampCache trainingExpertCampCache;

	@Override
	public void calculateAttr(Hero hero, BattleAttr outAttr) {

	}

	@Override
	public void calculateTroopAttr(long playerId, BattleAttr outAttr) {
		TrainingCamp trainingCamp = trainingCampService.getEntity(playerId);
		//普通训练营
		TrainingGeneralCampConfig generalCfg = trainingGeneralCampCache.getOrNull(trainingCamp.getLevel());
		if (generalCfg != null) {
			outAttr.merge(generalCfg.getLevelAttr());
		}
		//高级训练营
		TrainingExpertCampConfig expertCfg = trainingExpertCampCache.getOrNull(trainingCamp.getExpertLevel());
		if(expertCfg != null && expertCfg.getLevelAttr() != null) {
			outAttr.merge(expertCfg.getLevelAttr());
		}
		// 对基础属性进行结算
		settleBaseAttr(outAttr);
	}

	@Override
	public FightingScope fightingScope() {
		return FightingScope.TRAINING_CAMP;
	}
}
