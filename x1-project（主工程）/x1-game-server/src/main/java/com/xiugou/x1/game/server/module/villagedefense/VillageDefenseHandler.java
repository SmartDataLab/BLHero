/**
 *
 */
package com.xiugou.x1.game.server.module.villagedefense;

import java.util.ArrayList;
import java.util.List;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.battle.BattleContext;
import com.xiugou.x1.battle.constant.TeamSide;
import com.xiugou.x1.battle.sprite.Sprite;
import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.BattleTypeCache;
import com.xiugou.x1.design.module.FairylandCache;
import com.xiugou.x1.design.module.ItemRandomPackCache;
import com.xiugou.x1.design.module.autogen.BattleTypeAbstractCache.BattleTypeCfg;
import com.xiugou.x1.design.module.autogen.FairylandAbstractCache.FairylandCfg;
import com.xiugou.x1.design.struct.CostThing;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.battle.processor.BaseBattleProcessor;
import com.xiugou.x1.game.server.module.hero.model.Hero;
import com.xiugou.x1.game.server.module.hero.service.HeroService;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.villagedefense.model.Village;
import com.xiugou.x1.game.server.module.villagedefense.model.VillageSystem;
import com.xiugou.x1.game.server.module.villagedefense.service.VillageService;
import com.xiugou.x1.game.server.module.villagedefense.service.VillageSystemService;
import com.xiugou.x1.game.server.module.villagedefense.struct.VillageParam;

import pb.xiugou.x1.protobuf.village.Village.VillageBuildRequest;
import pb.xiugou.x1.protobuf.village.Village.VillageBuildResponse;
import pb.xiugou.x1.protobuf.village.Village.VillageConfirmHeroRequest;
import pb.xiugou.x1.protobuf.village.Village.VillageConfirmHeroResponse;
import pb.xiugou.x1.protobuf.village.Village.VillageInfoResponse;
import pb.xiugou.x1.protobuf.village.Village.VillageSweepRequest;
import pb.xiugou.x1.protobuf.village.Village.VillageSweepResponse;

/**
 * @author YY
 */
@Controller
public class VillageDefenseHandler extends AbstractModuleHandler {

    @Autowired
    private HeroService heroService;
    @Autowired
    private VillageService villageService;
    @Autowired
    private VillageSystemService villageSystemService;
    @Autowired
    private BattleTypeCache battleTypeCache;
    @Autowired
    private ThingService thingService;
    @Autowired
    private FairylandCache fairylandCache;
    @Autowired
    private ItemRandomPackCache itemRandomPackCache;

    @Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
    
    @Override
    public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
        Village village = villageService.getEntity(playerId);
        VillageSystem villageSystem = villageSystemService.getEntity();
        VillageInfoResponse.Builder response = VillageInfoResponse.newBuilder();
        response.setLevel(village.getLevel());
        response.setTodayTimes(village.getChallengeTimes());
        response.setNextResetTime(LocalDateTimeUtil.toEpochMilli(villageSystem.getNextResetTime()));
        playerContextManager.push(playerId, VillageInfoResponse.Proto.ID, response.build());
    }

    @PlayerCmd
    public VillageConfirmHeroResponse confirmHero(PlayerContext playerContext, VillageConfirmHeroRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);
        
        Hero hero = heroService.getEntity(playerContext.getId(), request.getHeroId());
        Sprite sprite = context.addAidHero(hero, TeamSide.ATK);

        VillageConfirmHeroResponse.Builder response = VillageConfirmHeroResponse.newBuilder();
        response.setHeroId(request.getHeroId());
        response.setSprite(sprite.build());
        return response.build();
    }

    @PlayerCmd
    public VillageSweepResponse sweep(PlayerContext playerContext, VillageSweepRequest request) {
        int level = request.getLevel();
        Village village = villageService.getEntity(playerContext.getId());
        Asserts.isTrue(village.getLevel() >= level, TipsCode.VILLAGE_LEVEL_UNFINISHED);

        BattleTypeCfg battleTypeCfg = battleTypeCache.getOrThrow(BattleType.VILLAGE_DEFENSE.getValue());
        RewardThing freeTicket = battleTypeCfg.getFreeTicket();
        thingService.cost(playerContext.getId(), CostThing.of(freeTicket.getThingId(), 1), GameCause.VILLAGE_SWEEP);

        village.setChallengeTimes(village.getChallengeTimes() + 1);
        villageService.update(village);

        FairylandCfg fairylandCfg = fairylandCache.getOrThrow(level);
        List<RewardThing> rewardThings = new ArrayList<>();
        rewardThings.add(fairylandCfg.getReward());
        for (int randomId: fairylandCfg.getRandomReward()){
            rewardThings.add(itemRandomPackCache.randomReward(randomId));
        }
        thingService.add(playerContext.getId(), rewardThings, GameCause.VILLAGE_SWEEP);
        
        return VillageSweepResponse.getDefaultInstance();
    }

    @PlayerCmd
    public VillageBuildResponse build(PlayerContext playerContext, VillageBuildRequest request) {
        BattleContext context = BaseBattleProcessor.getCurrContext(playerContext.getId());
        Asserts.isTrue(context != null, TipsCode.BATTLE_CONTEXT_MISS);

        VillageParam villageParam = (VillageParam) context.getExtraParam();

        Asserts.isTrue(villageParam.getBuildNum() < 3, TipsCode.VILLAGE_BUILD_LIMIT);

        FairylandCfg fairylandCfg = fairylandCache.getOrThrow(context.getSceneId());
        thingService.cost(playerContext.getId(), fairylandCfg.getFenceBuild(), GameCause.VILLAGE_BUILD);
        
        villageParam.setBuildNum(villageParam.getBuildNum() + 1);
        
        VillageBuildResponse.Builder response = VillageBuildResponse.newBuilder();
        response.setBuildNum(villageParam.getBuildNum());
        return response.build();
    }


}
