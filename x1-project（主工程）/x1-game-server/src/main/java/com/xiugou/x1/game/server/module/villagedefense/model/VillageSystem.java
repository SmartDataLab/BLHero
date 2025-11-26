package com.xiugou.x1.game.server.module.villagedefense.model;

import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.springframework.stereotype.Repository;

import com.xiugou.x1.game.server.module.dungeon.service.AbsDungeonSystemEntity;

/**
 * @author yh
 * @date 2023/8/16
 * @apiNote
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "village_system", comment = "村庄守卫赛季表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class VillageSystem extends AbsDungeonSystemEntity {
}
