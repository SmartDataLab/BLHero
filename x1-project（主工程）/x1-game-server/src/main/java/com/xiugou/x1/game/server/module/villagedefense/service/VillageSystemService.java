package com.xiugou.x1.game.server.module.villagedefense.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.VillageRankCache;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.dungeon.service.DungeonSystemService;
import com.xiugou.x1.game.server.module.dungeon.struct.DungeonSystemData;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.villagedefense.model.VillageSystem;

/**
 * @author yh
 * @date 2023/8/16
 * @apiNote
 */
@Service
public class VillageSystemService extends DungeonSystemService<VillageSystem> {

    @Autowired
    private ApplicationSettings applicationSettings;
    @Autowired
    private TimeSetting timeSetting;
    @Autowired
    private VillageRankCache villageRankCache;
    @Autowired
    private BattleConstCache battleConstCache;

    @Override
    protected VillageSystem createWhenNull(long entityId) {
        return null;
    }

    public LocalDateTime calculateResetTime() {
        return timeSetting.nextWeekMondayOTime();
    }

    public VillageSystem getEntity() {
        VillageSystem villageSystem = this.getEntity(applicationSettings.getGameServerId());
        if (villageSystem == null) {
            synchronized (this) {
                villageSystem = this.getEntity(applicationSettings.getGameServerId());
                if (villageSystem == null) {
                    villageSystem = new VillageSystem();
                    villageSystem.setServerId(applicationSettings.getGameServerId());
                    villageSystem.setRound(1);
                    villageSystem.setNextResetTime(calculateResetTime());
                    this.insert(villageSystem);
                }
            }
        }
        return villageSystem;
    }

    protected void runInSchedule() {
        VillageSystem entity = getEntity();
        if (!timeSetting.needReset(entity.getNextResetTime())) {
            return;
        }
        DungeonSystemData dungeonSystemData = new DungeonSystemData();
        dungeonSystemData.setRankType(RankType.VILLAGE);
        dungeonSystemData.setMailTemplate(MailTemplate.VILLAGE_RANK_FINISH);
        dungeonSystemData.setBackLevel(battleConstCache.getVillage_break_level());
        dungeonSystemData.setBattleType(BattleType.VILLAGE_DEFENSE.getValue());
        dungeonSystemData.setNextResetTime(calculateResetTime());
        dungeonSystemData.setGameCause(GameCause.VILLAGE_RANK_FINISH);
        settle(entity, dungeonSystemData);
        update(entity);
    }

    @Override
    public List<RewardThing> getRankReward(int ranking) {
        return villageRankCache.getRankReward(ranking);
    }
}
