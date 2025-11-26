/**
 *
 */
package com.xiugou.x1.game.server.module.hero.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.design.module.HeroAwakenCache;
import com.xiugou.x1.design.module.HeroLevelCache;
import com.xiugou.x1.design.module.HeroLevelCache.HeroLevelConfig;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.SkillLevelCache;
import com.xiugou.x1.design.module.autogen.HeroAwakenAbstractCache.HeroAwakenCfg;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;
import com.xiugou.x1.design.module.autogen.SkillLevelAbstractCache.SkillLevelCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.module.hero.model.Hero;

/**
 * @author YY
 *
 */
@Service
public class HeroFightingService extends AbstractHeroFightingSystem {

    @Autowired
    private HeroLevelCache heroLevelCache;
    @Autowired
    private SkillLevelCache skillLevelCache;
    @Autowired
    private HeroService heroService;
    @Autowired
	private HeroAwakenCache heroAwakenCache;
    @Autowired
    private HeroTypeCache heroTypeCache;

    @Override
    public void calculateAttr(Hero hero, BattleAttr outAttr) {
        //英雄等级属性
        HeroLevelConfig heroLevelCfg = heroLevelCache.getInHeroIdentityLevelIndex(hero.getIdentity(), hero.getLevel());
        for (Attr attr : heroLevelCfg.getLevelAttribute()) {
            outAttr.addById(attr.getAttrId(), attr.getValue());
        }
        //对基础属性进行结算
        settleBaseAttr(outAttr);
        //英雄自己技能的属性加成
        for (Keyv skillCfg : heroLevelCfg.getSkills()) {
            if (skillCfg.getLevel() <= 0) {
                continue;
            }
            SkillLevelCfg config = skillLevelCache.findInSkillIdLevelIndex(skillCfg.getId(), skillCfg.getLevel());
            if (config == null) {
                //技能没有全部制作完，也没有全部配置好，先做容错
                continue;
            }
            for (Attr attr : config.getNailAttr()) {
                outAttr.addById(attr.getAttrId(), attr.getValue());
            }
        }
        //觉醒属性
        HeroTypeCfg heroTypeCfg = heroTypeCache.getOrThrow(hero.getIdentity());
        HeroAwakenCfg heroAwakenCfg = heroAwakenCache.findInQualityLevelIndex(heroTypeCfg.getQuality(), hero.getAwakenLevel());
        if(heroAwakenCfg != null) {
        	for(Attr attr : heroAwakenCfg.getAttr()) {
        		outAttr.addById(attr.getAttrId(), attr.getValue());
        	}
        }
    }

    @Override
    public void calculateTroopAttr(long playerId, BattleAttr outAttr) {
        List<Hero> heroes = heroService.getEntities(playerId);
        for (Hero hero : heroes) {
            //英雄等级下的团队加成
            HeroLevelConfig heroLevelCfg = heroLevelCache.getInHeroIdentityLevelIndex(hero.getIdentity(), hero.getLevel());
            outAttr.merge(heroLevelCfg.getTroopAttr());
        }
    }

    @Override
    public FightingScope fightingScope() {
        return FightingScope.BASE;
    }
}
