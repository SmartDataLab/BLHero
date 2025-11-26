package com.xiugou.x1.game.server.module.evil.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.design.module.EvilCatalogCache;
import com.xiugou.x1.design.module.EvilSuitCache;
import com.xiugou.x1.design.module.EvilTypeCache;
import com.xiugou.x1.design.module.autogen.EvilCatalogAbstractCache.EvilCatalogCfg;
import com.xiugou.x1.design.module.autogen.EvilSuitAbstractCache.EvilSuitCfg;
import com.xiugou.x1.design.module.autogen.EvilTypeAbstractCache.EvilTypeCfg;
import com.xiugou.x1.game.server.module.evil.model.Evil;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.AbstractHeroFightingSystem;
import com.xiugou.x1.game.server.module.hero.service.FightingScope;

/**
 * @author yh
 * @date 2023/8/2
 * @apiNote
 */
@Service
public class EvilAttrService extends AbstractHeroFightingSystem {
	@Autowired
	private EvilService evilService;
	@Autowired
	private EvilCatalogCache evilCatalogCache;
	@Autowired
	private EvilSuitCache evilSuitCache;
	@Autowired
	private EvilTypeCache evilTypeCache;

	@Override
	public FightingScope fightingScope() {
		return FightingScope.EVIL_CATALOG;
	}

	@Override
	public void calculateAttr(Hero hero, BattleAttr outAttr) {
	}

	@Override
	public void calculateTroopAttr(long playerId, BattleAttr outAttr) {
		List<Attr> attrs = calculateEvilAttrs(playerId);
		List<Attr> suitAttrs = calculateSuitEvilAttrs(playerId);
		attrs.addAll(suitAttrs);
		for (Attr attr : attrs) {
			outAttr.addById(attr.getAttrId(), attr.getValue());
		}
		// 对基础属性进行结算
		settleBaseAttr(outAttr);
	}

	private List<Attr> calculateEvilAttrs(long playerId) {
		Map<Integer, Evil> evilMap = evilService.getEvilMap(playerId);
		List<Attr> attrs = new ArrayList<>();
		for (Evil evil : evilMap.values()) {
			EvilCatalogCfg evilCatalogCfg = evilCatalogCache.getInIdentityLevelIndex(evil.getIdentity(),
					evil.getLevel());
			attrs.addAll(evilCatalogCfg.getAttr());
		}
		return attrs;
	}

	private List<Attr> calculateSuitEvilAttrs(long playerId) {
		Map<Integer, Evil> evilMap = evilService.getEvilMap(playerId);
		HashMap<Integer, Integer> map = new HashMap<>(); // 系列-数量
		for (Evil evil : evilMap.values()) {
			EvilTypeCfg evilTypeCfg = evilTypeCache.getOrThrow(evil.getIdentity());
			int series = evilTypeCfg.getSeries();
			map.put(series, map.getOrDefault(series, 0) + 1);
		}
		List<Attr> attrs = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			ArrayList<EvilSuitCfg> evilSuitCfg = evilSuitCache.getInSuitIdCollector(entry.getKey());
			List<Attr> tempAttrs = null;
			for (EvilSuitCfg evilSuitConfig : evilSuitCfg) {
				if (evilSuitConfig.getSuitNum() > entry.getValue()) {
					break;
				}
				tempAttrs = evilSuitConfig.getAttrs();
			}
			if (tempAttrs != null) {
				attrs.addAll(tempAttrs);
			}
		}
		return attrs;
	}
}