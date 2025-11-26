package com.xiugou.x1.game.server.module.purgatory.service;

import java.util.ArrayList;
import java.util.List;

import org.gaming.ruler.util.DropUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.PurgatoryAttrCache;
import com.xiugou.x1.design.module.PurgatoryAttrCache.PurgatoryAttrConfig;
import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.purgatory.model.Purgatory;
import com.xiugou.x1.game.server.module.purgatory.model.PurgatorySystem;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote
 */
@Service
public class PurgatoryService extends PlayerOneToOneResetableService<Purgatory> {
	
	@Autowired
	private PurgatoryAttrCache purgatoryAttrCache;
	@Autowired
	private PurgatorySystemService purgatorySystemService;
	@Autowired
	private BattleConstCache battleConstCache;

	@Override
	protected Purgatory createWhenNull(long entityId) {
		Purgatory purgatory = new Purgatory();
		purgatory.setPid(entityId);
		purgatory.setRound(purgatorySystemService.getEntity().getRound());
		return purgatory;
	}

	protected void doDailyReset(Purgatory entity) {
		entity.setFreeTimes(0);
	}
	
	@Override
	protected boolean doSpecialReset(Purgatory entity) {
		PurgatorySystem purgatorySystem = purgatorySystemService.getEntity();
		if(entity.getRound() == purgatorySystem.getRound()) {
			return false;
		}
		int gap = purgatorySystem.getRound() - entity.getRound();
		int minus = battleConstCache.getPurgatory_break_level() * gap;
		entity.setRound(purgatorySystem.getRound());
		entity.setLevel(entity.getLevel() - minus);
		List<Integer> levelSet = new ArrayList<>(entity.getAttrs().keySet());
		for(int attrLevel : levelSet) {
			if(attrLevel > entity.getLevel()) {
				entity.getAttrs().remove(attrLevel);
			}
		}
		if(entity.getLevel() < 0) {
			entity.setLevel(0);
		}
		return true;
	}

	public Attr getAttrByStashId(int stashId) {
		List<PurgatoryAttrConfig> purgatoryAttrConfig = purgatoryAttrCache.getInStashIdCollector(stashId);
		PurgatoryAttrConfig attrConfig = DropUtil.randomDrop(purgatoryAttrConfig);
		Attr attr = new Attr();
		attr.setAttrId(attrConfig.getAttr().getAttrId());
		attr.setValue(attrConfig.getAttr().getValue());
		return attr;
	}
}
