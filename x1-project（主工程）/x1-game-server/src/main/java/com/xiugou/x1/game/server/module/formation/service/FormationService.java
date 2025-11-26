package com.xiugou.x1.game.server.module.formation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gaming.prefab.exception.Asserts;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.ruler.eventbus.Subscribe;
import org.gaming.tool.ListMapUtil;
import org.gaming.tool.SortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.IBattleType;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.battle.template.TemplateFighter;
import com.xiugou.x1.battle.template.TemplateSprite;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;
import com.xiugou.x1.game.server.foundation.service.OneToManyService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.battle.processor.BattleManager;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult;
import com.xiugou.x1.game.server.module.battle.struct.FormationResult.HeroAndPos;
import com.xiugou.x1.game.server.module.formation.model.Formation;
import com.xiugou.x1.game.server.module.formation.struct.HeroPos;
import com.xiugou.x1.game.server.module.hero.event.SomeHeroFightingChangeEvent;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.mainline.event.MainlineFormationChangeEvent;
import com.xiugou.x1.game.server.module.player.model.Player;
import com.xiugou.x1.game.server.module.player.service.PlayerService;

import pb.xiugou.x1.protobuf.battle.Battle.SwitchBattleHeroResponse;
import pb.xiugou.x1.protobuf.formation.Formation.FormationChangeMessage;
import pb.xiugou.x1.protobuf.formation.Formation.PbFormation;
import pb.xiugou.x1.protobuf.formation.Formation.PbFormationPos;

/**
 * @author yh
 * @date 2023/6/6
 * @apiNote
 */
@Service
public class FormationService extends OneToManyService<Formation> {

	private static Logger logger = LoggerFactory.getLogger(FormationService.class);
	
    @Autowired
    private HeroService heroService;
    @Autowired
    private PlayerContextManager playerContextManager;
    @Autowired
    private PlayerService playerService;

    public Formation getEntity(long playerId, IBattleType battleType) {
        Formation formation = repository().getByKeys(playerId, battleType.getValue());
        if (formation == null) {
        	formation = createFormation(playerId, battleType);
            this.insert(formation);
        }
        return formation;
    }
    
    private Formation createFormation(long playerId, IBattleType battleType) {
    	Formation formation = new Formation();
        formation.setPid(playerId);
        formation.setType(battleType.getValue());

        List<Hero> heroes = heroService.getEntities(playerId);
        SortUtil.sortDesc(heroes, Hero::getFighting);

		BaseBattleProcessor<?> battleProcessor = BattleManager.getProcessor(battleType);
        long fighting = 0;
        int index = 1;
        for (int i = 0; i < heroes.size() && i < battleProcessor.getFormationLimit(playerId); i++) {
        	Hero hero = heroes.get(i);
        	if(!battleProcessor.canAutoIntoFormation(formation, hero.getIdentity())) {
        		continue;
        	}
        	if(formation.getMainHeroIdentity() == 0) {
        		formation.setMainHeroIdentity(hero.getIdentity());
        	}
        	fighting += hero.getFighting();
        	
        	HeroPos heroPos = new HeroPos();
        	heroPos.setIdentity(hero.getIdentity());
        	heroPos.setPos(index);
            formation.getPositions().put(heroPos.getIdentity(), heroPos);
            
            index += 1;
        }
        formation.setFighting(fighting);
        return formation;
    }
    
    public List<Formation> getEntities(long playerId) {
    	List<Formation> formations = super.getEntities(playerId);
    	Map<Integer, Formation> formationMap = ListMapUtil.listToMap(formations, Formation::getType);
    	List<Formation> insertList = new ArrayList<>();
    	for(BattleType battleType : BattleType.values()) {
    		Formation formation = formationMap.get(battleType.getValue());
    		if(formation != null) {
    			continue;
    		}
    		formation = createFormation(playerId, battleType);
    		insertList.add(formation);
    		formations.add(formation);
    	}
    	this.insert(insertList);
    	return formations;
    }

    public PbFormation build(Formation formation) {
        PbFormation.Builder builder = PbFormation.newBuilder();
        builder.setType(formation.getType());
        builder.setMainHeroIdentity(formation.getMainHeroIdentity());
        builder.setFighting(formation.getFighting());
        for (HeroPos heroPos : formation.getPositions().values()) {
            PbFormationPos.Builder data = PbFormationPos.newBuilder();
            data.setIdentity(heroPos.getIdentity());
            data.setPos(heroPos.getPos());
            builder.addPosList(data);
        }
        builder.setManualChange(formation.isManualChange());
        return builder.build();
    }

    /**
     * 检查并更新编队
     *
     * @param formation
     * @param mainHero
     * @param sortedHeroAndPoses 有序的英雄与占位列表
     */
	public void checkAndUpdate(long playerId, IBattleType battleType, int mainHero, List<HeroAndPos> sortedHeroAndPoses) {
		Formation formation = saveFormation(playerId, battleType, mainHero, sortedHeroAndPoses);
        if(battleType == BattleType.MAINLINE) {
        	//主线编队发生更变
			EventBus.post(MainlineFormationChangeEvent.of(playerId, formation.getPositions().size(),
					formation.getFighting(), formation.getMainHeroIdentity(), GameCause.BATTLE_ENTER));
        }
        
        FormationChangeMessage.Builder builder = FormationChangeMessage.newBuilder();
        builder.addFormations(build(formation));
        playerContextManager.push(formation.getPid(), FormationChangeMessage.Proto.ID, builder.build());
    }
	
	public Formation saveFormation(long playerId, IBattleType battleType, int mainHero, List<HeroAndPos> sortedHeroAndPoses) {
		Formation formation = this.getEntity(playerId, battleType);
        boolean needUpdate = false;
        if (formation.getMainHeroIdentity() != mainHero || sortedHeroAndPoses.size() != formation.getPositions().size()) {
            needUpdate = true;
        } else {
        	List<HeroPos> sortList = new ArrayList<>(formation.getPositions().values());
        	SortUtil.sortInt(sortList, HeroPos::getPos);
        	
        	for(int i = 0; i < sortedHeroAndPoses.size(); i++) {
        		HeroPos currPos = sortList.get(i);
        		HeroAndPos heroAndPos = sortedHeroAndPoses.get(i);
        		
        		if(currPos.getIdentity() != heroAndPos.getHero().getIdentity() || currPos.getPos() != heroAndPos.getPos()) {
        			needUpdate = true;
                    break;
        		}
        	}
        }
        if(!formation.isManualChange()) {
        	needUpdate = true;
        }
        if (!needUpdate) {
        	return formation;
        }
    	formation.setManualChange(true);
        formation.setMainHeroIdentity(mainHero);
        formation.getPositions().clear();
        long fighting = 0;
        for(int i = 0; i < sortedHeroAndPoses.size(); i++) {
        	HeroAndPos heroAndPos = sortedHeroAndPoses.get(i);
            fighting += heroAndPos.getHero().getFighting();
            
            HeroPos heroPos = new HeroPos();
            heroPos.setIdentity(heroAndPos.getHero().getIdentity());
            heroPos.setPos(heroAndPos.getPos());
            formation.getPositions().put(heroPos.getIdentity(), heroPos);
        }
        formation.setFighting(fighting);
        this.update(formation);
        
        return formation;
	}
	

    @Subscribe
    private void listen(SomeHeroFightingChangeEvent event) {
        Map<Integer, Hero> heroMap = heroService.getHeroMap(event.getPid());
        List<Formation> formations = this.getEntities(event.getPid());

        List<Formation> updateList = new ArrayList<>();
        
        Formation mainFormation = null;
        for (Formation formation : formations) {
            for (Hero hero : event.getHeroOldFightingMap().keySet()) {
                if (!formation.getPositions().containsKey(hero.getIdentity())) {
                    continue;
                }
                long fighting = 0;
                long equipFighting = 0;
                for (int heroIdentity : formation.getPositions().keySet()) {
                    Hero posHero = heroMap.get(heroIdentity);
                    fighting += posHero.getFighting();
                    equipFighting += posHero.getEquipFighting();
                }
                formation.setFighting(fighting);
                formation.setEquipFighting(equipFighting);
                updateList.add(formation);
                
                if(formation.getType() == BattleType.MAINLINE.getValue()) {
                	mainFormation = formation;
                }
                break;
            }
        }
        this.update(updateList);
        
        if(mainFormation != null) {
			EventBus.post(MainlineFormationChangeEvent.of(event.getPid(), mainFormation.getPositions().size(),
					mainFormation.getFighting(), mainFormation.getMainHeroIdentity(), event.getGameCause()));
        }

        FormationChangeMessage.Builder builder = FormationChangeMessage.newBuilder();
        for (Formation formation : updateList) {
            builder.addFormations(build(formation));
        }
        playerContextManager.push(event.getPid(), FormationChangeMessage.Proto.ID, builder.build());
    }
    
    /**
     * 尝试自动上阵
     * @param playerId
     * @param heroIdentity
     */
    public void tryAutoIntoFormations(BattleContext context, int heroIdentity) {
    	try {
    		Formation formation = this.getEntity(context.getPlayerId(), context.getBattleType());
    		BaseBattleProcessor<?> processor = BattleManager.getProcessor(context.getBattleType());
            if(!processor.canAutoIntoFormation(formation, heroIdentity)) {
            	return;
            }
    		
    		List<PbFormationPos> posList = new ArrayList<>();
    		int maxIndex = 0;
    		for(HeroPos heroPos : formation.getPositions().values()) {
    			PbFormationPos.Builder pos = PbFormationPos.newBuilder();
    			pos.setIdentity(heroPos.getIdentity());
    			pos.setPos(heroPos.getPos());
    			posList.add(pos.build());
    			
    			if(heroPos.getPos() > maxIndex) {
    				maxIndex = heroPos.getPos();
    			}
    		}
    		PbFormationPos.Builder autoPos = PbFormationPos.newBuilder();
    		autoPos.setIdentity(heroIdentity);
    		autoPos.setPos(maxIndex + 1);
    		posList.add(autoPos.build());
    		
            int mainHero = processor.switchHeroes(context, formation.getMainHeroIdentity(), posList);
            
            SwitchBattleHeroResponse.Builder response = SwitchBattleHeroResponse.newBuilder();
            for (Sprite hero : context.getAtkTeam().getAllSpriteMap().values()) {
                response.addHeroes(hero.build());
            }
            response.setMainHero(mainHero);
            playerContextManager.push(context.getPlayerId(), SwitchBattleHeroResponse.Proto.ID, response.build());
		} catch (Exception e) {
			logger.error("尝试自动上阵失败", e);
		}
    }
    
    public FormationResult buildFormationResult(long playerId, int mainHero, List<PbFormationPos> heroPosList) {
    	Asserts.isTrue(!heroPosList.isEmpty(), TipsCode.BATTLE_HERO_EMPTY);

        FormationResult formationResult = new FormationResult();

        Set<Integer> heroSet = new HashSet<>();
        Set<Integer> posSet = new HashSet<>();

        for (PbFormationPos pbPos : heroPosList) {
            Asserts.isTrue(!heroSet.contains(pbPos.getIdentity()), TipsCode.BATTLE_HERO_CANT_REPEAT);
            Asserts.isTrue(!posSet.contains(pbPos.getPos()), TipsCode.BATTLE_POS_CANT_REPEAT);

            Hero hero = heroService.getEntity(playerId, pbPos.getIdentity());
            if (hero == null) {
                continue;
            }

            HeroAndPos heroPos = new HeroAndPos();
            heroPos.setHero(hero);
            heroPos.setPos(pbPos.getPos());
            formationResult.getList().add(heroPos);

            heroSet.add(pbPos.getIdentity());
            posSet.add(pbPos.getPos());

            if (pbPos.getIdentity() == mainHero) {
                formationResult.setMainHero(pbPos.getIdentity());
            }
        }
        Asserts.isTrue(!formationResult.getList().isEmpty(), TipsCode.BATTLE_HERO_EMPTY);
        SortUtil.sortInt(formationResult.getList(), HeroAndPos::getPos);

        if (formationResult.getMainHero() == 0) {
            formationResult.setMainHero(formationResult.getList().get(0).getHero().getIdentity());
        }
        return formationResult;
    }
    
    public TemplateFighter buildFighter(Formation formation) {
    	Player player = playerService.getEntity(formation.getPid());
    	TemplateFighter fighter = new TemplateFighter();
    	fighter.setId(player.getId());
    	fighter.setNick(player.getNick());
    	fighter.setLevel(player.getLevel());
    	fighter.setFighting(formation.getFighting());
    	fighter.setServerId(player.getServerId());
    	fighter.setType(1);
    	for(HeroPos heroPos : formation.getPositions().values()) {
    		Hero hero = heroService.getEntity(formation.getPid(), heroPos.getIdentity());
    		TemplateSprite sprite = new TemplateSprite();
    		sprite.setIdentity(hero.getIdentity());
    		sprite.setLevel(hero.getLevel());
    		sprite.setPos(heroPos.getPos());
    		sprite.setPanelAttr(hero.getPanelAttr());
    		fighter.getSprites().add(sprite);
    	}
    	return fighter;
    }
}
