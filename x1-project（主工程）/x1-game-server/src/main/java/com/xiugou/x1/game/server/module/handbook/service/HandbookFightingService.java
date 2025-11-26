/**
 * 
 */
package com.xiugou.x1.game.server.module.handbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.design.module.HandbookIdentityCache;
import com.xiugou.x1.design.module.HandbookLevelCache;
import com.xiugou.x1.design.module.HandbookSuitCache;
import com.xiugou.x1.design.module.autogen.HandbookIdentityAbstractCache.HandbookIdentityCfg;
import com.xiugou.x1.design.module.autogen.HandbookLevelAbstractCache.HandbookLevelCfg;
import com.xiugou.x1.design.module.autogen.HandbookSuitAbstractCache.HandbookSuitCfg;
import com.xiugou.x1.game.server.module.handbook.model.Handbook;
import com.xiugou.x1.game.server.module.handbook.struct.BookDetail;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.AbstractHeroFightingSystem;
import com.xiugou.x1.game.server.module.hero.service.FightingScope;

/**
 * @author YY
 *
 */
@Component
public class HandbookFightingService extends AbstractHeroFightingSystem {

	@Autowired
	private HandbookService handbookService;
	@Autowired
	private HandbookSuitCache handbookSuitCache;
	@Autowired
	private HandbookLevelCache handbookLevelCache;
	@Autowired
	private HandbookIdentityCache handbookIdentityCache;
	
	@Override
	public FightingScope fightingScope() {
		return FightingScope.HANDBOOK;
	}

	@Override
	public void calculateAttr(Hero hero, BattleAttr outAttr) {
		
	}

	@Override
	public void calculateTroopAttr(long playerId, BattleAttr outAttr) {
		List<Handbook> handbooks = handbookService.getEntities(playerId);
		for(Handbook handbook : handbooks) {
			for(BookDetail bookDetail : handbook.getBookDetails().values()) {
				HandbookIdentityCfg elementCfg = handbookIdentityCache.getOrThrow(bookDetail.getIdentity());
				HandbookLevelCfg levelCfg = handbookLevelCache.findInQualityUpTypeLevelIndex(elementCfg.getQuality(), elementCfg.getUpType(), bookDetail.getLevel());
				//添加等级属性
				for(Attr attr : levelCfg.getAttrs()) {
					outAttr.addById(attr.getAttrId(), attr.getValue());
				}
			}
		}
		//添加套装属性
		Handbook handbook0 = handbookService.getEntity(playerId, 0);
		for(int suitId : handbook0.getSuits()) {
			HandbookSuitCfg suitCfg = handbookSuitCache.getOrThrow(suitId);
			for(Attr attr : suitCfg.getAttrs()) {
				outAttr.addById(attr.getAttrId(), attr.getValue());
			}
		}
	}

}
