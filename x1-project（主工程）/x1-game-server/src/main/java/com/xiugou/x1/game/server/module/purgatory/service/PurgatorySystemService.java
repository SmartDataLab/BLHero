package com.xiugou.x1.game.server.module.purgatory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.module.BattleConstCache;
import com.xiugou.x1.design.module.PurgatoryRankCache;
import com.xiugou.x1.design.struct.RewardThing;
import com.xiugou.x1.game.server.TimeSetting;
import com.xiugou.x1.game.server.foundation.starting.ApplicationSettings;
import com.xiugou.x1.game.server.module.battle.constant.BattleType;
import com.xiugou.x1.game.server.module.dungeon.service.DungeonSystemService;
import com.xiugou.x1.game.server.module.dungeon.struct.DungeonSystemData;
import com.xiugou.x1.game.server.module.mail.constant.MailTemplate;
import com.xiugou.x1.game.server.module.purgatory.model.PurgatorySystem;
import com.xiugou.x1.game.server.module.rank.constant.RankType;
import com.xiugou.x1.game.server.module.shop.service.ShopSystemService;

/**
 * @author yhs
 * @date 2023/8/8
 * @apiNote
 */
@Service
public class PurgatorySystemService extends DungeonSystemService<PurgatorySystem> {
    @Autowired
    private TimeSetting timeSetting;
    @Autowired
    private PurgatoryRankCache purgatoryRankCache;
    @Autowired
    private ApplicationSettings applicationSettings;
    @Autowired
    private BattleConstCache battleConstCache;
    @Autowired
    private ShopSystemService shopSystemService;

    @Override
    protected PurgatorySystem createWhenNull(long entityId) {
        return null;
    }

    public PurgatorySystem getEntity() {
        PurgatorySystem purgatorySystem = this.getEntity(applicationSettings.getGameServerId());
        if (purgatorySystem == null) {
            synchronized (this) {
                purgatorySystem = this.getEntity(applicationSettings.getGameServerId());
                if (purgatorySystem == null) {
                    purgatorySystem = new PurgatorySystem();
                    purgatorySystem.setServerId(applicationSettings.getGameServerId());
                    purgatorySystem.setRound(1);
                    purgatorySystem.setNextResetTime(calculateResetTime());
                    this.insert(purgatorySystem);
                }
            }
        }
        return purgatorySystem;
    }

    //根据开服时间 day天重置一次
    public LocalDateTime calculateResetTime() {
        return shopSystemService.resetTimeByOpening(7);
    }

    protected void runInSchedule() {
        PurgatorySystem entity = getEntity();
        if (!timeSetting.needReset(entity.getNextResetTime())) {
            return;
        }
        DungeonSystemData dungeonSystemData = new DungeonSystemData();
        dungeonSystemData.setRankType(RankType.PURGATORY);
        dungeonSystemData.setMailTemplate(MailTemplate.PURGATORY_RANK_FINISH);
        dungeonSystemData.setBackLevel(battleConstCache.getPurgatory_break_level());
        dungeonSystemData.setBattleType(BattleType.PURGATORY.getValue());
        dungeonSystemData.setNextResetTime(calculateResetTime());
        dungeonSystemData.setGameCause(GameCause.PURGATORY_RANK_FINISH);

        settle(getEntity(), dungeonSystemData);
        update(getEntity());
    }

    @Override
    public List<RewardThing> getRankReward(int rank) {
        return purgatoryRankCache.getRankReward(rank);
    }
}
