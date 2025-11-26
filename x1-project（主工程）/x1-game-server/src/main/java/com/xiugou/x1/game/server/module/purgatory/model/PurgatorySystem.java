package com.xiugou.x1.game.server.module.purgatory.model;

import com.xiugou.x1.game.server.module.dungeon.service.AbsDungeonSystemEntity;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.springframework.stereotype.Repository;

/**
 * @author yh
 * @date 2023/8/8
 * @apiNote
 */
@Repository
@JvmCache(loadAllOnStart = true, cacheTime = 0)
@Table(name = "purgatory_system", comment = "炼狱轮回赛季表", dbAlias = "game", asyncType = AsyncType.UPDATE)
public class PurgatorySystem extends AbsDungeonSystemEntity {
}
