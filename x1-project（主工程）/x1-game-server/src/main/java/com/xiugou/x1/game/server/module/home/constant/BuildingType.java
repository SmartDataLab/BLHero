package com.xiugou.x1.game.server.module.home.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yh
 * @date 2023/8/9
 * @apiNote
 */
public enum BuildingType {

    GOD_EFFIGY(100, "神像（酒馆）"),
    ALTAR(110, "神坛（训练营）"),
    SHRINE(120, "神殿（装备）"),
    Logging_camp(200, "伐木场"),
    DIGGINGS(201, "矿山"),
    GOLD_PRODUCTION(202, "金币生产所"),

    STASH_FIRST(301,"仓库1级"),
    STASH_SECOND(302,"仓库2级"),
    STASH_THIRD(303,"仓库3级"),
    
    REBUILD_NPC(401,"重建NPC"),
    
    TELEPORT(500, "传送法阵"),
    DUNGEON_NPC(600, "日常副本"),
    EXCHANGE_SHOP(700, "兑换商店"),
    BUILDING_DAILY_DUP(800, "世界秘境"),
    ;
    private int buildingId;
    private String desc;

    private BuildingType(int buildingId,String desc){
        this.buildingId =buildingId;
        this.desc = desc;
    }

    private static Map<Integer, BuildingType> map = new HashMap<>();
    static {
        for(BuildingType buildingType : BuildingType.values()) {
            map.put(buildingType.buildingId, buildingType);
        }
    }
    public static BuildingType valueOf(int buildingId) {
        return map.get(buildingId);
    }
    public String getDesc() {
        return desc;
    }

    public int getBuildingId() {
        return buildingId;
    }
}
