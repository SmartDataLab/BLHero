/**
 *
 */
package com.xiugou.x1.game.server.module.hero.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gaming.prefab.IGameCause;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.ruler.lifecycle.Lifecycle;
import org.gaming.tool.ListMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.attr.BattleAttr;
import com.xiugou.x1.battle.config.Attr;
import com.xiugou.x1.battle.constant.BattleConst;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.HeroLevelCache;
import com.xiugou.x1.design.module.HeroLevelCache.HeroLevelConfig;
import com.xiugou.x1.design.module.HeroTypeCache;
import com.xiugou.x1.design.module.HeroTypeCache.HeroTypeConfig;
import com.xiugou.x1.design.module.SkillLevelCache;
import com.xiugou.x1.design.module.autogen.HeroLevelAbstractCache.HeroLevelCfg;
import com.xiugou.x1.design.module.autogen.HeroTypeAbstractCache.HeroTypeCfg;
import com.xiugou.x1.design.module.autogen.SkillLevelAbstractCache.SkillLevelCfg;
import com.xiugou.x1.design.struct.Keyv;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.formation.service.FormationService;
import com.xiugou.x1.game.server.module.hero.event.SomeHeroFightingChangeEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.model.HeroTroop;
import com.xiugou.x1.game.server.module.hero.struct.AttrCalculator;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.event.PlayerCreateEvent;

import pb.xiugou.x1.protobuf.hero.Hero.HeroFightingChangeMessage;
import pb.xiugou.x1.protobuf.hero.Hero.PbHero;
import pojo.xiugou.x1.pojo.module.hero.HeroForm;

/**
 * @author YY
 */
@Service
public class HeroService extends OneToManyService<Hero> implements Lifecycle {

    @Autowired
    private BattleConstCache battleConstCache;
    @Autowired
    private SkillLevelCache skillLevelCache;
    @Autowired
    private HeroLevelCache heroLevelCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private FormationService formationService;
    @Autowired
    private HeroTroopService heroTroopService;
    @Autowired
    private PlayerContextManager playerContextManager;
    @Autowired
    private HeroTypeCache heroTypeCache;
    @Autowired
    private HeroFightingService heroFightingService;

    private static List<AbstractHeroFightingSystem> fightingSystems = new ArrayList<>();

    private static Map<Integer, PbHero> defaultHeroes = new HashMap<>();

    
    public static void register(AbstractHeroFightingSystem system) {
        fightingSystems.add(system);
    }

    public Hero getEntity(long playerId, int identity) {
        return repository().getByKeys(playerId, identity);
    }

    public Map<Integer, Hero> getHeroMap(long playerId) {
        return ListMapUtil.listToMap(this.getEntities(playerId), Hero::getIdentity);
    }


    /**
     * 计算属性
     *
     * @param hero
     */
    protected BattleAttr calculateAttrWithoutScope(Hero hero, FightingScope excludeScope) {
        BattleAttr panelAttr = new BattleAttr();
        // 初始属性
        for (Attr attr : battleConstCache.getHero_init_attr()) {
            panelAttr.addById(attr.getAttrId(), attr.getValue());
        }
        BattleAttr outHeroAttr = new BattleAttr();
        // 其他各种属性系统的处理
        for (AbstractHeroFightingSystem fightingSystem : fightingSystems) {
            if (fightingSystem.fightingScope() == excludeScope) {
                // 排除不进行战力计算的属性系统
                continue;
            }
            outHeroAttr.clear();
            fightingSystem.calculateAttr(hero, outHeroAttr);
            // TODO 可以打印日志用于查找各个系统对英雄的属性影响
            //System.out.println(fightingSystem.getClass().getSimpleName() + " " + hero.getName() + " " + outHeroAttr.notZeroAttrMap());
            panelAttr.merge(outHeroAttr);
        }
        return panelAttr;
    }

    /**
     * @param playerId
     * @param excludeScope 排除哪个系统的属性加成
     * @return
     */
    private BattleAttr calculateTroopAttr(long playerId, FightingScope excludeScope) {
        BattleAttr troopAttr = new BattleAttr();
        BattleAttr outAttr = new BattleAttr();
        for (AbstractHeroFightingSystem fightingSystem : fightingSystems) {
            if (fightingSystem.fightingScope() == excludeScope) {
                // 排除不进行战力计算的属性系统
                continue;
            }
            outAttr.clear();
            fightingSystem.calculateTroopAttr(playerId, outAttr);
            // TODO 可以打印日志用于查找各个系统对英雄的属性影响
            if(excludeScope == null) {
            	//System.out.println(fightingSystem.getClass().getSimpleName() + " " + playerId + " " + outAttr.notZeroAttrMap());
            }
            troopAttr.merge(outAttr);
        }
        return troopAttr;
    }

    @Override
    public List<Hero> getEntities(long ownerId) {
        return super.getEntities(ownerId);
    }

    public void calculateAttr(Hero hero, IGameCause gameCause) {
        this.calculateAttr(hero, gameCause, "by" + hero.getIdentity());
    }

    public void calculateAttr(Hero hero, IGameCause gameCause, String remark) {
        HeroTroop heroTroop = heroTroopService.getEntity(hero.getPid());
        BattleAttr troopAttr = calculateTroopAttr(hero.getPid(), null);

        BattleAttr noEquipTroopAttr = calculateTroopAttr(hero.getPid(), FightingScope.EQUIP);
        BattleAttr noRelicTroopAttr = calculateTroopAttr(hero.getPid(), FightingScope.RELIC);

        // <英雄，旧战力>
        Map<Hero, Long> heroFightingChanges = new HashMap<>();
        if (!heroTroop.getTroopAttr().isEqual(troopAttr)) {
            heroTroop.setTroopAttr(troopAttr);
            heroTroopService.update(heroTroop);

            List<Hero> heroes = this.getEntities(hero.getPid());
            for (Hero tempHero : heroes) {
                AttrCalculator calculator = buildAttrCalculator(tempHero, troopAttr, null);
                if (calculator.getOldFighting() == calculator.getNewFighting()) {
                    continue;
                }

                tempHero.setFighting(calculator.getNewFighting());
                tempHero.setPanelAttr(calculator.getBattleAttr());
                // 计算装备系统提供的战力
                AttrCalculator noEquipCalculator = buildAttrCalculator(tempHero, noEquipTroopAttr, FightingScope.EQUIP);
                tempHero.setEquipFighting(calculator.getNewFighting() - noEquipCalculator.getNewFighting());
                // 计算遗物系统提供的战力
                AttrCalculator noRelicCalculator = buildAttrCalculator(tempHero, noRelicTroopAttr, FightingScope.RELIC);
                tempHero.setRelicFighting(calculator.getNewFighting() - noRelicCalculator.getNewFighting());

                heroFightingChanges.put(tempHero, calculator.getOldFighting());
            }
        } else {
            AttrCalculator calculator = buildAttrCalculator(hero, troopAttr, null);

            hero.setFighting(calculator.getNewFighting());
            hero.setPanelAttr(calculator.getBattleAttr());
            // 计算装备系统提供的战力
            AttrCalculator noEquipCalculator = buildAttrCalculator(hero, noEquipTroopAttr, FightingScope.EQUIP);
            hero.setEquipFighting(calculator.getNewFighting() - noEquipCalculator.getNewFighting());
            // 计算遗物系统提供的战力
            AttrCalculator noRelicCalculator = buildAttrCalculator(hero, noRelicTroopAttr, FightingScope.RELIC);
            hero.setRelicFighting(calculator.getNewFighting() - noRelicCalculator.getNewFighting());

            heroFightingChanges.put(hero, calculator.getOldFighting());
        }

        afterHerosFightingChange(hero.getPid(), heroFightingChanges, gameCause, remark);
    }

    public void calculateAndSetAttr(Hero hero) {
        BattleAttr troopAttr = calculateTroopAttr(hero.getPid(), null);
        AttrCalculator calculator = buildAttrCalculator(hero, troopAttr, null);

        hero.setFighting(calculator.getNewFighting());
        hero.setPanelAttr(calculator.getBattleAttr());
        
        // 计算装备系统提供的战力
        BattleAttr noEquipTroopAttr = calculateTroopAttr(hero.getPid(), FightingScope.EQUIP);
        AttrCalculator noEquipCalculator = buildAttrCalculator(hero, noEquipTroopAttr, FightingScope.EQUIP);
        hero.setEquipFighting(calculator.getNewFighting() - noEquipCalculator.getNewFighting());
        // 计算遗物系统提供的战力
        BattleAttr noRelicTroopAttr = calculateTroopAttr(hero.getPid(), FightingScope.RELIC);
        AttrCalculator noRelicCalculator = buildAttrCalculator(hero, noRelicTroopAttr, FightingScope.RELIC);
        hero.setRelicFighting(calculator.getNewFighting() - noRelicCalculator.getNewFighting());
    }

    /**
     * 计算所有英雄的属性
     *
     * @param playerId
     * @param gameCause
     */
    public void calculateAllHeroAttr(long playerId, IGameCause gameCause) {
        this.calculateAllHeroAttr(playerId, gameCause, "");
    }

    public void calculateAllHeroAttr(long playerId, IGameCause gameCause, String remark) {
        // 先计算团队属性
        List<Hero> heroes = this.getEntities(playerId);
        calculateAllHeroAttr(playerId, heroes, gameCause, remark);
    }

    private void calculateAllHeroAttr(long playerId, List<Hero> heroes, IGameCause gameCause, String remark) {
    	//TODO 
    	//System.out.println("==================计算队伍属性========================");
    	
    	BattleAttr troopAttr = calculateTroopAttr(playerId, null);

        HeroTroop heroTroop = heroTroopService.getEntity(playerId);
        heroTroop.setTroopAttr(troopAttr);
        heroTroopService.update(heroTroop);

        BattleAttr noEquipTroopAttr = calculateTroopAttr(playerId, FightingScope.EQUIP);
        BattleAttr noRelicTroopAttr = calculateTroopAttr(playerId, FightingScope.RELIC);
        
        //TODO 
        //System.out.println("==================计算单体属性========================");
        
        // <英雄，旧战力>
        Map<Hero, Long> heroFightingChanges = new HashMap<>();
        for (Hero tempHero : heroes) {
            AttrCalculator calculator = buildAttrCalculator(tempHero, troopAttr, null);
            if (calculator.getOldFighting() == calculator.getNewFighting()) {
                continue;
            }
            tempHero.setFighting(calculator.getNewFighting());
            tempHero.setPanelAttr(calculator.getBattleAttr());

            // 计算装备系统提供的战力
            AttrCalculator noEquipCalculator = buildAttrCalculator(tempHero, noEquipTroopAttr, FightingScope.EQUIP);
            tempHero.setEquipFighting(calculator.getNewFighting() - noEquipCalculator.getNewFighting());
            // 计算遗物系统提供的战力
            AttrCalculator noRelicCalculator = buildAttrCalculator(tempHero, noRelicTroopAttr, FightingScope.RELIC);
            tempHero.setRelicFighting(calculator.getNewFighting() - noRelicCalculator.getNewFighting());

            heroFightingChanges.put(tempHero, calculator.getOldFighting());
        }
        afterHerosFightingChange(playerId, heroFightingChanges, gameCause, remark);
    }

    private void afterHerosFightingChange(long playerId, Map<Hero, Long> heroFightingChanges, IGameCause gameCause,
                                          String remark) {
        if (heroFightingChanges.isEmpty()) {
            return;
        }
        // 更新数据
        List<Hero> updateList = new ArrayList<>(heroFightingChanges.keySet());
        this.update(updateList);
        // 推送英雄数据
        HeroFightingChangeMessage.Builder builder = HeroFightingChangeMessage.newBuilder();
        for (Hero hero : updateList) {
            builder.addHeroes(PbHelper.build(hero));
        }
        playerContextManager.push(playerId, HeroFightingChangeMessage.Proto.ID, builder.build());

        EventBus.post(SomeHeroFightingChangeEvent.of(playerId, heroFightingChanges, gameCause, remark));
    }

    public AttrCalculator buildAttrCalculator(Hero hero, BattleAttr troopAttr, FightingScope excludeScope) {
        AttrCalculator calculator = new AttrCalculator();
        calculator.setOldFighting(hero.getFighting());
        BattleAttr panelAttr = calculateAttrWithoutScope(hero, excludeScope);
        // 英雄属性=自身属性+团队属性
        panelAttr.merge(troopAttr);

        //结算属性
        settleAttr(hero.getIdentity(), panelAttr);

        calculator.setBattleAttr(panelAttr);
        calculator.setNewFighting(this.calculateFighting(panelAttr, hero.getIdentity(), hero.getLevel()));
        return calculator;
    }


    /**
     * 结算属性
     *
     * @param heroIdentity
     * @param battleAttr
     */
    private void settleAttr(int heroIdentity, BattleAttr battleAttr) {
        // 属性结算
        HeroTypeCfg heroTypeCfg = heroTypeCache.getOrThrow(heroIdentity);
        long elementHpRate = 0;
        long elementHp = 0;
        long elementDmgRate = 0;
        long elementDmg = 0;
        long elementPveDmg = 0;
        if (heroTypeCfg.getElement() == 1) {
            elementHpRate = battleAttr.getLlmaxRate();
            elementHp = battleAttr.getLlmax();
            elementDmgRate = battleAttr.getLldmgRate();
            elementDmg = battleAttr.getLldmg();
            elementPveDmg = battleAttr.getLlpvedmg();

        } else if (heroTypeCfg.getElement() == 2) {
            elementHpRate = battleAttr.getMjmaxRate();
            elementHp = battleAttr.getMjmax();
            elementDmgRate = battleAttr.getMjdmgRate();
            elementDmg = battleAttr.getMjdmg();
            elementPveDmg = battleAttr.getMjpvedmg();


        } else if (heroTypeCfg.getElement() == 3) {
            elementHpRate = battleAttr.getZlmaxRate();
            elementHp = battleAttr.getZlmax();
            elementDmgRate = battleAttr.getZldmgRate();
            elementDmg = battleAttr.getZldmg();
            elementPveDmg = battleAttr.getZlpvedmg();
        }

        long maxHp = (long) ((battleAttr.getMaxHp() + elementHp)
                * (1.0f + (battleAttr.getHpRate() + elementHpRate) / BattleConst.WAN));
//        System.out.println("自身基础血量：" + battleAttr.getMaxHp());
//        System.out.println("种族基础血量：" + elementHp);
//        System.out.println("自身血量加成：" + battleAttr.getHpRate());
//        System.out.println("种族血量加成：" + elementHpRate);
//        System.out.println("血量结果：" + maxHp);
        
        long atk = (long) (battleAttr.getAtk() * (1.0f + battleAttr.getAtkRate() / BattleConst.WAN));
        long def = (long) (battleAttr.getDef() * (1.0f + battleAttr.getDefRate() / BattleConst.WAN));

        battleAttr.setMaxHp(maxHp);
        battleAttr.setHpRate(0);
        battleAttr.setAtk(atk);
        battleAttr.setAtkRate(0);
        battleAttr.setDef(def);
        battleAttr.setDefRate(0);
        battleAttr.setDmg(battleAttr.getDmg() + elementDmg);
        battleAttr.setDmgRate(battleAttr.getDmgRate() + elementDmgRate);
        battleAttr.setAtkSpd((long) (battleAttr.getAtkSpd() * (1.0f + battleAttr.getAtkSpdRate() / BattleConst.WAN)));
        battleAttr.setMoveSpd((long) (battleAttr.getMoveSpd() * (1.0f + battleAttr.getMoveSpdRate() / BattleConst.WAN)));
        battleAttr.setPvedmg(elementPveDmg);

    }

    /**
     * 计算英雄原始属性
     */
    public long calculateInitFighting(long pid, int identity) {
        Hero hero = getEntity(pid, identity);
        HeroLevelConfig heroLevelConfig = heroLevelCache.getInHeroIdentityLevelIndex(identity, hero.getLevel());
        BattleAttr battleAttr = new BattleAttr();
        List<Attr> globalAttribute = heroLevelConfig.getGlobalAttribute();
        List<Attr> levelAttribute = heroLevelConfig.getLevelAttribute();
        for (Attr attr : globalAttribute) {
            battleAttr.addById(attr.getAttrId(), attr.getValue());
        }
        for (Attr attr : levelAttribute) {
            battleAttr.addById(attr.getAttrId(), attr.getValue());
        }
        settleAttr(identity, battleAttr);
        long attrFighting = calculateFighting(battleAttr);
        // 技能战力
        long skillFighting = calculateSkillFighting(heroLevelConfig.getSkills());
        return attrFighting + skillFighting;
    }


    /**
     * 计算属性战力
     *
     * @param battleAttr
     * @return
     */
    public long calculateFighting(BattleAttr attr) {
        List<Float> factors = battleConstCache.getFighting_factor();
        float f1 = factors.get(0); // 3
        float f2 = factors.get(1); // 2
        float f3 = factors.get(2); // 0.5
        float f4 = factors.get(3); // 0.25
        float f5 = factors.get(4); // 0.5
        float f6 = factors.get(5); // 0.5
        float f7 = factors.get(6); // 0.5
        float f8 = factors.get(7); // 0.05
        float f9 = factors.get(8); // 0.25
        float f10 = factors.get(9); // 0.25
        float f11 = factors.get(10);//0.25
        // 总战力=（3*最终攻击+2*最终生命）
        // *（1+0.5*（最终伤害加成+最终伤害减免）/10000
        // +0.25*（最终暴击-初始暴击）/10000
        // +0.5*（最终命中-初始命中+最终闪避-初始闪避）/10000
        // +0.5*(最终暴击伤害-初始爆伤)/10000
        // +0.5*最终吸血/10000+0.05*最终移动速度%/10000+0.25*最终攻击速度%/10000
        // +0.25*最终技能冷却%/10000 +0.25 * 最终对怪物伤害%/10000）+技能读表战力  +最终减伤固定值+最终增伤固定值
        double fighting = (attr.getAtk() * f1 + attr.getMaxHp() * f2) * (1
                + f3 * (attr.getDmgRate() + attr.getReduceDmgRate()) / BattleConst.WAN
                + f4 * attr.getCrit() / BattleConst.WAN + f5 * (attr.getHit() + attr.getDodge()) / BattleConst.WAN
                + f6 * attr.getCritDmgRate() / BattleConst.WAN + f7 * attr.getVampireRate() / BattleConst.WAN
                + f8 * attr.getMoveSpdRate() / BattleConst.WAN + f9 * attr.getAtkSpdRate() / BattleConst.WAN
                + f10 * attr.getCdSpdRate() / BattleConst.WAN + f11 * attr.getPvedmg() / BattleConst.WAN) + attr.getDmg() + attr.getReduceDmg();
//        System.out.println("base " + (attr.getAtk() * f1 + attr.getMaxHp() * f2));
//        System.out.println("f3 " + (f3 * (attr.getDmgRate() + attr.getReduceDmgRate()) / BattleConst.WAN));
//        System.out.println("f4 " + (f4 * attr.getCrit() / BattleConst.WAN));
//        System.out.println("f5 " + (f5 * (attr.getHit() + attr.getDodge()) / BattleConst.WAN));
//        System.out.println("f6 " + (f6 * attr.getCritDmgRate() / BattleConst.WAN));
//        System.out.println("f7 " + (f7 * attr.getVampireRate() / BattleConst.WAN));
//        System.out.println("f8 " + (f8 * attr.getMoveSpdRate() / BattleConst.WAN));
//        System.out.println("f9 " + (f9 * attr.getAtkSpdRate() / BattleConst.WAN));
//        System.out.println("f10 " + (f10 * attr.getCdSpdRate() / BattleConst.WAN));
//        System.out.println("f11 " + (f11 * attr.getPvedmg() / BattleConst.WAN));
//        System.out.println(attr.getDmg() + attr.getReduceDmg());
        return (long) Math.ceil(fighting);

    }

    public long calculateFighting(BattleAttr battleAttr, int identity, int level) {
        // 计算属性战力时需要先扣除初始化的属性值
        BattleAttr tempAttr = battleAttr.copy();
        for (Attr attr : battleConstCache.getHero_init_attr()) {
            tempAttr.subById(attr.getAttrId(), attr.getValue());
        }
        long attrFighting = calculateFighting(tempAttr);
        // 技能战力
        HeroLevelCfg heroLevelCfg = heroLevelCache.getInHeroIdentityLevelIndex(identity, level);
        long skillFighting = calculateSkillFighting(heroLevelCfg.getSkills());
        return attrFighting + skillFighting;
    }

    /**
     * 计算技能提供战力
     */
    public long calculateSkillFighting(List<Keyv> skills) {
        long skillFighting = 0;
        for (Keyv skill : skills) {
            if (skill.getLevel() <= 0) {
                continue;
            }
            SkillLevelCfg skillLevelCfg = skillLevelCache.findInSkillIdLevelIndex(skill.getId(), skill.getLevel());
            if (skillLevelCfg == null) {
                // 技能没有全部制作完，也没有全部配置好，先做容错
                continue;
            }
            skillFighting += skillLevelCfg.getFighting();
        }
        return skillFighting;
    }


    /**
     * 检查玩家是否拥有该英雄
     */
    public boolean checkHaveOrNotHero(long pid, int heroId) {
        Hero entity = getEntity(pid, heroId);
        if (entity == null) {
            return false;
        }
        return true;
    }

    @Subscribe
    private void listen(PlayerCreateEvent event) {
        // 配置初始获得英雄
        thingService.add(event.getPlayer().getId(), battleConstCache.getHero_on_create(), GameCause.HERO_ON_CREATE, NoticeType.NO, "");
        // 创建首个编队
        formationService.getEntity(event.getPlayer().getId(), BattleType.MAINLINE);
    }

    public HeroForm buildHeroForm(Hero hero) {
        HeroTypeConfig heroTypeConfig = heroTypeCache.getOrThrow(hero.getIdentity());
        HeroForm heroForm = new HeroForm();
        heroForm.setId(hero.getId());
        heroForm.setName(hero.getName());
        heroForm.setFighting(hero.getFighting());
        heroForm.setNum(hero.getFragment());
        heroForm.setLevel(hero.getLevel());
        heroForm.setQuality(heroTypeConfig.getQuality());
        return heroForm;
    }

    @Override
    public void start() throws Exception {
        //计算未解锁的英雄属性
        for (HeroTypeConfig config : heroTypeCache.all()) {
            Hero hero = new Hero();
            hero.setIdentity(config.getId());
            hero.setLevel(1);
            BattleAttr outAttr = new BattleAttr();
            heroFightingService.calculateAttr(hero, outAttr);
            this.settleAttr(config.getId(), outAttr);
            long fighting = this.calculateFighting(outAttr);
            hero.setFighting(fighting);
            hero.setPanelAttr(outAttr);

            PbHero pbHero = PbHelper.build(hero);
            defaultHeroes.put(hero.getIdentity(), pbHero);
        }
    }

    public PbHero getDefaultHero(int heroIdentity) {
        return defaultHeroes.get(heroIdentity);
    }
}
