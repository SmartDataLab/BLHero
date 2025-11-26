package com.xiugou.x1.game.server.module.bag.model;

import com.xiugou.x1.game.server.module.bag.struct.ItemGrid;
import org.gaming.db.annotation.Column;
import org.gaming.db.annotation.Id;
import org.gaming.db.annotation.JvmCache;
import org.gaming.db.annotation.Table;
import org.gaming.db.annotation.enuma.AsyncType;
import org.gaming.db.orm.AbstractEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/5/30
 * @apiNote
 */
@Repository
@JvmCache
@Table(name = "bag", comment = "玩家背包表", dbAlias = "game", asyncType = AsyncType.INSERT)
public class Bag extends AbstractEntity {
    @Id(strategy = Id.Strategy.IDENTITY)
    @Column(comment = "玩家ID")
    private long pid;
    @Column(name = "id_grid_map",comment = "道具数据 K:道具id v:格子信息",extra = "text")
    private Map<Integer, ItemGrid> idGridMap =new HashMap<>();

    public void setPId(long pId) {
        this.pid = pId;
    }

    public long getPId() {
        return pid;
    }

    public Map<Integer, ItemGrid> getIdGridMap() {
        return idGridMap;
    }

    public void setIdGridMap(Map<Integer, ItemGrid> idGridMap) {
        this.idGridMap = idGridMap;
    }
}
