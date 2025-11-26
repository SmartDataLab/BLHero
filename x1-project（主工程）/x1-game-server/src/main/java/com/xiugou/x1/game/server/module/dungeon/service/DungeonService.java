package com.xiugou.x1.game.server.module.dungeon.service;

import org.springframework.stereotype.Service;

import com.xiugou.x1.game.server.foundation.service.PlayerOneToOneResetableService;
import com.xiugou.x1.game.server.module.dungeon.model.Dungeon;

/**
 * @author yh
 * @date 2023/7/10
 * @apiNote
 */
@Service
public class DungeonService  extends PlayerOneToOneResetableService<Dungeon> {
    @Override
    protected Dungeon createWhenNull(long entityId) {
        Dungeon dungeon = new Dungeon();
        dungeon.setPid(entityId);
        return dungeon;
    }
    
    @Override
    protected void doDailyReset(Dungeon entity) {
    }
}
